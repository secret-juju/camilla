package com.dsm.camillia.domain.stock.controller

import com.dsm.camillia.domain.stock.controller.request.StockInitializationRequest
import com.dsm.camillia.global.crawler.StockCrawler
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class StockController(
    private val stockCrawler: StockCrawler,
) {

    @PostMapping("/initialization")
    fun initializeStock(
        @Valid
        @RequestBody
        request: StockInitializationRequest,
    ) {
        request.tickerSymbol.forEach {
            stockCrawler.stockInformationCrawling(it)
        }
    }
}