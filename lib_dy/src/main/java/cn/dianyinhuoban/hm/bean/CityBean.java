package cn.dianyinhuoban.hm.bean;

import java.util.List;

public class CityBean extends BaseAreaBean {

    private List<CountyBean> counties;

    public List<CountyBean> getCounties() {
        return counties;
    }

    public void setCounties(List<CountyBean> counties) {
        this.counties = counties;
    }
}
