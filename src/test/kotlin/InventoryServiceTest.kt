package com.example

import com.example.model.Operation
import com.example.service.InventoryService
import kotlin.test.Test
import kotlin.test.assertEquals

class InventoryServiceTest {

    private fun InventoryService.balance(group: String, product: String): Int =
        result().find { it.groupId == group && it.productId == product }?.quantity ?: 0

    @Test
    fun sale_deducts_from_highest_rank_cascading() {
        val service = InventoryService()
        service.apply(
            listOf(
                Operation.Supply("G1", "apple", 10),
                Operation.Supply("G1", "banana", 5),
                Operation.Supply("G1", "cherry", 3),
                Operation.Sale("G1", 12)
            )
        )
        assertEquals(0, service.balance("G1", "apple"))   // продан полностью
        assertEquals(3, service.balance("G1", "banana"))  // 5 - 2
        assertEquals(3, service.balance("G1", "cherry"))  // не тронут
    }

    @Test
    fun sale_exceeding_stock_goes_negative_on_last_product() {
        val service = InventoryService()
        service.apply(
            listOf(
                Operation.Supply("G2", "keyboard", 4),
                Operation.Supply("G2", "mouse", 6),
                Operation.Sale("G2", 15)
            )
        )
        assertEquals(0, service.balance("G2", "keyboard"))
        assertEquals(-5, service.balance("G2", "mouse"))
    }

    @Test
    fun repeated_supply_accumulates_quantity() {
        val service = InventoryService()
        service.apply(
            listOf(
                Operation.Supply("G3", "tv", 7),
                Operation.Supply("G3", "tv", 3),
                Operation.Supply("G3", "radio", 5),
                Operation.Sale("G3", 8)
            )
        )
        assertEquals(0, service.balance("G3", "radio"))
        assertEquals(7, service.balance("G3", "tv"))
    }

    @Test
    fun interleaved_operations_produce_correct_result() {
        val service = InventoryService()
        service.apply(
            listOf(
                Operation.Supply("G4", "apple", 5),
                Operation.Sale("G4", 3),
                Operation.Supply("G4", "banana", 2),
                Operation.Sale("G4", 1)
            )
        )
        assertEquals(1, service.balance("G4", "apple"))
        assertEquals(2, service.balance("G4", "banana"))
    }

    @Test
    fun sale_equal_to_stock_zeroes_product() {
        val service = InventoryService()
        service.apply(
            listOf(
                Operation.Supply("G5", "item", 10),
                Operation.Sale("G5", 10)
            )
        )
        assertEquals(0, service.balance("G5", "item"))
    }

    @Test
    fun groups_are_independent_of_each_other() {
        val service = InventoryService()
        service.apply(
            listOf(
                Operation.Supply("A", "apple", 5),
                Operation.Supply("B", "apple", 10),
                Operation.Sale("A", 2)
            )
        )
        assertEquals(3, service.balance("A", "apple"))
        assertEquals(10, service.balance("B", "apple"))
    }

    @Test
    fun sale_on_empty_group_creates_nothing() {
        val service = InventoryService()
        service.apply(listOf(Operation.Sale("EMPTY", 5)))
        assertEquals(0, service.result().size)
    }
}