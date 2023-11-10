package com.hseungho.study.cleanarchitecture.account.application.service

import com.hseungho.study.cleanarchitecture.account.application.port.`in`.SendMoneyCommand
import com.hseungho.study.cleanarchitecture.account.application.port.out.AccountLock
import com.hseungho.study.cleanarchitecture.account.application.port.out.LoadAccountPort
import com.hseungho.study.cleanarchitecture.account.application.port.out.UpdateAccountStatePort
import com.hseungho.study.cleanarchitecture.account.domain.Account
import com.hseungho.study.cleanarchitecture.account.domain.Money
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
class SendMoneyServiceTest {

    @MockK
    private lateinit var loadAccountPort: LoadAccountPort

    @MockK
    private lateinit var updateAccountStatePort: UpdateAccountStatePort

    @MockK
    private lateinit var accountLock: AccountLock

    @InjectMockKs
    private lateinit var service: SendMoneyService

    @Test
    fun `givenWithdrawalFails_thenOnlySourceAccountIsLockedAndReleased`() {
        // given
        val sourceAccount = givenSourceAccount()
        val sourceAccountId = sourceAccount.id

        val targetAccount = givenTargetAccount()
        val targetAccountId = targetAccount.id

        givenWithdrawalWillFail(sourceAccount)
        givenLockAccountExecuted(sourceAccountId)
        givenReleaseAccountExecuted(sourceAccountId)

        givenDepositWillSucceed(targetAccount)

        // when
        val command = SendMoneyCommand(
            sourceAccountId = sourceAccountId,
            targetAccountId = targetAccountId,
            money = Money.of(300)
        )
        val success = service.sendMoney(command)

        // then
        assertFalse(success)

        verify(exactly = 1) { accountLock.lockAccount(sourceAccountId) }
        verify(exactly = 1) { accountLock.releaseAccount(sourceAccountId) }
        verify(exactly = 0) { accountLock.lockAccount(targetAccountId) }
    }

    @Test
    fun `transactionSucceeds`() {
        // given
        val sourceAccount = givenSourceAccount()
        val targetAccount = givenTargetAccount()

        givenWithdrawalWillSucceed(sourceAccount)
        givenDepositWillSucceed(targetAccount)

        givenLockAccountExecuted(sourceAccount.id)
        givenLockAccountExecuted(targetAccount.id)

        givenUpdateActivities(sourceAccount)
        givenUpdateActivities(targetAccount)

        givenReleaseAccountExecuted(sourceAccount.id)
        givenReleaseAccountExecuted(targetAccount.id)

        val money = Money.of(500)

        val command = SendMoneyCommand(
            sourceAccountId = sourceAccount.id,
            targetAccountId = targetAccount.id,
            money = money
        )

        // when
        val success = service.sendMoney(command)

        // then
        assertTrue(success)

        val sourceAccountId = sourceAccount.id
        val targetAccountId = targetAccount.id

        verify(exactly = 1) { accountLock.lockAccount(sourceAccountId) }
        verify(exactly = 1) { sourceAccount.withdraw(money, targetAccountId) }
        verify(exactly = 1) { accountLock.releaseAccount(sourceAccountId) }

        verify(exactly = 1) { accountLock.lockAccount(targetAccountId) }
        verify(exactly = 1) { targetAccount.deposit(money, sourceAccountId) }
        verify(exactly = 1) { accountLock.releaseAccount(targetAccountId) }

        thenAccountsHaveBeenUpdated(sourceAccount, targetAccount)
    }

    private fun thenAccountsHaveBeenUpdated(vararg accounts: Account) {
        accounts.forEach {
            verifyOrder {
                updateAccountStatePort.updateActivities(it)
            }
        }
    }

    private fun givenUpdateActivities(account: Account) {
        every { updateAccountStatePort.updateActivities(account) } returnsArgument 0
    }

    private fun givenLockAccountExecuted(accountId: Account.AccountId) {
        every { accountLock.lockAccount(accountId) } returnsArgument 0
    }

    private fun givenReleaseAccountExecuted(accountId: Account.AccountId) {
        every { accountLock.releaseAccount(accountId) } returnsArgument 0
    }

    private fun givenDepositWillSucceed(account: Account) {
        every { account.deposit(any<Money>(), any<Account.AccountId>()) } returns true
    }

    private fun givenWithdrawalWillFail(account: Account) {
        every { account.withdraw(any<Money>(), any<Account.AccountId>()) } returns false
    }

    private fun givenWithdrawalWillSucceed(account: Account) {
        every { account.withdraw(any<Money>(), any<Account.AccountId>()) } returns true
    }

    private fun givenTargetAccount(value: Long): Account {
        return givenAnAccountWithId(Account.AccountId(value))
    }

    private fun givenTargetAccount(): Account {
        return givenTargetAccount(42)
    }

    private fun givenSourceAccount(value: Long): Account {
        return givenAnAccountWithId(Account.AccountId(value))
    }

    private fun givenSourceAccount(): Account {
        return givenSourceAccount(41)
    }

    private fun givenAnAccountWithId(id: Account.AccountId): Account {
        val account = mockk<Account>()
        every { account.id } returns id
        every { loadAccountPort.loadAccount(account.id, any<LocalDateTime>()) } returns account
        return account
    }
}
