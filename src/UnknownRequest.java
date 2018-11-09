import java.io.File;

/**
 * Class to handle any and all unknown request types to the server.
 */
class UnknownRequest {

    /**
     * Standard 404 file so that the user of of the website can return to
     * safety.
     */
    private final File notFound;
    private byte[] header;

    /**
     * Custom constructor for the UknownRequest object.
     */
    UnknownRequest() {
        notFound = new File(ConnectionHandler.getDir() + "/404.html");
        this.header = compileHeader();
    }

    /**
     * Method that will compile appropriate the 501 Not Implemented Header.
     * @return - byte array representation of the header
     */
    private byte[] compileHeader() {
        final String s = "HTTP/1.1 501 Not Implemented\n"
             + "Server: Simple Java Http Server\n"
             + "Content-Type: text/html\n"
             + "Content-Length: " + Request.findLength(notFound) + "\n\r\n";
        return s.getBytes();
    }

    /**
     * Method to return the response header.
     * @return - byte array representation of the response header string
     */
    byte[] getHeader() {
        return header;
    }
}
