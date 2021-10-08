package cn.dianyinhuoban.hm.mvp.bean;

import com.dy.tablayout.listener.CustomTabEntity;

public class TabEntity implements CustomTabEntity {
    private String title;

    public TabEntity(String title) {
        this.title = title;
    }

    @Override
    public String getTabTitle() {
        return title;
    }

    @Override
    public int getTabSelectedIcon() {
        return 0;
    }

    @Override
    public int getTabUnselectedIcon() {
        return 0;
    }
}
