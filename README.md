# This is a REST Api Build with Spring Boot using JPA and Lombok

## Features

- User can register and login `POST /api/users/register` `POST /api/users/login`
- User can create, update and delete categories `POST, PUT, DELETE /api/categories`
- User can create, update and delete transactions `POST, PUT, DELETE /api/categories/{category-id}/transactions`
- User can view all categories with total expense `GET /api/categories`
- User can view all transactions `GET /api/categories/{category-id}/transactions`

## Security
- JWT is used for authentication and authorization
- User can only access his own data
- User can only access his own categories and transactions
- You can generate a new token using `POST /api/users/login` and use the token in the `Authorization` header with the
  value `Bearer <token>` to access the protected routes.

## Protected Routes
- `POST, PUT, DELETE /api/categories/**`
- So you need to use JWT to access these routes.
- To get JWT you refer to the [Security](#security) section.

# How to run the application

1. Clone the repository
    ```bash
    git clone https://github.com/ashishbhoi/expense-tracker.git
    ```

2. Set Environment variable
    ```bash
    export JDBC_URL="jdbc:postgresql://<database-url>:<database-port>/<your-database-name>"
    export API_SECRET_KEY="<your-api-secret-key>"
    export JDBC_USER="<database-user-name>"
    export JDBC_PASS="<database-user-name>"
    ```
   > api secret key can be any string of your choice.

   > To user other database you need to modify the `build.gradle` file and add the dependency for the database driver.

   > Then you just need to change the `JDBC_URL` to your database url and `JDBC_USER` and `JDBC_PASS` to your database
   username and password.

3. Run the application
    ```bash
    gradle bootRun
    ```
   if you don't have gradle installed, you can use the `gradlew` file in the root directory.
    ```bash
    ./gradlew bootRun
    ```