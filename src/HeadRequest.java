import java.io.File;
import java.io.IOException;

/**
 * Class to handle the HEAD requests from the client.
 */
class HeadRequest {

    private byte[] header;

    /**
     * Custom constructor that will also generate and send the appropriate
     * responses to the client.
     * @param req - requested file
     */
    HeadRequest(String req) throws IOException {
        String root = ConnectionHandler.getDir();
        File notFound = new File(root + "/404.html");
        File reqFile = Request.grabFile(req);
        if (reqFile.isFile()) {
            this.header = Request.compileHeader(true, reqFile,
                    Request.findLength(reqFile));
        } else {
            //assume the file was not found, therefore generate 404 response
            this.header = Request.compileHeader(false, notFound,
                    Request.findLength(notFound));
        }
    }

    /**
     * Return the response header.
     * @return - response header as byte array
     */
    byte[] getHeader() {
        return header;
    }
}
