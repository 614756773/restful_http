package http;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket=new ServerSocket(10006);
        Socket socket=null;
        System.out.println("服务端已启动");
        while(true){
            socket=serverSocket.accept();
            new Thread(new ServerThread(socket)).start();
        }
    }
}

