package com.hand.hcm.hcm_position.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hcm.hcm_position.dto.PositionUser;

import java.util.List;

/**
 * description
 *
 * @author KOCDZX0 2020/03/11 9:55 AM
 */
public interface PositionUserMapper extends Mapper<PositionUser> {

    List<PositionUser> puQuery(PositionUser dto);
}
