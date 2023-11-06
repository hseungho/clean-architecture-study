package com.hseungho.study.cleanarchitecture.account.application.service

import com.hseungho.study.cleanarchitecture.account.domain.Money

object MoneyTransferProperties {
    val maximumTransferThreshold : Money = Money.of(1_000_000)
}
