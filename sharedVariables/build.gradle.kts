plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

repositories {
    mavenCentral()
    mavenLocal()
}

sourceSets.main {
    kotlin.srcDir(file("src"))
}

gradlePlugin {
    plugins {
        create("simplePlugin") {
            id = "io.github.notenoughupdates.moulconfig.shared-variables"
            implementationClass = "io.github.notenoughupdates.moulconfig.sharedvariables.NoOp"
        }
    }
}
