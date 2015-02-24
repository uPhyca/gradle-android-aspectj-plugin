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
        try {
            if (project.plugins.hasPlugin(AppPlugin)) {
                variants = project.android.applicationVariants
                plugin = project.plugins.getPlugin(AppPlugin)
            } else if (project.plugins.hasPlugin(LibraryPlugin)) {
                variants = project.android.libraryVariants
                plugin = project.plugins.getPlugin(LibraryPlugin)
            } else {
                throw new GradleException("The 'com.android.application' or 'com.android.library' plugin is required.")
            }
        } catch (Exception e) {
            throw new GradleException(e);
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
                def newTaskName = "compile${variantName}Aspectj"

                def aspectjCompile = project.task(newTaskName, overwrite: true, description: 'Compiles AspectJ Source', type: AspectjCompile) {
                    aspectpath = javaCompile.classpath
                    destinationDir = javaCompile.destinationDir
                    classpath = javaCompile.classpath
                    bootclasspath = bootClasspath.join(File.pathSeparator)
                    sourceroots = javaCompile.source
                }

                aspectjCompile.doFirst {

                    if (javaCompile.destinationDir.exists()) {

                        javaCompile.destinationDir.deleteDir()
                    }
                    
                    javaCompile.destinationDir.mkdirs()
                }

                def compileAspect = project.tasks.getByName(newTaskName)
                compileAspect.setDependsOn(variant.javaCompile.dependsOn)
                variant.javaCompile.deleteAllActions()
                variant.javaCompile.dependsOn compileAspect
            }
        }
    }
}
