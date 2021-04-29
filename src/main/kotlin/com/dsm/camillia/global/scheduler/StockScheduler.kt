package com.dsm.camillia.global.scheduler

import com.dsm.camillia.global.crawler.StockCrawler
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class StockScheduler(
    private val stockCrawler: StockCrawler,
) {

    @Scheduled(cron = "1 0/5 9-4 * * MON-FRI")
    fun createTodayStock() {
        stockCrawler.crawlingTodayStockInformation(
            "005930"
        )
    }
}