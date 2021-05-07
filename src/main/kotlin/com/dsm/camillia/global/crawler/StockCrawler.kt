package com.dsm.camillia.global.crawler

import com.dsm.camillia.domain.company.service.CompanySearchService
import com.dsm.camillia.domain.stock.domain.Stock
import com.dsm.camillia.domain.stock.service.StockCreationService
import com.dsm.camillia.domain.stock.service.StockModificationService
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.springframework.stereotype.Component
import java.time.LocalDate

private const val DEFAULT_FIRST_PAGE_NUMBER = 1
private const val DEFAULT_LAST_PAGE_NUMBER = 7
private const val MAIN_CRAWLING_TARGET_URL =
    "https://vip.mk.co.kr/newSt/price/daily.php"
private const val MARKET_CAPITALIZATION_CRAWLING_TARGET_URL =
    "https://comp.fnguide.com/SVO2/ASP/SVD_Main.asp?pGB=1&cID=&MenuYn=Y&ReportGB=&NewMenuID=101&stkGb=701&gicode=A"

@Component
class StockCrawler(
    private val stockCreationService: StockCreationService,
    private val stockModificationService: StockModificationService,
    private val companySearchService: CompanySearchService,
) {

    fun crawlingStockInformation(
        tickerSymbol: String,
        firstPageNumber: Int = DEFAULT_FIRST_PAGE_NUMBER,
        lastPageNumber: Int = DEFAULT_LAST_PAGE_NUMBER,
    ) {
        val marketCapitalization = getMarketCapitalization(tickerSymbol)
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
                .map {
                    val decomposedStock = decompose(it).toMutableList()
                    decomposedStock.add(marketCapitalization)
                    decomposedStock
                }
                .map {
                    createStock(
                        stockInformation = it,
                        tickerSymbol = tickerSymbol,
                    )
                }
                .toList()
        )
    }

    fun crawlingTodayStockInformation(companyTickerSymbol: String) {
        val refinedStock = decompose(
            getStockInformation(
                tickerSymbol = companyTickerSymbol,
                page = 1,
                endDate = LocalDate.now(),
                startDate = LocalDate.now(),
            )[21]
        ).toMutableList()
        refinedStock.add(getMarketCapitalization(companyTickerSymbol))

        stockModificationService.modifyStock(
            createStock(
                stockInformation = refinedStock,
                tickerSymbol = companyTickerSymbol,
            )
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
        MAIN_CRAWLING_TARGET_URL +
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
            .filter { it.index < 7 }
            .map { it.extractStockInformation(stockInformation) }
            .toList()
    }

    private fun createStock(stockInformation: List<String>, tickerSymbol: String) =
        Stock(
            date = LocalDate.parse(stockInformation[StockIndex.DATE.index]),
            closingPrice = stockInformation[StockIndex.CLOSING_PRICE.index].toLong(),
            differenceFromYesterday = stockInformation[StockIndex.DIFFERENCE_FROM_YESTERDAY.index].toLong(),
            fluctuationRate = stockInformation[StockIndex.FLUCTUATION_RATE.index].toDouble(),
            openingPrice = stockInformation[StockIndex.OPENING_PRICE.index].toLong(),
            highPrice = stockInformation[StockIndex.HIGH_PRICE.index].toLong(),
            lowPrice = stockInformation[StockIndex.LOW_PRICE.index].toLong(),
            marketCapitalization = stockInformation[StockIndex.MARKET_CAPITALIZATION.index].toLong(),
            company = companySearchService.getCompanyByTickerSymbol(tickerSymbol),
        )

    private fun getMarketCapitalization(companyTickerSymbol: String) =
        Jsoup.connect(MARKET_CAPITALIZATION_CRAWLING_TARGET_URL + companyTickerSymbol)
            .get()
            .body()
            .getElementsByTag("td")
            .filter { it.hasClass("r") }
            .elementAt(6)
            .toString()
            .replace("<td class=\"r\">", "")
            .replace("<strong class=\"arrow_bold\" title=\"상한\">↑</strong>", "")
            .replace("</td>", "")
            .replace(",", "")
}