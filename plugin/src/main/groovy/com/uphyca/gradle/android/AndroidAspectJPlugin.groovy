// This plugin is based on https://github.com/JakeWharton/hugo
package com.uphyca.gradle.android

import com.android.build.gradle.api.BaseVariant
import com.android.builder.model.BaseConfig
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.DefaultDomainObjectSet
import org.gradle.api.tasks.compile.JavaCompile

class AndroidAspectJPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        def configuration = new AndroidConfiguration(project)

        project.repositories {
            mavenCentral()
        }
        project.dependencies {
            compile 'org.aspectj:aspectjrt:1.8.7'
        }


        if (!project.configurations.findByName('ajInpath')) {
            def ajInpathConfiguration = project.configurations.create('ajInpath')
        }

        //def aspectjConfiguration = project.configurations.create('aspectj').extendsFrom(project.configurations.compile, project.configurations.provided)

        createConfigurations(project, project.android.buildTypes)
        createConfigurations(project, project.android.productFlavors)
        createConfigurations(project, configuration.variants)

        project.afterEvaluate {
            configuration.variants.all { variant ->

                def configurationName = "${variant.name}Aspectj"

                def variantName = variant.name.capitalize()
                def taskName = "compile${variantName}Aspectj"

                JavaCompile javaCompile = variant.hasProperty('javaCompiler') ? variant.javaCompiler : variant.javaCompile

                def aspectjCompile = project.task(taskName, overwrite: true, group: 'build', description: 'Compiles AspectJ Source', type: AspectjCompile) {

                    sourceCompatibility = javaCompile.sourceCompatibility
                    targetCompatibility = javaCompile.targetCompatibility
                    encoding = javaCompile.options.encoding

                    aspectpath = javaCompile.classpath
                    destinationDir = javaCompile.destinationDir
                    classpath = javaCompile.classpath
                    bootclasspath = configuration.bootClasspath.join(File.pathSeparator)



                    def sourceSets = new ArrayList()
                    variant.variantData.extraGeneratedSourceFolders.each {
                        source it
                    }
                    variant.variantData.javaSources.each {
                        if (it instanceof File) {
                            source it
                        } else {
                            it.asFileTrees.each {
                                source it.dir
                            }
                        }
                    }
                    inpath = project.configurations.ajInpath
                }

                // javaCompile.classpath does not contain exploded-aar/**/jars/*.jars till first run
                javaCompile.doLast {
                    aspectjCompile.classpath = javaCompile.classpath
                }

                aspectjCompile.dependsOn javaCompile
                javaCompile.finalizedBy aspectjCompile
            }
        }
    }

    private void createConfigurations(Project project, DefaultDomainObjectSet<? extends BaseVariant> variants) {
        variants.all {
            createConfiguration(project, it)
        }
    }

    private void createConfigurations(Project project, NamedDomainObjectContainer<? extends BaseConfig> configs) {
        configs.each {
            createConfiguration(project, it)
        }
        configs.whenObjectAdded {
            createConfiguration(project, it)
        }
    }

    private void createConfiguration(Project project, def config) {
        def configurationName = "${config.name}Aspectj"
        if (!project.configurations.findByName(configurationName)) {
            project.configurations.create(configurationName)
        }
    }
}
