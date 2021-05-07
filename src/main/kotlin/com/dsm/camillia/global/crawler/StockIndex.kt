package com.dsm.camillia.global.crawler

import org.jsoup.select.Elements
import java.time.LocalDate

enum class StockIndex(val index: Int) {
    DATE(0),
    CLOSING_PRICE(1),
    DIFFERENCE_FROM_YESTERDAY(2),
    FLUCTUATION_RATE(3),
    OPENING_PRICE(4),
    HIGH_PRICE(5),
    LOW_PRICE(6),
    MARKET_CAPITALIZATION(7);
    

    fun extractStockInformation(elements: Elements) =
        elements[this.index].html()
            .replace("<span class=\"t_11_red\"><span>", "")
            .replace("<span class=\"t_11_blue\"><span>", "")
            .replace("<span class=\"t_11_black\"><span>", "")
            .replace("</span>", "")
            .replace("&nbsp;", "")
            .replace("▼", "-")
            .replace("▲", "")
            .replace(",", "")
            .replace("<strong class=\"arrow_bold\" title=\"상한\">↑</strong>", "")
            .replace(Regex("^[0-9]{2}/[0-9]{2}/[0-9]{2}$")) {
                "${LocalDate.now().year / 100}${it.value.replace("/", "-")}"
            }
}