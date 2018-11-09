import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Class to handle the HEAD requests from the client.
 */
class HeadRequest {

    /**
     * Custom constructor that will also generate and send the appropriate
     * responses to the client.
     * @param req - requested file
     */
    HeadRequest(String req) {
        int backslash;
        int end;
        String root = ConnectionHandler.getDir();
        File notFound = new File(root + "/404.html");
        byte[] header;
        String filepath = "";

        //Grab the filename from the request
        if (req.contains("/")) {
            backslash = req.indexOf("/");
            end = req.indexOf(" ", backslash);
            filepath = root + req.substring(backslash, end);
        }

        //Validate and process request
        File reqFile = new File(filepath);
        if (reqFile.isFile()) {
            header = compileHeader(true, reqFile);
        } else {
            //assume the file was not found, therefore generate 404 response
            header = compileHeader(false, notFound);
        }
        sendResponse(header);
    }

    /**
     * Method to compile the appropriate response header to be returned to the
     * client. Header is returned as a byte array
     * @param b - boolean representing if the file has been found
     * @param reqFile - file to get the length of
     * @return - byte array representing the requested file
     */
    private byte[] compileHeader(boolean b, File reqFile) {
        int length = findLength(reqFile);
        if (b) {
            final String s = "HTTP/1.1 200 OK\n"
                    + "Server: Simple Java Http Server\n"
                    + "Content-Type: text/html\n"
                    + "Content-Length: " + length + "\n\r\n";
            return s.getBytes();
        } else {
            final String s = "HTTP/1.1 404 Not Found\n"
                    + "Server: Simple Java Http Server\n"
                    + "Content-Type: text/html\n"
                    + "Content-Length: " + length + "\n\r\n";
            return s.getBytes();
        }
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

    /**
     * Method to send the given header response to the client over the
     * given socket.
     * @param header - response header
     */
    private void sendResponse(byte[] header) {
        try {
            ConnectionHandler.getConn().setTcpNoDelay(true);
            BufferedOutputStream out = new BufferedOutputStream(
                    ConnectionHandler.getOs()
            );
            out.write(header);

            logging.compileResponse(header, new byte[0]);
            logging.writeToLog();

            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
