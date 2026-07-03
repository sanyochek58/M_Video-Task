package com.example

import com.example.parser.CsvParser
import java.nio.file.Paths

fun main(){

    val inputPath = Paths.get("src/main/resources/info.csv")
    val operations = CsvParser.parse(inputPath.toFile())

    operations.forEach { operation -> println(operation) }
}