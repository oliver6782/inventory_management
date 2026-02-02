# Medication inventory management system

## Introduction:
This Spring Boot project aims to develop a medicine inventory management system designed to 
help organizations track and manage their inventory effectively.
The system will allow users to add, update, 
delete, and search inventory items, along with maintaining the stock levels.

## Tech stack:
+ Spring Boot
+ Spring AOP
+ Spring Actuator
+ Spring Security
+ Spring Data JPA
+ Spring Validator
+ Spring Async
+ Spring Cache
+ Redis
+ Maven
+ Docker
+ Swagger OpenAPI
+ JUnit & Mockito
+ Jacoco
+ Postman
+ MySQL

For details, please check the `pom.xml` for dependency coordinates.

This project initialize an `ADMIN` user at running time.

| Name  | Email           | Password     | Role  |
|-------|-----------------|--------------|-------|
| admin | admin@gmail.com | Admin123123@ | ADMIN |

note: password pattern must match these criteria:
- At least one digit [0-9]
- At least one lowercase letter [a-z]
- At least one uppercase letter [A-Z]
- At least one special character [@#$%^&+=]
- No whitespace allowed
- At least 8 characters long


## Deployment (Heroku) ✅

This repo contains both a Spring Boot backend and a Flask frontend. The recommended deployment is to run them as two separate Heroku apps:

1. Create two Heroku apps:

   - heroku create inventory-backend
   - heroku create inventory-frontend

2. Deploy the backend (Java/Spring Boot):

   - git push https://git.heroku.com/inventory-backend.git main

   The Java buildpack will detect the `pom.xml` and build your app. Heroku will provide a `PORT` environment variable which the backend is configured to use (`server.port=${PORT:8080}`).

3. Deploy the frontend (Python/Flask):

   - Ensure `frontend/requirements.txt` contains `gunicorn` and `frontend/Procfile` exists with:
     `web: gunicorn app:app --bind 0.0.0.0:$PORT`

   - Push the `frontend` subdirectory as the app root:
     `git subtree push --prefix frontend https://git.heroku.com/inventory-frontend.git main`

4. Configure the frontend to talk to the backend:

   - Get the backend URL (e.g., https://inventory-backend.herokuapp.com)
   - Set the frontend config var: `heroku config:set BACKEND_URL=https://inventory-backend.herokuapp.com -a inventory-frontend`

5. Optional / Security:

   - Set a JWT secret on the backend: `heroku config:set JWT_SECRET=<your-secret> -a inventory-backend` (the backend uses `jwt.secret` property if provided).
   - The H2 in-memory database is used for demo purposes and will reset on dyno restarts. Consider provisioning a managed DB add-on (ClearDB, JawsDB, or an external MySQL/Postgres) for production workloads.

That’s it — once both apps are running and `BACKEND_URL` is configured in the frontend app, sign-in and operations should work from the hosted frontend.


