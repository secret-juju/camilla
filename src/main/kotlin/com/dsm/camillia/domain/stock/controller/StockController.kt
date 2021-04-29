package com.dsm.camillia.domain.stock.controller

import com.dsm.camillia.domain.stock.controller.request.StockInitializationRequest
import com.dsm.camillia.global.crawler.StockCrawler
import org.springframework.web.bind.annotation.*
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
            stockCrawler.crawlingStockInformation(it)
        }
    }

    @GetMapping("/ticker-symbol/{tickerSymbol}")
    fun test(
        @PathVariable("tickerSymbol") tickerSymbol: String
    ) {
        stockCrawler.crawlingTodayStockInformation(tickerSymbol)
    }
}