import java.io.*;
import java.nio.file.Files;

class GetRequest extends ConnectionHandler{

    GetRequest(String req) {
        int backslash;
        int end;
        String filepath = ConnectionHandler.getDir();
        String header;
        byte[] body;

        if(req.contains("/")) {
            backslash = req.indexOf("/");
            end = req.indexOf(" ", backslash);
            filepath += req.substring(backslash, end);
            System.out.println(filepath);
        }

        File reqFile = new File(filepath);
        if (reqFile.isFile()) {
            System.out.println("serving file");
            body = compileBody(reqFile);
            header = compileHeader(body.length);
            sendResponse(header.getBytes(), body);
        }
    }

    private String compileHeader(int length) {

        final String s = "HTTP/1.1 200 OK\n" +
                "Server: Simple Java Http Server\n" +
                "Content-Type: text/html\n" +
                "Content-Length: " + length + "\n" +
                "\r\n";
        return s;
    }

    private byte[] compileBody(File reqFile) {
        byte[] fileContent = null;

        try {
            fileContent = Files.readAllBytes(reqFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileContent;
    }

    private void sendResponse(byte[] header, byte[] body) {
        try {
            ConnectionHandler.getConn().setTcpNoDelay(true);
            BufferedOutputStream out = new BufferedOutputStream(ConnectionHandler.getOs());

            out.write(header);
            out.write(body);

            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
