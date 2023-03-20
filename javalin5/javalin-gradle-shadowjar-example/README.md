# Javalin Gradle ShadowJar Example

This project demonstrates how to create a shadow jar, also known as fat jar or uber jar with the Javalin library as an
example.

## Requirements

- Java 17
- Gradle 8 (provided by the Gradle Wrapper ./gradlew)
- Gradle Shadow Plugin

## Creating a shadow jar

Run `./gradlew shadowJar`. The resulting jar file `app/build/libs/app-all.jar` can now be run with
`java -jar app/build/libs/app-all.jar` when inside the default working directory.

```shell
./gradlew shadowJar
cd app/build/libs
java -jar app-all.jar
```
