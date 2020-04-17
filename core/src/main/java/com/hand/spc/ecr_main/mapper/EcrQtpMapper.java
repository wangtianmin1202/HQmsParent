package com.hand.spc.ecr_main.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.ecr_main.dto.EcrQtp;
import com.hand.spc.ecr_main.view.EcrQtpV0;

import java.util.List;

/**
 * description
 *
 * @author KOCDZX0 2020/03/12 1:50 PM
 */
public interface EcrQtpMapper extends Mapper<EcrQtp> {

    List<EcrQtpV0> eqQuery(EcrQtpV0 dto);
}
