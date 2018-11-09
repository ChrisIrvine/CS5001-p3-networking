import java.io.File;
import java.io.IOException;
import java.io.BufferedOutputStream;
import java.nio.file.Files;
import java.util.Objects;

/**
 * Class to handle the GET requests for the server, inherits class variables and
 * methods from the ConnectionHandler class.
 */
class GetRequest extends ConnectionHandler {

    private String root;
    public byte[] body;
    public byte[] header;


    /**
     * Custom Constructor for the GetRequest class. Will extract the file to GET
     * from the request header, validate the file path and will proceed to
     * serve the Header and Body to the Client or will return a 404 file not
     * found error.
     * @param req - request for the file
     */
    GetRequest(String req) throws IOException {
        root = ConnectionHandler.getDir();
        File notFound = new File(root + "/404.html");
        byte[] header;
        byte[] body = new byte[0];

        //String filepath = processReq(req);

        int backslash;
        int end;
        String filepath = "";

        //Grab the filename from the request
        File reqFile = Request.grabFile(req);

        //Validate and process request
        //File reqFile = new File(Objects.requireNonNull(filepath));
        if (reqFile.isFile()) {

            this.body = compileBody(reqFile);
            //System.out.println(new String(this.body, Configuration.ENCODING));
            this.header = compileHeader(true, reqFile, this.body.length);
            //sendResponse(header, body);
        } else {
            //assume the file was not found, therefore generate 404 response
            this.body = compileBody(notFound);
            this.header = compileHeader(false, reqFile, body.length);
            //sendResponse(header, body);
        }
    }

//    private String processReq(String req) {
//        int backslash;
//        int end;
//        String filepath = "";
//
//        //Grab the filename from the request
//        if (req.contains("/")) {
//            backslash = req.indexOf("/");
//            end = req.indexOf(" ", backslash);
//            filepath = root + req.substring(backslash, end);
//
//        }
//        return filepath;
//    }

//    /**
//     * Method to generate the Header response string. Takes the length of the
//     * file to GET in bytes.
//     *
//     * @param b - was the file found
//     * @param reqFile - requested file
//     * @param length - length of the file in bytes.
//     * @return - Header of the response as a String.
//     */
//    private byte[] compileHeader(boolean b, File reqFile, int length)
//            throws IOException {
//        if (b) {
//            final String s = "HTTP/1.1 200 OK\n"
//                    + "Server: Simple Java Http Server\n"
//                    + "Content-Type: "
//                    + Files.probeContentType(reqFile.toPath()) + "\n"
//                    + "Content-Length: " + length + "\n\r\n";
//            return s.getBytes();
//        } else {
//            final String s = "HTTP/1.1 404 Not Found\n"
//                    + "Server: Simple Java Http Server\n"
//                    + "Content-Type: "
//                    + Files.probeContentType(reqFile.toPath()) + "\n"
//                    + "Content-Length: " + length + "\n\r\n";
//            return s.getBytes();
//        }
//    }

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
    
    byte[] compileResponse(byte[] header, byte[] body) {
        byte[] response = new byte[header.length + body.length];

        System.arraycopy(header, 0, response, 0, header.length);
        System.arraycopy(body, 0, response, header.length, body.length);
        
        return response;
    }

//    /**
//     * Send the Header and Body of the GET request response, as a streams of
//     * bytes. Catches IOExceptions.
//     * @param header - Header of the response as a byte array
//     * @param body - Body of the response as a byte array
//     */
//    private void sendResponse(byte[] header, byte[] body) {
//        try {
//            ConnectionHandler.getConn().setTcpNoDelay(true);
//            BufferedOutputStream out = new BufferedOutputStream(
//                    ConnectionHandler.getOs()
//            );
//            out.write(header);
//            out.write(body);
//
//            logging.compileResponse(header, body);
//
//            logging.writeToLog();
//            //out.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
