install:
	./gradlew clean install

start-dist:
	./build/install/app/bin/app

start:
	./gradlew bootRun --args='--spring.profiles.active=dev'

start-prod:
	./gradlew bootRun --args='--spring.profiles.active=prod'

check-updates:
	./gradlew dependencyUpdates

lint:
	./gradlew checkstyleMain

build:
	./gradlew clean build

report:
	./gradlew jacocoTestReport

test:
	./gradlew test

.PHONY: build