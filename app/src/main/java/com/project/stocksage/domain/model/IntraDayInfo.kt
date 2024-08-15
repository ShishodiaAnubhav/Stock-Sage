package com.project.stocksage.domain.model

import java.time.LocalDateTime

data class IntraDayInfo(
    val data: LocalDateTime,
    val close: Double
)
