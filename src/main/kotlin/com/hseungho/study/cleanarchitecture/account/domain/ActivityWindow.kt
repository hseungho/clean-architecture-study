package com.hseungho.study.cleanarchitecture.account.domain

import org.springframework.lang.NonNull
import java.time.LocalDateTime
import java.util.*


data class ActivityWindow(
    private val _activities: MutableList<Activity>
) {

    constructor(@NonNull vararg activity: Activity) : this(mutableListOf(*activity))

    val activities: List<Activity>
        get() = Collections.unmodifiableList(this._activities)

    fun addActivity(activity: Activity) {
        this._activities.add(activity)
    }

    val startTimestamp: LocalDateTime
        get() = this._activities.minOfOrNull { it.timestamp }
            ?: throw IllegalStateException()

    val endTimestamp: LocalDateTime
        get() = this._activities.maxOfOrNull { it.timestamp }
            ?: throw IllegalStateException()

    fun calculateBalance(accountId: Account.AccountId): Money {
        val depositBalance = _activities
            .filter { it.targetAccountId == accountId }
            .map { it.money }
            .fold(Money.ZERO, Money::add)

        val withdrawalBalance = _activities
            .filter { it.sourceAccountId == accountId }
            .map { it.money }
            .fold(Money.ZERO, Money::add)

        return Money.add(depositBalance, withdrawalBalance.negate())
    }

}