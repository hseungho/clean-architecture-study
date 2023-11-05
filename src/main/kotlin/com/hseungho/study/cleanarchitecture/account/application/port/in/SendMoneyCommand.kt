package com.hseungho.study.cleanarchitecture.account.application.port.`in`

import com.hseungho.study.cleanarchitecture.account.domain.Account
import com.hseungho.study.cleanarchitecture.account.domain.Money
import com.hseungho.study.cleanarchitecture.common.SelfValidating
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import java.math.BigInteger

/**
 * 유스케이스 API의 일부이기 때문에 인커밍 포트 패키지에 위치한다
 */
data class SendMoneyCommand(
    @NotNull
    val sourceAccountId: Account.AccountId,
    @NotNull
    val targetAccountId: Account.AccountId,
    @NotNull
    @Min(1)
    val money: Money
): SelfValidating<SendMoneyCommand>() {

    init {
        require(money.amount > BigInteger.ZERO)
        this.validateSelf()
    }

}
