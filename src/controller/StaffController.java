package controller;

import com.alibaba.fastjson.JSONObject;
import entity.Staff;
import http.Request;
import util.DataBase;
import util.JsonData;
import util.QinAnnotation;
import view.View;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeSet;

public class StaffController {

    View view=new View();

    @QinAnnotation(url = "/home",method = "get")
    public void showHomePage(Request request,PrintStream out){
        view.outPutHtml("staff.html",out);
    }

    /**
     * 模糊搜索，可以根据姓名 性别 年龄进行搜索
     * @param searchKey
     * @param request
     * @param out
     */
    @QinAnnotation(url = "/home/staff/{searchKey}",method = "get")
    public void find(String searchKey, Request request, PrintStream out){
        HashMap<String,Staff> _return=new HashMap<>();
        JsonData jsonData;
        for(Map.Entry<String,Staff> e:DataBase.STAFF.entrySet()){
            int age=-1;
            //有可能搜索的关键字并不是字符型的数字 所以要捕获一下异常
            try {
                age=Integer.parseInt(searchKey);
            } catch (Exception e1) {}
            Staff staff=e.getValue();
            if(staff.getName().contains(searchKey)||
                    staff.getSex().contains(searchKey)||
                    age==staff.getAge()){
                _return.put(e.getKey(),staff);
            }
        }
        if(_return.size()!=0) {
            jsonData=new JsonData(1,"success",_return);
        }else{
            jsonData=new JsonData(0,"No search results");
        }
        view.outPutJson(JSONObject.toJSONString(jsonData),out);
    }

    @QinAnnotation(url = "/home/staff",method = "get")
    public void findAll(Request request,PrintStream out){
        HashMap map=DataBase.STAFF;
        String json="";
        if(map.size()==0){
            json=JSONObject.toJSONString(new JsonData(0,"There are no staff"));
            view.outPutJson(json,out);
            return;
        }
        json=JSONObject.toJSONString(new JsonData(1,"success",map));
        view.outPutJson(json,out);
    }

    /**
     * 这个方法的前台是通过ajax请求的，type类型为DELETE
     * 使用了使用CORS头文件
     * @param id
     * @param request
     * @param out
     */
    @QinAnnotation(url = "/home/staff/{id}",method = "delete")
    public void delete(String id,Request request,PrintStream out){
        DataBase.STAFF.remove(id);
        JsonData json=new JsonData(1,"success");
        view.outPutJson(JSONObject.toJSONString(json),out);
    }

    @QinAnnotation(url ="/home/staff",method = "post")
    public void add(Request request,PrintStream out){
        HashMap<String, Staff> map=DataBase.STAFF;
        //放在新id重复
        Random random=new Random();
        int id= random.nextInt(1<<16);
        while (map.get(id)!=null){
            id=random.nextInt(1<<16);
        }
        //获取staff
        HashMap<String,String> parameter=request.getParameter();
        String name=parameter.get("name");
        String sex=parameter.get("sex");
        if(!sex.equals("man")&&!sex.equals("woman")){
            sex="unknow";
        }
        String age_tmp=parameter.get("age");
        int age;
        try {
            age=Integer.parseInt(age_tmp);
        } catch (NumberFormatException e) {
            age=0;
        }
        Staff staff=new Staff(name,sex,age);
        map.put(String.valueOf(id),staff);
        //返回
        view.outPutHtml("staff.html",out);
    }

    @QinAnnotation(url = "/home/staff/{id}",method = "put")
    public void update(String id,Request request,PrintStream out){
        HashMap<String, Staff> map=DataBase.STAFF;
        //获取staff
        HashMap<String,String> parameter=request.getParameter();
        String name=parameter.get("name");
        String sex=parameter.get("sex");
        if(!sex.equals("man")&&!sex.equals("woman")){
            sex="unknow";
        }
        String age_tmp=parameter.get("age");
        int age;
        try {
            age=Integer.parseInt(age_tmp);
        } catch (NumberFormatException e) {
            age=0;
        }
        Staff staff=new Staff(name,sex,age);
        map.put(id,staff);
        //返回
        view.outPutHtml("staff.html",out);
    }
}
