// This plugin is based on https://github.com/JakeWharton/hugo
package com.uphyca.gradle.android

import org.gradle.api.GradleException
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import org.aspectj.bridge.IMessage
import org.aspectj.bridge.MessageHandler
import org.aspectj.tools.ajc.Main
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile

class AndroidAspectJPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        final def log = project.logger
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

        project.dependencies {
            compile 'org.aspectj:aspectjrt:1.8.+'
        }

        variants.all { variant ->

            JavaCompile javaCompile = variant.javaCompile
            javaCompile.doLast {

                def bootClasspath
                if (plugin.properties['runtimeJarList']) {
                    bootClasspath = plugin.runtimeJarList
                } else  {
                    bootClasspath = plugin.bootClasspath
                }

                def sourceRoots = []
                project.android.sourceSets.main.java.srcDirs.findAll { it.exists() }.each {
                    sourceRoots << it.absolutePath
                }
                project.android.sourceSets[new File(variant.dirName).name].java.srcDirs.findAll {
                    it.exists()
                }.each { sourceRoots << it.absolutePath }
                variant.productFlavors.each {
                    project.android.sourceSets[it.name].java.srcDirs.findAll { it.exists() }.each {
                        sourceRoots << it.absolutePath
                    }
                }
                def generatedSourcesDir = getGeneratedSourcesDir(project)
                sourceRoots << "${project.buildDir}/${generatedSourcesDir}/r/${variant.dirName}"
                sourceRoots << "${project.buildDir}/${generatedSourcesDir}/buildConfig/${variant.dirName}"

                def String[] args = [
                    "-showWeaveInfo",
                    "-encoding", "UTF-8",
                    "-" + project.android.compileOptions.sourceCompatibility,
                    "-inpath", javaCompile.destinationDir.toString(),
                    "-aspectpath", javaCompile.classpath.asPath,
                    "-d", javaCompile.destinationDir.toString(),
                    "-classpath", javaCompile.classpath.asPath,
                    "-bootclasspath", bootClasspath.join(File.pathSeparator),
                    "-sourceroots", sourceRoots.join(File.pathSeparator)
                ]


                log.debug "ajc args: " + Arrays.toString(args)

                MessageHandler handler = new MessageHandler(true);
                new Main().run(args, handler);
                for (IMessage message : handler.getMessages(null, true)) {
                    switch (message.getKind()) {
                        case IMessage.ABORT:
                        case IMessage.ERROR:
                        case IMessage.FAIL:
                            log.error message.message, message.thrown
                            throw new GradleException(message.message, message.thrown)
                        case IMessage.WARNING:
                            log.warn message.message, message.thrown
                            break;
                        case IMessage.INFO:
                            log.info message.message, message.thrown
                            break;
                        case IMessage.DEBUG:
                            log.debug message.message, message.thrown
                            break;
                    }
                }
            }
        }
    }

    String getGeneratedSourcesDir(Project project) {
        return getNameByVersion(project, 0, 11, 'generated/source', 'source')
    }

    String getNameByVersion(Project project, int major, int minor, String newName, String oldName) {
        def androidPluginVersion = getPluginVersion(project, "com.android.tools.build", "gradle")

        if (androidPluginVersion == null) {
            return newName
        }
        def vArray = androidPluginVersion.split("\\.")
        if (vArray.length < 2) {
            return newName
        }
        if (major < Integer.parseInt(vArray[0])) {
            return newName
        }
        if (major == Integer.parseInt(vArray[0]) && minor <= Integer.parseInt(vArray[1])) {
            return newName
        }
        return oldName
    }

    String getPluginVersion(Project project, String group, String name) {
        def Project targetProject = project
        while (targetProject != null) {
            def version
            targetProject.buildscript.configurations.classpath.resolvedConfiguration.firstLevelModuleDependencies.each {
                e-> if (e.moduleGroup.equals(group) && e.moduleName.equals(name)) {version = e.moduleVersion}
            }
            if (version != null) {
                return version
            }
            targetProject = targetProject.parent
        }
        return null
    }
}
