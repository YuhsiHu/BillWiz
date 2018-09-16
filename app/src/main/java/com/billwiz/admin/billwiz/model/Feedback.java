package com.billwiz.admin.billwiz.model;
import cn.bmob.v3.BmobObject;


public class Feedback extends BmobObject {

    String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
