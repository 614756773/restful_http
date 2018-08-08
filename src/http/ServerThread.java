package http;

import controller.LoginController;
import controller.StaffController;
import util.Container;
import view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class ServerThread implements Runnable {
    private Socket socket;
    private int contentLength;
    private View view=new View();
    private Container container=new Container();
    public ServerThread(Socket socket){
        this.socket=socket;
    }
    @Override
    public void run() {
        try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintStream out = new PrintStream(socket.getOutputStream());
                String tmp=reader.readLine();
                //如果是发送的非get/post类型的请求 那请求行里的类型会是'OPTIONS'
                //需要服务器告诉客户端自己能接受哪些类型的请求，如果客户端的请求在那些类型里则再发送第二次真实的请求
                if(tmp.contains("OPTIONS")){
                    out.println("HTTP/1.1 200 OK");
                    out.println("Content-Type:application/json;charset: UTF-8");
                    out.println("Access-Control-Allow-Origin: *");
                    out.println("Access-Control-Allow-Methods: POST, GET, PUT,DELETE");
                    out.println();
                    socket.close();
                    return;
                }
                //用浏览器访问时会请求图标,不处理这个请求
                if(!tmp.contains("favicon")){
                    Request request = new Request();
                    request.parser(tmp);
                    while (!(tmp = reader.readLine()).equals("")) {
                        if (tmp.indexOf("Content-Length") != -1) {
                            contentLength = Integer.parseInt(tmp.substring(tmp.indexOf("Content-Length") + 16));
                        }
                        request.parser(tmp);
                    }
                    //需要注意的是put请求 实质上也是一个post请求，只是附带了一个_method=put参数
                    //所以也会进入下面的处理，处理之后才变成put请求
                    if (request.getMethod().equals("POST"))
                        getdata(reader, request);
                    System.out.println(request.getMethod()+" "+request.getRequestURI()+" "+request.getProtocol());
                    //这儿很蠢，如果有新的Controller还需要手动添加分支
                    //以后有空再改
                    if(request.getRequestURI().contains("login")||request.getRequestURI().contains("register"))
                        container.run(new LoginController(),request,out);
                    else
                        container.run(new StaffController(),request,out);
                }
                socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getdata(BufferedReader reader, Request request) throws IOException {
        //取得数据！
        byte[] buf = {};
        int size = 0;
        if (contentLength != 0) {
            buf = new byte[contentLength];
            while(size<contentLength){
                int c = reader.read();
                buf[size++] = (byte)c;

            }
            String s=new String(buf, 0, size);
            request.parserPostParameter(s);
        }
    }
}
