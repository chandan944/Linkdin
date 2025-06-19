plugins {
	java
	id("org.springframework.boot") version "3.5.0"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.Linkdin"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(23)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
	runtimeClasspath {
		extendsFrom(configurations.developmentOnly.get()) // ✅ Already provided by Spring Boot
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
// implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-mail")
	implementation("org.springframework.boot:spring-boot-starter-validation")

//// 🔍 Hibernate Search 6 with Lucene Backend
//	implementation("org.hibernate.search:hibernate-search-mapper-orm:6.2.6.Final")
//	implementation("org.hibernate.search:hibernate-search-backend-lucene:6.2.6.Final")
//
//	developmentOnly("org.springframework.boot:spring-boot-devtools") // ✅ Devtools

// 🔐 JWT
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

	runtimeOnly("org.postgresql:postgresql")

// 💡 Lombok
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

// ✅ Testing
	testImplementation("org.springframework.boot:spring-boot-starter-test")
// testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

}

tasks.withType<Test> {
	useJUnitPlatform()
}
