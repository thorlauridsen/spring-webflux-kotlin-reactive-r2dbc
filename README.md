# Spring WebFlux + Spring Data R2DBC + Kotlin Coroutines

This is a sample project for how you can set up a 
[multi-project Gradle build](https://docs.gradle.org/current/userguide/multi_project_builds.html) 
using
[Spring WebFlux](https://docs.spring.io/spring-framework/reference/web/webflux.html)
, [Spring Data R2DBC](https://spring.io/projects/spring-data-r2dbc)
and [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
to create a REST API with reactive database interactions.

## Usage
Clone the project to your local machine, go to the root directory and use:
```
./gradlew api:bootRun
```
This will start the project using an in-memory H2 database.

### Docker Compose
To run the project with [Docker Compose](https://docs.docker.com/compose/), go to the root directory and use:
```
docker-compose up -d
```
This will launch the project using a PostgreSQL database.

### Swagger Documentation
Once the system is running, navigate to http://localhost:8080/ 
to view the Swagger documentation.

## Technology
- [JDK21](https://openjdk.org/projects/jdk/21/) - Latest JDK with long-term support 
- [Kotlin](https://github.com/JetBrains/kotlin) - Programming language with Java interoperability
- [Gradle](https://github.com/gradle/gradle) - Used for compilation, building, testing and dependency management
- [Spring WebFlux](https://docs.spring.io/spring-framework/reference/web/webflux.html) - For creating reactive REST APIs
- [Spring Data R2DBC](https://spring.io/projects/spring-data-r2dbc) - For reactive database interactions
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) - For asynchronous programming
- [Jackson](https://github.com/FasterXML/jackson-module-kotlin) - Provides a Kotlin module for automatic JSON serialization and deserialization
- [Springdoc](https://github.com/springdoc/springdoc-openapi) - Provides Swagger documentation for REST APIs
- [PostgreSQL](https://www.postgresql.org/) - Open-source relational database
- [H2database](https://github.com/h2database/h2database) - Provides an in-memory database for simple local testing
- [R2DBC-H2](https://github.com/r2dbc/r2dbc-h2) - R2DBC driver for H2 database
- [Liquibase](https://github.com/liquibase/liquibase) - Used to manage database schema changelogs
- [Testcontainers](https://github.com/testcontainers) - Creates a temporary PostgreSQL database for tests

## Testing
You can run the tests for this project using the following command:
```
./gradlew test
```
Please note that this project uses
[Testcontainers](https://github.com/testcontainers)
to create a temporary PostgreSQL database for tests. This requires
a local Docker instance to be running when executing the tests.

## Gradle best practices for Kotlin
[kotlinlang.org](https://kotlinlang.org/docs/gradle-best-practices.html)

### ✅ Use Kotlin DSL
This project uses Kotlin DSL instead of the traditional Groovy DSL by 
using **build.gradle.kts** files instead of **build.gradle** files.
The benefit here is that we do not need to use another programming 
language (Groovy) and can simply use Kotlin in our **build.gradle.kts** files.
This gives us the benefits of strict typing which lets IDEs provide 
better support for refactoring and auto-completion.

### ✅ Use a version catalog 

This project uses a version catalog 
[local.versions.toml](gradle/local.versions.toml)
which allows us to centralize dependency management. 
We can define versions, libraries, bundles and plugins here.
This enables us to use Gradle dependencies consistently across the entire project.

Dependencies can then be implemented in a specific **build.gradle.kts** file as such:
```kotlin
implementation(local.spring.boot.starter)
```

The Kotlinlang article says to name the version catalog **libs.versions.toml** 
but for this project it has been named **local.versions.toml**. The reason 
for this is that we can create a shared common version catalog which can 
be used across Gradle projects. Imagine that you are working on multiple 
similar Gradle projects with different purposes, but each project has some 
specific dependencies but also some dependencies in common. The dependencies 
that are common across projects could be placed in the shared version catalog 
while specific dependencies are placed in the local version catalog.

### ✅ Use local build cache

This project uses a local 
[build cache](https://docs.gradle.org/current/userguide/build_cache.html)
for Gradle which is a way to increase build performance because it will 
re-use outputs produced by previous builds. It will store build outputs 
locally and allow subsequent builds to fetch these outputs from the cache 
when it knows that the inputs have not changed. 
This means we can save time building

Gradle build cache is disabled by default so it has been enabled for this 
project by updating the root [gradle.properties](gradle.properties) file:
```properties
org.gradle.caching=true
```

This is enough to enable the local build cache
and by default, this will use a directory in the Gradle User Home 
to store build cache artifacts.

### ✅ Use configuration cache

This project uses 
[Gradle configuration cache](https://docs.gradle.org/current/userguide/configuration_cache.html)
and this will improve build performance by caching the result of the 
configuration phase and reusing this for subsequent builds. This means 
that Gradle tasks can be executed faster if nothing has been changed 
that affects the build configuration. If you update a **build.gradle.kts** 
file, the build configuration has been affected.

This is not enabled by default, so it is enabled by defining this in 
the root [gradle.properties](gradle.properties) file:
```properties
org.gradle.configuration-cache=true
org.gradle.configuration-cache.parallel=true
```

### ✅ Use modularization

This project uses modularization to create a 
[multi-project Gradle build](https://docs.gradle.org/current/userguide/multi_project_builds.html).
The benefit here is that we optimize build performance and structure our 
entire project in a meaningful way. This is more scalable as it is easier 
to grow a large project when you structure the code like this.

```
root
│─ build.gradle.kts
│─ settings.gradle.kts
│─ apps
│   └─ api
│       └─ build.gradle.kts
│─ modules
│   ├─ model
│   │   └─ build.gradle.kts
│   └─ persistence
│       └─ build.gradle.kts
```

This also allows us to specifically decide which Gradle dependencies will be used
for which subproject. Each subproject should only use exactly the dependencies
that they need.

Subprojects located under [apps](apps) are runnable, so this means we can 
run the **api** project to spin up a Spring Boot REST API. We can add more 
subprojects under [apps](apps) to create additional runnable microservices.

Subprojects located under [modules](modules) are not independently runnable. 
The subprojects are used to structure code into various layers. The **model** 
subproject is the most inner layer and contains domain model classes and this 
subproject knows nothing about any of the other subprojects. The purpose of 
the **persistence** subproject is to manage the code responsible for 
interacting with the database. We can add more non-runnable subprojects 
under [modules](modules) if necessary. This could for example 
be a third-party integration.

---

#### Subproject with other subproject as dependency

The subprojects in this repository may use other subprojects as dependencies.

In our root [settings.gradle.kts](settings.gradle.kts) we have added:
```kotlin
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
```
Which allows us to add a subproject as a dependency in another subproject:

```kotlin
dependencies {
    implementation(projects.model)
}
```

This essentially allows us to define this structure:

```
api  
│─ model  
└─ persistence

persistence  
└─ model

model has no dependencies
```

## Meta

This project has been created with the sample code structure from
[thorlauridsen/spring-boot-kotlin-sample](https://github.com/thorlauridsen/spring-boot-kotlin-sample)
