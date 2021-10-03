import com.google.protobuf.gradle.*

val grpcVersion: String by project
val grpcKotlinVersion: String by project
val coroutinesVersion: String by project

buildscript {
    dependencies {
        classpath("com.google.protobuf:protobuf-gradle-plugin:0.8.17")
    }
}

plugins {
    kotlin("jvm") version "1.5.10"
    id("com.google.protobuf") version "0.8.17"
    id("maven-publish")
}

group = "com.nahidalgo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // grpc
    implementation("io.grpc:grpc-protobuf:$grpcVersion")
    implementation("io.grpc:grpc-stub:$grpcVersion")
    implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion")

    // kotlin coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.17.3"
    }
    // generatedFilesBaseDir = "$projectDir/src/main/proto/generated"
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:$grpcVersion"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:$grpcKotlinVersion:jdk7@jar"
        }
    }
    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                id("grpc")
                id("grpckt")
            }
        }
    }
}

sourceSets {
    main {
        proto {
            srcDir("proto")
            include("./**/*.proto")
        }
    }
}

tasks.withType<Jar> {
    archiveBaseName.set("proto-cart.proto-service")
}


publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()

            from(components["java"])
        }
    }
}