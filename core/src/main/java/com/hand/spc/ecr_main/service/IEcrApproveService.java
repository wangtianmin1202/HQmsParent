package com.hand.spc.ecr_main.service;

import com.hand.hap.core.IRequest;
import com.hand.spc.ecr_main.view.EcrApproveV1;
import com.hand.spc.ecr_main.view.EcrApproveV2;
import com.hand.spc.ecr_main.view.EcrApproveV3;

import java.util.List;

/**
 * description ECR批准生效接口
 *
 * @author KOCDZX0 2020/03/09 4:30 PM
 */
public interface IEcrApproveService  {

    /**
     * ECR主数据查询
     * @param vo
     * @return
     */
    List<EcrApproveV1> approve1Query(IRequest requestCtx,EcrApproveV1 vo);

    /**
     * 物料主数据查询
     * @param vo
     * @return
     */
    List<EcrApproveV2> approve2Query(IRequest requestCtx,EcrApproveV2 vo);

    /**
     * SKU数据查询
     * @param vo
     * @return
     */
    List<EcrApproveV3> approve3Query(EcrApproveV3 vo);
}
