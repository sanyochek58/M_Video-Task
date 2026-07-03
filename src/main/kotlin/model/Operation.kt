package com.example.model

sealed class Operation{
    data class Sale(val groupId: String, var quantity: Int ): Operation()
    data class Supply(val groupId: String, val productId: String, val quantity: Int): Operation()
}
