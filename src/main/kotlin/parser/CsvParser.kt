package com.example.parser

import com.example.model.Operation
import java.io.File

object CsvParser {

    fun parse(file: File): List<Operation> {
        return file.readLines()
            .map { it.trim() }
            .filter{ it.isNotBlank() }
            .map { line -> parseLine(line) }
    }

    private fun parseLine(line: String): Operation {
        val parts = line.split(";")
        return when(parts.size){
            3 -> Operation.Supply(
                groupId = parts[0],
                productId = parts[1],
                quantity = parts[2].toInt())

            2 -> Operation.Sale(
                groupId = parts[0],
                quantity = parts[1].toInt()
            )

            else -> throw IllegalArgumentException("Некорректная строка $line")
        }
    }
}