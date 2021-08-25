# Locust Load Testing Example

## Example Application: **ToDoList**

The example application is a to-do list application, consisting of three main components:

1. A node express server `todolist-frontend` to serve static assets for the React.JS frontend.
2. A Java Spring Boot server `todolist-userservice` to serve a REST API for login, signup and session handling.
3. A Java Spring Boot server `todolist-eventservice` to serve a GraphQL API for list querying and mutations.

## Prerequisites

1. Docker must be installed on your machine, and able to run `docker-compose` (v3.3+) stacks.

## Getting Started

A `Makefile` has been provided, that supports the following commands:

| **Command** | **Description** |
|-----|-----|
| `make clean` | Clean up existing build artifacts |
| `make build` | Build source code for all projects |
| `make test` | Build source code and run unit tests |
| `make start-example` | Build source code and start a `docker-compose` stack with only the ToDoList application running |
| `make start` | Build source code and start a `docker-compose` stack with both the ToDoList application and Locust |
