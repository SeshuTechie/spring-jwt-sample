# JWT Based Authentication in Spring

This project just helps in demonstrating JWT based authentication using Spring. This has a REST service to provide user signup, login, logout functionality. This also has a simple *User Notes* service that provides create, update, delete and list of some text content (notes). This user notes REST service is accessible only after user login.

### List of API Provided

- POST /users : signup a user
- POST /access-tokens : login user
- DEL /access-tokens : logout user
- POST /access-tokens/refresh : gives a refreshed access token using refresh key

*Below API needs authentication*

- POST /notes : create a note
- GET /notes : get list of notes (page)
- PUT /notes/{id} : update notes
- DEL /notes/{id} : delete notes
- GET /user-info : gets user details
