import java.awt.*;
import java.io.*;
import java.net.Socket;

class ConnectionHandler {

    static Socket conn;       // socket representing TCP/IP connection to Client
    private InputStream is;    // get data from client on this input stream
    private OutputStream os;   // can send data back to the client on this output stream
    private BufferedReader br;         // use buffered reader to read client data
    static String dir;        // directory from which files are served

    ConnectionHandler(Socket conn, String dir) {
        ConnectionHandler.conn = conn;
        ConnectionHandler.dir = dir;
        try {
            is = conn.getInputStream();     // get data from client on this input stream
            os = conn.getOutputStream();  // to send data back to the client on this stream
            br = new BufferedReader(new InputStreamReader(is)); // use buffered reader to read client data
        } catch (IOException ioe) {
            System.out.println("ConnectionHandler: " + ioe.getMessage());
        }
    }

    ConnectionHandler() {
    }

    void handleClientRequest() {
        System.out.println("new ConnectionHandler constructed .... ");
        try {
            //printClientData();
            String line = br.readLine();
            if(!line.isEmpty()) { process(line); }
        } catch (Exception e) { // exit cleanly for any Exception (including IOException, ClientDisconnectedException)
            System.out.println("ConnectionHandler.handleClientRequest: " + e.getMessage());
            cleanup();     // cleanup and exit
        }
    }

    private void process(String req) {
        try {
            if(req.contains("GET")) {
                //handleGETRequest(req);
                System.out.println("Sending to get");
                new GetRequest(req);
            } else if (req.contains("HEAD")) {
                handleHEADRequest();
            }
        } catch (Exception e) {
            e.printStackTrace();
            cleanup();
        }
    }

    private void handleHEADRequest() {
        System.out.println("HEAD found");
    }

//    private void handleGETRequest(String req) {
//        try {
//            int backslash;
//            int end;
//            String filepath = dir;
//
//            if(req.contains("/")) {
//                backslash = req.indexOf("/");
//                end = req.indexOf(" ", backslash);
//                filepath = req.substring(backslash, end);
//                System.out.println(filepath);
//            }
//
//            try {
//                System.out.println(dir + filepath);
//                File file = new File(dir + filepath);
//                if(file.isFile()) {
//                    Desktop.getDesktop().browse(file.toURI());
//                    //replace with print writer
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                cleanup();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            cleanup();
//        }
//
//    }


    private void printClientData() throws DisconnectedException, IOException {
        while (true) {
            String line = br.readLine(); // get data from client over socket
            // if readLine fails we can deduce here that the connection to the client is broken
            // and shut down the connection on this side cleanly by throwing a DisconnectedException
            // which will be passed up the call stack to the nearest handler (catch block)
            // in the run method
            if (line == null || line.equals("null") || line.equals(Configuration.exitString)) {
                throw new DisconnectedException(" ... client has closed the connection ... ");
            }
            System.out.println("ConnectionHandler: " + line); // assuming no exception, print out line received from client
        }
    }

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