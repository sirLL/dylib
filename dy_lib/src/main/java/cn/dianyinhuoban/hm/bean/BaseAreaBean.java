package cn.dianyinhuoban.hm.bean;

public class BaseAreaBean {

    /**
     *  "areaId": "1",
     *     "areaName": "北京",
     */

    private String areaId;
    private String areaName;

    public BaseAreaBean() {
    }

    public BaseAreaBean(String areaId, String areaName) {
        this.areaId = areaId;
        this.areaName = areaName;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

}
