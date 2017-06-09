package io.github.trylovecatch.baselibrary.log;

import android.support.annotation.NonNull;

public final class LogPrinter extends Timber.DebugTree {

    private PrintStyle style;

    /**
     * 因为如果设置了tag，那么会在timber中多走一个方法，方法栈会发生变化，造成不准确的情况。
     */
    private boolean isCustomTag = true;

    private final LogBuilder logBuilder;

    private static final String PROPERTY = System.getProperty("line.separator");

    LogPrinter(LogBuilder logBuilder) {
        this.logBuilder = logBuilder;
        this.style = logBuilder.style;
    }

    /**
     * Auto tag
     */
    @Override
    protected String createStackElementTag(StackTraceElement ignored) {
        String tag;
        isCustomTag = false;
        if (logBuilder.globalTag != null) {
            tag = logBuilder.globalTag;
        } else {
            int offset = Logger.STACK_OFFSET + logBuilder.methodOffset - 2; // 调整栈的位置
            final StackTraceElement[] stackTrace = new Throwable().getStackTrace();
            final int length = stackTrace.length;
            tag = super.createStackElementTag(length > offset ? stackTrace[offset] : stackTrace[length - 1]);
        }
        return maybeAddPrefix(tag);
    }

    public String maybeAddPrefix(String tag) {
        String tagPrefix = logBuilder.tagPrefix;
        if (tagPrefix != null) {
            tag = tagPrefix + "-" + tag;
        }
        return tag;
    }

    /**
     * @param tag from {@link #createStackElementTag(StackTraceElement)}
     */
    @Override
    protected void log(int priority, String tag, @NonNull String message, Throwable ignored) {
        if (style.beforePrint() != null) {
            super.log(priority, tag, style.beforePrint(), null);
        }

        String[] lines = message.split(PROPERTY);
        for (int i = 0, length = lines.length; i < length; i++) {
            String logStr = style.printLog(lines[i], i, length);
            super.log(priority, tag, logStr, null);
        }

        if (style.afterPrint() != null) {
            super.log((priority), tag, style.afterPrint(), null);
        }
        isCustomTag = true;
    }

    /**
     * 根据级别显示log
     *
     * @return 默认所有级别都显示
     */
    @Override
    protected boolean isLoggable(String tag, int priority) {
        return priority >= logBuilder.priority;
    }

    boolean isCustomTag() {
        return isCustomTag;
    }

    public LogBuilder getLogBuilder() {
        return logBuilder;
    }
}
