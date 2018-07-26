package cn.edu.hit.ir;

import cn.edu.hit.ir.entity.WantedOrder;

import java.util.ArrayList;
import java.util.List;

public class WantedOrderResponse {
    String status;
    List<WantedOrder> data;

    public List<WantedOrder> getData() {
        return data;
    }


    public void setData(List<WantedOrder> data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
