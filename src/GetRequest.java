import java.io.*;
import java.nio.file.Files;

class GetRequest extends ConnectionHandler{

    GetRequest(String req) {
        int backslash;
        int end;
        String filepath = dir;
        String header = "";
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
//        //byte[] header = req.getBytes();
//        try {
//            //BufferedOutputStream out = new BufferedOutputStream(conn.getOutputStream());
//            //out.write(header, 0, header.length);
//            PrintWriter out = new PrintWriter(conn.getOutputStream());
//
//
//            out.flush();
//        } catch (IOException e) {
//            System.out.println("Sending Header issue: " + e.getMessage());
//        }
        StringBuilder sb = new StringBuilder();

        sb.append("HTTP/1.1 200 OK\n");
        sb.append("Server: Simple Java Http Server\n");
        sb.append("Content-Type: text/html\n"); //find a way to derive this
        sb.append("Content-Length: ").append(length).append("\n");
        sb.append("\r\n");

        return sb.toString();
    }

    private byte[] compileBody(File reqFile) {
//        try {
//            BufferedInputStream in = new BufferedInputStream(new FileInputStream(reqFile));
//            BufferedOutputStream out = new BufferedOutputStream(conn.getOutputStream());
//            //PrintWriter out = new PrintWriter(conn.getOutputStream());
//            byte[] buffer = new byte[8192];
//            int count;
//
//            while((count = in.read(buffer)) > 0) {
//                out.write(buffer, 0, count);
//            }
//
//            in.close();
//            out.close();
//        } catch (IOException e) {
//            System.out.println("Sending Body issue: " + e.getMessage());
//        }
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
            conn.setTcpNoDelay(true);
            //BufferedOutputStream out = new BufferedOutputStream(conn.getOutputStream());
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());

            out.write(header);
            out.write(body);

            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
