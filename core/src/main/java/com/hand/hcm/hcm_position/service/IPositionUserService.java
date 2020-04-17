package com.hand.hcm.hcm_position.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hcm.hcm_position.dto.PositionUser;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * description 物料分类属性-工程师角色接口
 *
 * @author KOCDZX0 2020/03/11 9:58 AM
 */
@Service
public interface IPositionUserService extends IBaseService<PositionUser>, ProxySelf<IPositionUserService> {

    /**
     * 工程师-角色查询
     * @param request
     * @param dto
     * @param page
     * @param pageSize
     * @return
     */
    List<PositionUser> query(IRequest request,PositionUser dto,int page,int pageSize);

    /**
     * 工程师-角色增加
     * @param requestCtx
     * @param dto
     * @return
     */
    List<PositionUser> add(IRequest requestCtx, PositionUser dto);
}
