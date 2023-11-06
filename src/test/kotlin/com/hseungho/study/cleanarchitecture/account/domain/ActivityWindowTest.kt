package com.hseungho.study.cleanarchitecture.account.domain

import com.hseungho.study.cleanarchitecture.common.ActivityTestData.defaultActivity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class ActivityWindowTest {

    @Test
    fun `calculateStartTimestamp`() {
        // given
        val window = ActivityWindow(
            defaultActivity().withTimestamp(startDate()).build(),
            defaultActivity().withTimestamp(inBetweenDate()).build(),
            defaultActivity().withTimestamp(endDate()).build()
        )
        // when
        // then
        assertEquals(startDate(), window.startTimestamp)
    }

    @Test
    fun `calculateEndTimestamp`() {
        // given
        val window = ActivityWindow(
            defaultActivity().withTimestamp(startDate()).build(),
            defaultActivity().withTimestamp(inBetweenDate()).build(),
            defaultActivity().withTimestamp(endDate()).build()
        )
        // when
        // then
        assertEquals(endDate(), window.endTimestamp)
    }

    private fun startDate(): LocalDateTime = LocalDateTime.of(2023, 11, 1, 0, 0)
    private fun inBetweenDate(): LocalDateTime = LocalDateTime.of(2023, 11, 3, 0, 0)
    private fun endDate(): LocalDateTime = LocalDateTime.of(2023, 11, 5, 0, 0)
}
