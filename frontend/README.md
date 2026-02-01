# Inventory Management - Flask Frontend

A minimal Flask frontend that proxies requests to the existing Spring Boot backend.

Prerequisites
- Python 3.9+
- Backend running (default: http://localhost:8080)

Quick start

1. cd frontend
2. python -m venv .venv && source .venv/bin/activate
3. pip install -r requirements.txt
4. export BACKEND_URL=http://localhost:8080  # if different
5. flask --app app run

Features
- Login / Signup (JWT stored in session)
- List / Create / Delete medications
- View a medication and add inbound/outbound transactions
- Fetch FDA drug info for a medication

