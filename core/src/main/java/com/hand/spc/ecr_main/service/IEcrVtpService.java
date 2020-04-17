package com.hand.spc.ecr_main.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.ecr_main.dto.EcrVtp;
import com.hand.spc.ecr_main.view.EcrVtpVO;

import java.util.List;

/**
 * description
 *
 * @author KOCDZX0 2020/03/09 11:43 AM
 */
public interface IEcrVtpService extends IBaseService<EcrVtp>, ProxySelf<IEcrVtpService> {

     List<EcrVtpVO> listQuery(EcrVtpVO vo);
}
