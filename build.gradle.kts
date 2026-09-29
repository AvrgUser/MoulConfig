import dev.kikugie.stonecutter.StonecutterExperimentalAPI
import net.fabricmc.loom.task.RemapSourcesJarTask
import net.fabricmc.loom.task.ValidateAccessWidenerTask
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
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
    else -> minecraftVersion
}

val runDirectory = rootProject.file("run")
runDirectory.mkdirs()

// Minecraft configuration:
@OptIn(StonecutterExperimentalAPI::class)
loom {
    val accessWidenerFile = sc.process(
        rootProject.file("src/main/resources/moulconfig.accesswidener"),
        "build/moulconfig.accesswidener",
    )

    if (accessWidenerFile.exists()) {
        accessWidenerPath = accessWidenerFile
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
    archiveVersion.set("$version-mc$minecraftVersion")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

dependencies {
    minecraft("com.mojang:minecraft:$fullMinecraftVersion")
    compileOnly(libs.jbAnnotations)
    implementation("net.fabricmc.fabric-api:fabric-api:${libs.versions.fabric.api.get()}+$fullMinecraftVersion")

    implementation(libs.fabric.loader)
    implementation(libs.libninepatch)
    include(libs.libninepatch)

    annotationProcessor(libs.lombok)
    compileOnly(libs.lombok)

    testImplementation(libs.junit)
    testRuntimeOnly(libs.junit.launcher)
}

tasks.withType<RemapSourcesJarTask>().configureEach {
    enabled = false
}

tasks.withType<ValidateAccessWidenerTask>().configureEach {
    dependsOn("stonecutterGenerate")
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        showStackTraces = true
        exceptionFormat = TestExceptionFormat.FULL
    }
    javaLauncher.set(javaToolchains.launcherFor(java.toolchain))
    workingDir(file(runDirectory))
    systemProperty("junit.jupiter.extensions.autodetection.enabled", "true")
    jvmArgs(
        "--add-opens", "java.base/java.lang=ALL-UNNAMED",
        "--add-opens", "java.base/java.util=ALL-UNNAMED",
        "-XX:+EnableDynamicAgentLoading",
        // Tests start NPE-ing without this on Java 25
        "-Dnet.bytebuddy.experimental=true",
    )
}

repositories {
    mavenLocal()
    mavenCentral()
}
