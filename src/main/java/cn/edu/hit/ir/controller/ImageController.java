package cn.edu.hit.ir.controller;


import cn.edu.hit.ir.BaseResponse;
import cn.edu.hit.ir.Data;
import cn.edu.hit.ir.entity.AlarmInfo;
import cn.edu.hit.ir.service.IAlarmInfoService;
import com.google.gson.Gson;
import io.goeasy.GoEasy;
import io.goeasy.publish.GoEasyError;
import io.goeasy.publish.PublishListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/image")
public class ImageController {
    private Gson gson = new Gson();

    @Resource
    private IAlarmInfoService alarmInfoService;

    //    private String rootPath = "C:\\Users\\ZhaoYang\\Pictures\\image";
    private String rootPath = "F:\\SecurityCompetition\\FaceCompletion\\src\\main\\webapp\\resources\\images";
    @RequestMapping(value = "/uploadFileHandler", method = RequestMethod.POST)
    @ResponseBody
    public String uploadFileHandler(@RequestParam("file") MultipartFile file, @RequestParam("imgName") String imgName) {
        System.out.println("调用uploadFileHandler函数");
        System.out.println(imgName);
        if (!file.isEmpty()) {
            try {
                // 文件存放服务端的位置
                File dir = new File(rootPath + File.separator + "tmpFiles");
                //if (!dir.exists())
                    //dir.mkdirs();
                // 写文件到服务器
                File serverFile = new File(dir.getAbsolutePath() + File.separator + imgName + ".jpg");
                file.transferTo(serverFile);
                return file.getOriginalFilename();
            } catch (Exception e) {
                return "You failed to upload " + file.getOriginalFilename() + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + file.getOriginalFilename() + " because the file was empty.";
        }
    }

    //上传多个文件
    @RequestMapping(value = "/appUploadFileHandler")
    @ResponseBody
    public void appUploadFileHandler(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        System.out.println("调用uploadFileHandler函数");
        //System.out.println(imgName);
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;

        List<MultipartFile> files = multiRequest.getFiles("file");
        String content = request.getParameter("content");
        String tel = request.getParameter("tel");
        String longitude = request.getParameter("longitude");
        String latitude = request.getParameter("latitude");
        System.out.println("longitude"+longitude);
        System.out.println(""+latitude);
        Float lon = Float.valueOf(longitude);
        Float lat = Float.valueOf(latitude);
        String filename = request.getParameter("filename");
        System.out.println(filename);
        int user_id = Integer.parseInt(request.getParameter("user_id") + "");
        int i = 0;
        if (files != null && files.size() != 0) {
            try {
                for (i = 0;i < files.size(); i++){
                    File dir = new File(rootPath + File.separator + "tmpFiles" + File.separator + filename+i);
                    //if (!dir.exists())
                    //dir.mkdirs();
                    // 写文件到服务器
                    File serverFile = new File(dir.getAbsolutePath()+ ".jpg");
                    files.get(i).transferTo(serverFile);
                }
                AlarmInfo info = new AlarmInfo();
                info.setContent(content);
                info.setTel(tel);
                info.setLongitude(lon);
                info.setLatitude(lat);
                info.setUserId(user_id);
                info.setImgName(filename+ "_" + i);
                alarmInfoService.insert(info);
                // 文件存放服务端的位置
                BaseResponse baseResponse = new BaseResponse();
                baseResponse.setStatus("200");
                baseResponse.setData(new Data("success",""));
                response.getWriter().write(gson.toJson(baseResponse));
                response.getWriter().close();

                Map<String, String> map = new HashMap<>();
                map.put("content", content);
                map.put("user_id", String.valueOf(user_id));
                map.put("tel", tel);
                map.put("longitude", String.valueOf(longitude));
                map.put("latitude",String.valueOf(latitude));
                map.put("img_name", filename+"_" + i);

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
            } catch (Exception e) {
                e.printStackTrace();
                BaseResponse baseResponse = new BaseResponse();
                baseResponse.setStatus("500");
                response.getWriter().write(gson.toJson(baseResponse));
                response.getWriter().close();
            }
        } else {
            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setStatus("500");
            response.getWriter().write(gson.toJson(baseResponse));
            response.getWriter().close();
        }
    }

    @RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
    @ResponseBody
    public void downloadFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //response.setContentType("multipart/form-data");
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String img_name = request.getParameter("img_name");
        System.out.println("downloadFile被执行");
        String imgRootPath = rootPath + File.separator + "tmpFiles/";
        String imgPath = imgRootPath + img_name;
        FileInputStream ips = new FileInputStream(new File(imgPath));
        response.setContentType("multipart/form-data");
        ServletOutputStream out = response.getOutputStream();
        //读取文件流
        int len = 0;
        byte[] buffer = new byte[1024 * 10];
        while ((len = ips.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
    }

    @RequestMapping(value = "/faceCompletion", method = RequestMethod.POST)
    @ResponseBody
    public String faceCompletion(@RequestParam("file") MultipartFile file)
            throws IOException{
        System.out.println("faceCompletion被执行");
        if (!file.isEmpty()) {
            // 文件存放服务端的位置
            File dir = new File(rootPath + File.separator + "faceCompletion");
            String pathStr = dir.getAbsolutePath() + File.separator + file.getOriginalFilename();
            // 写文件到服务器
            File serverFile = new File(pathStr);
            file.transferTo(serverFile);

            //发送图片
            Socket socket = new Socket("127.0.0.1", 8888);
            BufferedInputStream bin = new BufferedInputStream(new FileInputStream(new File(pathStr)));
            OutputStream out = socket.getOutputStream();
            out.write('0');

            byte buf[] = new byte[1024];
            int len = 0;
            while((len = bin.read(buf)) != -1){
                out.write(buf,0,len);
            }
            out.close();
            socket.close();

            System.out.println("等待接收图片...");
            ServerSocket server = new ServerSocket(7777);
            socket = server.accept();
            InputStream is = socket.getInputStream();
            String path = rootPath + File.separator + "tmpFiles";
            //FileOutputStream fileOutputStream = new FileOutputStream(getExternalFilesDir(null)+ File.separator+"result.png");
            FileOutputStream fileOutputStream = new FileOutputStream(path + File.separator + "faceCompletion.jpg");
            byte[] buff = new byte[1024];
            int len2 = 0;
            while ((len2 = is.read(buff)) != -1) {
                fileOutputStream.write(buff, 0, len2);
            }
            is.close();
            fileOutputStream.close();
            socket.close();
            System.out.println("图片传输完成");
            return "success";
        }else{
            return "error";
        }
    }

    @RequestMapping(value = "/faceCompare", method = RequestMethod.POST)
    @ResponseBody
    public void faceCompare(@RequestParam("file") MultipartFile file, HttpServletResponse response)
            throws IOException{
        System.out.println("faceCompletion被执行");
        if (!file.isEmpty()) {
            // 文件存放服务端的位置
            File dir = new File(rootPath + File.separator + "faceCompare");
            String pathStr = dir.getAbsolutePath() + File.separator + file.getOriginalFilename();
            // 写文件到服务器
            File serverFile = new File(pathStr);
            file.transferTo(serverFile);

            //发送图片
            Socket socket = new Socket("127.0.0.1", 8888);
            BufferedInputStream bin = new BufferedInputStream(new FileInputStream(new File(pathStr)));
            OutputStream out = socket.getOutputStream();
            out.write('0');

            byte buf[] = new byte[1024];
            int len = 0;
            while((len = bin.read(buf)) != -1){
                out.write(buf,0,len);
            }
            out.close();
            socket.close();

            System.out.println("等待接收结果...");
            ServerSocket server = new ServerSocket(7777);
            socket = server.accept();
            InputStream is = socket.getInputStream();
            BufferedReader br=new BufferedReader(new InputStreamReader(is));
            String result = br.readLine();
            System.out.println(result);
            is.close();
            socket.close();
            System.out.println("数据接受完成");
            Map<String, String> map = new HashMap<String,String>();
            map.put("id", result.split(",")[0]);
            map.put("conf", result.split(",")[1]);
            response.getWriter().write(gson.toJson(map));
        }
    }
}
