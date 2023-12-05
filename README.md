# ProGuard & R8 Dictionaries Generator

Gradle Plugin that generates randomized dictionaries for ProGuard/R8

**Tested on Android Gradle Plugin version 8.1 & 8.2**

## How to add
Add to your **root project's** `build.gradle(.kts)`:

For **Kotlin DSL**
```kotlin
// Using the plugins DSL (in the app module's build.gradle.kts):
plugins {
    id("ru.cleverpumpkin.proguard-dictionaries-generator") version "1.0.8"
}

// Using legacy plugin application (in the root project buildscript):
buildscript {
    repositories {
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }
    dependencies {
        classpath("gradle.plugin.ru.cleverpumpkin.proguard-dictionaries-generator:plugin:1.0.8")
    }
}
```

For **Groovy**
```groovy
// Using the plugins DSL (in the app module's build.gradle):
plugins {
    id "ru.cleverpumpkin.proguard-dictionaries-generator" version "1.0.8"
}

// Using legacy plugin application (in the root project buildscript):
buildscript {
    repositories {
        maven { 
            url "https://plugins.gradle.org/m2/" 
        }
    }

    dependencies {
        classpath "gradle.plugin.ru.cleverpumpkin.proguard-dictionaries-generator:plugin:1.0.8"
    }
}
```

## Simple configuration
Add to your **app module's** `build.gradle`:

For **Kotlin DSL**
```kotlin
plugins {
    // Only required with legacy plugin application
    id("ru.cleverpumpkin.proguard-dictionaries-generator")
}

proguardDictionaries {
    dictionaryNames = listOf(
         "build/class-dictionary",
         "build/package-dictionary",
         "build/obfuscation-dictionary"
      )
}
```

For **Groovy**
```groovy
// Only required with legacy plugin application
apply plugin: "ru.cleverpumpkin.proguard-dictionaries-generator"

proguardDictionaries {
    dictionaryNames = [
        "build/class-dictionary",
        "build/package-dictionary",
        "build/obfuscation-dictionary"
    ]
}
```

This setup will generate files `class-dictionary.txt`, `package-dictionary.txt` 
and `obfuscation-dictionary.txt` in `build` subdirectory of the module to which the plugin 
is applied. You can specify any directory **relatively to your module's root**. 

These files will be generated from scratch on every build, so you'll have different dictionaries 
for each build.

### Advanced configuration

For **Kotlin DSL**
```kotlin
proguardDictionaries {
    dictionaryNames = listOf("any", "files, "you", "want") // resolved relatively to project dir
    minLineLength = 20  // Default value: 10
    maxLineLength = 50  // Default value: 30
    linesCountInDictionary = 50000  // Default value: 30000
}
```

For **Groovy**
```groovy
proguardDictionaries {
    dictionaryNames = [ "any", "files, "you", "want" ] // resolved relatively to project dir
    minLineLength 20  // Default value: 10
    maxLineLength 50  // Default value: 30
    linesCountInDictionary 50000  // Default value: 30000
}
```

### How to use plugin output result

Add to your `proguard-rules.pro` file:

```
-obfuscationdictionary build/obfuscation-dictionary.txt
-classobfuscationdictionary build/class-dictionary.txt
-packageobfuscationdictionary build/package-dictionary.txt
```

## Program classes number issue

**Important:** if the final number of classes in `*.apk` after code shrinking **exceeds the 
`linesCountInDictionary` value**, then all remaining classes **will be named by default** – 
starting with first alphabet letters.

Thus, the `linesCountInDictionary` value must be greater than a number of program classes after 
code shrinking. 

The default size of the dictionary is `30000` lines.

You can find the number of classes in your `*.apk` by following next steps:

1. While performing build in Android Studio, find the last **Optimizing** step in the console,
e. g. "Optimizing (Step 5/5)".
2. Check the value in line "Final number of program classes".

Or:

1. Drag-n-drop an `*.apk` file into Android Studio (or open it via menu **Build** -> 
**Analyze APK...**).
2. In APK Analyzer window select `classes.dex` file. Check the value in the line 
"This dex file defines X classes..."
3. If more `*.dex` files present in APK, select each of them and sum up all classes numbers.

Finally, set the value of `linesCountInDictionary` slightly greater than the resulting number 
of program classes to keep an extra space for application grow (e. g. if program contains 
9802 classes, you can set the value 12000).


### ProGuard and R8 support

Plugin runs automatically when android plugin executes ProGuard or R8 task:

- `transformClassesAndResourcesWithR8For{BuiltFlavor}{BuiltType}`
- `transformClassesAndResourcesWithProguardFor{BuiltFlavor}{BuiltType}`

You don't have to specify anything special to get it work.


### Dictionary alphabet

Whitespaces, punctuation characters, duplicate words, and comments after a `#` sign are ignored 
in dictionaries by ProGuard. So generated file could contains any symbols except listed above.

<https://www.guardsquare.com/en/products/proguard/manual/usage#obfuscationoptions>

### Contribution

You need to test plugin locally, right?
To build plugin locally `uploadArchives` gradle task is used that puts plugin into 
`PROJECT_DIR/plugin/pluginRepo` folder.
Plugin should be added to project's buildscript.
First time after cloning the project there's no locally built plugin so plugin should be downloaded from Gradle Plugin portal thus artifactId is 
`gradle.plugin.ru.cleverpumpkin.proguard-dictionaries-generator:plugin`
After project sync upload plugin locally and set artifactId to `ru.cleverpumpkin.proguard-dictionaries-generator:plugin`.
You can find artifactId [here](https://github.com/CleverPumpkin/Proguard-Dictionaries-Generator/blob/master/buildSrc/src/main/kotlin/Dependencies.kt#L45)

## Maintainers
Updated to support Gradle configuration cache and maintained by Max Rumpf (@Maxr1998)

## Original Authors
Developed by Sergey Chuprin (<gregamer@gmail.com>)<br/>
Supported by Ruslan Arslanov (<arslanov.r.f@gmail.com>)