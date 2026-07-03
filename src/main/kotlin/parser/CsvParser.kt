package com.example.parser

import com.example.model.Operation
import org.slf4j.LoggerFactory
import java.io.File

object CsvParser {

    private val log = LoggerFactory.getLogger(CsvParser::class.java)

    fun parse(file: File): List<Operation> {
        log.info("Чтение файла операций: {}", file.path)
        val operations = file.readLines()
            .map { it.trim() }
            .filter{ it.isNotBlank() }
            .map { line -> parseLine(line) }
        log.info("Разобрано операций: {}", operations.size)
        return operations
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

            else -> {
                log.error("Некорректная строка: {}", line)
                throw IllegalArgumentException("Некорректная строка $line")
            }
        }
    }
}