# Banking Payment Simulation System

A RESTful banking and payment simulation system built with **Spring Boot 3.2.3** and **MySQL**. Supports user management, account creation, deposits, withdrawals, transfers, and transaction history.

## Tech Stack

- **Java 17**
- **Spring Boot 3.2.3** (Web, Data JPA, Validation)
- **MySQL** — Relational database
- **Hibernate** — ORM with auto DDL
- **Lombok** — Boilerplate reduction
- **Maven** — Build tool

## Project Structure

```
src/main/java/com/banking/system/
├── BankingSystemApplication.java        # Entry point
├── controller/
│   ├── AccountController.java           # Account & banking operations
│   ├── TransactionController.java       # Transaction history
│   └── UserController.java             # User management
├── dto/                                 # Request/Response DTOs
├── entity/                              # JPA entities
├── exception/                           # Global exception handling
├── repository/                          # Spring Data repositories
└── service/                             # Business logic
```

## Prerequisites

- **Java 17+**
- **Maven 3.8+**
- **MySQL 8.0+** running on `localhost:3306`

## Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/Yadlasunny/banking-system.git
   cd banking-system
   ```

2. **Configure MySQL**

   The application auto-creates the database. Update credentials in `src/main/resources/application.properties` if needed:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/banking_system?createDatabaseIfNotExist=true
   spring.datasource.username=root
   spring.datasource.password=root
   ```

3. **Build & Run**
   ```bash
   mvn spring-boot:run
   ```
   The server starts on **http://localhost:8080**.

## API Endpoints

### Users

| Method | Endpoint       | Description      | Request Body                          |
|--------|---------------|------------------|---------------------------------------|
| POST   | `/api/users`  | Create a new user | `{ "name": "John", "email": "john@example.com" }` |

### Accounts

| Method | Endpoint                  | Description                  | Request Body |
|--------|--------------------------|------------------------------|--------------|
| POST   | `/api/accounts`          | Create account for a user    | `{ "userId": 1 }` |
| POST   | `/api/accounts/deposit`  | Deposit money                | `{ "accountNumber": "1234567890", "amount": 500.00 }` |
| POST   | `/api/accounts/withdraw` | Withdraw money               | `{ "accountNumber": "1234567890", "amount": 200.00 }` |
| POST   | `/api/accounts/transfer` | Transfer between accounts    | `{ "fromAccountNumber": "1234567890", "toAccountNumber": "0987654321", "amount": 100.00 }` |

### Transactions

| Method | Endpoint                        | Description                         |
|--------|---------------------------------|-------------------------------------|
| GET    | `/api/transactions/{accountId}` | Get transaction history for account |

## Sample Usage

```bash
# 1. Create a user
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name": "John Doe", "email": "john@example.com"}'

# 2. Create an account
curl -X POST http://localhost:8080/api/accounts \
  -H "Content-Type: application/json" \
  -d '{"userId": 1}'

# 3. Deposit money
curl -X POST http://localhost:8080/api/accounts/deposit \
  -H "Content-Type: application/json" \
  -d '{"accountNumber": "YOUR_ACCOUNT_NUMBER", "amount": 1000.00}'

# 4. View transactions
curl http://localhost:8080/api/transactions/1
```

## Error Handling

All errors return a consistent JSON response:

```json
{
  "status": 404,
  "message": "Account not found with accountNumber: '1234567890'",
  "timestamp": "2026-02-19T10:00:00",
  "errors": null
}
```

| HTTP Status | Scenario                    |
|-------------|-----------------------------|
| 400         | Validation error / Insufficient balance |
| 404         | User or account not found   |
| 409         | Duplicate email             |
| 500         | Unexpected server error     |
