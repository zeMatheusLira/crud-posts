plugins {
    java
    id("org.springframework.boot") version "4.0.5"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
description = "crud-posts"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
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
    // Core & Web
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Validação (Essencial para o requisito de "Validações na aplicação")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // Swagger/OpenAPI (Atende o requisito "Desejável")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.5")

    // Database
    runtimeOnly("org.postgresql:postgresql")

    // Produtividade (Lombok)
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Ferramentas de Desenvolvimento
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // Testes (Unificado e completo: JUnit 5, Mockito, AssertJ)
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}