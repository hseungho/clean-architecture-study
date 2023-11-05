package com.hseungho.study.cleanarchitecture.account.adapter.out.persistence

import com.hseungho.study.cleanarchitecture.account.application.port.out.LoadAccountPort
import com.hseungho.study.cleanarchitecture.account.application.port.out.UpdateAccountStatePort
import com.hseungho.study.cleanarchitecture.account.domain.Account
import com.hseungho.study.cleanarchitecture.common.PersistenceAdapter
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDateTime

@PersistenceAdapter
class AccountPersistenceAdapter(
    private val accountRepository: AccountRepository,
    private val activityRepository: ActivityRepository,
    private val accountMapper: AccountMapper
): LoadAccountPort, UpdateAccountStatePort {

    override fun load(
        accountId: Account.AccountId,
        baselineDate: LocalDateTime
    ): Account {
        val account =
            accountRepository.findByIdOrNull(accountId.value)
                ?: throw EntityNotFoundException()

        val activities =
            activityRepository.findByOwnerSince(
                accountId.value,
                baselineDate)

        val withdrawalBalance = orZero(activityRepository
            .getWithdrawalBalanceUntil(
                accountId.value,
                baselineDate))

        val depositBalance = orZero(activityRepository
            .getDepositBalanceUntil(
                accountId.value,
                baselineDate))

        return accountMapper.mapToDomainEntity(
            account = account,
            activities = activities,
            withdrawalBalance = withdrawalBalance,
            depositBalance = depositBalance
        )
    }

    private fun orZero(value: Long?): Long = value ?: 0L

    override fun updateActivities(
        account: Account
    ) {
        account.activityWindow.activities
            .filter { it.id == null }
            .forEach { activityRepository.save(accountMapper.mapToJpaEntity(it)) }
    }
}
