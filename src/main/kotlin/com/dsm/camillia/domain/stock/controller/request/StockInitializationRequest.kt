package com.dsm.camillia.domain.stock.controller.request

data class StockInitializationRequest(
    val tickerSymbol: List<String>
)