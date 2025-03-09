# Workout Tracker API

## Description
🏋️ An API-based workout tracker application that enables users to plan, schedule, and track their workouts while providing admin features for managing users.
Built with **Spring Boot**, it implements JWT-based authentication, MapStruct for DTO mapping, JPA audit and supports seamless user experience with robust functionality.

---

## 🚀 Features

### System
 - **Authentication:**
    * 🤝 JWT-based authentication with access tokens and multiple refresh tokens for multi-device log-in.
 - **Admin dashboard:**
    * ✔️ Block and unblock users.
    * 🏁 Promote users to admin or revoke admin rights.
 - **Data Initialization:**
    * 🎄 Seeder to pre-populate the database with exercises, roles, and an admin user.

### User
 - 🙆 Sign up and log in with secure authentication.
 - 📓 Create, view, update, and delete workout plans.
 - 💬 Add comments to workout plans.
 - 👀 List workout plans by `ACTIVE` or `PENDING` status, sorted by date and time.
 - 📆 Schedule workout plans for specific dates and times.
 - 📊 Track workout progress:
    * 🏃 View the number of "active" workout plans.
    * 📌 View the number of "pending" workout plans.
    * 📍 Retrieve the oldest scheduled workout.

---

## 🛠 Tech Stack
 - **Backend Framework:** Spring Boot 3 🌱
 - **Database:** PostgreSQL 🐘
 - **Authentication:** JWT 🚔
 - **Mapping:** MapStruct 🧙
 - **Build Tool:** Maven 🪶
 - **Language:** Java ☕️

---

## 📖 Getting Started
### Prerequisites
 - Java 17+ ☕️
 - Maven 🪶
 - PostgreSQL 🐘

### Setup
1. Clone the repository:
   ```bash
   git clone https://github.com/MuhammadAkbar007/workout-tracker.git
   ```
2. Navigate to the project directory:
    ```bash
    cd workout-tracker-api
    ```
3. Create the database and configure `application.properties`:
    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/database_name
    spring.datasource.username=username
    spring.datasource.password=password
    spring.jpa.hibernate.ddl-auto=create
    spring.jpa.show-sql=true
    ```
4. Run the application:
    ```bash
    mvn spring-boot:run
    ```

---

## 💡 Project idea
The idea is taken from [Roadmap](https://roadmap.sh/) intermediate project [task](https://roadmap.sh/projects/fitness-workout-tracker)

## ✍️Author
Created by [Akbar](https://github.com/MuhammadAkbar007).
