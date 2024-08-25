package com.project.stocksage.data.mapper

import com.project.stocksage.data.local.CompanyListingEntity
import com.project.stocksage.data.remote.dto.CompanyInfoDto
import com.project.stocksage.domain.model.CompanyInfo
import com.project.stocksage.domain.model.CompanyListing

fun CompanyListingEntity.toCompanyListing(): CompanyListing{
    return CompanyListing(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}

fun CompanyListing.toCompanyListingEntity(): CompanyListingEntity{
    return CompanyListingEntity(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}

fun CompanyInfoDto.toCompanyInfo(): CompanyInfo {
    return CompanyInfo(
        symbol = symbol ?: "N/A",
        description = description ?: "N/A",
        name = name ?: "N/A",
        country = country  ?: "N/A",
        industry = industry ?: "N/A"
    )

}