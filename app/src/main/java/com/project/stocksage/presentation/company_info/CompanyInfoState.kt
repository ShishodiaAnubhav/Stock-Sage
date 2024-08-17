package com.project.stocksage.presentation.company_info

import com.project.stocksage.domain.model.CompanyInfo
import com.project.stocksage.domain.model.IntraDayInfo

data class CompanyInfoState (
    val stockInfo: List<IntraDayInfo> = emptyList(),
    val company: CompanyInfo? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)