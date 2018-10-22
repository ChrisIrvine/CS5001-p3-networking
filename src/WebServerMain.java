import java.io.IOException;

/**
 * Main class from which this Java Server will run.
 */
public class WebServerMain {

    public static void main(String[] args) throws IOException {
        argsCheck(args);
    }

    private static void argsCheck(String[] args) {
        if(args == null || args.length < 2) {
            System.out.println("Usage: java WebServerMain <document_root> <port>");
            System.exit(1);
        } else {
            WebServer ws = new WebServer(args[0], Integer.getInteger(args[1]));
        }
    }
}
