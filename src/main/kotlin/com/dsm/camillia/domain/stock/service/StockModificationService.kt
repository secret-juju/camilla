package com.dsm.camillia.domain.stock.service

import com.dsm.camillia.domain.company.domain.Company
import com.dsm.camillia.domain.company.exception.CompanyNotFoundException
import com.dsm.camillia.domain.stock.domain.Stock
import com.dsm.camillia.domain.stock.repository.StockRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class StockModificationService(
    private val stockRepository: StockRepository,
) {

    @Transactional
    fun modifyStock(newStock: Stock) {
        val stock = getTodayStock(
            date = newStock.date,
            company = newStock.company,
        )

        if (stock == null)
            stockRepository.save(newStock)
        else
            stock.updateStock(
                closingPrice = newStock.closingPrice,
                differenceFromYesterday = newStock.differenceFromYesterday,
                fluctuationRate = newStock.fluctuationRate,
                openingPrice = newStock.openingPrice,
                highPrice = newStock.highPrice,
                lowPrice = newStock.lowPrice,
            )
    }

    private fun getTodayStock(date: LocalDate, company: Company) =
        stockRepository.findByDateAndCompanyId(
            date = date,
            companyId = company.id ?: throw CompanyNotFoundException(company.tickerSymbol)
        )
}