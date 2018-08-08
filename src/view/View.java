package view;

import http.Request;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class View {

    public void display(PrintStream out, Request request) {
    }


    /**
     * 给body添加上响应行，响应头
     */
    public void outPutHtml(String html_file, PrintStream out) {
        String html=readHtml(html_file);

        out.println("HTTP/1.1 200 OK");
        out.println("Content-Type:text/html;charset: UTF-8");
        out.println("Access-Control-Allow-Origin: *");
        out.println();
        out.println(html);
        out.flush();
        out.close();
    }

    public void outPutJson(String json, PrintStream out) {
        out.println("HTTP/1.1 200 OK");
        out.println("Content-Type:application/json;charset: UTF-8");
        out.println("Access-Control-Allow-Origin: *");
        out.println();
        out.println(json);
        out.flush();
        out.close();
    }

    private String readHtml(String html_file) {
        File file=new File("src/view/"+html_file);
        FileInputStream fis=null;
        byte[] buffer=new byte[1024];
        int len=0;
        StringBuilder sb=new StringBuilder();
        try {
            fis=new FileInputStream(file);
            while ((len=fis.read(buffer))!=-1){
                sb.append(new String(buffer,0,len));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}
