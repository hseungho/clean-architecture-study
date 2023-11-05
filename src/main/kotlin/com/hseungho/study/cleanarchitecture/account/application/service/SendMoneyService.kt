package com.hseungho.study.cleanarchitecture.account.application.service

import com.hseungho.study.cleanarchitecture.account.application.port.`in`.SendMoneyCommand
import com.hseungho.study.cleanarchitecture.account.application.port.`in`.SendMoneyUseCase
import com.hseungho.study.cleanarchitecture.common.UseCase
import org.springframework.transaction.annotation.Transactional

@UseCase
class SendMoneyService : SendMoneyUseCase {

    @Transactional
    override fun sendMoney(command: SendMoneyCommand): Boolean {
        TODO("Not yet implemented")
        // TODO: 비즈니스 규칙 검증
        // TODO: 모델 상태 조작
        // TODO: 출력 값 반환
    }

}
