plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.gradle.publish)
    signing
}

object PluginInfo {
    const val GROUP = "de.maxr1998"
    const val ID = "$GROUP.proguard-dictionaries-generator"
    const val IMPLEMENTATION_CLASS = "$GROUP.plugin.ProguardR8DictionaryGeneratorPlugin"
    const val GIT_REPO = "https://github.com/Maxr1998/Proguard-Dictionaries-Generator"
}

group = PluginInfo.GROUP
version = libs.versions.proguarddictionariesgenerator.get()

gradlePlugin {
    website = PluginInfo.GIT_REPO
    vcsUrl = "${PluginInfo.GIT_REPO}.git"

    plugins {
        create("proguardDictionariesGeneratorPlugin") {
            id = PluginInfo.ID
            displayName = "Proguard R8 Dictionaries Generator Plugin"
            description = "Gradle Plugin that generates randomized dictionaries for ProGuard/R8"
            version = libs.versions.proguarddictionariesgenerator.get()
            implementationClass = PluginInfo.IMPLEMENTATION_CLASS

            tags = listOf(
                "android",
                "dictionary",
                "generator",
                "minification",
                "obfuscation",
                "proguard",
                "R8",
                "shrinking",
            )
        }
    }
}

dependencies {
    compileOnly(gradleApi())
    compileOnly(libs.android.tools)
}

signing {
    useGpgCmd()
    sign(publishing.publications)
}