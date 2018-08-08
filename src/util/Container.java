package util;

import http.Request;

import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class Container {

    //必须有reqeust和out,这样才能获取值和输出值，这两个参数在controller的方法中必须是在最后两个有序放好
    //位置定死了有点蠢。
    public void run(Object target, Request request, PrintStream out) {
        Method[] methods=target.getClass().getMethods();
        ArrayList<Object> args=new ArrayList<>();//用来method存储调用的参数
        boolean success=false;
        for(Method method:methods){
            if(pipei(method,request.getRequestURI(),request.getMethod(),args)){
                try {
                    args.add(request);
                    args.add(out);
                    method.invoke(target, args.toArray());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    private boolean pipei(Method method, String url, String _method, ArrayList<Object> args) {
        if(!method.isAnnotationPresent(QinAnnotation.class))
            return false;
        //如果请求类型不一致 不匹配
        QinAnnotation myannotation= method.getAnnotation(QinAnnotation.class);
        String annotation_url=myannotation.url();
        String annotation_method=myannotation.method();
        if(!annotation_method.toLowerCase().equals(_method.toLowerCase()))
            return false;
        //如果长度不一致 肯定不匹配
        String[] url_array=url.split("/");
        String[] annotationUrl_array=annotation_url.split("/");
        if(url_array.length!=annotationUrl_array.length)
            return false;

        //把调用方法的参数放在args数组里
        for(int i=0;i<annotationUrl_array.length;i++){
            if(annotationUrl_array[i].startsWith("{")&&annotationUrl_array[i].endsWith("}")){
                args.add(url_array[i]);
                continue;
            }
            //如果路径名称不一致 不匹配
            if(!annotationUrl_array[i].equals(url_array[i]))
                return false;
        }
        return true;
    }
}
