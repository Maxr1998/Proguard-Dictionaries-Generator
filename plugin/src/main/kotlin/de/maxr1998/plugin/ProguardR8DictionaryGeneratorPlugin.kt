package de.maxr1998.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

/**
 * Created by Sergey Chuprin on 16/01/2019.
 */
abstract class ProguardR8DictionaryGeneratorPlugin : Plugin<Project> {

    private companion object {
        const val LOG_TAG = "ProguardR8DictionaryGenerator"
        val TARGET_TASKS_REGEX = listOf(
            Regex("minify(.*?)With(?:R8|Proguard)"),
            Regex("transformClassesAndResourcesWith(?:R8|Proguard)For.*?")
        )
    }

    override fun apply(project: Project) {
        with(project) {
            extensions.add(
                ProguardR8DictionaryPluginExtension.NAME,
                ProguardR8DictionaryPluginExtension::class.java
            )
            afterEvaluate {
                setupPlugin()
            }
        }
    }

    private fun Project.setupPlugin() {
        val pluginExtension = findPluginExtension()
        val obfuscationTasks = findObfuscationTasks()

        if (obfuscationTasks.isEmpty()) {
            logger.lifecycle("$LOG_TAG: neither proguard tasks, nor R8 tasks were not found")
            return
        }
        if (pluginExtension.dictionaryNames.isEmpty()) {
            logger.lifecycle("$LOG_TAG: you've applied plugin, but didn't specified dictionary names")
            return
        }

        val taskProvider = tasks.register(
            ProguardR8DictionaryGeneratorTask.NAME,
            ProguardR8DictionaryGeneratorTask::class.java,
        ) { task ->
            task.linesCountInDictionary.set(pluginExtension.linesCountInDictionary)
            task.minLineLength.set(pluginExtension.minLineLength.coerceAtLeast(1))
            task.maxLineLength.set(pluginExtension.maxLineLength.coerceAtLeast(1))
            task.dictionaryFiles.setFrom(pluginExtension.dictionaryNames.map { name -> "$name.txt" })
        }

        obfuscationTasks.forEach { task ->
            logger.lifecycle("$LOG_TAG: applying dictionaries dependency to task ${task.name}")
            task.dependsOn(taskProvider.get())
        }
    }

    private fun Project.findObfuscationTasks(): List<Task> {
        return TARGET_TASKS_REGEX.flatMap { taskNameRegex ->
            tasks.filter { task ->
                taskNameRegex.matches(task.name)
            }
        }
    }

    private fun Project.findPluginExtension(): ProguardR8DictionaryPluginExtension {
        return extensions.getByType(ProguardR8DictionaryPluginExtension::class.java)
    }
}