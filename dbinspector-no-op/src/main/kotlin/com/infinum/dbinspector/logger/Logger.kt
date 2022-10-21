package com.infinum.dbinspector.logger

/**
 * Logger interface exposing logging messages by level, or using direct overloads for debug, info and error messages.
 * Implementation class also must support setting a tag and level.
 */
public interface Logger {

    /**
     * Log a message with an explicit level.
     *
     * @param level Level for any output in terminal.
     * @param message Text output in terminal.
     */
    public fun log(level: Level, message: String)

    /**
     * Log a debug message.
     *
     * @param message Text output in terminal.
     */
    public fun debug(message: String)

    /**
     * Log an info message.
     *
     * @param message Text output in terminal.
     */
    public fun info(message: String)

    /**
     * Log an error message.
     *
     * @param message Text output in terminal.
     */
    public fun error(message: String)

    /**
     * Set a tag for a logger instance.
     *
     * @param tag Textual tag for any output in terminal.
     */
    public fun setTag(tag: String)

    /**
     * Set a default level for a logger instance.
     *
     * @param level Level for any output in terminal.
     */
    public fun setLevel(level: Level)
}
