import magik.createGithubPublication
import magik.github
import org.jetbrains.kotlin.gradle.dsl.KotlinCompile
import org.lwjgl.Lwjgl
import org.lwjgl.lwjgl
import org.lwjgl.Lwjgl.Module.jemalloc
import org.lwjgl.Snapshot
import org.lwjgl.sonatype

plugins {
    kotlin("jvm") version embeddedKotlinVersion
    id("org.lwjgl.plugin") version "0.0.33"
    id("elect86.magik") version "0.3.2"
    `maven-publish`
}

repositories {
    mavenCentral()
    sonatype()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    lwjgl {
        version = Snapshot.`3_3_2`
        implementation(Lwjgl.Preset.gettingStarted + jemalloc)
    }
    implementation(Lwjgl.Addons.`joml 1_10_5`)
    testImplementation(kotlin("test"))
}

kotlin.jvmToolchain {
    languageVersion.set(JavaLanguageVersion.of(8))
}

tasks {
    withType<KotlinCompile<*>>().all {
        kotlinOptions {
            languageVersion = "1.8"
            freeCompilerArgs += listOf("-opt-in=kotlin.RequiresOptIn")
        }
    }
    val generateCode by registering(kool.gen.GenerateCode::class)
    kotlin.sourceSets {
        main { kotlin.srcDir(generateCode) }
    }
    test { useJUnitPlatform() }
}

publishing {
    publications {
        createGithubPublication {
            from(components["java"])
            suppressAllPomMetadataWarnings()
        }
    }
    repositories {
        github {
            domain = "kotlin-graphics/mary"
        }
    }
}

java.withSourcesJar()