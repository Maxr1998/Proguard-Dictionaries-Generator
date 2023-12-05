plugins {
    alias(libs.plugins.kotlin.jvm)
    `java-gradle-plugin`
}

object PluginInfo {
    const val ID = "ru.cleverpumpkin.proguard-dictionaries-generator"
    const val IMPLEMENTATION_CLASS = "ru.cleverpumpkin.plugin.ProguardR8DictionaryGeneratorPlugin"
}

group = PluginInfo.ID
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