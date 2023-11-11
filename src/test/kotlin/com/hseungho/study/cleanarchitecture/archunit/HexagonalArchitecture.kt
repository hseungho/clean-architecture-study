package com.hseungho.study.cleanarchitecture.archunit

import com.tngtech.archunit.core.domain.JavaClasses
import java.util.*

internal class HexagonalArchitecture(
    override val basePackage: String
): ArchitectureElement(basePackage = basePackage) {

    private lateinit var adapters: Adapters
    private lateinit var applicationLayer: ApplicationLayer
    private lateinit var configurationPackage: String
    private val domainPackages: MutableList<String> = mutableListOf()

    companion object {
        fun boundedContext(basePackage: String): HexagonalArchitecture {
            return HexagonalArchitecture(basePackage)
        }
    }

    fun withAdaptersLayer(adaptersPackage: String): Adapters {
        this.adapters = Adapters(
            basePackage = fullQualifiedPackage(adaptersPackage),
            parentContext = this
        )
        return this.adapters
    }

    fun withDomainLayer(domainPackage: String): HexagonalArchitecture {
        this.domainPackages.add(fullQualifiedPackage(domainPackage))
        return this
    }

    fun withApplicationLayer(applicationPackage: String): ApplicationLayer {
        this.applicationLayer = ApplicationLayer(
            basePackage = fullQualifiedPackage(applicationPackage),
            parentContext = this
        )
        return this.applicationLayer
    }

    fun withConfiguration(packageName: String): HexagonalArchitecture {
        this.configurationPackage = fullQualifiedPackage(packageName)
        return this
    }

    private fun domainDoesNotDependOnOtherPackages(classes: JavaClasses) {
        denyAnyDependency(this.domainPackages, Collections.singletonList(adapters.basePackage), classes)
        denyAnyDependency(this.domainPackages, Collections.singletonList(applicationLayer.basePackage), classes)
    }

    fun check(classes: JavaClasses) {
        this.adapters.doesNotContainEmptyPackage()
        this.adapters.dontDependOnEachOther(classes)
        this.adapters.doesNotDependOn(this.configurationPackage, classes)
        this.applicationLayer.doesNotContainEmptyPackage()
        this.applicationLayer.doesNotDependOn(this.adapters.basePackage, classes)
        this.applicationLayer.doesNotDependOn(this.configurationPackage, classes)
        this.applicationLayer.incomingAndOutgoingPortsDoNotDependOnEachOther(classes)
        this.domainDoesNotDependOnOtherPackages(classes)
    }
}
