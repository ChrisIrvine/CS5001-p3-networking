import java.io.InputStream;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.net.Socket;

/**
 * Client class for the client-side of the Java Web Server.
 */
class Client {

    /** Socket the connection is hosted upon. */
    private Socket socket;
    /** The host to connect to over the socket. */
    private String host;
    /** Port in which the socket is hosted on. */
    private int port;
    /** BufferedReader object for use within the class. */
    private BufferedReader br;
    /** PrintWriter object for use within the class. */
    private PrintWriter pw;

    /**
     * Constructor for the client class, takes a host and port as parameters.
     * @param host - name of the host
     * @param port - number of the port to connect over
     */
    Client(String host, int port) {
        this.host = host;
        this.port = port;
        runClient();
    }

    /**
     * Method to run the client, over the designated port.
     */
    private void runClient() {
        try {
            this.socket = new Socket(host, port);
            System.out.println("Client connected to " + host + " on port "
                    + port + ".");
            System.out.println("To exit enter a single line containing: "
                    + Configuration.EXIT_STRING);
            br = new BufferedReader(new InputStreamReader(System.in));
            pw = new PrintWriter(new OutputStreamWriter(
                    socket.getOutputStream())
            );
            InputStream test_is = socket.getInputStream();
            printUserInputToSocket(); // this runs until something goes wrong

            /* exit cleanly for any Exception (including IOException,
             * DisconnectedException) */
        } catch (Exception e) {
            System.out.println("Ooops on connection to " + host + " on port "
                    + port + ". " + e.getMessage());
            cleanup(); // execute cleanup method to close connections cleanly
        }
    }

    /**
     * Method to print the Client request to the socket over the connection.
     * @throws IOException - For when I/O goes wrong
     * @throws DisconnectedException - For when the connection goes wrong
     */
    private void printUserInputToSocket() throws IOException,
            DisconnectedException {
        while (true) {
            // get user input
            String line = br.readLine();
            // print line out on the socket's output stream
            pw.println(line);
            // flush the output stream so that the server gets message immediately
            pw.flush();
            // user has entered exit command
            if (line.equals(Configuration.EXIT_STRING)) {
                throw new DisconnectedException(" ... user has entered exit "
                        + "command ... ");
            }
        }
    }

    /**
     * Clean up the client side connection by closing the relevant streams.
     */
    private void cleanup() {
        System.out.println("Client: ... cleaning up and exiting ... ");
        try {
            pw.close();
            br.close();
            socket.close();
        } catch (IOException ioe) {
            System.out.println("Ooops " + ioe.getMessage());
        }
    }
}
