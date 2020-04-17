package com.hand.spc.ecr_main.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.ecr_main.dto.EcrPci;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 * description ECR-PCI接口
 *
 * @author KOCDZX0 2020/03/11 5:48 PM
 */
public interface IEcrPciService extends IBaseService<EcrPci>, ProxySelf<IEcrPciService> {


    /**
     * ECR-PCI主数据查询
     * @param requestCtx 请求上下文
     * @param dto  PCI实体类
     * @return PCI集合
     */
    List<EcrPci> query(IRequest requestCtx, EcrPci dto);

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
    int updateAndDelFile(IRequest requestContext, List<EcrPci> dto);
}
