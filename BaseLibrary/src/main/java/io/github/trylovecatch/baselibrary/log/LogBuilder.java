package io.github.trylovecatch.baselibrary.log;

import android.util.Log;

/**
 * @author Kale
 * @date 2016/3/27
 */
public class LogBuilder {

    public int methodOffset = 0;

    public boolean showMethodLink = true;

    public boolean showThreadInfo = false;

    public String tagPrefix = null;

    public String globalTag = null;

    int priority = Log.VERBOSE;

    public PrintStyle style;

    public static LogBuilder create() {
        return new LogBuilder();
    }

    public LogBuilder methodOffset(int methodOffset) {
        this.methodOffset = methodOffset;
        return this;
    }

    public LogBuilder showThreadInfo(boolean showThreadInfo) {
        this.showThreadInfo = showThreadInfo;
        return this;
    }

    public LogBuilder showMethodLink(boolean showMethodLink) {
        this.showMethodLink = showMethodLink;
        return this;
    }

    /**
     * @param priority one of
     *                 {@link Log#VERBOSE},
     *                 {@link Log#DEBUG},
     *                 {@link Log#INFO},
     *                 {@link Log#WARN},
     *                 {@link Log#ERROR}
     */
    public LogBuilder logPriority(int priority) {
        this.priority = priority;
        return this;
    }

    public LogBuilder logPrintStyle(PrintStyle style) {
        this.style = style;
        return this;
    }

    public LogBuilder globalTag(String globalTag) {
        this.globalTag = globalTag;
        return this;
    }

    public LogBuilder tagPrefix(String prefix) {
        this.tagPrefix = prefix;
        return this;
    }

    public LogBuilder build() {
        return this; // simple
    }
}
