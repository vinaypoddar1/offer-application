# offer-application

## Task

Build a backend service that handles a (very simple) recruiting process. The process requires two types of objects: job offers and applications from candidates.
minimum required fields for the objects are:

    Offer:
        jobTitle (unique)
        startDate
        numberOfApplications

    Application:
        related offer
        candidate email (unique per Offer)
        resume text
        applicationStatus (APPLIED, INVITED, REJECTED, HIRED)
Not all of the fields have to be persisted. You may use ad hoc calculation, event sourcing, or whatever you see fit. These are the fields that must be returned by the API. You may add fields where necessary.

## Use cases

- user has to be able to create a job offer and read a single and list all offers.
- candidate has to be able to apply for an offer.
- user has to be able to read one and list all applications per offer.
- user has to be able to progress the status of an application.
- user has to be able to track the number of applications.
- status change triggers a notification (*)

(*) a log output will suffice as a notification here, but you should design it as if each status change triggers a completely different business case.

## Technical requirements

use SpringBoot to build this service. The service must run standalone and must not require any third party software to be installed.
the service must communicate Json over http (REST).
return proper status codes for the most common problems.
the data does not have to be stored permanently, it may be handled in-memory during runtime.

## Things we will focusing on

- a description how to build and use the service
- clean code
- use of the spring framework and spring best practices
- structure of the project
- how you test your code

## Instructions to clone, install and run the application
#### Requirement
- JDK8+
- Maven
- Postman or any other Rest Client

#### Clone Repository
```
git clone https://github.com/vinaypoddar1/offer-application.git
cd offer-application
```

#### Build
```
mvn clean install
```

#### Run
```
mvn spring-boot:run //by deafult it will run on port 8080
```

## API Details
#### Common HEADER to use in all the APIs
`Content-Type:application/json`

### Use Case 1 - user has to be able to create a job offer and read a single and list all offers.
#### Create Job Offer
##### Request
```
  POST localhost:8080/heavenHR/v1/offers
```
#### Body
```
{
  "jobTitle":"Python",
  "startDate":"2018-10-20"
}	
```
##### Response
```
201 CREATED
{
    "timestamp": 1538210407225,
    "status": 201,
    "error": "Created",
    "message": "Offer Created Successfully",
    "path": "/heavenHR/v1/offers"
}
```


#### List All Offers
##### Request
```
  GET localhost:8080/heavenHR/v1/offers
```
##### Response
```
200 OK
[
    {
      "jobTitle": "Python",
      "startDate": 1538206614998,
      "numberOfApplications": 0
    }
]
```

#### Read a Job
##### Request
```
  GET localhost:8080/heavenHR/v1/offers/{Job-Title}
  Example:
    GET localhost:8080/heavenHR/v1/offers/Python
```
##### Response
```
200 OK
{
  "jobTitle": "Python",
  "startDate": 1538206614998,
  "numberOfApplications": 0
}
```

## Use Case 2 - candidate has to be able to apply for an offer.
#### Apply for an Offer
##### Request
```
  POST localhost:8080/heavenHR/v1/offers/{Job-Title}/applications
  Example:
    POST localhost:8080/heavenHR/v1/offers/Python/applications
```
#### Body
```
{
  "offer":"Python",
  "candidateEmail":"paul@aol.com",
  "resumeText":"Sample Python Resume"
}	
```
##### Response
```
201 CREATED
{
    "timestamp": 1538210475739,
    "status": 201,
    "error": "Created",
    "message": "Application Created Successfully",
    "path": "/heavenHR/v1/offers/Python/applications"
}
```

## Use Case 3 - user has to be able to read one and list all applications per offer.
#### Read an Applications for per Offer
##### Request
```
  POST localhost:8080/heavenHR/v1/offers/{Job-Title}/applications/track
  Example:
    POST localhost:8080/heavenHR/v1/offers/Python/applications/track
```
#### Body
```
{
  "email":"paul@aol.com"
}	
```
##### Response
```
200 OK
{
  "candidateEmail": "paul@aol.com",
  "resumeText":"Sample Python Resume",
  "applicationStatus": "APPLIED",
  "offerTitle": "Python"
}
```

#### List All Applications for per Offer
##### Request
```
  GET localhost:8080/heavenHR/v1/offers/{Job-Title}/applications
  Example:
    GET localhost:8080/heavenHR/v1/offers/Python/applications
```
##### Response
```
200 OK
[
  {
    "candidateEmail": "paul@aol.com",
    "resumeText":"Sample Python Resume",
    "applicationStatus": "APPLIED",
    "offerTitle": "Python"
  }
]
```

## Use Case 4 - user has to be able to progress the status of an application.
#### Chanage status of application
##### Request
```
  PUT localhost:8080/heavenHR/v1/offers/{Job-Title}/applications/update
  Example:
    PUT localhost:8080/heavenHR/v1/offers/Python/applications/update
```
#### Body
```
{
  "email":"paul@aol.com",
  "status": "HIRED"
}	
```
##### Response
```
200 OK
{
    "timestamp": 1538210295889,
    "status": 200,
    "error": "OK",
    "message": "Application Status Updated Successfully",
    "path": "/heavenHR/v1/offers/Python/applications/update"
}
```

## Use Case 5 - user has to be able to track the number of applications.
#### count the number of applicants per offer
##### Request
```
  GET localhost:8080/heavenHR/v1/offers/{Job-Title}
  Example:
    GET localhost:8080/heavenHR/v1/offers/Python
```
##### Response
```
200 OK
{
  "jobTitle": "Python",
  "startDate": 1538206614998,
  "numberOfApplications": 1
}
```

#### Candidate can trank all his/her application by email
##### Request
```
  POST localhost:8080/heavenHR/v1/offers/applications/email
```
#### Body
```
{
  "email":"paul@aol.com"
}	
```
##### Response
```
200 OK
[
  {
    "candidateEmail": "paul@aol.com",
    "resumeText":"Sample Python Resume",
    "applicationStatus": "HIRED", // Yippie!!!
    "offerTitle": "Python"
  }
]
```
