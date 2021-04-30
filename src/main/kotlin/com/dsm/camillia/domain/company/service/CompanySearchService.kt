package com.dsm.camillia.domain.company.service

import com.dsm.camillia.domain.company.domain.Company
import com.dsm.camillia.domain.company.exception.CompanyNotFoundException
import com.dsm.camillia.domain.company.repository.CompanyRepository
import org.springframework.stereotype.Service

@Service
class CompanySearchService(
    private val companyRepository: CompanyRepository,
) {

    fun getCompanyByTickerSymbol(tickerSymbol: String) =
        companyRepository.findByTickerSymbol(tickerSymbol)
            ?: throw CompanyNotFoundException(tickerSymbol)

    fun getAllCompany(): List<Company> = companyRepository.findAll()
}