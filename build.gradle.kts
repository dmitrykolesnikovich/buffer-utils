import kx.Lwjgl
import kx.Lwjgl.Modules.jemalloc

plugins {
    val build = "0.7.3+8"
    id("kx.kotlin") version build
    //    id("kx.dokka") version build
    id("kx.publish") version build
}

dependencies {
    Lwjgl { implementation(jemalloc) }
}