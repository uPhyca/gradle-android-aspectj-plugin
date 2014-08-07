// This plugin is based on https://github.com/JakeWharton/hugo
package com.uphyca.gradle.android

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile

class AndroidAspectJPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        final def variants
        final def plugin
        if (project.plugins.hasPlugin(AppPlugin)) {
            variants = project.android.applicationVariants
            plugin = project.plugins.getPlugin(AppPlugin)
        } else if (project.plugins.hasPlugin(LibraryPlugin)) {
            variants = project.android.libraryVariants
            plugin = project.plugins.getPlugin(LibraryPlugin)
        } else {
            throw new GradleException("The 'android' or 'android-library' plugin is required.")
        }

        project.repositories {
            mavenCentral()
        }
        project.dependencies {
            compile 'org.aspectj:aspectjrt:1.8.+'
        }

        project.afterEvaluate {
            variants.all { variant ->

                JavaCompile javaCompile = variant.javaCompile
                def bootClasspath
                if (plugin.properties['runtimeJarList']) {
                    bootClasspath = plugin.runtimeJarList
                } else {
                    bootClasspath = plugin.bootClasspath
                }

                def variantName = variant.name.capitalize()
                def taskName = "compile${variantName}Aspectj"

                def aspectjCompile = project.task(taskName, type: AspectjCompile) {
                    aspectpath = javaCompile.classpath
                    destinationDir = javaCompile.destinationDir
                    classpath = javaCompile.classpath
                    bootclasspath = bootClasspath.join(File.pathSeparator)
                    sourceroots = javaCompile.source
                }

                aspectjCompile.dependsOn(javaCompile)
                javaCompile.finalizedBy(aspectjCompile)
            }
        }
    }
}
