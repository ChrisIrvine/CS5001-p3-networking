/**
 * Main class for the Client side of the java web server, will check arguments
 * before creating the Client object.
 */
public class ClientMain {

    /**
     * Main method for the ClientMain class, accepts a single command line
     * argument as parameters. After a args check, the Client will be generated
     * if the args are okay, Client will exit with usage instructions if not.
     * @param args - [0] = hostname
     */
    public static void main(String[] args) {
        if (args.length < 1 || args.length > 1) {
            System.out.println("Usage: ClientMain <hostname>");
            System.exit(1);
        }
        Client c = new Client(args[0], Configuration.DEFAULT_PORT);
    }
}
