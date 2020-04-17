package com.hand.spc.ecr_main.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.ecr_main.dto.EcrDetail;
import com.hand.spc.ecr_main.view.EcrDetailsVO;

import javax.xml.bind.ValidationException;

public interface IEcrDetailService extends IBaseService<EcrDetail>,ProxySelf<IEcrDetailService>{


    /**
     * 根据ECR编号和物料id查询库存明细
     * @param dto
     * @return
     */
    List<EcrDetail> inventoryDetailsQuery(IRequest requestCtx,EcrDetail dto,int page,int pageSize);

    /**
     * 库存明细更新
     * @param requestCtx
     * @param dto
     * @return
     */
	ResponseData update(IRequest requestCtx, EcrDetail dto) throws ValidationException;

	List<EcrDetailsVO>  stockInfoQuery(Long itemId);

}
