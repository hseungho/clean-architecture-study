package com.hseungho.study.cleanarchitecture.common

import com.hseungho.study.cleanarchitecture.account.domain.Account
import com.hseungho.study.cleanarchitecture.account.domain.ActivityWindow
import com.hseungho.study.cleanarchitecture.account.domain.Money

object AccountTestData {
    fun defaultAccount(): AccountBuilder {
        return AccountBuilder()
            .withAccountId(Account.AccountId(42))
            .withBaselineBalance(Money.of(999))
            .withActivityWindow(
                ActivityWindow(
                    ActivityTestData.defaultActivity().build(),
                    ActivityTestData.defaultActivity().build()
                )
            )
    }

    class AccountBuilder {
        lateinit var accountId: Account.AccountId
        lateinit var baselineBalance: Money
        lateinit var activityWindow: ActivityWindow

        fun withAccountId(accountId: Account.AccountId): AccountBuilder {
            this.accountId = accountId
            return this
        }

        fun withBaselineBalance(baselineBalance: Money): AccountBuilder {
            this.baselineBalance = baselineBalance
            return this
        }

        fun withActivityWindow(activityWindow: ActivityWindow): AccountBuilder {
            this.activityWindow = activityWindow
            return this
        }

        fun build(): Account {
            requireNotNull(this.accountId)
            requireNotNull(this.baselineBalance)
            requireNotNull(this.activityWindow)
            return Account(
                id = this.accountId,
                baselineBalance = this.baselineBalance,
                activityWindow = this.activityWindow
            )
        }
    }
}