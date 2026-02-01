import os
from flask import Flask, render_template, request, redirect, url_for, session, flash
import requests
from dotenv import load_dotenv

load_dotenv()

app = Flask(__name__)
app.secret_key = os.environ.get('FLASK_SECRET', 'dev-secret')
BACKEND_URL = os.environ.get('BACKEND_URL', 'http://localhost:8080')

# --- Helpers ---

def api_headers():
    headers = {'Content-Type': 'application/json'}
    token = session.get('token')
    if token:
        headers['Authorization'] = f'Bearer {token}'
    return headers


class BackendErrorResponse:
    def __init__(self, message, status_code=503):
        self.ok = False
        self.text = message
        self.status_code = status_code

    def json(self):
        return {}


def safe_request(req_func, *args, **kwargs):
    try:
        return req_func(*args, **kwargs)
    except requests.exceptions.RequestException as e:
        return BackendErrorResponse(str(e))


def api_get(path, params=None):
    return safe_request(requests.get, f"{BACKEND_URL}{path}", headers=api_headers(), params=params, timeout=5)


def api_post(path, json=None):
    return safe_request(requests.post, f"{BACKEND_URL}{path}", headers=api_headers(), json=json, timeout=5)


def api_put(path, json=None):
    return safe_request(requests.put, f"{BACKEND_URL}{path}", headers=api_headers(), json=json, timeout=5)


def api_delete(path):
    return safe_request(requests.delete, f"{BACKEND_URL}{path}", headers=api_headers(), timeout=5)


def format_backend_message(r):
    # r can be a requests.Response or BackendErrorResponse
    status = getattr(r, 'status_code', None)
    try:
        # try to parse JSON error if present
        if hasattr(r, 'json'):
            jr = r.json()
            if isinstance(jr, dict):
                # prefer explicit message
                if jr.get('message'):
                    return jr.get('message')
                # gather field-level validation errors
                field_keys = ['password', 'email', 'username', 'message', 'error']
                msgs = []
                for k in field_keys:
                    if k in jr:
                        val = jr.get(k)
                        if isinstance(val, str):
                            msgs.append(val)
                        elif isinstance(val, list):
                            msgs.extend([str(x) for x in val])
                if msgs:
                    return "; ".join(msgs)
                # fallback to any stringified dict
                return str(jr)
    except Exception:
        pass
    if status == 401:
        return 'You are not authenticated. Please log in.'
    if status == 403:
        return 'You are not authorized to perform this action.'
    return getattr(r, 'text', str(r))

# --- Routes ---

@app.route('/')
def index():
    # show medications with pagination
    page = int(request.args.get('page', 0))
    size = int(request.args.get('size', 5))
    r = api_get('/medications', params={'page': page, 'size': size})
    meds = []
    if not r.ok:
        flash(format_backend_message(r), 'danger')
    else:
        try:
            meds = r.json()
        except Exception:
            meds = []
    return render_template('index.html', meds=meds, page=page, size=size, token=session.get('token'))


@app.route('/login', methods=['GET', 'POST'])
def login():
    if request.method == 'POST':
        email = request.form['email']
        password = request.form['password']
        r = requests.post(f"{BACKEND_URL}/users/login", json={'email': email, 'password': password})
        if r.ok:
            token = r.json().get('token')
            session['token'] = token
            flash('Logged in successfully', 'success')
            return redirect(url_for('index'))
        else:
            # show backend message if present
            try:
                jr = r.json()
                msg = jr.get('message') or jr.get('error') or str(jr)
            except Exception:
                msg = r.text or 'Login failed'
            flash(msg, 'danger')
    return render_template('login.html')


@app.route('/signup', methods=['GET', 'POST'])
def signup():
    if request.method == 'POST':
        username = request.form['username']
        email = request.form['email']
        password = request.form['password']
        r = requests.post(f"{BACKEND_URL}/users/signup", json={'username': username, 'email': email, 'password': password})
        # debug log backend response
        try:
            app.logger.info(f"Signup backend response status={getattr(r,'status_code',None)} json={r.json()}")
        except Exception:
            app.logger.info(f"Signup backend response status={getattr(r,'status_code',None)} text={getattr(r,'text',None)[:800]}")
        if r.ok:
            token = r.json().get('token')
            session['token'] = token
            flash('Signed up and logged in', 'success')
            return redirect(url_for('index'))
        else:
            # extract possible validation messages (field-specific)
            try:
                jr = r.json()
                if isinstance(jr, dict):
                    # show field-level validation if available
                    msg = jr.get('password') or jr.get('email') or jr.get('username') or jr.get('message') or str(jr)
                else:
                    msg = str(jr)
            except Exception:
                msg = r.text or format_backend_message(r) or 'Signup failed'
            flash(msg, 'danger')
    return render_template('signup.html')


