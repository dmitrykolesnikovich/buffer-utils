import org.gradle.api.attributes.java.TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE
import org.gradle.internal.os.OperatingSystem.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URL

plugins {
    java
    kotlin("jvm") version "1.4.0"
    `maven-publish`
    id("org.jetbrains.dokka") version "1.4.0-dev-62"
    id("com.github.johnrengelman.shadow").version("6.0.0")
}

group = "com.github.kotlin_graphics"

val kotestVersion = "4.2.0"
val lwjglVersion = "3.2.3"
val lwjglNatives = "natives-" + when (current()) {
    WINDOWS -> "windows"
    LINUX -> "linux"
    else -> "macos"
}

repositories {
    mavenCentral()
    jcenter()
    maven("https://jitpack.io")
    maven("https://maven.pkg.jetbrains.space/kotlin/p/dokka/dev")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))
    listOf("", "-jemalloc").forEach {
        implementation("org.lwjgl", "lwjgl$it", classifier = lwjglNatives)
        runtimeOnly("org.lwjgl", "lwjgl$it", classifier = lwjglNatives)
    }

    testImplementation("io.kotest:kotest-runner-junit5-jvm:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core-jvm:$kotestVersion")

    testImplementation(kotlin("test-junit5"))
    listOf("engine", "api", "params").forEach {
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-$it:5.6.2")
    }
}

tasks {

    dokkaHtml {
        dokkaSourceSets.configureEach {
            sourceLink {
                localDirectory.set(file("src/main/kotlin"))
                remoteUrl.set(URL("https://github.com/kotlin-graphics/kool/tree/master/src/main/kotlin"))
                remoteLineSuffix.set("#L")
            }
            val root = "$rootDir/src/test/kotlin/kool/buffers"
            samples.from("$root/Collections.kt", "$root/Arrays.kt", "$root/Iterables.kt", "$root/Sequences.kt")
        }
    }

    withType<KotlinCompile>().all {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs += listOf("-Xinline-classes", "-Xopt-in=kotlin.RequiresOptIn")
        }
        sourceCompatibility = "1.8"
    }

    withType<Test> { useJUnitPlatform() }
}

configurations.all { attributes.attribute(TARGET_JVM_VERSION_ATTRIBUTE, 8) }

val dokkaJavadocJar by tasks.register<Jar>("dokkaJavadocJar") {
    dependsOn(tasks.dokkaJavadoc)
    from(tasks.dokkaJavadoc.get().outputDirectory.get())
    archiveClassifier.set("javadoc")
}

val dokkaHtmlJar by tasks.register<Jar>("dokkaHtmlJar") {
    dependsOn(tasks.dokkaHtml)
    from(tasks.dokkaHtml.get().outputDirectory.get())
    archiveClassifier.set("html-doc")
}

val sourceJar = task("sourceJar", Jar::class) {
    dependsOn(tasks["classes"])
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

artifacts {
    archives(dokkaJavadocJar)
    archives(dokkaHtmlJar)
    archives(sourceJar)
}

publishing {
    publications.create<MavenPublication>("mavenJava") {
        from(components["java"])
        artifact(sourceJar)
    }
    repositories.maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/kotlin-graphics/kool")
        credentials {
            username = System.getenv("GITHUB_ACTOR")
            password = System.getenv("GITHUB_TOKEN")
        }
    }
}