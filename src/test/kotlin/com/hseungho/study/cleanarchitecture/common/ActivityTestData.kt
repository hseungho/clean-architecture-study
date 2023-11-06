package com.hseungho.study.cleanarchitecture.common

import com.hseungho.study.cleanarchitecture.account.domain.Account
import com.hseungho.study.cleanarchitecture.account.domain.Account.AccountId
import com.hseungho.study.cleanarchitecture.account.domain.Activity
import com.hseungho.study.cleanarchitecture.account.domain.Activity.ActivityId
import com.hseungho.study.cleanarchitecture.account.domain.Money
import java.time.LocalDateTime


object ActivityTestData {

    fun defaultActivity(): ActivityBuilder {
        return ActivityBuilder()
            .withOwnerAccount(AccountId(42))
            .withSourceAccount(AccountId(42))
            .withTargetAccount(AccountId(41))
            .withTimestamp(LocalDateTime.now())
            .withMoney(Money.of(999))
    }

    class ActivityBuilder {
        var id: Activity.ActivityId? = null
        lateinit var ownerAccountId: Account.AccountId
        lateinit var sourceAccountId: Account.AccountId
        lateinit var targetAccountId: Account.AccountId
        var timestamp: LocalDateTime = LocalDateTime.now()
        lateinit var money: Money

        fun withId(id: ActivityId?): ActivityBuilder {
            this.id = id!!
            return this
        }

        fun withOwnerAccount(accountId: AccountId): ActivityBuilder {
            ownerAccountId = accountId
            return this
        }

        fun withSourceAccount(accountId: AccountId): ActivityBuilder {
            sourceAccountId = accountId
            return this
        }

        fun withTargetAccount(accountId: AccountId): ActivityBuilder {
            targetAccountId = accountId
            return this
        }

        fun withTimestamp(timestamp: LocalDateTime): ActivityBuilder {
            this.timestamp = timestamp
            return this
        }

        fun withMoney(money: Money): ActivityBuilder {
            this.money = money
            return this
        }

        fun build(): Activity {
            requireNotNull(this.ownerAccountId)
            requireNotNull(this.targetAccountId)
            requireNotNull(this.money)
            return Activity(
                id = this.id,
                ownerAccountId = this.ownerAccountId,
                sourceAccountId = this.sourceAccountId,
                targetAccountId = this.targetAccountId,
                timestamp = this.timestamp,
                money = this.money
            )
        }
    }
}
