package cn.edu.hit.ir.controller;


import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/image")
public class ImageController {
    private Gson gson = new Gson();

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
