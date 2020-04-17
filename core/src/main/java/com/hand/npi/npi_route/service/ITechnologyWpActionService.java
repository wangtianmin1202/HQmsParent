package com.hand.npi.npi_route.service;

import java.util.List;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hcm.hcm_item_b.dto.ItemB;
import com.hand.npi.npi_route.dto.TechnologyWpAction;
import com.hand.npi.npi_technology.dto.EbomDetail;
import com.hand.npi.npi_technology.dto.EbomMain;
import com.hand.npi.npi_technology.dto.TechnologySparePartDetails;
import com.hand.npi.npi_technology.dto.TechnologySpecDetail;

public interface ITechnologyWpActionService extends IBaseService<TechnologyWpAction>, ProxySelf<ITechnologyWpActionService>{
	
	List<TechnologyWpAction> queryWpactionList(TechnologyWpAction dto);
	
	List<TechnologyWpAction> queryData(IRequest request, TechnologyWpAction condition, int pageNum, int pageSize);

	List<EbomDetail> queryItemLov(IRequest request, EbomMain dto, int pageNum, int pageSize);
	List<TechnologySparePartDetails> queryMatAttrLov(IRequest request, TechnologyWpAction dto, int pageNum, int pageSize);
	
	List<TechnologySpecDetail> checkMatAttr(String materielIds,String id);
	
	List<TechnologyWpAction> queryActionInfo(IRequest request, TechnologyWpAction condition, int pageNum, int pageSize);

}