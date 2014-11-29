package commons;

import java.io.PrintStream;

/**
 * Logs to the Console. Depends on the grepconsole plugin.
 */
public class Logger {
	private static final String DEBUG = "[DEBUG]";
	private static final String WARN = "[WARN]";
	private static final String ERROR = "[ERROR]";
	private static final String FATAL = "[FATAL]";

	private PrintStream m_out;

	/**
	 * Constructs a Logger to the PrintStream.
	 * 
	 * @param out
	 */
	public Logger(PrintStream out) {
		m_out = out;
	}

	/**
	 * Prints a String without formatting.
	 * 
	 * @param string
	 */
	public void regular(String string) {
		m_out.println(string);
	}

	/**
	 * Prints a debug String.
	 * 
	 * @param debug
	 */
	public void debug(String debug) {
		m_out.println(DEBUG + debug);
	}

	/**
	 * Prints a warning String.
	 * 
	 * @param warning
	 */
	public void warn(String warning) {
		m_out.println(WARN + warning);
	}

	/**
	 * Prints an error String.
	 * 
	 * @param error
	 */
	public void error(String error) {
		m_out.println(ERROR + error);
	}

	/**
	 * Prints a fatal String.
	 * 
	 * @param fatal
	 */
	public void fatal(String fatal) {
		m_out.println(FATAL + fatal);
	}

	private static Logger s_logger;

	/**
	 * Gets an instance of the Logger to the console.
	 * 
	 * @return
	 */
	public static Logger instance() {
		if (s_logger == null)
			s_logger = new Logger(System.out);
		return s_logger;
	}
}
