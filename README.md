# Task manager:
[![Actions Status](https://github.com/IVF13/java-project-lvl5/workflows/hexlet-check/badge.svg)](https://github.com/IVF13/java-project-lvl5/actions)
[![Build workflow](https://github.com/IVF13/java-project-lvl5/actions/workflows/build.yml/badge.svg)](https://github.com/IVF13/java-project-lvl5/actions/workflows/build.yml)
[![Maintainability](https://api.codeclimate.com/v1/badges/49f0669aaa1c728da4cb/maintainability)](https://codeclimate.com/github/IVF13/java-project-lvl5/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/49f0669aaa1c728da4cb/test_coverage)](https://codeclimate.com/github/IVF13/java-project-lvl5/test_coverage)
### [Demonstration page](https://majestic-lassen-volcanic-51335.herokuapp.com/login "Task manager")
### [Documentation page](https://majestic-lassen-volcanic-51335.herokuapp.com/swagger-ui.html "Swagger")  

Task Manager is a flexible tool that allows you to control the execution of tasks in a multi-user environment, assign tasks to different statuses, group by labels and assign executors.
The backend of this app is based on Spring-Boot framework, us JWT authentication, H2(development)/PostrgreSQL(production) DB and Swagger Open-API, for tests it using Junit and MockMVC frameworks. 
The fronted was download from Hexlet repository.
The demonstration and documentation pages of this project and is deployed on Heroku service. Also, it connected with Rollbar service for error tracking and bug fixing.
## You can download this app and run it locally.
### Instruction:
1. Clone this repository to your machine by ```$git clone https://github.com/IVF13/java-project-lvl5``` or by your IDE features.
2. Do install by ```$make install```
3. Run it by ```$make run-dist```
4. Open app on [localhost:5000](http://localhost:5000/login) address  

Also you can run it by your IDE just by running the App class.  
