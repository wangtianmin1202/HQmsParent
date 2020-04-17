/**
 * 流程相关信息
 */
(function (w) {

    var Activiti = w.Activiti = {};

    /**
     * 获取流程实例信息详情
     *
     * @param processInstId
     */
    Activiti.getProcessInstanceDetail = function (processInstId) {
        if (!processInstId) {
            return null;
        }

        var data = null;
        $.ajax({
            url: _basePath + '/wfl/instance/' + processInstId,
            async: false,
            success: function (result) {
                data = result;
            }
        });
        return data;

    };

    /**
     * 获取任务实例信息详情
     *
     * @param processInstId
     */
    Activiti.getTaskDetail = function (taskId) {
        if (!taskId) {
            return null;
        }
        var data = null;
        var url = _basePath + '/wfl/runtime/tasks/{taskId}/details';
        url = url.replace("{taskId}", taskId);
        $.ajax({
            url: url,
            async: false,
            success: function (result) {
                data = result;
            }
        });
        return data;

    };

    /**
     * 获取任务表单key
     *
     * @param processInstId
     */
    Activiti.getTaskFormKey = function (taskId) {
        var formKey = (Activiti.getTaskDetail(taskId) || {}).formKey;
        if (formKey) {
            formKey = Activiti.getFormKeyByStr(formKey);
        }
        return formKey;
    };

    /**
     * 获取详情表单key
     *
     * @param processInstId
     */
    Activiti.getFormKey = function (processInstId) {
        var formKey = (Activiti.getProcessInstanceDetail(processInstId) || {}).formKey;
        if (formKey) {
            formKey = Activiti.getFormKeyByStr(formKey);
        }
        return formKey;
    };

    /**
     * 处理表单key
     *
     * @param formKey
     * @returns {*}
     */
    Activiti.getFormKeyByStr = function (formKey) {
        if (formKey) {
            if (/^\//.test(formKey)) {
                formKey = formKey.substring(1);
            }
        }
        return formKey;
    };

})(window);