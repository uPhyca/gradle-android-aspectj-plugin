package com.uphyca.gradle.android

import org.aspectj.bridge.IMessage
import org.aspectj.bridge.MessageHandler
import org.aspectj.tools.ajc.Main
import org.gradle.api.GradleException
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.compile.AbstractCompile

class AspectjCompile extends AbstractCompile {

    private String encoding
    private FileCollection inpath
    private FileCollection aspectpath
    private String bootclasspath

//    @Override
    @TaskAction
    protected void compile() {

        final def log = project.logger

        //http://www.eclipse.org/aspectj/doc/released/devguide/ajc-ref.html
        //
        // -sourceRoots:
        //  Find and build all .java or .aj source files under any directory listed in DirPaths. DirPaths, like classpath, is a single argument containing a list of paths to directories, delimited by the platform- specific classpath delimiter. Required by -incremental.
        // -inpath:
        //  Accept as source bytecode any .class files in the .jar files or directories on Path. The output will include these classes, possibly as woven with any applicable aspects. Path is a single argument containing a list of paths to zip files or directories, delimited by the platform-specific path delimiter.
        // -classpath:
        //  Specify where to find user class files. Path is a single argument containing a list of paths to zip files or directories, delimited by the platform-specific path delimiter.
        // -aspectPath:
        //  Weave binary aspects from jar files and directories on path into all sources. The aspects should have been output by the same version of the compiler. When running the output classes, the run classpath should contain all aspectpath entries. Path, like classpath, is a single argument containing a list of paths to jar files, delimited by the platform- specific classpath delimiter.
        // -bootclasspath:
        //  Override location of VM's bootclasspath for purposes of evaluating types when compiling. Path is a single argument containing a list of paths to zip files or directories, delimited by the platform-specific path delimiter.
        // -d:
        //  Specify where to place generated .class files. If not specified, Directory defaults to the current working dir.
        // -preserveAllLocals:
        //  Preserve all local variables during code generation (to facilitate debugging).

        def args = [
                "-showWeaveInfo",
                "-encoding", getEncoding(),
                "-source", getSourceCompatibility(),
                "-target", getTargetCompatibility(),
                "-d", destinationDir.absolutePath,
                "-classpath", classpath.asPath,
                "-bootclasspath", bootclasspath,
                "-sourceroots", sourceRoots.join(File.pathSeparator)
        ]
        if (!getInpath().isEmpty()) {
            args << '-inpath'
            args << getInpath().asPath
        }
        if (!getAspectpath().isEmpty()) {
            args << '-aspectpath'
            args << getAspectpath().asPath
        }

        log.debug "ajc args: " + Arrays.toString(args as String[])

        MessageHandler handler = new MessageHandler(true);
        new Main().run(args as String[], handler);
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

    @Input
    String getEncoding() {
        return encoding
    }

    void setEncoding(String encoding) {
        this.encoding = encoding
    }

    @InputFiles
    FileCollection getInpath() {
        return inpath
    }

    void setInpath(FileCollection inpath) {
        this.inpath = inpath
    }

    @InputFiles
    FileCollection getAspectpath() {
        return aspectpath
    }

    void setAspectpath(FileCollection aspectpath) {
        this.aspectpath = aspectpath
    }

    @Input
    String getBootclasspath() {
        return bootclasspath
    }

    void setBootclasspath(String bootclasspath) {
        this.bootclasspath = bootclasspath
    }

    File[] getSourceRoots() {
        def sourceRoots = []
        source.sourceCollections.each {
            it.asFileTrees.each {
                sourceRoots << it.dir
            }
        }
        return sourceRoots
    }
}
