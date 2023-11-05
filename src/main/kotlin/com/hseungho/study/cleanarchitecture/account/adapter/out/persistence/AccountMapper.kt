package com.hseungho.study.cleanarchitecture.account.adapter.out.persistence

import com.hseungho.study.cleanarchitecture.account.domain.Account
import com.hseungho.study.cleanarchitecture.account.domain.Activity
import com.hseungho.study.cleanarchitecture.account.domain.ActivityWindow
import com.hseungho.study.cleanarchitecture.account.domain.Money
import com.hseungho.study.cleanarchitecture.common.DomainMapper

@DomainMapper
class AccountMapper {

    internal fun mapToDomainEntity(
        account: AccountJpaEntity,
        activities: List<ActivityJpaEntity>,
        withdrawalBalance: Long,
        depositBalance: Long
    ): Account {
        val baselineBalance = Money.subtract(
            Money.of(depositBalance),
            Money.of(withdrawalBalance)
        )
        return Account(
            id = Account.AccountId(account.id),
            baselineBalance = baselineBalance,
            activityWindow = mapToActivityWindow(activities)
        )
    }

    internal fun mapToActivityWindow(
        activities: List<ActivityJpaEntity>
    ): ActivityWindow {
        val mappedActivities = activities
            .map { activity ->
                Activity(
                    id = Activity.ActivityId(activity.id ?: throw IllegalStateException()),
                    ownerAccountId = Account.AccountId(activity.ownerAccountId),
                    sourceAccountId = Account.AccountId(activity.sourceAccountId),
                    targetAccountId = Account.AccountId(activity.targetAccountId),
                    timestamp = activity.timestamp,
                    money = Money.of(activity.amount)
                )
            }.toMutableList()
        return ActivityWindow(mappedActivities)
    }

    internal fun mapToJpaEntity(
        activity: Activity
    ): ActivityJpaEntity {
        return ActivityJpaEntity(
            id = activity.id?.value,
            ownerAccountId = activity.ownerAccountId.value,
            sourceAccountId = activity.sourceAccountId.value,
            targetAccountId = activity.targetAccountId.value,
            timestamp = activity.timestamp,
            amount = activity.money.amount.longValueExact()
        )
    }

}
