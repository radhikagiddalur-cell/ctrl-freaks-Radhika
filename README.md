# NovaBank Transfer Portal (hackathon-app)

> ⚠️ **Fictional bank. Intentionally insecure training application** for the DevSecOps hackathon.
> Never deploy it outside a lab. Do not reuse any key, password or token found in this repository.

A small **Java 21 / Spring Boot 3** application that lets customers of the fictional *NovaBank* move money
between accounts.


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



