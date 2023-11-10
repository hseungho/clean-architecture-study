package com.hseungho.study.cleanarchitecture.account.adapter.out.persistence

import com.hseungho.study.cleanarchitecture.account.domain.Account
import com.hseungho.study.cleanarchitecture.account.domain.ActivityWindow
import com.hseungho.study.cleanarchitecture.account.domain.Money
import com.hseungho.study.cleanarchitecture.common.AccountTestData
import com.hseungho.study.cleanarchitecture.common.ActivityTestData
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.jdbc.Sql
import java.time.LocalDateTime

@DataJpaTest
@Import(AccountPersistenceAdapter::class, AccountMapper::class)
class AccountPersistenceAdapterTest {

    @Autowired
    private lateinit var adapterUnderTest: AccountPersistenceAdapter

    @Autowired
    private lateinit var activityRepository: ActivityRepository

    @Test
    @Sql("AccountPersistenceAdapterTest.sql")
    fun `loadsAccount`() {
        // given
        // when
        val account = adapterUnderTest.loadAccount(
            Account.AccountId(1),
            LocalDateTime.of(2018, 8, 10, 0, 0)
        )
        // then
        assertEquals(2, account.activityWindow.activities.size)
        assertEquals(Money.of(500), account.calculateBalance())
    }

    @Test
    fun `updateActivities`() {
        // given
        val account = AccountTestData.defaultAccount()
            .withBaselineBalance(Money.of(555))
            .withActivityWindow(
                ActivityWindow(
                    ActivityTestData.defaultActivity()
                        .withId(null)
                        .withMoney(Money.of(1))
                        .build()
                )
            )
            .build()
        // when
        adapterUnderTest.updateActivities(account)
        // then
        assertEquals(1, activityRepository.count())

        val savedActivity = activityRepository.findAll()[0]
        assertEquals(1, savedActivity.amount)
    }

}
