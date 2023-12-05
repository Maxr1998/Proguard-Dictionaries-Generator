plugins {
    alias(libs.plugins.kotlin.jvm)
    `java-gradle-plugin`
}

object PluginInfo {
    const val GROUP = "de.maxr1998"
    const val ID = "$GROUP.proguard-dictionaries-generator"
    const val IMPLEMENTATION_CLASS = "$GROUP.plugin.ProguardR8DictionaryGeneratorPlugin"
}

group = PluginInfo.GROUP
version = libs.versions.proguarddictionariesgenerator.get()

gradlePlugin {
    plugins {
        create("plugin") {
            version = libs.versions.proguarddictionariesgenerator.get()
            id = PluginInfo.ID
            displayName = "Proguard R8 Dictionaries Generator Plugin"
            implementationClass = PluginInfo.IMPLEMENTATION_CLASS
        }
    }
}

dependencies {
    compileOnly(gradleApi())
    compileOnly(libs.android.tools)
}