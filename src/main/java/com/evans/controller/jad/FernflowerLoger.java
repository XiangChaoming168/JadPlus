package com.evans.controller.jad;

import com.evans.common.Constants;
import org.jetbrains.java.decompiler.main.extern.IFernflowerLogger;
import org.jetbrains.java.decompiler.util.TextUtil;


public class FernflowerLoger extends IFernflowerLogger {

    //private final PrintStream stream;
    private int indent;

    public FernflowerLoger() {
        //stream = printStream;
        indent = 0;
    }

    @Override
    public void writeMessage(String message, IFernflowerLogger.Severity severity) {
        if (accepts(severity)) {
            String msg = severity.prefix + TextUtil.getIndentString(indent) + message;
            Constants.LOGGER.info(msg);
            //System.out.println(msg);
        }
    }

    @Override
    public void writeMessage(String message, IFernflowerLogger.Severity severity, Throwable t) {
        if (accepts(severity)) {
            writeMessage(message, severity);
            //System.out.println(stream);
        }
    }

    @Override
    public void startReadingClass(String className) {
        if (accepts(IFernflowerLogger.Severity.INFO)) {
            writeMessage("Decompiling class " + className, IFernflowerLogger.Severity.INFO);
            ++indent;
        }
    }

    @Override
    public void endReadingClass() {
        if (accepts(IFernflowerLogger.Severity.INFO)) {
            --indent;
            writeMessage("... done", IFernflowerLogger.Severity.INFO);
        }
    }

    @Override
    public void startClass(String className) {
        if (accepts(IFernflowerLogger.Severity.INFO)) {
            writeMessage("Processing class " + className, IFernflowerLogger.Severity.TRACE);
            ++indent;
        }
    }

    @Override
    public void endClass() {
        if (accepts(IFernflowerLogger.Severity.INFO)) {
            --indent;
            writeMessage("... proceeded", IFernflowerLogger.Severity.TRACE);
        }
    }

    @Override
    public void startMethod(String methodName) {
        if (accepts(IFernflowerLogger.Severity.INFO)) {
            writeMessage("Processing method " + methodName, IFernflowerLogger.Severity.TRACE);
            ++indent;
        }
    }

    @Override
    public void endMethod() {
        if (accepts(IFernflowerLogger.Severity.INFO)) {
            --indent;
            writeMessage("... proceeded", IFernflowerLogger.Severity.TRACE);
        }
    }

    @Override
    public void startWriteClass(String className) {
        if (accepts(IFernflowerLogger.Severity.INFO)) {
            writeMessage("Writing class " + className, IFernflowerLogger.Severity.TRACE);
            ++indent;
        }
    }

    @Override
    public void endWriteClass() {
        if (accepts(IFernflowerLogger.Severity.INFO)) {
            --indent;
            writeMessage("... written", IFernflowerLogger.Severity.TRACE);
        }
    }
}
