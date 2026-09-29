plugins {
    alias(libs.plugins.loom) apply false
    id("dev.kikugie.stonecutter")
}


allprojects {
    group = "io.github.notenoughupdates.moulconfig"


    val buildToolsPath = when (name) {
        "MoulConfig" -> layout.projectDirectory.dir("buildTools")
        else -> layout.projectDirectory.dir("../../buildTools")
    }

    /**
     * The version of the project.
     * Stable version
     * Beta version
     * Bugfix version
     */
    version = providers.fileContents(buildToolsPath.file("PROJECT_VERSION")).asText.map { it.trim() }.get()

    repositories {
        mavenCentral()
        mavenLocal()

        // Fabric
        exclusiveContent {
            forRepository {
                maven("https://maven.fabricmc.net")
            }
            filter {
                includeGroupAndSubgroups("net.fabricmc")
            }
        }
    }
}


stonecutter active "26.3"
