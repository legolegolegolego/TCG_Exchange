plugins {
	java
//	war
	id("org.springframework.boot") version "3.4.0"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "com.es"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-mail") // email
//	implementation("org.springframework.boot:spring-boot-starter-tomcat") redundante, springboot lo incluye
	implementation("org.postgresql:postgresql:42.7.10") // PostgreSQL
	developmentOnly("org.springframework.boot:spring-boot-devtools")
//	runtimeOnly("com.mysql:mysql-connector-j") MYSQL
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
//	providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

// asegurar jar en lugar de war
tasks.named("bootJar") {
	enabled = true
}
//