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

    /**
     * Method to translate the file that was requested into an array of bytes.
     * Catches IOExceptions.
     * @param reqFile - requested file
     * @return - requested file as a byte array
     */
    static byte[] compileBody(File reqFile) {
        byte[] fileContent = null;

        try {
            fileContent = Files.readAllBytes(reqFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileContent;
    }

    static byte[] compileResponse(byte[] header, byte[] body) {
        byte[] response = new byte[header.length + body.length];

        System.arraycopy(header, 0, response, 0, header.length);
        System.arraycopy(body, 0, response, header.length, body.length);

        return response;
    }

    /**
     * Method to calculate the length of a file when represented as a byte
     * array.
     * @param reqFile - requested file to be copied into a byte array
     * @return - legnth of the requested file as a byte array
     */
    static int findLength(File reqFile) {
        byte[] fileContent = null;
        try {
            fileContent = Files.readAllBytes(reqFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent != null ? fileContent.length : 0;
    }
}
