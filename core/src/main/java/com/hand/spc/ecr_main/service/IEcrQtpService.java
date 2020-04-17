package com.hand.spc.ecr_main.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.ecr_main.dto.EcrQtp;
import com.hand.spc.ecr_main.view.EcrQtpV0;

import java.util.List;

/**
 * description ECR-QTP接口
 *
 * @author KOCDZX0 2020/03/12 1:51 PM
 */
public interface IEcrQtpService extends IBaseService<EcrQtp>, ProxySelf<IEcrQtpService> {

    /**
     * ECR-QTP数据查询
     * @param request 请求上下文
     * @param dto QTP
     * @return QTP集合
     */
    List<EcrQtpV0> eqQuery(IRequest request, EcrQtpV0 dto);
}
