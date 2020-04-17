package com.hand.hcs.hcs_station.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hcs.hcs_station.dto.SupplierItemStation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ISupplierItemStationService extends IBaseService<SupplierItemStation>, ProxySelf<ISupplierItemStationService>{

    /**
     * 供应商制程要求查询
     * @param request
     * @param dto
     * @return
     */
    List<SupplierItemStation> query(IRequest request, SupplierItemStation dto);

    /**
     * 新增要求与工位
     * @param request
     * @param dto
     * @return
     */
    ResponseData inserMuli(IRequest request , List<SupplierItemStation> dto);

    /**
     * 新增工位
     * @param request
     * @param dto
     * @return
     */
    ResponseData add(IRequest request , SupplierItemStation dto);

    /**
     * 生效/失效
     * @param request
     * @param id 表id
     * @param flag 生效标识 Y-生效  N-失效
     * @return
     */
    ResponseData enable(IRequest request,Float id,String flag);
}