@app.route('/logout')
def logout():
    session.pop('token', None)
    flash('Logged out', 'info')
    return redirect(url_for('index'))


@app.route('/medications/new', methods=['GET', 'POST'])
def new_medication():
    if request.method == 'POST':
        name = request.form['name']
        description = request.form['description']
        quantity = int(request.form['quantity'])
        med_type = request.form['type']
        payload = {'name': name, 'description': description, 'quantity': quantity, 'type': med_type}
        r = api_post('/medications', json=payload)
        if r.ok:
            flash('Medication created', 'success')
            return redirect(url_for('index'))
        else:
            flash(r.text, 'danger')
    return render_template('new_medication.html')


@app.route('/medications/<int:med_id>')
def medication_detail(med_id):
    r = api_get(f'/medications/{med_id}')
    if not r.ok:
        flash(f'Backend error: {r.text}', 'danger')
        return redirect(url_for('index'))
    med = r.json()
    in_r = api_get('/inbound/transactions')
    out_r = api_get('/outbound/transactions')
    in_txns = in_r.json() if in_r.ok else []
    out_txns = out_r.json() if out_r.ok else []
    # filter transactions for this medication
    in_filtered = [t for t in in_txns if t.get('medicationId') == med.get('id') or (t.get('medication') and t.get('medication').get('id')==med.get('id'))]
    out_filtered = [t for t in out_txns if t.get('medicationId') == med.get('id') or (t.get('medication') and t.get('medication').get('id')==med.get('id'))]
    # pop any FDA data saved in session by fetch_fda
    fda = session.pop(f'fda_{med_id}', None)
    return render_template('medication.html', med=med, inbound=in_filtered, outbound=out_filtered, fda=fda) 


@app.route('/medications/<int:med_id>/delete', methods=['POST'])
def medication_delete(med_id):
    r = api_delete(f'/medications/{med_id}')
    if r.ok:
        flash('Medication deleted', 'success')
    else:
        flash(r.text, 'danger')
    return redirect(url_for('index'))


@app.route('/medications/<int:med_id>/inbound', methods=['POST'])
def add_inbound(med_id):
    quantity = int(request.form['quantity'])
    supplier = request.form['supplier']
    payload = {'medicationId': med_id, 'quantity': quantity, 'supplier': supplier}
    r = api_post('/inbound/transactions', json=payload)
    if r.ok:
        flash('Inbound transaction recorded', 'success')
    else:
        flash(r.text, 'danger')
    return redirect(url_for('medication_detail', med_id=med_id))


@app.route('/medications/<int:med_id>/outbound', methods=['POST'])
def add_outbound(med_id):
    quantity = int(request.form['quantity'])
    receiver = request.form['receiver']
    payload = {'medicationId': med_id, 'quantity': quantity, 'receiver': receiver}
    r = api_post('/outbound/transactions', json=payload)
    if r.ok:
        flash('Outbound transaction recorded', 'success')
    else:
        # try to show error message
        try:
            msg = r.json()
        except Exception:
            msg = r.text
        flash(f'Error: {msg}', 'danger')
    return redirect(url_for('medication_detail', med_id=med_id))


@app.route('/medications/fetch/<int:med_id>')
def fetch_fda(med_id):
    # Fetch FDA info by medication name and store in session to render inline
    m = api_get(f'/medications/{med_id}')
    if not m.ok:
        flash('Medication not found', 'danger')
        return redirect(url_for('index'))
    med = m.json()
    name = med.get('name')
    r = api_get(f'/medications/fetch-data/{name}')
    if r.ok:
        data = r.json()
        session[f'fda_{med_id}'] = data
    else:
        flash('Could not fetch FDA data', 'danger')
    return redirect(url_for('medication_detail', med_id=med_id))


if __name__ == '__main__':
    app.run(debug=True)
