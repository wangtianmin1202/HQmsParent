package com.hand.spc.ecr_main.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.ecr_main.dto.EcrVtp;
import com.hand.spc.ecr_main.view.EcrVtpVO;

import java.util.List;

/**
 * description
 *
 * @author KOCDZX0 2020/03/09 11:38 AM
 */
public interface EcrVtpMapper extends Mapper<EcrVtp> {

    List<EcrVtpVO> vtpQuery(EcrVtpVO vo);
}
