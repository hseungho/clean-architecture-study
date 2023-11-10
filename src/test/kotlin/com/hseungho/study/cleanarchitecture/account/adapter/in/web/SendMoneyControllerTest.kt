package com.hseungho.study.cleanarchitecture.account.adapter.`in`.web

import com.hseungho.study.cleanarchitecture.account.application.port.`in`.SendMoneyCommand
import com.hseungho.study.cleanarchitecture.account.application.port.`in`.SendMoneyUseCase
import com.hseungho.study.cleanarchitecture.account.domain.Account
import com.hseungho.study.cleanarchitecture.account.domain.Money
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@WebMvcTest(SendMoneyController::class)
class SendMoneyControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var sendMoneyUseCase: SendMoneyUseCase

    @Test
    fun `testSendMoney`() {
        // given
        val command = SendMoneyCommand(
            sourceAccountId = Account.AccountId(41),
            targetAccountId = Account.AccountId(42),
            money = Money.of(500)
        )
        every { sendMoneyUseCase.sendMoney(command) } returns true
        // when
        // then
        mockMvc.post("/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}", 41, 42, 500) {
            header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        }.andExpect {
            status { isOk() }
        }
        verify(exactly = 1) { sendMoneyUseCase.sendMoney(command) }
    }
}
