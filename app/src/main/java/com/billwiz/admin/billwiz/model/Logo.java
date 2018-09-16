package com.billwiz.admin.billwiz.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;



public class Logo extends BmobObject {

    private BmobFile file;

    public Logo(BmobFile file) {
        this.file = file;
    }

    public Logo(String tableName, BmobFile file) {
        super(tableName);
        this.file = file;
    }

    public BmobFile getFile() {
        return file;
    }

    public void setFile(BmobFile file) {
        this.file = file;
    }
}
