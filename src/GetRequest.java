import java.io.*;

class GetRequest extends ConnectionHandler{

    GetRequest(String req) {
        int backslash;
        int end;
        String filepath = dir;

        sendHeader(req);

        if(req.contains("/")) {
            backslash = req.indexOf("/");
            end = req.indexOf(" ", backslash);
            filepath += req.substring(backslash, end);
            System.out.println(filepath);
        }

        File reqFile = new File(filepath);
        if (reqFile.isFile()) {
            System.out.println("serving file");
            sendBody(reqFile);
        }
    }

    private void sendHeader(String req) {
        byte[] header = req.getBytes();
        try {
            BufferedOutputStream out = new BufferedOutputStream(conn.getOutputStream());
            out.write(header, 0, header.length);
            out.flush();
        } catch (IOException e) {
            System.out.println("Sending Header issue: " + e.getMessage());
        }
    }

    private void sendBody(File reqFile) {
        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(reqFile));
            BufferedOutputStream out = new BufferedOutputStream(conn.getOutputStream());
            //PrintWriter out = new PrintWriter(conn.getOutputStream());
            byte[] buffer = new byte[8192];
            int count;

            while((count = in.read(buffer)) > 0) {
                out.write(buffer, 0, count);
            }

            in.close();
            out.close();
        } catch (IOException e) {
            System.out.println("Sending Body issue: " + e.getMessage());
        }
    }
}
