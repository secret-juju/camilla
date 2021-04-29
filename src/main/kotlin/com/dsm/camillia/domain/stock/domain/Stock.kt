package com.dsm.camillia.domain.stock.domain

import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "stock")
class Stock(
    val date: LocalDate,
    val closingPrice: Long,
    val differenceFromYesterday: Long,
    val fluctuationRate: Double,
    val openingPrice: Long,
    val highPrice: Long,
    val lowPrice: Long,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}