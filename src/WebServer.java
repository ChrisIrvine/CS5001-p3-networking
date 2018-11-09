import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.Random;

/**
 * WebServer class that will listen to the given (or default) port and open up
 * a new instance of the ConnectionHandler class for each request given to the
 * server.
 */
class WebServer {

    /** Class variable to hold the server side socket. */
    private ServerSocket ss;
    private Random r = new Random();

    /**
     * Constructor method for the WebServer object. Takes a string (filepath) to
     * the directory from which files are served and the port to serve them
     * upon. Will try to create the ConnectionHandler. Throws IOExceptions.
     * @param dir - filepath to the server directory
     * @param port - port to serve the files across
     */
    WebServer(String dir, int port) {
        try {
            ss = new ServerSocket(port);
            System.out.println("Server started.... \n"
                    + "Listening on port " + port + "....");
            while (true) {
                Socket conn = ss.accept();
                System.out.println("Server got new connection request from "
                        + (Objects.requireNonNull(conn).getInetAddress()));
                new ConnectionThread(conn, dir, "Thread " + r.nextInt(10));
            }
        } catch (IOException e) {
            System.out.println("Ooops " + e.getMessage());
        }
    }
}
