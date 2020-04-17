package com.hand.hcs.hcs_produce.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hcs.hcs_produce.dto.ItemStationProduceLine;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

public interface IItemStationProduceLineService extends IBaseService<ItemStationProduceLine>, ProxySelf<IItemStationProduceLineService>{

    List<ItemStationProduceLine> listQuery(IRequest request, ItemStationProduceLine dto);

    ResponseData add(IRequest request, ItemStationProduceLine dto);

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
    int updateAndDelFile(IRequest requestContext, List<ItemStationProduceLine> dto);


}