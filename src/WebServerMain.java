import java.io.File;
import java.io.IOException;

/**
 * Main class from which this Java Server will run.
 */
public class WebServerMain {

    public static void main(String[] args) throws IOException {
        argsCheck(args);
    }

    private static void argsCheck(String[] args) {
        if (args == null || args.length == 1 || args.length < 2) {
            System.out.println("Usage: java WebServerMain <document_root> <port>");
            System.exit(1);
        } else if (dirCheck(args[0]) || portCheck(args[1])) {
            WebServer ws = new WebServer(args[0], Integer.parseInt(args[1]));
        }
    }

    private static boolean portCheck(String port) {
        try {
            Integer.parseInt(port);
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Second argument is not an number");
            return false;
        }
    }

    private static boolean dirCheck(String dir) {
        File dirTest = new File(dir);
        return dirTest.isDirectory();
    }
}
