# Nurupo
nurupo  
ga
# What's it
It's a homework of MicroService Course (Cloud Computing) in our University.

This is a very simple Movie Recommendation Website with Nodejs, Spring-boot, MySQL and Spark.

# Author
* Weihui Hong (Leader)
* Shien Huang
* Shaojie Wang
* Junpeng You
* Ru Duan

# How to use it
## Database
For data storing, we use MySQL5 and Hibernate.

Change the config in `src/main/resources/application.yml` for each service.

## Service
* eureka
* gateway
* history
* movie
* recommend
* users  

`cd {each module}` and then `mvn spring-boot:run`  
Using `Apache Maven 3.5.2` when testing

## Web
Nodejs is required for frontend.
```
cd frontend/originJS  
npm install && npm run server
```
Open browser and goto `http://localhost:7000`
