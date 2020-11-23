[![N|Solid](https://github.com/Rob097/mongo-react-spring-jwt-multimodule-template/blob/master/Risorse%20Varie/React%20%2B%20Spring%20Logo.png)](https://github.com/Rob097/mongo-react-spring-jwt-multimodule-template)

# React & Spring Boot: Multi-models Project

Hi! This is a simple template to begin programming a React & Spring boot Web application.

### How to create the project structure
To create a project structure like this one, so a multi-module structure, with Eclipse IDE you have to:
  - File > New > Project > Spring Starter Project
  - Give to your project a Name, a Group and an artifact id
  - The Dependencies that you may need are: Spring Web, Spring Data Mongo DB, Spring Boot DevTools and Lombok
  - Finish
    
Now you have your project but you need to set it up, so:
  - Delete the src/ Source Folders 
  - Remove from Build Path the JRE System Library and the Maven Dependencies
  - The pom.xml have to be like the one in the folder "Defaul Files" of this project
  - Attention! Be sure that the packaging in the pom.xml is "pom"
    
Well Done! Now you just need to create your modules:
  - Right click on the project > New > Other > Maven Module.
  - Check On "Create a simple project" and give it a name > Finish
  - On each module's pom.xml add "<packaging>jar</packaging>"
  - If you now check the pom file of the parent project you may see that now you'd have something like this:

```sh
<modules>
	<module>runnable</module>
	<module>clients</module>
	<module>users</module>
</modules>
```

## Mongo Database
The database is developed using the NoSql technology by MongoDB.

## React frontend
The frontend is developed in React JS and it is fully commented to explain everything.

## Java Backend
The backend is developed in java using the spring boot framework.
We have developed a multimodule project because is simpler to understand, is more solid and it's also quicker when we have to build or deploy just one module not to build everything inside the project. Everything in the project it's carrefoully commented to understand all better. Now let's see the main modules in this template project.

### RUNNABLE MODULE
I reccomend you to create always a different module to manage the Main class and also to build and copying in the jar package the frontend. In fact this is what it is the runnable module. It contains the Main Class (Application.java) and in resources we have the the build folder of frontend to use when we are in production. It also contain a folder named "frontend" that is the actual REACT prject. The module need this folder to create a build folder and coping it inside the jar package when we build the project (It does that thanks to two plugin inside the pom.xml of this module).
This module also contains in the resources folder the application.properties file. This file contains the parameters to connect the project with the Database and some constant used somewhere inside the project.
You could put all these files directly inside the users module but, expecially if the application is going to be big or complex, is better to separate all these aspects in a different module.

### USERS MODULE
The Users module is extremely important because is the module that manage the users that can log in or sign up in the application. The authentication is managed by JWT standard. (https://jwt.io/).

Signin:
The signin function is quite complex. 
When a user logging in, it call the method JWTAuthServer of AuthenticationService in react frontend and this method call the api authenticateUser at the "/signin" path. Here are stored the information that the user used to authenticate so username, password and the boolean value of the checkbox remindMe.
There is a control in the database to check if the credentials are correct and to get the user details in particolar the roles of the user.
Then 2 cookies are created: rememberMe and token
in the token cookie is stored the jwt token and in the rememberMe the boolean value of the checkbox. If it's true then the expiration of both cookies and of the JWT token are set to 30 days, if instead is false then the expiration dates are set to 2 hours.
When the user is enough close to the expiration date, an alert window is showed and if the user chose to refresh the token, then a new token is generated and if there was a cookie rememberMe set to true then the expiration date of the new token is also set to 30 days if else it was set to false then the token exp date is set to 2 hours. Then two new cookie are generated with the new informations.

### CLIENTS MODULE
The clients module is just a simple module to understand better how to link the different modules and of what is composed a module. Indeed it cointains just the basict so:
  - Modules (Entity)
  - Repository (Connection to Database)
  - Services (Methods)
  - Controllers (Operation linked to a particular web path and http method)

If a module need to be integrate in another you have to include it as a dependency. In order to do that you have to add in the pom.xml file of the module A, the one that need something to the module B, the following code:

```sh
<dependencies>
	<dependency>
		<groupId>com.multitenant.jwt</groupId>
		<artifactId>clients</artifactId>
		<version>0.0.1-SNAPSHOT</version>
	</dependency>
</dependencies>
```
Into the runnable module, beause it has tha Main class that run the entire project, you have to add the above code for every module and also the dependency for the spring-boot-starter-web.
