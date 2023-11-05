package com.hseungho.study.cleanarchitecture.account.application.service

import com.hseungho.study.cleanarchitecture.account.application.port.`in`.GetAccountBalanceQuery
import com.hseungho.study.cleanarchitecture.account.application.port.out.LoadAccountPort
import com.hseungho.study.cleanarchitecture.account.domain.Account
import com.hseungho.study.cleanarchitecture.account.domain.Money
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

open class GetAccountBalanceService(
    private val loadAccountPort: LoadAccountPort
) : GetAccountBalanceQuery {

    @Transactional
    override fun getAccountBalance(accountId: Account.AccountId): Money {
        return loadAccountPort.load(accountId, LocalDateTime.now())
            .calculateBalance()
    }

}
