package cn.dianyinhuoban.hm.bean;

import com.sunfusheng.marqueeview.IMarqueeItem;

public class CustomModel implements IMarqueeItem {
    private String title;
    private String content;

    @Override
    public CharSequence marqueeMessage() {
        return title;
    }

    public CustomModel(String message, String content) {
        this.title = message;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
