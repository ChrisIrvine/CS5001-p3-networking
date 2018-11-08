import java.io.File;
import java.io.IOException;
import java.io.BufferedOutputStream;
import java.nio.file.Files;

/**
 * Class to handle the GET requests for the server, inherits class variables and
 * methods from the ConnectionHandler class.
 */
class GetRequest extends ConnectionHandler {

    private String root;

    /**
     * Custom Constructor for the GetRequest class. Will extract the file to GET
     * from the request header, validate the file path and will proceed to
     * serve the Header and Body to the Client or will return a 404 file not
     * found error.
     * @param req - request for the file
     */
    GetRequest(String req) {
        int backslash;
        int end;
        root = ConnectionHandler.getDir();
        String header;
        String filepath = "";
        byte[] body;

        //Grab the filename from the request
        if (req.contains("/")) {
            backslash = req.indexOf("/");
            end = req.indexOf(" ", backslash);
            filepath = root + req.substring(backslash, end);
            System.out.println(filepath);
        }

        //Validate and process request
        File reqFile = new File(filepath);
        if (reqFile.isFile()) {
            System.out.println("serving file");
            body = compileBody(reqFile);
            header = compileHeader(body.length);
            sendResponse(header.getBytes(), body);
        } else {
            //assume the file was not found, therefore generate 404 response
            send404();
        }
    }

    /**
     * Method that is triggered when the requested file cannot be found.
     * Will redirect the browser to the 404 - file not found - error page.
     */
    private void send404() {
        byte[] fileContent = null;

        try {
            File notFound = new File(root + "/404.html");
            fileContent = Files.readAllBytes(notFound.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert fileContent != null;
        final String errorHeader = "HTTP/1.1 404 Not Found\n"
                + "Server: Simple Java Http Server\n"
                + "Content-Type: text/html\n"
                + "Content-Length: " + fileContent.length + "\n\r\n";

        sendResponse(errorHeader.getBytes(), fileContent);
    }

    /**
     * Method to generate the Header response string. Takes the length of the
     * file to GET in bytes.
     * @param length - length of the file in bytes.
     * @return - Header of the response as a String.
     */
    private String compileHeader(int length) {
        final String s = "HTTP/1.1 200 OK\n"
                + "Server: Simple Java Http Server\n"
                + "Content-Type: text/html\n"
                + "Content-Length: " + length + "\n\r\n";
        return s;
    }

    /**
     * Method to translate the file that was requested into an array of bytes.
     * Catches IOExceptions.
     * @param reqFile - requested file
     * @return - requested file as a byte array
     */
    private byte[] compileBody(File reqFile) {
        byte[] fileContent = null;

        try {
            fileContent = Files.readAllBytes(reqFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileContent;
    }


    /**
     * Send the Header and Body of the GET request response, as a streams of
     * bytes. Catches IOExceptions.
     * @param header - Header of the response as a byte array
     * @param body - Body of the response as a byte array
     */
    private void sendResponse(byte[] header, byte[] body) {
        try {
            ConnectionHandler.getConn().setTcpNoDelay(true);
            BufferedOutputStream out = new BufferedOutputStream(ConnectionHandler.getOs());

            System.out.println("sending header");
            out.write(header);
            System.out.println("sending body");
            out.write(body);

            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
