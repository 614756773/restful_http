package controller;

import com.alibaba.fastjson.JSONObject;
import http.Request;
import util.DataBase;
import util.JsonData;
import util.QinAnnotation;
import view.View;

import java.io.PrintStream;
import java.util.HashMap;

public class LoginController {
    private View view=new View();

    @QinAnnotation(url="/login",method = "get")
    public void showLogin(Request request,PrintStream out){
        view.outPutHtml("login.html",out);
    }

    @QinAnnotation(url = "/login",method = "post")
    public void login(Request request, PrintStream out){
        HashMap<String,String> parameter=request.getParameter();
        String username=parameter.get("username");
        String password=parameter.get("password");
        String pd=DataBase.USER.get(username);
        String json="";
        //说明USER里还没有改用户
        if(pd==null){
            json= JSONObject.toJSONString(new JsonData(0,"this username don't exist"));
            view.outPutJson(json,out);
            return;
        }
        if(pd==null||pd.equals("")||pd.length()==0){
            json= JSONObject.toJSONString(new JsonData(0,"password can't be blank"));
            view.outPutJson(json,out);
            return;
        }
        if(!pd.equals(password)){
            json= JSONObject.toJSONString(new JsonData(0,"password wrong"));
            view.outPutJson(json,out);
            return;
        }
        json= JSONObject.toJSONString(new JsonData(1,"success"));
        view.outPutJson(json,out);
    }

    @QinAnnotation(url="/register",method = "get")
    public void showRegister(Request request,PrintStream out){
        view.outPutHtml("register.html",out);
    }

    @QinAnnotation(url="/register",method = "post")
    public void register(Request request,PrintStream out){
        Boolean isExits=true;//用户判断新添加的用户名称是否已经存在
        HashMap<String,String> parameter=request.getParameter();
        String username=parameter.get("username");
        String password=parameter.get("password");
        String repassowrd=parameter.get("repassword");
        String json="";
        if(DataBase.USER.get(username)!=null){
            json=JSONObject.toJSONString(new JsonData(0,"username already exists"));
            view.outPutJson(json,out);
            return;
        }
        if(!password.equals(repassowrd)){
            json=JSONObject.toJSONString(new JsonData(0,"Inconsistent password"));
            view.outPutJson(json,out);
            return;
        }
        DataBase.USER.put(username,password);
        json=JSONObject.toJSONString(new JsonData(1,"success"));
        view.outPutJson(json,out);
    }
}
