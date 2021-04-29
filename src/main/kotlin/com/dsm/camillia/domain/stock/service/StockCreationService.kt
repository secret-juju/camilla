package com.dsm.camillia.domain.stock.service

import com.dsm.camillia.domain.stock.domain.Stock
import com.dsm.camillia.domain.stock.repository.StockRepository
import org.springframework.stereotype.Service

@Service
class StockCreationService(
    val stockRepository: StockRepository,
) {

    fun saveAllStock(stocks: List<Stock>) {
        stockRepository.saveAll(stocks)
    }
}