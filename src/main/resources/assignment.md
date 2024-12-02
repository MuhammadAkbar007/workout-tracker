# Entity
## User
* id
* username
* email
* password
* userStatus -> enum

## Role
* id
* roleType -> enum

### user_role
* userId
* roleId

## Workout
* workoutStatus (ACTIVE or PENDING) -> enum
* userId
* createdAt
* updatedAt
* createdBy
* updatedBy

## Exercise
* id
* name
* description
* category (cardio, strength, flexibility)
* muscle group (chest, back, legs)
* repetition
* set
* weight
* createdAt
* updatedAt
* createdBy
* updatedBy
* workoutId

## Comment
* id
* content
* workoutId
* createdAt
* updatedAt
* createdBy
* updatedBy

## RefreshToken
* id
* token
* expiryDate
* createdAt
* userId

> [!NOTE]
> * Exercises will be used to create workout plans.
> * Users should only be able to access their own workout plans.

# Functionality
## System
- [x] Write a data seeder to populate database with a list of exercises.
- [x] implement JWT authentication
- [ ] admin dashboard functionalities

## User
- [x] can sign up
- [x] can log in
- [ ] can log out [!WARNING] (could not done ❌)

> [!NOTE]
> in-progress this task 👇
- [ ] create workout plans: Allow users to create workouts composed of multiple exercises.

- [ ] update workout plans: Allow users to update workouts and add comments.
- [ ] delete workout plans
- [ ] schedule workout plans for specific date and times
- [ ] list workouts:  List active or pending workouts sorted by date and time
- [ ] track his progress
- [ ] Generate reports on past workouts
