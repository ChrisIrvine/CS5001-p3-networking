import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.*;

class WebServer {
    private ServerSocket ss;

    WebServer(String dir, int port) {
        try {
            ss = new ServerSocket(port);
            System.out.println("Server started.... \n" +
                    "Listening on port " + port + "....");
            File htmlFile = new File(dir + "index.html");
            Desktop.getDesktop().browse(htmlFile.toURI());
            while (true) {
                Socket conn = ss.accept();
                System.out.println("Server got new connection request from " + conn.getInetAddress());
            }
        } catch (IOException e) {
            System.out.println("Ooops " + e.getMessage());
        }
    }

}
