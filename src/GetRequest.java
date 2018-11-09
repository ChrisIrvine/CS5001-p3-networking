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

    public byte[] body;
    byte[] header;


    /**
     * Custom Constructor for the GetRequest class. Will extract the file to GET
     * from the request header, validate the file path and will proceed to
     * serve the Header and Body to the Client or will return a 404 file not
     * found error.
     * @param req - request for the file
     */
    GetRequest(String req) throws IOException {
        String root = ConnectionHandler.getDir();
        File notFound = new File(root + "/404.html");

        //Grab the filename from the request
        File reqFile = Request.grabFile(req);

        if (reqFile.isFile()) {
            this.body = Request.compileBody(reqFile);
            this.header = Request.compileHeader(true, reqFile, this.body.length);
        } else {
            //assume the file was not found, therefore generate 404 response
            this.body = Request.compileBody(notFound);
            this.header = Request.compileHeader(false, reqFile, this.body.length);
        }
    }

    
    byte[] compileResponse(byte[] header, byte[] body) {
        byte[] response = new byte[header.length + body.length];

        System.arraycopy(header, 0, response, 0, header.length);
        System.arraycopy(body, 0, response, header.length, body.length);
        
        return response;
    }
}
