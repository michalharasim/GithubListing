# GithubListing

## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)

## General info
This project is an API consumer designed to interact with the GitHub API. It allows users to retrieve information about GitHub repositories owned by a specified user such as repository name, owner login, branches names with the last commit sha.

## Technologies
Project is created with:
* Java **17**
* Spring Boot **3.1.0**
	
## Setup
To run this project:

Clone this repository
```bash
$ git clone https://github.com/michalharasim/GithubList.git
```

Navigate to the root directory of cloned repository and run
```bash
$ mvn clean install
```
Run the project with
```bash
$ mvn spring-boot:run
```

Server is now running.
To retrieve repositories from user's profile, send a GET request to the following endpoint:
```
GET localhost:8080/users/{username}/repos
```
or use:
```
$ curl -H "Accept: application/json" http://localhost:8080/users/{username}/repos
```
Replace {username} with the username of the profile you want to scan repositories from.
