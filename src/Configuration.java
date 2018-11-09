import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Abstract class for defining the certain configuration elements for the server
 * such as the exit message and default port.
 */
abstract class Configuration {

    /** exit trigger for the client. */
    static final String EXIT_STRING = "exit";
    /** default port for the server. */
    static final int DEFAULT_PORT = 12345;
    /** filepath to logging file. */
    static final String LOG_FILE = "../log.txt";
    /** Standard encoding. */
    static final Charset ENCODING = StandardCharsets.UTF_8;
    /** Breaker string. */
    static final String BREAKER = "===========================================";

}
