package com.dsm.camillia.domain.stock.controller

import com.dsm.camillia.domain.company.service.CompanySearchService
import com.dsm.camillia.global.crawler.StockCrawler
import org.springframework.web.bind.annotation.*

@RestController
class StockController(
    private val stockCrawler: StockCrawler,
    private val companySearchService: CompanySearchService,
) {

    @PostMapping("/initialization")
    fun initializeStock() =
        companySearchService.getAllCompany()
            .map { it.tickerSymbol }
            .forEach {
                stockCrawler.crawlingStockInformation(it)
            }

    @PutMapping("/initialization/today")
    fun modifyTodayStock() =
        companySearchService.getAllCompany()
            .map { it.tickerSymbol }
            .forEach {
                stockCrawler.crawlingTodayStockInformation(it)
            }
}