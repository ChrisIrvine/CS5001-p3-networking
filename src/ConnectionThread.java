//import java.net.Socket;
//
//public class ConnectionThread implements Runnable {
//
//    private Thread t;
//    private String tDir;
//    private Socket tConn;
//
//
//    ConnectionThread(Socket conn, String dir, String name) {
//        this.tConn = conn;
//        this.tDir = dir;
//        System.out.println("Creating thread " + name);
//        t = new Thread(this, name);
//        t.start();
//    }
//
//    @Override
//    public void run() {
//        ConnectionHandler ch = new ConnectionHandler(this.tConn, this.tDir);
//        ch.handleClientRequest();
//        System.out.println("Exiting thread");
//    }
//}
