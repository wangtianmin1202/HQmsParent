package com.hand.spc.ecr_main.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.ecr_main.dto.EcrPci;

import java.util.List;

/**
 * description
 *
 * @author KOCDZX0 2020/03/11 5:48 PM
 */
public interface EcrPciMapper extends Mapper<EcrPci> {

    List<EcrPci> pciQuery(EcrPci dto);
}
