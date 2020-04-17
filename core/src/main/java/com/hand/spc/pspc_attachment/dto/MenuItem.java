//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hand.spc.pspc_attachment.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.List;

public class MenuItem implements Comparable<MenuItem>, Serializable {
    private static final long serialVersionUID = -1412546289282861225L;
    private List<MenuItem> children;
    private boolean expand = false;
    private String functionCode;
    private String icon;
    private Long id;
    private Boolean ischecked;
    @JsonIgnore
    private MenuItem parent;
    private long score;
    private Long shortcutId;
    private String text;
    private String url;
    /**
     * 类型
     */
    private String type;

    public MenuItem() {
    }

    @Override
    public int compareTo(MenuItem o) {
        return (int)(this.score - o.score);
    }

    public List<MenuItem> getChildren() {
        return this.children;
    }

    public String getIcon() {
        return this.icon;
    }

    public Long getId() {
        return this.id;
    }

    public Boolean getIschecked() {
        return this.ischecked;
    }

    public MenuItem getParent() {
        return this.parent;
    }

    public long getScore() {
        return this.score;
    }

    public Long getShortcutId() {
        return this.shortcutId;
    }

    public String getText() {
        return this.text;
    }

    public String getUrl() {
        return this.url;
    }

    public boolean isExpand() {
        return this.expand;
    }

    public void setChildren(List<MenuItem> children) {
        this.children = children;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setIschecked(Boolean ischecked) {
        this.ischecked = ischecked;
    }

    public void setParent(MenuItem parent) {
        this.parent = parent;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public void setShortcutId(Long shortcutId) {
        this.shortcutId = shortcutId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFunctionCode() {
        return this.functionCode;
    }

    public void setFunctionCode(String functionCode) {
        this.functionCode = functionCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
