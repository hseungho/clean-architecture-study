package com.hseungho.study.cleanarchitecture.account.domain

import com.hseungho.study.cleanarchitecture.common.AccountTestData
import com.hseungho.study.cleanarchitecture.common.ActivityTestData
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class AccountTest {

    @Test
    fun `withdrawalSucceeds`() {
        // given
        val accountId = Account.AccountId(1)
        val account = AccountTestData.defaultAccount()
            .withAccountId(accountId)
            .withBaselineBalance(Money.of(555))
            .withActivityWindow(
                ActivityWindow(
                    ActivityTestData.defaultActivity()
                        .withTargetAccount(accountId)
                        .withMoney(Money.of(999))
                        .build(),
                    ActivityTestData.defaultActivity()
                        .withTargetAccount(accountId)
                        .withMoney(Money.of(1))
                        .build()
                )
            ).build()
        // when
        val success = account.withdraw(Money.of(555),Account.AccountId(99))
        // then
        assertTrue(success)
        assertEquals(3, account.activityWindow.activities.size)
        assertEquals(Money.of(1000), account.calculateBalance())
    }

    @Test
    fun `withdrawalFailure`() {
        // given
        val accountId = Account.AccountId(1)
        val account = AccountTestData.defaultAccount()
            .withAccountId(accountId)
            .withBaselineBalance(Money.of(555))
            .withActivityWindow(
                ActivityWindow(
                    ActivityTestData.defaultActivity()
                        .withTargetAccount(accountId)
                        .withMoney(Money.of(999))
                        .build(),
                    ActivityTestData.defaultActivity()
                        .withTargetAccount(accountId)
                        .withMoney(Money.of(1))
                        .build()
                )
            ).build()
        // when
        val success = account.withdraw(Money.of(1556), Account.AccountId(99))
        // then
        assertFalse(success)
        assertEquals(2, account.activityWindow.activities.size)
        assertEquals(Money.of(1555), account.calculateBalance())
    }

    @Test
    fun `calculateBalance`() {
        // given
        val accountId = Account.AccountId(1)
        val account = AccountTestData.defaultAccount()
            .withAccountId(accountId)
            .withBaselineBalance(Money.of(555))
            .withActivityWindow(
                ActivityWindow(
                    ActivityTestData.defaultActivity()
                        .withTargetAccount(accountId)
                        .withMoney(Money.of(999))
                        .build(),
                    ActivityTestData.defaultActivity()
                        .withTargetAccount(accountId)
                        .withMoney(Money.of(1))
                        .build()
                )
            ).build()
        // when
        val balance = account.calculateBalance()
        // then
        assertEquals(Money.of(1555), balance)
    }

    @Test
    fun `depositSuccess`() {
        // given
        val accountId = Account.AccountId(1)
        val account = AccountTestData.defaultAccount()
            .withAccountId(accountId)
            .withBaselineBalance(Money.of(555))
            .withActivityWindow(
                ActivityWindow(
                    ActivityTestData.defaultActivity()
                        .withTargetAccount(accountId)
                        .withMoney(Money.of(999))
                        .build(),
                    ActivityTestData.defaultActivity()
                        .withTargetAccount(accountId)
                        .withMoney(Money.of(1))
                        .build()
                )
            ).build()
        // when
        val success = account.deposit(Money.of(445), Account.AccountId(99))
        // then
        assertTrue(success)
        assertEquals(3, account.activityWindow.activities.size)
        assertEquals(Money.of(2000), account.calculateBalance())
    }
}
