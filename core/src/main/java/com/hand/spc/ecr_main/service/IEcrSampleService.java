package com.hand.spc.ecr_main.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.ecr_main.dto.EcrSample;
import com.hand.spc.ecr_main.view.EcrSampleV0;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

public interface IEcrSampleService extends IBaseService<EcrSample>, ProxySelf<IEcrSampleService>{
	public List<EcrSampleV0> baseQuery(EcrSample dto , int page, int pageSize);

    /**
     * 上传文件
     * @param requestCtx 请求上下文
     * @param request    请求
     * @return
     * @throws IllegalStateException
     * @throws IOException
     */
    ResponseData fileUpload(IRequest requestCtx, HttpServletRequest request) throws IllegalStateException, IOException;

    /**
     * 删除文件
     * @param requestContext 请求上下文
     * @param dto
     * @return
     */
    int updateAndDelFile(IRequest requestContext, List<EcrSample> dto);
}