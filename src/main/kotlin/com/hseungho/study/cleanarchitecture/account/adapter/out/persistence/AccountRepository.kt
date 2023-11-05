package com.hseungho.study.cleanarchitecture.account.adapter.out.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepository: JpaRepository<AccountJpaEntity, Long> {
}
