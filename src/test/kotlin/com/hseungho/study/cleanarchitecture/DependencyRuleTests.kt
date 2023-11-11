package com.hseungho.study.cleanarchitecture

import com.hseungho.study.cleanarchitecture.archunit.HexagonalArchitecture
import com.tngtech.archunit.core.importer.ClassFileImporter

class DependencyRuleTests {

//    @Test
    fun `validateRegistrationContextArchitecture`() {
        HexagonalArchitecture.boundedContext("com.hseungho.cleanarchitecture.account")

            .withDomainLayer("domain")

            .withAdaptersLayer("adapter")
            .incoming("in.web")
            .outgoing("out.persistence")
            .and()

            .withApplicationLayer("application")
            .services("service")
            .incomingPorts("port.in")
            .outgoingPorts("port.out")
            .and()

            .withConfiguration("configuration")
            .check(ClassFileImporter().importPackages("com.hseungho.cleanarchitecture.."))
    }
}
