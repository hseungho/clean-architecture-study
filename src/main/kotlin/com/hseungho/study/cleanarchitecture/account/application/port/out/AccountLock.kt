package com.hseungho.study.cleanarchitecture.account.application.port.out

import com.hseungho.study.cleanarchitecture.account.domain.Account

interface AccountLock {

    fun lockAccount(accountId: Account.AccountId)

    fun releaseAccount(accountId: Account.AccountId)

}
