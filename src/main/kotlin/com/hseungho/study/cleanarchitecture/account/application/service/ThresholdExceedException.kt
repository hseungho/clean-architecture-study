package com.hseungho.study.cleanarchitecture.account.application.service

import com.hseungho.study.cleanarchitecture.account.domain.Money

class ThresholdExceedException(
    threshold: Money,
    actual: Money
) : RuntimeException(
    "Maximum threshold for transferring money exceeded: tried to transfer $actual but threshold is $threshold!"
)
