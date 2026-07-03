package com.example.service

import com.example.model.Operation
import com.example.model.Stock
import org.slf4j.LoggerFactory

class InventoryService{
    private val stocks: MutableList<Stock> = mutableListOf()
    private val log = LoggerFactory.getLogger(InventoryService::class.java)

    fun apply(operations: List<Operation>) {
        for (op in operations) {
            when (op) {
                is Operation.Supply -> supply(op)
                is Operation.Sale -> sale(op)
            }
        }
        log.info("Обработано операций ${operations.size}. Итоговых позиций ${stocks.size}")
    }

    private fun supply(operation: Operation.Supply) {
        val existing = stocks.find {it.groupId == operation.groupId && it.productId == operation.productId}
        if (existing != null) {
            existing.quantity += operation.quantity
        }else{
            stocks.add(Stock(operation.groupId, operation.productId, operation.quantity))
        }
    }

    private fun sale(operation: Operation.Sale) {
        val groupProducts = stocks
            .filter { it.groupId == operation.groupId }
            .sortedBy { it.productId }

        if (groupProducts.isNotEmpty()) {
            log.warn("Продажа по группе '{}' без остатков — операция пропущена", operation.groupId)
            return
        }

        var remaining = operation.quantity
        for (stock in groupProducts) {
            if (remaining == 0) break
            if (stock.quantity >= remaining) {
                stock.quantity -= remaining
                remaining = 0
            } else {
                remaining -= stock.quantity
                stock.quantity = 0
            }
        }

        if (remaining > 0) {
            val last = groupProducts.last()
            last.quantity -= remaining
        }
    }
}