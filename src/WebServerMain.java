import java.io.File;

/**
 * Main class from which this Java Web Server will run.
 */
public class WebServerMain {

    /**
     * Main method that takes arguments from the command line, and then send the
     * arguments to be checked.
     * @param args - command line arguments.
     */
    public static void main(String[] args) {
        argsCheck(args);
    }

    /**
     * Method to check the arguments passed in from the command line.
     * Firstly will check the number of the arguments passed in and then will
     * check that the directory and ports passed into the program are valid.
     * If the number of arguments is unexpected, the expected command line
     * arguemtns are printed out.
     * @param args - command line arguments
     */
    private static void argsCheck(String[] args) {
        if (args == null || args.length == 1 || args.length < 2) {
            System.out.println("Usage: java WebServerMain <document_root> "
                    + "<port>");
            System.exit(1);
        } else if (dirCheck(args[0]) && portCheck(args[1])) {
            new WebServer(args[0], Integer.parseInt(args[1]));
        }
    }

    /**
     * Method to validate the port argument is correct (can the string be
     * converted to an integer).
     * @param port - String to evaluate
     * @return - returns true or false depending on the check
     */
    private static boolean portCheck(String port) {
        try {
            Integer.parseInt(port);
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Second argument is not an number");
            return false;
        }
    }

    /**
     * Method to validate the filepath to the directory passed in from the
     * command line. Uses the File library's isDirectory() method.
     * If the check fails, the use is notified that the filepath is invalid.
     * @param dir - file path to validate
     * @return - returns either true or false depending on the result of the
     *              check.
     */
    private static boolean dirCheck(String dir) {
        File dirTest = new File(dir);

        if (dirTest.isDirectory()) {
            return true;
        } else {
            System.out.println(dir + " is not a directory");
            return false;
        }
    }
}
