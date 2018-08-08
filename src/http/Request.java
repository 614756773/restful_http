package http;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * HTTP请求的封装
 */
public class Request {
    private String method;
    private String protocol;// 协议版本
    private String requestURI;
    private String host;
    private String Connection;//Http请求连接状态信息 对应HTTP请求中的Connection
    private String agent;// 代理，标识浏览器信息,对应HTTP请求中的User-Agent:
    private String language;//对应Accept-Language
    private String encoding;
    private String charset;
    private String accept;// 对应Accept;
    private HashMap<String,String> parameter;

    public HashMap<String, String> getParameter() {
        return parameter;
    }

    public void setParameter(HashMap<String, String> parameter) {
        this.parameter = parameter;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getConnection() {
        return Connection;
    }

    public void setConnection(String connection) {
        Connection = connection;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getAccept() {
        return accept;
    }

    public void setAccept(String accept) {
        this.accept = accept;
    }

    //暂时只支持get和post
    public void parser(String s) {
        String a="";
        if (s.startsWith("GET")) {
            String method = "GET";
            setMethod(method);

            int index = s.indexOf("HTTP");
            String uri = s.substring(3 + 1, index - 1);// 用index-1可以去掉连接中的空格
            setRequestURI(uri);
            parserGetParameter(uri);

            String protocol = s.substring(index);
            setProtocol(protocol);
        } else if(s.startsWith("POST")){
            String method = "POST";
            setMethod(method);

            int index = s.indexOf("HTTP");
            String uri = s.substring(4 + 1, index - 1);
            setRequestURI(uri);

            String protocol = s.substring(index);
            setProtocol(protocol);

        }else if(s.startsWith("DELETE")){
            setMethod("DELETE");

            int index = s.indexOf("HTTP");
            String uri = s.substring(6 + 1, index - 1);
            setRequestURI(uri);
            parserGetParameter(uri);

            String protocol = s.substring(index);
            setProtocol(protocol);
        }else if(s.startsWith("PUT")){
            setMethod("PUT");

            int index = s.indexOf("HTTP");
            String uri = s.substring(3 + 1, index - 1);
            setRequestURI(uri);
            parserGetParameter(uri);

            String protocol = s.substring(index);
            setProtocol(protocol);
        }
        else{
            switch (s.substring(0,s.indexOf(":"))){
                case "Accept":
                    String accept = s.substring("Accept:".length() + 1);
                    setAccept(accept);
                    break;
                case "User-Agent":
                    String agent = s.substring("User-Agent:".length() + 1);
                    setAgent(agent);
                    break;
                case "Host":
                    String host = s.substring("Host:".length() + 1);
                    setHost(host);
                    break;
                case "Accept-Language":
                    String language = s.substring("Accept-Language:".length() + 1);
                    setLanguage(language);
                    break;
                case "Accept-Charset":
                    String charset = s.substring("Accept-Charset:".length() + 1);
                    setCharset(charset);
                    break;
                case "Accept-Encoding":
                    String encoding = s.substring("Accept-Encoding:".length() + 1);
                    setEncoding(encoding);
                    break;
                case "Connection":
                    String connection = s.substring("Connection:".length() + 1);
                    setConnection(connection);
                    break;
            }
        }

    }

    /**
     * 解析get方式请求的参数
     * @param uri
     */
    private void parserGetParameter(String uri) {
        if(!uri.contains("?"))
            return;
        this.parameter=new HashMap<>();
        uri=uri.substring(uri.indexOf("?")+1);
        String[] list=uri.split("&");
        for(String e:list){
            String[] key_value=e.split("=");
            parameter.put(key_value[0],key_value[1]);
        }
    }

    /**
     * post的参数解析必须外部手动调用。
     * 知道这样做还有点蠢= =，时间紧迫
     * @param s 形如username=%E7%A7%A6%E8%91%97&password=123
     * 如果包含参数_method则说明这是前台提交过来的非get/post请求，需要重新设置一下请求行
     */
    public void parserPostParameter(String s){
        if(s==null||s.length()==0)
            return;
        this.parameter=new HashMap<>();
        String[] list=s.split("&");
        for(String e:list){
            String[] key_value=e.split("=");
            //kev_value==1时是提交空表单的情况
            if(key_value.length!=1){
                //处理非get post请求
                if(key_value[0].equals("_method")){
                    this.setMethod(key_value[1].toUpperCase());
                }else{
                    parameter.put(key_value[0],key_value[1]);
                }
            }
        }
    }
}
