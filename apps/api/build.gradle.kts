plugins {
	kotlin("plugin.spring") version local.versions.kotlin
	alias(local.plugins.springboot)
	alias(local.plugins.spring.dependencies)
}

dependencies {
	// The api subproject needs access to both the model and persistence subproject
	implementation(projects.model)
	implementation(projects.persistence)

	// Spring Boot dependencies
	implementation(local.springboot.starter)
	implementation(local.springboot.starter.webflux)

	// Kotlin Coroutines
	// Reactor is required for Spring Boot controller suspend endpoints
	implementation(local.kotlin.coroutines)
	implementation(local.kotlin.coroutines.reactor)
	testImplementation(local.kotlin.coroutines.test)

	// Springdoc for swagger docs supporting Spring WebFlux
	implementation(local.springdoc.openapi.starter.webflux)

	// Liquibase for database migrations
	implementation(local.liquibase.core)

	// H2 in-memory database
	implementation(local.h2database)

	// R2DBC driver for reactive database access for H2
	implementation(local.h2r2dbc)

	// FasterXML Jackson module for Kotlin support
	implementation(local.jackson.module.kotlin)

	// Test dependencies
	testImplementation(local.springboot.starter.test)
	testImplementation(local.kotlin.test.junit5)
	testRuntimeOnly(local.junit.platform.launcher)
}

tasks.withType<Test> {
	useJUnitPlatform()
}
