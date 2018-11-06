import java.io.*;

class GetRequest extends ConnectionHandler{

    GetRequest(String req) {
        int backslash;
        int end;
        String filepath = dir;

        if(req.contains("/")) {
            backslash = req.indexOf("/");
            end = req.indexOf(" ", backslash);
            filepath += req.substring(backslash, end);
            System.out.println(filepath);
        }

        File reqFile = new File(filepath);
        if (reqFile.isFile()) {
            System.out.println("serving file");
            handleGetRequest(reqFile);
        }
    }

    private void handleGetRequest(File reqFile) {
        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(reqFile));
            BufferedOutputStream out = new BufferedOutputStream(conn.getOutputStream());
            int c;

            System.out.println("copying bytes");
            while ((c = in.read()) != -1) {
                out.write(c);
            }
            System.out.println("copied bytes");

            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
