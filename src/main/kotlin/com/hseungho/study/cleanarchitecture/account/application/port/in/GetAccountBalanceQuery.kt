package com.hseungho.study.cleanarchitecture.account.application.port.`in`

import com.hseungho.study.cleanarchitecture.account.domain.Account
import com.hseungho.study.cleanarchitecture.account.domain.Money

interface GetAccountBalanceQuery {

    fun getAccountBalance(accountId: Account.AccountId): Money

}
