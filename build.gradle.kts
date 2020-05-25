import org.jetbrains.dokka.gradle.DokkaTask
import org.gradle.internal.os.OperatingSystem.*

plugins {
    java
    kotlin("jvm") version "1.3.72"
    id("org.jetbrains.dokka") version "0.10.1"
    id("com.github.johnrengelman.shadow").version("5.2.0")
}

group = "com.github.kotlin_graphics"
val moduleName = "$group.kool"

val kotestVersion = "4.0.5"
val lwjglVersion = "3.2.3"
val lwjglNatives = when(current()) {
    WINDOWS -> "windows"
    LINUX -> "linux"
    else -> "macos"
}

repositories {
    mavenCentral()
    jcenter()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("stdlib-jdk8"))

    attributesSchema.attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE).compatibilityRules.add(ModularJarCompatibilityRule::class)
    components { withModule<ModularKotlinRule>(kotlin("stdlib")) }
    components { withModule<ModularKotlinRule>(kotlin("stdlib-jdk8")) }

    listOf("", "-jemalloc").forEach {
        implementation("org.lwjgl:lwjgl$it:$lwjglVersion")
        runtimeOnly("org.lwjgl:lwjgl$it:$lwjglVersion:natives-$lwjglNatives")
    }

    listOf("runner-junit5", "assertions-core"/*, "property"*/).forEach {
        testImplementation("io.kotest:kotest-$it-jvm:$kotestVersion")
    }
}

java {
    modularity.inferModulePath.set(true)
}

tasks {
    val dokka by getting(DokkaTask::class) {
        outputFormat = "html"
        outputDirectory = "$buildDir/dokka"
    }

    compileKotlin {
        kotlinOptions {
            jvmTarget = "11"
            freeCompilerArgs = listOf("-XXLanguage:+InlineClasses, -Xopt-in=kotlin.RequiresOptIn")
        }
        sourceCompatibility = "11"
    }

    compileTestKotlin {
        kotlinOptions.jvmTarget = "11"
        sourceCompatibility = "11"
    }

    compileJava {
        // this is needed because we have a separate compile step in this example with the 'module-info.java' is in 'main/java' and the Kotlin code is in 'main/kotlin'
        options.compilerArgs = listOf("--patch-module", "$moduleName=${sourceSets.main.get().output.asPath}")
    }

    withType<Test> { useJUnitPlatform() }
}

val dokkaJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles Kotlin docs with Dokka"
    archiveClassifier.set("javadoc")
    from(tasks.dokka)
}

val sourceJar = task("sourceJar", Jar::class) {
    dependsOn(tasks["classes"])
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

artifacts {
    archives(sourceJar)
    archives(dokkaJar)
}

// == Add access to the 'modular' variant of kotlin("stdlib"): Put this into a buildSrc plugin and reuse it in all your subprojects
configurations.all {
    val n = name.toLowerCase()
    if (n.endsWith("compileclasspath") || n.endsWith("runtimeclasspath"))
        attributes.attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, objects.named("modular-jar"))
    if (n.endsWith("compile") || n.endsWith("runtime"))
        isCanBeConsumed = false
}

abstract class ModularJarCompatibilityRule : AttributeCompatibilityRule<LibraryElements> {
    override fun execute(details: CompatibilityCheckDetails<LibraryElements>): Unit = details.run {
        if (producerValue?.name == LibraryElements.JAR && consumerValue?.name == "modular-jar")
            compatible()
    }
}

abstract class ModularKotlinRule : ComponentMetadataRule {

    @javax.inject.Inject
    abstract fun getObjects(): ObjectFactory

    override fun execute(ctx: ComponentMetadataContext) {
        val id = ctx.details.id
        listOf("compile", "runtime").forEach { baseVariant ->
            ctx.details.addVariant("${baseVariant}Modular", baseVariant) {
                attributes {
                    attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, getObjects().named("modular-jar"))
                }
                withFiles {
                    removeAllFiles()
                    addFile("${id.name}-${id.version}-modular.jar")
                }
                withDependencies {
                    clear() // 'kotlin-stdlib-common' and  'annotations' are not modules and are also not needed
                }
            }
        }
    }
}