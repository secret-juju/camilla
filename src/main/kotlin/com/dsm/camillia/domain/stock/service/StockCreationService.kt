package com.dsm.camillia.domain.stock.service

import com.dsm.camillia.domain.stock.domain.Stock
import com.dsm.camillia.domain.stock.repository.StockRepository
import org.springframework.stereotype.Service

@Service
class StockCreationService(
    private val stockRepository: StockRepository,
) {

    fun saveStock(stock: Stock) {
        stockRepository.save(stock)
    }

    fun saveAllStock(stocks: List<Stock>) {
        stockRepository.saveAll(stocks)
    }
}