# Sticky Notes
 A fully responsive web application that allows you to create and store notes. The application shows use of the:
 - MongoDB NoSQL database
 - RWD (Responsive Web Design)
 - Libraries for creating pdf, csv, xls files
 - Pagination, sorting and searching mechanism
 - Application security using Spring Security, Google reCAPTCHA and verification tokens.

## Live
<a href="https://sticky-notes-dev.herokuapp.com/">Sticky Notes</a>

### Account for tests
<b>Username: </b> stickytests
<b>Password: </b> stickytests

## General info
At any time, the user can edit, add, delete his notes and modify his user account. Notes can be sorted by various properties and search for them by content and title. The application contains a full registration and login process. During registration, an account activation email is sent, Google reCAPTCHA was used as an additional security for the registration process. If you forget your password, you can reset it. The application uses a role system; users and admins. Admins can make changes to user accounts, delete them, change their roles or block/unblock accounts. In addition, admins are possible to download the list of registered users as a pdf, xls or csv file.
The application uses the Cloud MongoDB database to store users data. The view layer is based on Bootstrap Material Design.

## Technology stack
- Spring Boot
- Spring Security
- MongoDB
- Maven
- Thymeleaf
- Bootstrap Material Design
- HTML5
- JavaMail
- SuperCSV
- Apache POI
- IText PDF
- Google reCAPTCHA
- Heroku

## Screenshots
<img src="https://user-images.githubusercontent.com/44239776/64531593-31cd8200-d310-11e9-8a14-b818aa79474b.png">

<img src="https://user-images.githubusercontent.com/44239776/64531594-31cd8200-d310-11e9-9cb4-c7a6f1001a8b.png">

<img src="https://user-images.githubusercontent.com/44239776/64531595-32661880-d310-11e9-8f35-da29772a905f.png">

<img src="https://user-images.githubusercontent.com/44239776/64531596-32661880-d310-11e9-8d0b-05657bd29125.png">

<img src="https://user-images.githubusercontent.com/44239776/64532125-347ca700-d311-11e9-919a-a7fe7eb8b8ca.png">

<img src="https://user-images.githubusercontent.com/44239776/64532112-2c246c00-d311-11e9-8870-b5be24622a5e.png">

<img src="https://user-images.githubusercontent.com/44239776/64532172-478f7700-d311-11e9-9a9a-9f808f844d97.png">

<img src="https://user-images.githubusercontent.com/44239776/64531601-32feaf00-d310-11e9-9f35-5ef7bc4ddea2.png">

<img src="https://user-images.githubusercontent.com/44239776/64531625-3a25bd00-d310-11e9-9a59-733994a82b48.png">

<img src="https://user-images.githubusercontent.com/44239776/64532229-5fff9180-d311-11e9-9321-353e08a29bc8.png">







