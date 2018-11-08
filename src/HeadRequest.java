import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

class HeadRequest {
    private final String root;
    private final File notFound;

    HeadRequest(String req) {
        int backslash;
        int end;
        root = ConnectionHandler.getDir();
        notFound = new File(root + "/404.html");
        byte[] header;
        String filepath = "";

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
            header = compileHeader(true, reqFile);
        } else {
            //assume the file was not found, therefore generate 404 response
            header = compileHeader(false, notFound);
        }
        sendResponse(header);
    }

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

    private int findLength(File reqFile) {
        byte[] fileContent = null;
        try {
            fileContent = Files.readAllBytes(reqFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent != null ? fileContent.length : 0;
    }

    private void sendResponse(byte[] header) {
        try {
            ConnectionHandler.getConn().setTcpNoDelay(true);
            BufferedOutputStream out = new BufferedOutputStream(ConnectionHandler.getOs());

            System.out.println("sending header");
            out.write(header);

            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
