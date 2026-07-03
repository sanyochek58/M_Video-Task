package com.example.parser

import java.io.File

object CsvParser {

    val file: File = File("src/main/resources/info.csv")

    fun parse(): String{
        val lines: List<String> = file.readLines()

        for (line in lines) {
            println(line)
        }

        return lines.joinToString("\n")
    }
}