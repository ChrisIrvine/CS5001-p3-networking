import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Abstract class that implements the methods necessary to log client requests,
 * and the server response codes.
 */
abstract class logging {

    private static File log = new File(Configuration.LOG_FILE);
    private static String request = "";
    private static String response = "";
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

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
     * @param body - response body to log (may not be present)
     */
    static void compileResponse(byte[] header, byte[] body) {
        String responseHeader = new String(header, Configuration.ENCODING);
        String responseBody = new String(body, Configuration.ENCODING);

        if (body.length > 1) {
            logging.response = "Response...\nHeader: \n" + responseHeader
                    + "Body: \n" + responseBody + "\nAt: " + compileDateTime();
        } else {
            logging.response = "Response...\nHeader: \n" + responseHeader
                    + "At: " + compileDateTime();
        }
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
     * Method to write the compiled request and response strings into the
     * predesignated log file. Uses PrintWriter for ease and will close the
     * connection into the log file upon job completion.
     *
     * Includes IOException catching.
     */
    static void writeToLog() {
        try {
            PrintWriter out = new PrintWriter(new FileWriter(log, true));

            out.append(request);
            out.append(response);
            out.append(Configuration.BREAKER + Configuration.BREAKER + "\n");

            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
