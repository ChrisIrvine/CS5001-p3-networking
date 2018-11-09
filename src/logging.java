import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Abstract class that implements the methods necessary to log client requests,
 * and the server response codes.
 */
abstract class logging {

    private static String request = "";
    private static String response = "";
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern(
            "dd/MM/yyyy HH:mm:ss"
    );

    /**
     * Method to compile the Client request string into a readable format for
     * the log file, and incorporating the date and time of the request.
     * @param request - Client request to log
     */
    static void compileRequest(String request) {
        logging.request = "Request: " + request + "\nAt: "
                + compileDateTime();
    }

    /**
     * Method to compile the Response byte arrays into a readable String format
     * for the log file, incorporating date and time of the response. If the
     * response only contains a header, the body is ignored during compilation.
     * @param header - response header to log
     */
    static void compileResponse(byte[] header) {
        String responseHeader = new String(header, Configuration.ENCODING);
        logging.response = "Response...\nHeader: \n" + responseHeader
                + "At: " + compileDateTime();
    }

    /**
     * Method to compile the current date and time into a readable format for
     * the log.
     * @return - date and time as a String
     */
    private static String compileDateTime() {
        LocalDateTime now = LocalDateTime.now();
        return DTF.format(now) + "\n";
    }

    /**
     * Return the client request String for the log.
     * @return - client request String
     */
    static String getRequest() {
        return request;
    }


    /**
     * Return the server response String for the log.
     * @return - server response String
     */
    static String getResponse() {
        return response;
    }
}
