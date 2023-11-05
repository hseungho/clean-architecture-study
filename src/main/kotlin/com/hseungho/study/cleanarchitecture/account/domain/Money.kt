package com.hseungho.study.cleanarchitecture.account.domain

import org.springframework.lang.NonNull
import java.math.BigInteger

data class Money(
    @NonNull
    val amount: BigInteger
) {

    companion object {

        val ZERO: Money = Money.of(0L)

        fun of(value: Long): Money = Money(BigInteger.valueOf(value))

        fun add(a: Money, b: Money): Money = Money(a.amount.add(b.amount))

        fun subtract(a: Money, b: Money): Money = Money(a.amount.subtract(b.amount))

    }

    fun isPositiveOrZero(): Boolean = this.amount >= BigInteger.ZERO

    fun isNegative(): Boolean = this.amount < BigInteger.ZERO

    fun isPositive(): Boolean = this.amount > BigInteger.ZERO

    fun isGreaterThanOrEqualTo(money: Money): Boolean = this.amount >= money.amount

    fun isGreaterThan(money: Money): Boolean = this.amount > money.amount

    fun plus(money: Money): Money = Money(this.amount.add(money.amount))

    fun minus(money: Money): Money = Money(this.amount.subtract(money.amount))

    fun negate(): Money = Money(this.amount.negate())

}