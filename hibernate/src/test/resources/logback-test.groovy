import ch.qos.logback.classic.PatternLayout
import ch.qos.logback.core.ConsoleAppender

import static ch.qos.logback.classic.Level.DEBUG
import static ch.qos.logback.classic.Level.INFO
import static ch.qos.logback.classic.Level.TRACE

appender("CONSOLE", ConsoleAppender) {
  layout(PatternLayout) {
	pattern = "%d{MMM dd yyyy HH:mm:ss,SSS} %-5p %c - %msg%n"
  }
}

logger("org", TRACE)
root(TRACE, ["CONSOLE"])