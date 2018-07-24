package cn.edu.hit.ir.controller;

import cn.edu.hit.ir.entity.WantedOrder;
import cn.edu.hit.ir.service.IWantedOrderService;
import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
@RequestMapping("/wanted")
public class WantedOrderController {

    @Resource
    private IWantedOrderService wantedOrderService;

    private Gson gson = new Gson();

    @RequestMapping(value = "insertWantedOrder", method = RequestMethod.POST)
    public void insertWantedOrder(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        System.out.println("调用insertWantedOrder函数");
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String info = request.getParameter("info");
        String detail = request.getParameter("detail");
        String datetime = request.getParameter("datetime");
        String number = request.getParameter("number");
        String imgName = request.getParameter("imgName");
        System.out.println("info: " + info + "\ndetail: " + detail
                + "\ndatetime: " + datetime + "\nnumber: " + number
                + "\nimName: " + imgName);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        WantedOrder wantedOrder = new WantedOrder();
        wantedOrder.setDetail(detail);
        wantedOrder.setImgName(imgName);
        wantedOrder.setNumber(number);
        wantedOrder.setInfo(info);
        wantedOrder.setDatetime(formatter.parse(datetime));

        wantedOrderService.insertSelective(wantedOrder);

        response.getWriter().write("success");
    }

    @RequestMapping(value = "getAllWantedOrder")
    public void getAllWantedOrder(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("调用getAllWantedOrder函数");
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        List<WantedOrder> wantedOrderList = wantedOrderService.selectAll();

        response.getWriter().write(gson.toJson(wantedOrderList));

    }

    @RequestMapping(value="/getWantedOrderById", method = RequestMethod.GET)
    @ResponseBody
    public void getWantedOrderById(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("调用getWantedOrderById函数");
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        int id = Integer.parseInt(request.getParameter("id"));

        WantedOrder wantedOrder = wantedOrderService.selectByPrimaryKey(id);

        response.getWriter().write(gson.toJson(wantedOrder));
    }
}
