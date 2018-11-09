import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

abstract class Request {

    static File grabFile(String reqFile) {
        int backslash;
        int end;
        String filepath = null;

        if (reqFile.contains("/")) {
            backslash = reqFile.indexOf("/");
            end = reqFile.indexOf(" ", backslash);
            filepath = ConnectionHandler.getDir() + reqFile.substring(backslash, end);
        }

        // we do not need to catch the NullPointerException as we can assume
        // that if the request has got this far, it will contain a file, given
        // the standardised format of HTTP/1.1 requests.
        return new File(Objects.requireNonNull(filepath));
    }

    /**
     * Method to generate the Header response string. Takes the length of the
     * requested file in bytes.
     *
     * @param valid - was the file found
     * @param reqFile - requested file
     * @param length - length of the file in bytes.
     * @return - Header of the response as a String.
     */
    static byte[] compileHeader(boolean valid, File reqFile, int length)
            throws IOException {
        if (valid) {
            final String s = "HTTP/1.1 200 OK\n"
                    + "Server: Simple Java Http Server\n"
                    + "Content-Type: "
                    + Files.probeContentType(reqFile.toPath()) + "\n"
                    + "Content-Length: " + length + "\n\r\n";
            return s.getBytes();
        } else {
            final String s = "HTTP/1.1 404 Not Found\n"
                    + "Server: Simple Java Http Server\n"
                    + "Content-Type: "
                    + Files.probeContentType(reqFile.toPath()) + "\n"
                    + "Content-Length: " + length + "\n\r\n";
            return s.getBytes();
        }
    }
}
