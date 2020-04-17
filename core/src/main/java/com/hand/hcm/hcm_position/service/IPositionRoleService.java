package com.hand.hcm.hcm_position.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hcm.hcm_position.dto.PositionRole;
import com.hand.hqm.file_type.dto.FileType;

import java.util.List;

/**
 * description 物料分类属性-工程师职位接口
 *
 * @author KOCDZX0 2020/03/11 9:26 AM
 */
public interface IPositionRoleService extends IBaseService<PositionRole>, ProxySelf<IPositionRoleService> {

    /**
     * 工程师-职位查询
     * @param dto
     * @param requestContext
     * @param page
     * @param pageSize
     * @return
     */
    List<PositionRole> query(PositionRole dto, IRequest requestContext, int page, int pageSize);

    /**
     * 工程师-职位添加
     * @param requestCtx
     * @param dto
     * @return
     */
    List<PositionRole> add(IRequest requestCtx, PositionRole dto);



}
