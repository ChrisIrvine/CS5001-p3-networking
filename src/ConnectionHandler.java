import java.net.Socket;
import java.io.*;

public class ConnectionHandler {

    private Socket conn;
    private InputStream is;
    private OutputStream os;
    BufferedReader br;

    public ConnectionHandler(Socket conn) {
        this.conn = conn;
        try {
            is = conn.getInputStream();
            os = conn.getOutputStream();
            br = new BufferedReader(new InputStreamReader(is));
        } catch (IOException e) {
            System.out.println("ConnectionHandler" + e.getMessage());
        }
    }

    public void handleClientRequest() {
        System.out.println("New ConnectionHandler constructed...");
        try {
            printClientData();
        } catch (Exception e) {
            System.out.println("ConnectionHandler.handleClientRequest: " +
                    e.getMessage());
            cleanUp();
        }
    }

    private void printClientData() throws IOException, DisconnectedException {
        while(true){
            String line = br.readLine();
            if(line == null || line.equals("null") || line.equals(Configuration.exitString)) {
                throw new DisconnectedException(" ... client has closed the connection ... ");
            }
            System.out.println("ConnectionHandler: " + line);
        }
    }

    private void cleanUp() {
        System.out.println("ConnectionHandler: ... cleaning up and exiting " +
                "... ");
        try {
            br.close();
            is.close();
            conn.close();
        } catch (IOException e) {
            System.out.println("ConnectionHandler:cleanup " + e.getMessage());
        }
    }
}
