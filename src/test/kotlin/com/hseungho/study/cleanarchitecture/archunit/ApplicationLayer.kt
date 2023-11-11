package com.hseungho.study.cleanarchitecture.archunit

import com.tngtech.archunit.core.domain.JavaClasses

internal class ApplicationLayer(
    override val basePackage: String,
    private val parentContext: HexagonalArchitecture
): ArchitectureElement(basePackage = basePackage) {

    private val incomingPortsPackages: MutableList<String> = mutableListOf()
    private val outgoingPortsPackages: MutableList<String> = mutableListOf()
    private val servicePackages: MutableList<String> = mutableListOf()

    fun incomingPorts(packageName: String): ApplicationLayer {
        this.incomingPortsPackages.add(fullQualifiedPackage(packageName))
        return this
    }

    fun outgoingPorts(packageName: String): ApplicationLayer {
        this.outgoingPortsPackages.add(fullQualifiedPackage(packageName))
        return this
    }

    fun services(packageName: String): ApplicationLayer {
        this.servicePackages.add(fullQualifiedPackage(packageName))
        return this
    }

    fun and(): HexagonalArchitecture {
        return parentContext
    }

    fun doesNotDependOn(
        packageName: String,
        classes: JavaClasses
    ) {
        denyDependency(this.basePackage, packageName, classes)
    }

    fun incomingAndOutgoingPortsDoNotDependOnEachOther(classes: JavaClasses) {
        denyAnyDependency(this.incomingPortsPackages, this.outgoingPortsPackages, classes)
        denyAnyDependency(this.outgoingPortsPackages, this.incomingPortsPackages, classes)
    }

    private fun allPackages(): List<String> {
        val allPackages = mutableListOf<String>()
        allPackages.addAll(this.incomingPortsPackages)
        allPackages.addAll(this.outgoingPortsPackages)
        allPackages.addAll(this.servicePackages)
        return allPackages
    }

    fun doesNotContainEmptyPackage() {
        denyEmptyPackages(allPackages())
    }
}
