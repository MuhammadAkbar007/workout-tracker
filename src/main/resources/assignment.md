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

## RefreshToken
* id
* token
* expiryDate
* createdAt
* userId

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

> [!NOTE]
> * Exercises will be used to create workout plans.
> * Users should only be able to access their own workout plans.

# Functionality
## User
- [ ] can sign up
- [ ] can log in
- [ ] can log out
- [ ] create workout plans > [!WARNING] (only authenticated)
* Allow users to create workouts composed of multiple exercises.

- [ ] update workout plans > [!WARNING] (only authenticated)
* Allow users to update workouts and add comments.

- [ ] delete workout plans > [!WARNING] (only authenticated)
- [ ] schedule workout plans for specific date and times > [!WARNING] (only authenticated)
- [ ] list workouts > [!WARNING] (only authenticated)
* List active or pending workouts sorted by date and time

- [ ] track his progress > [!WARNING] (only authenticated)
- [ ] Generate reports on past workouts > [!WARNING] (only authenticated)

# TODO
- [ ] Write a data seeder to populate database with a list of exercises.
- [ ] JWT authentication
- [ ] CRUD for workouts

