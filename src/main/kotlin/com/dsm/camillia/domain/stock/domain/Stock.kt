package com.dsm.camillia.domain.stock.domain

import com.dsm.camillia.domain.company.domain.Company
import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "stock")
class Stock(

    @Column(name = "date")
    val date: LocalDate,

    closingPrice: Long,
    differenceFromYesterday: Long,
    fluctuationRate: Double,
    openingPrice: Long,
    highPrice: Long,
    lowPrice: Long,

    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    val company: Company,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null

    @Column(name = "closing_price")
    var closingPrice = closingPrice
        private set

    @Column(name = "difference_from_yesterday")
    var differenceFromYesterday = differenceFromYesterday
        private set

    @Column(name = "fluctuation_rate")
    var fluctuationRate = fluctuationRate
        private set

    @Column(name = "opening_price")
    var openingPrice = openingPrice
        private set

    @Column(name = "high_price")
    var highPrice = highPrice
        private set

    @Column(name = "low_price")
    var lowPrice = lowPrice
        private set

    fun updateStock(
        closingPrice: Long,
        differenceFromYesterday: Long,
        fluctuationRate: Double,
        openingPrice: Long,
        highPrice: Long,
        lowPrice: Long,
    ) {
        this.closingPrice = closingPrice
        this.differenceFromYesterday = differenceFromYesterday
        this.fluctuationRate = fluctuationRate
        this.openingPrice = openingPrice
        this.highPrice = highPrice
        this.lowPrice = lowPrice
    }
}