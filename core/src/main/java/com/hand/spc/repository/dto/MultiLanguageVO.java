package com.hand.spc.repository.dto;

import java.io.Serializable;

public class MultiLanguageVO implements Serializable {
    private static final long serialVersionUID = 6461193288690767048L;
    public MultiLanguageVO(String coloumName,String lang,String values)
    {
        this.coloumName=coloumName;
        this.lang=lang;
        this.values=values;
    }

    private String coloumName;
    private String lang;
    private String values;

    public String getColoumName() {
        return coloumName;
    }

    public void setColoumName(String coloumName) {
        this.coloumName = coloumName;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }
}
