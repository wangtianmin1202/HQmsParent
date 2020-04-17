package com.hand.hcs.hcs_produce.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hcs.hcs_produce.dto.ItemStationProduce;
import com.hand.wfl.util.ActException;

import java.util.List;
import javax.xml.bind.ValidationException;

public interface IItemStationProduceService extends IBaseService<ItemStationProduce>, ProxySelf<IItemStationProduceService>{

    List<ItemStationProduce> listQuery(IRequest request, ItemStationProduce dto);

    ResponseData insertMutiData(IRequest request,List<ItemStationProduce> dtos);

    ResponseData add(IRequest request, ItemStationProduce dto);

    /**
     * 创建制程监控工作流
     * @param request
     * @param dto
     * @return
     * @throws ActException
     * @throws ValidationException
     */
    List<ItemStationProduce> approve(IRequest request, ItemStationProduce dto) throws ActException, ValidationException;

}