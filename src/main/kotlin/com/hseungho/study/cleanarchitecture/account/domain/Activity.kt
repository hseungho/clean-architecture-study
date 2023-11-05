package com.hseungho.study.cleanarchitecture.account.domain

import org.springframework.lang.NonNull
import java.time.LocalDateTime

data class Activity(
    val id: ActivityId? = null,
    @NonNull
    val ownerAccountId: Account.AccountId,
    @NonNull
    val sourceAccountId: Account.AccountId,
    @NonNull
    val targetAccountId: Account.AccountId,
    @NonNull
    val timestamp: LocalDateTime = LocalDateTime.now(),
    @NonNull
    val money: Money
) {
    data class ActivityId(
        val value: Long
    )
}