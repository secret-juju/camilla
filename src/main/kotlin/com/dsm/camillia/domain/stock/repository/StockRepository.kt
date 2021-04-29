package com.dsm.camillia.domain.stock.repository

import com.dsm.camillia.domain.stock.domain.Stock
import org.springframework.data.jpa.repository.JpaRepository

interface StockRepository : JpaRepository<Stock, Long>