package com.dsm.camillia.domain.company.repository

import com.dsm.camillia.domain.company.domain.Company
import org.springframework.data.jpa.repository.JpaRepository

interface CompanyRepository : JpaRepository<Company, Long> {
    fun findByTickerSymbol(tickerSymbol: String): Company?
}