import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

class UnknownRequest {
    private final File notFound;

    UnknownRequest(String req) {
        int backslash;
        int end;
        notFound = new File(ConnectionHandler.getDir() + "/404.html");
        byte[] header;
        String filepath = "";

        header = compileHeader();
        sendResponse(header);
    }

    private byte[] compileHeader() {
        int length = findLength(notFound);
        final String s = "HTTP/1.1 501 Not Implemented\n"
             + "Server: Simple Java Http Server\n"
             + "Content-Type: text/html\n"
             + "Content-Length: " + length + "\n\r\n";
        return s.getBytes();
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
