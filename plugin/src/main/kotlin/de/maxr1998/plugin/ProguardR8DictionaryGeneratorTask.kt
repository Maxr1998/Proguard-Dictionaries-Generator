package de.maxr1998.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFiles
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.util.Random
import java.util.concurrent.ThreadLocalRandom

/**
 * Created by Sergey Chuprin on 16/01/2019.
 */
abstract class ProguardR8DictionaryGeneratorTask : DefaultTask() {

    companion object {
        const val NAME = "generateProguardDictionaries"
    }

    @get:Input
    abstract val linesCountInDictionary: Property<Int>

    @get:Input
    abstract val minLineLength: Property<Int>

    @get:Input
    abstract val maxLineLength: Property<Int>

    @get:OutputFiles
    abstract val dictionaryFiles: ConfigurableFileCollection

    // All available symbols.
    // Whitespace, punctuation characters, duplicate words,
    // and comments after a # sign are ignored.
    private val alphabet = (('a'..'z') + ('A'..'Z') + ('0'..'9')).joinToString("")

    @TaskAction
    fun run() = dictionaryFiles.forEach(::generate)

    private fun generate(dictionaryFile: File) {
        val random = ThreadLocalRandom.current()

        val dictionarySet = (0 until linesCountInDictionary.get())
            .fold(mutableSetOf<String>()) { set, _ ->
                set.apply {
                    val lineLength = random.nextInt(minLineLength.get(), maxLineLength.get())
                    add(randomizeString(lineLength, alphabet, random))
                }
            }

        dictionaryFile.run {
            parentFile.mkdirs()
            writeText(dictionarySet.joinToString("\n"))
        }
    }

    private fun randomizeString(lineLength: Int, alphabet: String, random: Random): String {
        return (1..lineLength)
            .map { alphabet[random.nextInt(alphabet.length)] }
            .joinToString("")
    }
}