import dev.kikugie.stonecutter.StonecutterExperimentalAPI
import io.github.notenoughupdates.moulconfig.sharedvariables.ProjectTarget
import net.fabricmc.loom.task.RemapSourcesJarTask
import net.fabricmc.loom.task.ValidateAccessWidenerTask
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.jvm.tasks.Jar as GradleJar

plugins {
    idea
    java
    id("net.fabricmc.fabric-loom")
}

val target = ProjectTarget.entries.find { it.versionName == project.name }!!

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
        println("No accessWidner file for ${target.versionName}")
    }

    runs {
        named("client") {
            generateRunConfig.set(true)
            preferGradleTask = true
            appendProjectPathToDisplayName.set(true)
            this.runDirectory = rootProject.file("versions/${target.versionName}/run").relativeTo(projectDir)
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
    archiveVersion.set("$version-mc${target.versionName}")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.processResources {
    val fapiVersion = target.fabricApiVersion.split(":").last()
    val floaderVersion = target.fabricLoaderVersion.split(":").last()
    val minecraftVersion = target.fabricModJsonVersion
    val props = buildMap {
        put("version", version)
        put("minecraft", minecraftVersion)
        put("fapi", fapiVersion)
        put("floader", floaderVersion)
    }

    props.forEach(inputs::property)

    filesMatching("fabric.mod.json") {
        expand(props)
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${target.minecraftVersion}")
    compileOnly(libs.jbAnnotations)
    implementation(target.fabricApiVersion)

    implementation(target.fabricLoaderVersion)
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
