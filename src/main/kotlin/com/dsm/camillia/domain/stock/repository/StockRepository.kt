package com.dsm.camillia.domain.stock.repository

import com.dsm.camillia.domain.stock.domain.Stock
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface StockRepository : JpaRepository<Stock, Long> {
    fun findByDateAndCompanyId(date: LocalDate, companyId: Long): Stock?
}