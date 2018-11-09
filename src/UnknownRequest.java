import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Class to handle any and all unknown request types to the server.
 */
class UnknownRequest {

    /**
     * Standard 404 file so that the user of of the website can return to
     * safety.
     */
    private final File notFound;
    public byte[] body;
    public byte[] header;

    /**
     * Custom constructor for the UknownRequest object.
     */
    UnknownRequest() {
        notFound = new File(ConnectionHandler.getDir() + "/404.html");
        this.body = new byte[1];
        this.header = compileHeader();
        //sendResponse(header);
    }

    /**
     * Method that will compile appropriate the 501 Not Implemented Header.
     * @return - byte array representation of the header
     */
    private byte[] compileHeader() {
        int length = findLength(notFound);
        final String s = "HTTP/1.1 501 Not Implemented\n"
             + "Server: Simple Java Http Server\n"
             + "Content-Type: text/html\n"
             + "Content-Length: " + length + "\n\r\n";
        return s.getBytes();
    }

    /**
     * Method to calculate the length of a file when represented as a byte
     * array.
     * @param reqFile - requested file to be copied into a byte array
     * @return - legnth of the requested file as a byte array
     */
    private int findLength(File reqFile) {
        byte[] fileContent = null;
        try {
            fileContent = Files.readAllBytes(reqFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent != null ? fileContent.length : 0;
    }

    byte[] compileResponse(byte[] header, byte[] body) {
        byte[] response = new byte[header.length + body.length];

        System.arraycopy(header, 0, response, 0, header.length);
        if (body.length > 1) {
            System.arraycopy(body, 0, response, header.length, body.length);
        }

        return response;
    }

//    /**
//     * Method to send the given header response to the client over the
//     * given socket.
//     * @param header - response header
//     */
//    private void sendResponse(byte[] header) {
//        try {
//            ConnectionHandler.getConn().setTcpNoDelay(true);
//            BufferedOutputStream out = new BufferedOutputStream(ConnectionHandler.getOs());
//
//            out.write(header);
//
//            logging.compileResponse(header, new byte[0]);
//            logging.writeToLog();
//
//            out.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
