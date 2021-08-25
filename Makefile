SHELL := /bin/bash

clean:	## Clean previous builds
		@./gradlew clean

build:	## Build source code
		@./gradlew clean -xtest

test:	## Test source code
		@./gradlew clean test

start:	## Run docker compose
		#@./gradlew clean build -xtest
		@cd tools && docker-compose -f "docker-compose.yml" up && cd ..

start-example: ## Run docker compose without locust
	    #@./gradlew clean build -xtest
		@cd tools && docker-compose -f "docker-compose-app-only.yml" up && cd ..
