package com.example

import com.example.parser.CsvParser
import com.example.service.InventoryService
import com.example.writer.CsvWriter
import org.slf4j.LoggerFactory
import java.nio.file.Paths

private val log = LoggerFactory.getLogger("com.example.Main")

fun main(){

    val inputPath = Paths.get("src/main/resources/info.csv")
    val outputPath = Paths.get("src/main/resources/output.csv")

    log.info("Запуск обработки. Вход: {}, выход: {}", inputPath, outputPath)

    val operations = CsvParser.parse(inputPath.toFile())
    val service = InventoryService()

    service.apply(operations)

    CsvWriter.write(outputPath.toFile(), service.result())

    log.info("Обработка завершена")
}