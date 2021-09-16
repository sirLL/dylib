package cn.dianyinhuoban.hm.bean;


import java.util.List;

public class ProvinceBean extends BaseAreaBean {

    private List<CityBean> cities;

    public List<CityBean> getCities() {
        return cities;
    }

    public void setCities(List<CityBean> cities) {
        this.cities = cities;
    }

}
