package com.hseungho.study.cleanarchitecture.account.application.port.out

import com.hseungho.study.cleanarchitecture.account.domain.Account

interface UpdateAccountStatePort {

    fun updateActivities(account: Account)

}
