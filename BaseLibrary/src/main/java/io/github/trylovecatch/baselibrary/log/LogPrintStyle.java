package io.github.trylovecatch.baselibrary.log;

import android.support.annotation.NonNull;

/**
 * @author Kale
 * @date 2016/12/8
 */

public class LogPrintStyle extends PrintStyle {

    private static final String PREFIX_BORDER = "║ ";

    private StringBuilder sb = new StringBuilder();

    @Override
    public String beforePrint() {
        return null;
    }

    @NonNull
    @Override
    public String printLog(String message, int line, int wholeCount) {
        if (line == wholeCount - 1) {
            // last line
            return "╚ " + message + getTail();
        } else {
            return PREFIX_BORDER + message;
        }
    }

    @Override
    public String afterPrint() {
        return null;
    }

    /**
     * ==> onCreate(MainActivity.java:827) Thread:main
     */
    private String getTail() {
        if (!getSettings().showMethodLink) {
            return "";
        }

        int index = Logger.STACK_OFFSET + getSettings().methodOffset + 1;
        if (getPrinter().isCustomTag()) {
            index -= 2;
        }
        final StackTraceElement stack = Thread.currentThread().getStackTrace()[index];

        if (sb.length() < 0) {
            sb = new StringBuilder();
        } else {
            sb.setLength(0);
        }

        sb.append(String.format(" ==> %s(%s:%s)",
                stack.getMethodName(),
                stack.getFileName(),
                stack.getLineNumber()));

        if (getSettings().showThreadInfo) {
            sb.append(" Thread: ").append(Thread.currentThread().getName()); // Thread:main
        }
        return sb.toString();
    }

}
