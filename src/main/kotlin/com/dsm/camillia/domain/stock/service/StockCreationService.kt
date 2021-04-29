package com.dsm.camillia.service

import com.dsm.camillia.domain.Stock
import com.dsm.camillia.repository.StockRepository
import org.springframework.stereotype.Service

@Service
class StockCreationService(
    val stockRepository: StockRepository,
) {

    fun saveAllStock(stocks: List<Stock>) {
        stockRepository.saveAll(stocks)
    }
}