package com.hseungho.study.cleanarchitecture.account.adapter.out.persistence

import com.hseungho.study.cleanarchitecture.account.domain.Account
import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepository: JpaRepository<Account, Long> {
}
