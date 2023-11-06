package com.hseungho.study.cleanarchitecture.account.application.service

import com.hseungho.study.cleanarchitecture.account.application.port.`in`.SendMoneyCommand
import com.hseungho.study.cleanarchitecture.account.application.port.`in`.SendMoneyUseCase
import com.hseungho.study.cleanarchitecture.account.application.port.out.AccountLock
import com.hseungho.study.cleanarchitecture.account.application.port.out.LoadAccountPort
import com.hseungho.study.cleanarchitecture.account.application.port.out.UpdateAccountStatePort
import com.hseungho.study.cleanarchitecture.common.UseCase
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@UseCase
class SendMoneyService(
    private val loadAccountPort: LoadAccountPort,
    private val updateAccountStatePort: UpdateAccountStatePort,
    private val accountLock: AccountLock
) : SendMoneyUseCase {

    @Transactional
    override fun sendMoney(command: SendMoneyCommand): Boolean {

        checkThreshold(command)

        val baselineDate = LocalDateTime.now().minusDays(10)

        val sourceAccount = loadAccountPort.loadAccount(
            command.sourceAccountId,
            baselineDate
        )
        val targetAccount = loadAccountPort.loadAccount(
            command.targetAccountId,
            baselineDate
        )

        val sourceAccountId = sourceAccount.id
        val targetAccountId = targetAccount.id

        accountLock.lockAccount(sourceAccountId)
        if (!sourceAccount.withdraw(command.money, targetAccountId)) {
            accountLock.releaseAccount(sourceAccountId)
            return false
        }

        accountLock.lockAccount(targetAccountId)
        if (!targetAccount.deposit(command.money, sourceAccountId)) {
            accountLock.releaseAccount(targetAccountId)
            return false
        }

        updateAccountStatePort.updateActivities(sourceAccount)
        updateAccountStatePort.updateActivities(targetAccount)

        accountLock.releaseAccount(sourceAccountId)
        accountLock.releaseAccount(targetAccountId)

        return true
    }

    private fun checkThreshold(command: SendMoneyCommand) {
        if (command.money.isGreaterThan(MoneyTransferProperties.maximumTransferThreshold)) {
            throw ThresholdExceedException(MoneyTransferProperties.maximumTransferThreshold, command.money)
        }
    }

}
