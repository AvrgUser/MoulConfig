package io.github.notenoughupdates.moulconfig.sharedvariables

enum class ProjectTarget(
    val versionName: String,
    val fabricApiVersion: String,
    val fabricLoaderVersion: String,
    private val versionNameOverride: String? = null,
) {
    MC26_1(
        "26.1",
        fabricApiVersion = "net.fabricmc.fabric-api:fabric-api:0.155.2+26.1.2",
        fabricLoaderVersion = "net.fabricmc:fabric-loader:0.19.3",
        versionNameOverride = "26.1.2"
    ),
    MC26_2(
        "26.2",
        fabricApiVersion = "net.fabricmc.fabric-api:fabric-api:0.155.2+26.2",
        fabricLoaderVersion = "net.fabricmc:fabric-loader:0.19.3",
    ),
    MC26_3(
        "26.3",
        fabricApiVersion = "net.fabricmc.fabric-api:fabric-api:0.160.3+26.3",
        fabricLoaderVersion = "net.fabricmc:fabric-loader:0.19.3",
    ),

    ;

    val minecraftVersion get() = versionNameOverride ?: versionName

    /**
     * The version string used in fabric.mod.json's minecraft dependency field.
     * For versions using the new 26.x+ versioning scheme, a tilde is prepended to allow compatible patch versions.
     */
    val fabricModJsonVersion: String get() = "~${minecraftVersion.toSemVer()}"

    private fun String.toSemVer() = replace("-snapshot", "-alpha").replace(Regex("""-(\d+)$"""), ".$1")
}
