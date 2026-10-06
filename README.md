# NovaBank Transfer Portal (hackathon-app)

> ⚠️ **Fictional bank. Intentionally insecure training application** for the DevSecOps hackathon.
> Never deploy it outside a lab. Do not reuse any key, password or token found in this repository.

A small **Java 21 / Spring Boot 3** application that lets customers of the fictional *NovaBank* move money
between accounts.

👉 **Hackathon participants: start with [HACKATHON.md](HACKATHON.md).**

## Build and run

```bash
mvn clean package
java -jar target/novabank-transfer.jar
```

Open `http://<SERVER_IP>:8082`.

## Demo accounts

| Account | Holder | PIN |
|---|---|---|
| ACC1001 | Priya Sharma | 1234 |
| ACC1002 | Arjun Mehta | 4321 |
| ACC1003 | Fatima Khan | 1111 |
| ACC1004 | Daniel Thomas | 2222 |

## Endpoints

| URL | Description |
|---|---|
| `/` | Accounts + transfer form |
| `POST /transfer` | Transfer (HTML form) |
| `GET /api/accounts` | Accounts as JSON |
| `POST /api/transfers` | Transfer (JSON: `fromAccount`, `toAccount`, `amount`, `pin`, `note`) |
| `GET /api/transfers/history?account=ACC1001` | Transfer history |
| `GET /api/accounts/{number}/token` | Opaque token for an account number |
| `POST /api/templates/import` | Import transfer templates (YAML body) |
| `/actuator/health` | Health check |

## Pipeline

Your `Jenkinsfile` (teams write it; it starts as an empty template) must run: Checkout → Gitleaks (optional) → `mvn clean package` → Docker build → Trivy → deploy on port 8082.

## Run with Docker

```bash
mvn clean package
docker build -t hackathon-app:1 .
docker run -d --name hackathon-app -p 8082:8082 hackathon-app:1
curl http://localhost:8082/actuator/health
```
