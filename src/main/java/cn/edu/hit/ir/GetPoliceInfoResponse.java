package cn.edu.hit.ir;

import cn.edu.hit.ir.entity.AlarmInfo;

import java.util.List;

public class GetPoliceInfoResponse {

    String status;
    List<AlarmInfo> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<AlarmInfo> getData() {
        return data;
    }

    public void setData(List<AlarmInfo> data) {
        this.data = data;
    }
}
