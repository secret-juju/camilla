package com.dsm.camillia.repository

import com.dsm.camillia.domain.Stock
import org.springframework.data.jpa.repository.JpaRepository

interface StockRepository : JpaRepository<Stock, Long>