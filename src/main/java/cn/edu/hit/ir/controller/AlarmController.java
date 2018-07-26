package cn.edu.hit.ir.controller;

import cn.edu.hit.ir.BaseResponse;
import cn.edu.hit.ir.Data;
import cn.edu.hit.ir.GetPoliceInfoResponse;
import cn.edu.hit.ir.entity.AlarmInfo;
import cn.edu.hit.ir.service.IAlarmInfoService;
import com.google.gson.Gson;
import io.goeasy.GoEasy;
import io.goeasy.publish.GoEasyError;
import io.goeasy.publish.PublishListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/alarm")
public class AlarmController {

    private Gson gson = new Gson();

    @Resource
    private IAlarmInfoService alarmInfoService;

    private String rootPath = "F:\\SecurityCompetition\\FaceCompletion\\src\\main\\webapp\\resources\\images\\tmpFiles";

    @RequestMapping(value="/callToPolice")
    public void callToPolice(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        int user_id = Integer.parseInt(request.getParameter("user_id"));
        String content = request.getParameter("content");
        String tel = request.getParameter("tel");
        Float longitude = Float.parseFloat(request.getParameter("longitude"));
        Float latitude = Float.parseFloat(request.getParameter("latitude"));
        String img_name = request.getParameter("img_name");

        //TODO 存入数据库
        AlarmInfo alarmInfo = new AlarmInfo();
        alarmInfo.setContent(content);
        alarmInfo.setUserId(user_id);
        alarmInfo.setTel(tel);
        alarmInfo.setLongitude(longitude);
        alarmInfo.setLatitude(latitude);
        alarmInfo.setImgName(img_name);
        alarmInfoService.insert(alarmInfo);

        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus("200");
        baseResponse.setData(new Data("success", "no reason"));

        response.getWriter().write(gson.toJson(baseResponse));
        Map<String, String> map = new HashMap<>();
        map.put("content", content);
        map.put("user_id", String.valueOf(user_id));
        map.put("tel", tel);
        map.put("longitude", String.valueOf(longitude));
        map.put("latitude",String.valueOf(latitude));
        map.put("img_name", img_name);

        //向前端推送消息
        GoEasy goEasy = new GoEasy("BC-681930150556484b927df348d97cff9e");
        goEasy.publish("alarm", gson.toJson(map), new PublishListener() {
            @Override
            public void onFailed(GoEasyError error) {
                System.out.println("推送失败 Error code:" + error.getCode() + "; error content:" + error.getContent());
            }
            @Override
            public void onSuccess() {
                System.out.println("推送成功");
            }
        });
    }

    @RequestMapping(value="/getPoliceInfo", method = RequestMethod.GET)
    public void getPoliceInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("调用getPoliceInfo函数");
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        int user_id = Integer.parseInt(request.getParameter("user_id"));

        AlarmInfo alarmInfo = alarmInfoService.selectByPrimaryKey(user_id);

        response.getWriter().write(gson.toJson(alarmInfo));
    }

    @RequestMapping(value="/getAlarmInfo", method = RequestMethod.GET)
    public void getAlarmInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("调用getAlarmInfo函数");
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        int user_id = Integer.parseInt(request.getParameter("user_id"));
        String content = request.getParameter("content");
        String tel = request.getParameter("tel");
        Float longitude = Float.parseFloat(request.getParameter("longitude"));
        Float latitude = Float.parseFloat(request.getParameter("latitude"));
        String img_name = request.getParameter("img_name");

        Map<String, String> map = new HashMap<>();
        map.put("content", content);
        map.put("user_id", String.valueOf(user_id));
        map.put("tel", tel);
        map.put("longitude", String.valueOf(longitude));
        map.put("latitude",String.valueOf(latitude));
//        int count = Integer.parseInt(img_name.split("_")[1]);
//        List<String> images = new ArrayList<>();
//        for(int i = 0; i < count; i++){
//            images.add(img_name.split("_")[0] + i);
//        }
        map.put("img_name", img_name);
        response.getWriter().write(gson.toJson(map));
    }

    @RequestMapping(value = "getAllAlarmInfo")
    public void getAllAlarmInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("调用getAllWantedOrder函数");
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        List<AlarmInfo> alarmInfoList = alarmInfoService.selectAll();
        //List<AlarmInfo> alarmInfoList = alarmInfoService.selectByUserId(2);

        response.getWriter().write(gson.toJson(alarmInfoList));

    }

    @RequestMapping(value="/appGetPoliceInfo")
    public void appGetPoliceInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("调用getPoliceInfo函数");
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        int user_id = Integer.parseInt(request.getParameter("user_id")+"");

        List<AlarmInfo> alarmInfo = alarmInfoService.selectByUserId(user_id);

        for (int i=0;i<alarmInfo.size();i++){
            String imgName = alarmInfo.get(i).getImgName();
            String img = imgName.split("_")[0];
            int num = Integer.valueOf(imgName.split("_")[1]);
            System.out.println(img);

            byte[] result = null;
            File f = new File(rootPath, img+"0"+".jpg");
            if (f.exists() && f.isFile()) {
                FileInputStream inputStream = new FileInputStream(f);
                int available = inputStream.available();
                result = new byte[available];
                inputStream.read(result);
                alarmInfo.get(i).setPics(result);
            } else {
                System.out.println("获取这个文件出错！没有这个文件！");
                throw new FileNotFoundException();
            }

        }

        GetPoliceInfoResponse infoResponse = new GetPoliceInfoResponse();
        infoResponse.setStatus("200");
        infoResponse.setData(alarmInfo);
        response.getWriter().write(gson.toJson(infoResponse));
    }

}
