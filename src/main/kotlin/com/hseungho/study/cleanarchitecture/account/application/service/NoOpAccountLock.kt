package com.hseungho.study.cleanarchitecture.account.application.service

import com.hseungho.study.cleanarchitecture.account.application.port.out.AccountLock
import com.hseungho.study.cleanarchitecture.account.domain.Account
import org.springframework.stereotype.Component

@Component
class NoOpAccountLock : AccountLock {

    override fun lockAccount(accountId: Account.AccountId) {
        // do nothing
    }

    override fun releaseAccount(accountId: Account.AccountId) {
        // do nothing
    }

}
