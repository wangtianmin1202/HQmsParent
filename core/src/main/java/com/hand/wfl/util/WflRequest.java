package com.hand.wfl.util;

import com.hand.hap.activiti.dto.TaskActionRequestExt;

import java.util.List;

/**
 * 流程数据请求工具类
 *
 * @param <T>
 */
public class WflRequest<T> {
    private TaskActionRequestExt actionRequest;

    private List<T> list;

    private T dto;

    public TaskActionRequestExt getActionRequest() {
        return actionRequest;
    }

    public void setActionRequest(TaskActionRequestExt actionRequest) {
        this.actionRequest = actionRequest;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public T getDto() {
        return dto;
    }

    public void setDto(T dto) {
        this.dto = dto;
    }
}
