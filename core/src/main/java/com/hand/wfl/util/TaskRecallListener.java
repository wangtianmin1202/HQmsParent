package com.hand.wfl.util;

import com.hand.hap.activiti.event.dto.TaskRecallInfo;
import com.hand.hap.core.IRequest;

/**
 * description
 *
 * @author KOCE3G3 2020/03/26 12:18 PM
 */
public interface TaskRecallListener {

    String processDefinitionKey();

    void doRecall(IRequest request, TaskRecallInfo taskRecallInfo);

}
