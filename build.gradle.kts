import dev.kikugie.stonecutter.StonecutterExperimentalAPI
import net.fabricmc.loom.task.RemapSourcesJarTask
import net.fabricmc.loom.task.ValidateAccessWidenerTask
import org.gradle.jvm.tasks.Jar as GradleJar
import org.gradle.jvm.toolchain.JavaLanguageVersion

plugins {
    idea
    java
    id("net.fabricmc.fabric-loom")
}

val minecraftVersion = project.name
val fullMinecraftVersion = when (minecraftVersion) {
    "26.1" -> "26.1.2"
    "26.2" -> "26.2"
    else -> throw GradleException("Unknown Minecraft version: $minecraftVersion")
}


val runDirectory = rootProject.file("run")
runDirectory.mkdirs()

// Minecraft configuration:
@OptIn(StonecutterExperimentalAPI::class)
loom {
    val accessWidenerFike = sc.process(
        rootProject.file("src/main/resources/moulconfig.accesswidener"),
        "build/moulconfig.accesswidener",
    )

    if (accessWidenerFike.exists()) {
        accessWidenerPath = accessWidenerFike
    } else {
        println("No accessWidner file for $minecraftVersion")
    }

    runs {
        named("client") {
            generateRunConfig.set(true)
            preferGradleTask = true
            appendProjectPathToDisplayName.set(true)
            this.runDirectory = rootProject.file("versions/$minecraftVersion/run").relativeTo(projectDir)
            jvmArguments.add("-Xmx4G")
        }
        removeIf { it.name == "server" }
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
    withSourcesJar()
    withJavadocJar()
}

tasks.named<JavaExec>("runClient") {
    this.javaLauncher.set(javaToolchains.launcherFor(java.toolchain))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.release = 25
}

tasks.withType<GradleJar> {
    archiveBaseName.set("MoulConfig")
    archiveVersion.set("$version-mc$fullMinecraftVersion")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

dependencies {
    minecraft("com.mojang:minecraft:$fullMinecraftVersion")
    compileOnly(libs.jbAnnotations)
    implementation("net.fabricmc.fabric-api:fabric-api:${libs.versions.fabric.api.get()}+$fullMinecraftVersion")

    implementation(libs.fabric.loader)

    testImplementation(libs.junit)
    testRuntimeOnly(libs.junit.launcher)
}

tasks.withType<RemapSourcesJarTask>().configureEach {
    enabled = false
}

tasks.withType<ValidateAccessWidenerTask>().configureEach {
    dependsOn("stonecutterPrepare")
}

repositories {
    mavenLocal()
    mavenCentral()
}