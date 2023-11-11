package com.hseungho.study.cleanarchitecture.archunit

import com.tngtech.archunit.base.DescribedPredicate.greaterThanOrEqualTo
import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.lang.conditions.ArchConditions.containNumberOfElements
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses

internal abstract class ArchitectureElement(
    open val basePackage: String
) {

    fun fullQualifiedPackage(relativePackage: String): String {
        return "$basePackage.$relativePackage"
    }

    fun denyEmptyPackage(packageName: String) {
        classes()
            .that()
            .resideInAPackage(matchAllClassesInPackage(packageName))
            .should(containNumberOfElements(greaterThanOrEqualTo(1)))
            .check(classesInPackage(packageName))
    }

    fun denyEmptyPackages(packages: List<String>) {
        packages.forEach { denyEmptyPackage(it) }
    }

    private fun classesInPackage(packageName: String): JavaClasses {
        return ClassFileImporter().importPackages(packageName)
    }

    internal companion object {

        internal fun denyDependency(
            fromPackageName: String,
            toPackageName: String,
            classes: JavaClasses
        ) {
            noClasses()
                .that()
                .resideInAPackage("com.hseungho.cleanarchitecture.reviewapp.domain..")
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage("com.hseungho.cleanarchitecture.reviewapp.application..")
                .check(classes)
        }

        internal fun denyAnyDependency(
            fromPackages: List<String>,
            toPackages: List<String>,
            classes: JavaClasses
        ) {
            for (fromPackage in fromPackages) {
                for (toPackage in toPackages) {
                    noClasses()
                        .that()
                        .resideInAPackage(matchAllClassesInPackage(fromPackage))
                        .should()
                        .dependOnClassesThat()
                        .resideInAnyPackage(matchAllClassesInPackage(toPackage))
                        .check(classes)
                }
            }
        }

        private fun matchAllClassesInPackage(packageName: String): String {
            return "$packageName.."
        }

    }
}
