package com.hand.hcm.hcm_position.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hcm.hcm_position.dto.PositionRole;

import java.util.List;

/**
 * description
 *
 * @author KOCDZX0 2020/03/11 9:13 AM
 */
public interface PositionRoleMapper extends Mapper<PositionRole> {

    List<PositionRole> prQuery(PositionRole dto);

}
