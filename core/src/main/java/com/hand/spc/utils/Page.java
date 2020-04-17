package com.hand.spc.utils;

// import io.choerodon.core.domain.page
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.github.pagehelper.PageInfo;

@JsonFormat(
    shape = Shape.OBJECT
)
public class Page<E> extends AbstractList<E> {
    private int totalPages;
    private long totalElements;
    private int numberOfElements;
    private int size;
    private int number;
    private List<E> content;

    public Page() {
        this.content = new ArrayList();
    }

    public Page(List<E> content, PageInfo pageInfo, long total) {
        this.content = content;
        this.number = pageInfo.getPageNum();
        this.size = pageInfo.getSize();
        this.totalElements = total;
        this.totalPages = (int)(total - 1L) / this.size + 1;
        this.numberOfElements = content.size();
    }

    public int getTotalPages() {
        return this.totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalElements() {
        return this.totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getNumberOfElements() {
        return this.numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNumber() {
        return this.number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<E> getContent() {
        return this.content;
    }

    public void setContent(List<E> content) {
        this.content = content;
    }

    public E get(int i) {
        return this.content.get(i);
    }

    public int size() {
        return this.content.size();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            if (!super.equals(o)) {
                return false;
            } else {
                Page<?> page = (Page)o;
                if (this.totalPages != page.totalPages) {
                    return false;
                } else if (this.totalElements != page.totalElements) {
                    return false;
                } else if (this.numberOfElements != page.numberOfElements) {
                    return false;
                } else if (this.size != page.size) {
                    return false;
                } else if (this.number != page.number) {
                    return false;
                } else {
                    return this.content != null ? this.content.equals(page.content) : page.content == null;
                }
            }
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + this.totalPages;
        result = 31 * result + (int)(this.totalElements ^ this.totalElements >>> 32);
        result = 31 * result + this.numberOfElements;
        result = 31 * result + this.size;
        result = 31 * result + this.number;
        result = 31 * result + (this.content != null ? this.content.hashCode() : 0);
        return result;
    }
}
