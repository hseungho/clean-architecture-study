package com.hseungho.study.cleanarchitecture.archunit

import com.tngtech.archunit.core.domain.JavaClasses

internal class Adapters(
    override val basePackage: String,
    private val parentContext: HexagonalArchitecture
): ArchitectureElement(basePackage = basePackage) {

    private val incomingAdapterPackages: MutableList<String> = mutableListOf()
    private val outgoingAdapterPackages: MutableList<String> = mutableListOf()

    fun outgoing(packageName: String): Adapters {
        this.incomingAdapterPackages.add(fullQualifiedPackage(packageName))
        return this
    }

    fun incoming(packageName: String): Adapters {
        this.outgoingAdapterPackages.add(fullQualifiedPackage(packageName))
        return this
    }

    private fun allAdapterPackages(): List<String> {
        val allAdapters = mutableListOf<String>()
        allAdapters.addAll(this.incomingAdapterPackages)
        allAdapters.addAll(this.outgoingAdapterPackages)
        return allAdapters
    }

    fun and(): HexagonalArchitecture {
        return parentContext
    }

    fun dontDependOnEachOther(classes: JavaClasses) {
        val allAdapters = allAdapterPackages()
        for (adapter1 in allAdapters) {
            for (adapter2 in allAdapters) {
                if (adapter1 != adapter2) {
                    denyDependency(adapter1, adapter2, classes)
                }
            }
        }
    }

    fun doesNotDependOn(
        packageName: String,
        classes: JavaClasses
    ) {
        denyDependency(this.basePackage, packageName, classes)
    }

    fun doesNotContainEmptyPackage() {
        denyEmptyPackages(allAdapterPackages())
    }

}
