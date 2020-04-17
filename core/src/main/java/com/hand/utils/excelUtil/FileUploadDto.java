package com.hand.utils.excelUtil;

import java.util.List;

public class FileUploadDto {
    private List<FileUploadBaseDto> list; //将file解析为List集合

    private int colNum; //列数

    public List<FileUploadBaseDto> getList() {
        return list;
    }

    public void setList(List<FileUploadBaseDto> list) {
        this.list = list;
    }

    public int getColNum() {
        return colNum;
    }

    public void setColNum(int colNum) {
        this.colNum = colNum;
    }
}
