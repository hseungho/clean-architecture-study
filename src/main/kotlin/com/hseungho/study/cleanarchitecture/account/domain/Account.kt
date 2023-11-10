package com.hseungho.study.cleanarchitecture.account.domain

data class Account(
    val id: AccountId,
    val baselineBalance: Money,
    val activityWindow: ActivityWindow
) {

    fun calculateBalance(): Money {
        return Money.add(
            this.baselineBalance,
            this.activityWindow.calculateBalance(this.id)
        )
    }

    fun withdraw(money: Money, targetAccountId: AccountId): Boolean {
        if (!mayWithdraw(money)) {
            return false
        }
        val withdrawal = Activity(
            ownerAccountId = this.id,
            sourceAccountId = this.id,
            targetAccountId = targetAccountId,
            money = money
        )
        this.activityWindow.addActivity(withdrawal)
        return true
    }

    private fun mayWithdraw(money: Money): Boolean {
        return Money.add(this.calculateBalance(), money.negate())
            .isPositiveOrZero()
    }

    fun deposit(money: Money, sourceAccountId: AccountId): Boolean {
        val deposit = Activity(
            ownerAccountId = this.id,
            sourceAccountId = sourceAccountId,
            targetAccountId = this.id,
            money = money
        )
        this.activityWindow.addActivity(deposit)
        return true
    }

    data class AccountId(
        val value: Long
    )
}