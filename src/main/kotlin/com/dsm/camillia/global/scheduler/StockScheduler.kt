package com.dsm.camillia.global.scheduler

import com.dsm.camillia.domain.company.service.CompanySearchService
import com.dsm.camillia.global.crawler.StockCrawler
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

private val log = LoggerFactory.getLogger(StockScheduler::class.java)

@Component
class StockScheduler(
    private val stockCrawler: StockCrawler,
    private val companySearchService: CompanySearchService,
) {

    @Scheduled(cron = "1 0/5 9-16 * * MON-FRI")
    fun createTodayStock() {
        log.info("[${LocalDateTime.now()}] 스케줄러 동작")
        val companyTickerSymbols = companySearchService.getAllCompany()
            .map { it.tickerSymbol }
        log.info("회사목록: $companyTickerSymbols")

        companyTickerSymbols.forEach {
            stockCrawler.crawlingTodayStockInformation(it)
        }
    }
}