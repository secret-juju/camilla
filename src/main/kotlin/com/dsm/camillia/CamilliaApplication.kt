package com.dsm.camillia

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class CamilliaApplication

fun main(args: Array<String>) {
    runApplication<CamilliaApplication>(*args)
}
