plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.app)
    alias(libs.plugins.proguarddictionariesgenerator)
}

proguardDictionaries {
    dictionaryNames = listOf(
        "build/class-dictionary",
        "build/package-dictionary",
        "build/obfuscation-dictionary"
    )
}

android {
    namespace = "ru.cleverpumpkin.dictgen"

    compileSdk = 34

    defaultConfig {
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        maybeCreate("release").apply {
            isDebuggable = false
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("debug")

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                File("proguard-rules.pro")
            )
        }
        maybeCreate("debug").apply {
            isDebuggable = true
            isMinifyEnabled = false
        }
    }
    lint {
        abortOnError = false
        sarifReport = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
}