package com.hseungho.study.cleanarchitecture.account.application.port.out

import com.hseungho.study.cleanarchitecture.account.domain.Account
import java.time.LocalDateTime

interface LoadAccountPort {

    fun load(accountId: Account.AccountId, baselineDate: LocalDateTime): Account

}
