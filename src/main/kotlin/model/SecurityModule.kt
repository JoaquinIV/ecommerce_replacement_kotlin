package model

import model.security.TokenService
import model.security.dao.TokenDao
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import utils.http.HttpTools

val securityModule = module {
    singleOf(::TokenService)
    singleOf(::TokenDao)
    singleOf(::HttpTools)
}
