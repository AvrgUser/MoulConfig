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

enum class ProjectTarget(
    val projectName: String,
    val fabricApiVersion: String,
    private val minecraftVersionOverride: String? = null,
    ) {
    MC26_1(
        "26.1",
        fabricApiVersion = "net.fabricmc.fabric-api:fabric-api:0.155.2+26.1.2",
        minecraftVersionOverride = "26.1.2"
    ),
    MC26_2(
        "26.2",
        fabricApiVersion = "net.fabricmc.fabric-api:fabric-api:0.155.2+26.2",
    ),
    MC26_3(
        "26.3",
        fabricApiVersion = "net.fabricmc.fabric-api:fabric-api:0.160.3+26.3",
    ),

    ;

    val fullMinecraftVersion get() = minecraftVersionOverride ?: projectName
}

val target = ProjectTarget.entries.find { it.projectName == project.name }!!

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
        println("No accessWidner file for ${target.projectName}")
    }

    runs {
        named("client") {
            generateRunConfig.set(true)
            preferGradleTask = true
            appendProjectPathToDisplayName.set(true)
            this.runDirectory = rootProject.file("versions/${target.projectName}/run").relativeTo(projectDir)
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
    archiveVersion.set("$version-mc${target.projectName}")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

dependencies {
    minecraft("com.mojang:minecraft:${target.fullMinecraftVersion}")
    compileOnly(libs.jbAnnotations)
    implementation(target.fabricApiVersion)

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
