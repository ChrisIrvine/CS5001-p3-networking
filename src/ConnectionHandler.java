import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;

/**
 * Java Class that handles the connection between the server and the client or
 * browser. Will pass the request on to either a GetRequest object or a
 * HeadRequest object, depending on the type of request.
 */
class ConnectionHandler implements Runnable {

    /** socket representing TCP/IP connection to Client. */
    private static Socket conn;
    /** get data from client on this input stream. */
    private InputStream is;
    /** can send data back to the client on this output stream. */
    private static OutputStream os;
    /** use buffered reader to read client data. */
    private BufferedReader br;
    /** directory from which files are served. */
    private static String dir;

    /**
     * Custom constructor for the ConnectionHandler class, takes a socket
     * and a string as arguments. Will generate I/O streams for the Connection
     * from the connection. BufferedReader is used to read data regarding the
     * request.
     * @param conn - Socket in which the connection is hosted
     * @param dir - Directory from which to the serve the files
     */
    ConnectionHandler(Socket conn, String dir) {
        ConnectionHandler.conn = conn;
        ConnectionHandler.dir = dir;
        try {
            is = conn.getInputStream();
            os = conn.getOutputStream();
            br = new BufferedReader(new InputStreamReader(is));
        } catch (IOException ioe) {
            System.out.println("ConnectionHandler: " + ioe.getMessage());
        }
    }

    /**
     * Default constructor for the ConnectionHandler class, does nothing,
     * implemented to satisfy the GetRequest inheritance requirements.
     * This is never called anywhere in the program.
     */
    ConnectionHandler() { }

    /**
     * Getter method for the Socket.
     * @return - socket for the connection
     */
    static Socket getConn() {
        return conn;
    }

    /**
     * Setter method for the Socket connection.
     * @param conn - new socket to connect to
     */
    public static void setConn(Socket conn) {
        ConnectionHandler.conn = conn;
    }

    /**
     * Getter method for the filepath to the directory from which the files are
     * served from.
     * @return - directory filepath
     */
    static String getDir() {
        return dir;
    }

    /**
     * Setter method for the filepath to the directory from which the files are
     * served from.
     * @param dir - new filepath
     */
    public static void setDir(String dir) {
        ConnectionHandler.dir = dir;
    }

    /**
     * Getter method to retrieve the output stream of the socket.
     * @return - output stream
     */
    static OutputStream getOs() {
        return os;
    }

    /**
     * Setter method to set the output steam for the socket.
     * @param os - new output stream
     */
    public void setOs(OutputStream os) {
        ConnectionHandler.os = os;
    }

    @Override
    public void run() {
          System.out.println("new ConnectionHandler constructed .... ");
        try {
            printClientData();
        } catch (Exception e) { // exit cleanly for any Exception (including IOException, ClientDisconnectedException)
            System.out.println("ConnectionHandler.handleClientRequest: " + e.getMessage());
        } finally {
            cleanup();
        }
    }

    /**
     * Private method that will send the request to a GetRequest or HeadRequest
     * object depending on the header of the request. Throws exceptions if there
     * is an issue.
     * @param req - header of the request to be processed
     */
    private byte[] process(String req) {
        try {
            if (req.contains("GET")) {
                GetRequest gr = new GetRequest(req);

                return Request.compileResponse(gr.getHeader(), gr.getBody());
            } else if (req.contains("HEAD")) {
                HeadRequest hr = new HeadRequest(req);
                return Request.compileResponse(hr.getHeader(), new byte[0]);
            } else {
                UnknownRequest ur = new UnknownRequest();
                return Request.compileResponse(ur.getHeader(), new byte[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            //cleanup();
        }
        return new byte[0];
    }

    /**
     * Private method that will send the client data to be processed and will
     * throw a DisconnectedException should there be an unexpected issue or the
     * connection is closed by the client.
     */
    private void printClientData() throws DisconnectedException, IOException {
        String line = br.readLine();
        logging.compileRequest(line);
        byte[] response = process(line);

        //send response
        os.write(response);
        os.flush();
        os.close();

        //compile response and request for log
        logging.compileRequest(line);
        logging.compileResponse(response);

        //write log
        PrintWriter logger = new PrintWriter(new FileWriter(Configuration.LOG_FILE, true));

        logger.write(logging.getRequest());
        logger.write(logging.getResponse());

        //close log
        logger.flush();
        logger.close();

        if (line == null || line.equals("null")
                || line.equals(Configuration.EXIT_STRING)) {
            throw new DisconnectedException(" ... client has closed the "
                    + "connection ... ");
        }
        System.out.println("ConnectionHandler: " + line);
    }

    /**
     * Private method that will close the connection once the server has handled
     * (either correctly or incorrectly) the request. Throws an IOException
     * should there be one, printed out to the console.
     */
    private void cleanup() {
        System.out.println("ConnectionHandler: ... cleaning up and exiting ... ");
        try {
            br.close();
            is.close();
            conn.close();
        } catch (IOException ioe) {
            System.out.println("ConnectionHandler:cleanup " + ioe.getMessage());
        }
    }
}
