package com.dsm.camillia.global.crawler

import com.dsm.camillia.domain.stock.domain.Stock
import com.dsm.camillia.domain.stock.service.StockCreationService
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.springframework.stereotype.Component
import java.time.LocalDate

private const val DEFAULT_FIRST_PAGE_NUMBER = 1
private const val DEFAULT_LAST_PAGE_NUMBER = 7
private const val CRAWLING_TARGET_URL = "https://vip.mk.co.kr/newSt/price/daily.php"

@Component
class StockCrawler(
    private val stockCreationService: StockCreationService,
) {

    fun stockInformationCrawling(
        tickerSymbol: String,
        firstPageNumber: Int = DEFAULT_FIRST_PAGE_NUMBER,
        lastPageNumber: Int = DEFAULT_LAST_PAGE_NUMBER,
    ) {
        stockCreationService.saveAllStock(
            (firstPageNumber..lastPageNumber)
                .asSequence()
                .map {
                    getStockInformation(
                        tickerSymbol = tickerSymbol,
                        page = it,
                    )
                }
                .map { it.subList(21, it.size - 1) }
                .map { it.subList(0, it.size - 17) }
                .flatten()
                .map { decompose(it) }
                .map {
                    Stock(
                        date = LocalDate.parse(it[StockIndex.DATE.index]),
                        closingPrice = it[StockIndex.DATE.index].toLong(),
                        differenceFromYesterday = it[StockIndex.DIFFERENCE_FROM_YESTERDAY.index].toLong(),
                        fluctuationRate = it[StockIndex.FLUCTUATION_RATE.index].toDouble(),
                        openingPrice = it[StockIndex.OPENING_PRICE.index].toLong(),
                        highPrice = it[StockIndex.HIGH_PRICE.index].toLong(),
                        lowPrice = it[StockIndex.LOW_PRICE.index].toLong(),
                    )
                }
                .toList()
        )
    }

    private fun getStockInformation(
        tickerSymbol: String,
        page: Int,
        endDate: LocalDate = LocalDate.now(),
        startDate: LocalDate =
            if (endDate.dayOfYear < 365) LocalDate.ofYearDay(endDate.year - 1, endDate.dayOfYear + 1)
            else LocalDate.ofYearDay(endDate.year, 1)
    ) = Jsoup.connect(
        CRAWLING_TARGET_URL +
                "?stCode=$tickerSymbol" +
                "&p_page=$page" +
                "&y1=${startDate.year}" +
                "&m1=${String.format("%02d", startDate.monthValue)}" +
                "&d1=${String.format("%02d", startDate.dayOfMonth)}" +
                "&y2=${endDate.year}" +
                "&m2=${String.format("%02d", endDate.monthValue)}" +
                "&d2=${String.format("%02d", endDate.dayOfMonth)}"
    ).get().body().getElementsByTag("tr").toList()

    private fun decompose(element: Element): List<String> {
        val stockInformation = element.getElementsByTag("td")
        return StockIndex.values()
            .map { it.extractStockInformation(stockInformation) }
            .toList()
    }
}