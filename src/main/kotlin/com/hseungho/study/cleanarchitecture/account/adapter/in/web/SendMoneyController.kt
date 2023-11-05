package com.hseungho.study.cleanarchitecture.account.adapter.`in`.web

import com.hseungho.study.cleanarchitecture.account.application.port.`in`.SendMoneyCommand
import com.hseungho.study.cleanarchitecture.account.application.port.`in`.SendMoneyUseCase
import com.hseungho.study.cleanarchitecture.account.domain.Account
import com.hseungho.study.cleanarchitecture.account.domain.Money
import com.hseungho.study.cleanarchitecture.common.WebAdapter
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@WebAdapter
@RestController
class SendMoneyController(
    private val sendMoneyUseCase: SendMoneyUseCase
) {

    @PostMapping("/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}")
    fun sendMoney(
        @PathVariable("sourceAccountId") sourceAccountId: Long,
        @PathVariable("targetAccountId") targetAccountId: Long,
        @PathVariable("amount") amount: Long
    ) {

        val command = SendMoneyCommand(
            sourceAccountId = Account.AccountId(sourceAccountId),
            targetAccountId = Account.AccountId(targetAccountId),
            money = Money.of(amount)
        )

        sendMoneyUseCase.sendMoney(command)
    }

}
