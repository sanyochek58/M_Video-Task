package com.example.writer

import com.example.model.Stock
import org.slf4j.LoggerFactory
import java.io.File
import kotlin.comparisons.compareBy

object CsvWriter {
    private const val DELIMITER = ";"
    private val log = LoggerFactory.getLogger(CsvWriter::class.java)

    fun write(file: File, stocks: List<Stock>) {
        val sorted = stocks.sortedWith(compareBy({ it.groupId }, { it.productId }))

        val text = sorted.joinToString("\n") { stock ->
            "${stock.groupId}$DELIMITER${stock.productId}$DELIMITER${stock.quantity}"
        }

        file.writeText(text)
        log.info("Записано позиций: {} в файл {}", sorted.size, file.path)
    }
}