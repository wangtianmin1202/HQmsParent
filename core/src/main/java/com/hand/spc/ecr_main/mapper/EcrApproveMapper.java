package com.hand.spc.ecr_main.mapper;

import com.hand.spc.ecr_main.view.EcrApproveV1;
import com.hand.spc.ecr_main.view.EcrApproveV2;
import com.hand.spc.ecr_main.view.EcrApproveV3;

import java.util.List;

/**
 * description
 *
 * @author KOCDZX0 2020/03/09 4:06 PM
 */
public interface EcrApproveMapper  {

    List<EcrApproveV1> approve1Query(EcrApproveV1 vo);

    List<EcrApproveV2> approve2Query(EcrApproveV2 vo);

    List<EcrApproveV3> approve3Query(EcrApproveV3 vo);
}
