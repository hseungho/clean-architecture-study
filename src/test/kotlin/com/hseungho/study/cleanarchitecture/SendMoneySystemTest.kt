package com.hseungho.study.cleanarchitecture

import com.hseungho.study.cleanarchitecture.account.application.port.out.LoadAccountPort
import com.hseungho.study.cleanarchitecture.account.domain.Account
import com.hseungho.study.cleanarchitecture.account.domain.Money
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*
import org.springframework.test.context.jdbc.Sql
import java.time.LocalDateTime

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SendMoneySystemTest {

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    protected lateinit var loadAccountPort: LoadAccountPort

    @Test
    @Sql("SendMoneySystemTest.sql")
    fun `sendMoney`() {
        // given
        val initialSourceBalance = sourceAccount().calculateBalance()
        val initialTargetBalance = targetAccount().calculateBalance()

        // when
        val response = whenSendMoney(
            sourceAccountId(),
            targetAccountId(),
            transferredAmount()
        )

        // then
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(initialSourceBalance.minus(transferredAmount()), balanceOf(sourceAccountId()))
        assertEquals(initialTargetBalance.plus(transferredAmount()), balanceOf(targetAccountId()))
    }

    private fun whenSendMoney(
        sourceAccountId: Account.AccountId,
        targetAccountId: Account.AccountId,
        amount: Money
    ): ResponseEntity<*> {
        val headers = HttpHeaders().also {
            it.contentType = MediaType.APPLICATION_JSON
        }
        val request = HttpEntity(null, headers)

        return restTemplate.exchange(
            "/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}",
            HttpMethod.POST,
            request,
            Any::class.java,
            sourceAccountId.value,
            targetAccountId.value,
            amount.amount
        )
    }

    private fun loadAccount(accountId: Account.AccountId): Account {
        return loadAccountPort.loadAccount(accountId, LocalDateTime.now())
    }

    private fun sourceAccount(): Account {
        return loadAccount(sourceAccountId())
    }

    private fun sourceAccountId(): Account.AccountId {
        return Account.AccountId(1)
    }

    private fun targetAccount(): Account {
        return loadAccount(targetAccountId())
    }

    private fun targetAccountId(): Account.AccountId {
        return Account.AccountId(2)
    }

    private fun transferredAmount(): Money {
        return Money.of(500)
    }

    private fun balanceOf(accountId: Account.AccountId): Money {
        val account = loadAccountPort.loadAccount(accountId, LocalDateTime.now())
        return account.calculateBalance()
    }

}
