package com.hand.spc.ecr_main.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.ecr_main.dto.EcrDetail;
import com.hand.spc.ecr_main.dto.EcrInfluencedmaterial;
import com.hand.spc.ecr_main.mapper.EcrDetailMapper;
import com.hand.spc.ecr_main.mapper.EcrInfluencedmaterialMapper;
import com.hand.spc.ecr_main.service.IEcrDetailService;
import com.hand.spc.ecr_main.service.IEcrInfluencedmaterialService;
import com.hand.spc.ecr_main.service.IEcrItemReportService;
import com.hand.spc.ecr_main.view.EcrDetailsVO;
import com.hand.spc.ecr_main.view.EcrItemReportV2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.ValidationException;


@Service
@Transactional(rollbackFor=Exception.class)
public class EcrDetailServiceImpl extends BaseServiceImpl<EcrDetail> implements IEcrDetailService {


	@Autowired
	private EcrDetailMapper detailMapper;

	@Autowired
	private EcrInfluencedmaterialMapper ecrInfluencedmaterialMapper;
	
	@Autowired
	private IEcrInfluencedmaterialService ecrInfluencedmaterialService;
	
	@Autowired
	private IEcrItemReportService ecrItemReportService;
	
    @Override
    public List<EcrDetail> inventoryDetailsQuery(IRequest requestCtx, EcrDetail dto, int page, int pageSize) {
    	
        PageHelper.startPage(page,pageSize);
      //查询之前重新匹配库存数据
        List<EcrDetail>  tmp=detailMapper.inventoryDetailsQuery(dto);
    	EcrInfluencedmaterial eim=new EcrInfluencedmaterial();
    	eim.setKid(dto.getEcrItemKid());
    	Float poQty=Float.valueOf("0");
		Float calculateOnhand=Float.valueOf("0");
		Float supplierOnhand=Float.valueOf("0");
		Float supplierWipOnhand=Float.valueOf("0");
		Float specialOnhand=Float.valueOf("0"); 
        for(EcrDetail ed:tmp) {
        	EcrItemReportV2 ecrItemReportV2 =ecrItemReportService.getEcrOnhand(ed.getSupplierId(), dto.getMaterialId().toString());               
				if(ecrItemReportV2.getCalculateOnhand()!=null) {
					ed.setCalculateOnhand(ecrItemReportV2.getCalculateOnhand());
					calculateOnhand+=ecrItemReportV2.getCalculateOnhand();
				}
				if(ecrItemReportV2.getPoQty()!=null) {
					ed.setPoQty(ecrItemReportV2.getPoQty());
					poQty+=ecrItemReportV2.getPoQty();
				}
				if(ecrItemReportV2.getSupplierOnhand()!=null) {
					ed.setSupplierOnhand(ecrItemReportV2.getSupplierOnhand());
					supplierOnhand+=ecrItemReportV2.getSupplierOnhand();
				}
				if(ecrItemReportV2.getSpecialOnhand()!=null) {
					ed.setSpecialOnhand(ecrItemReportV2.getSpecialOnhand());
					specialOnhand+=ecrItemReportV2.getCalculateOnhand();
				}
				if(ecrItemReportV2.getSupplierWipOnhand()!=null) {
					ed.setSpecialWipOnhand(ecrItemReportV2.getSupplierWipOnhand());
					supplierWipOnhand+=ecrItemReportV2.getSupplierWipOnhand();
				}				 
				//更新数据
				self().updateByPrimaryKey(requestCtx, ed);			
        }
      //将求和的数据回写到物料属性和对应的报表数据上
		eim.setPoQty(poQty.toString());
		eim.setCalculateOnhand(calculateOnhand.toString());
		eim.setSpecialOnhand(specialOnhand.toString());
		eim.setSupplierOnhand(supplierOnhand.toString());
		eim.setSpecialWipOnhand(supplierWipOnhand);
		ecrInfluencedmaterialService.updateByPrimaryKey(requestCtx, eim);        
        return detailMapper.inventoryDetailsQuery(dto);
    }

    @Override
	public ResponseData update(IRequest requestCtx, EcrDetail dto) throws ValidationException {
		ResponseData responseData = new ResponseData();
		if (ObjectUtils.isEmpty(dto.getKid())) {
		    dto.setEcrItemKid(dto.getHeiKid());
			self().insertSelective(requestCtx,dto);
		}else {
			   dto.setEcrItemKid(dto.getHeiKid());
			self().updateByPrimaryKeySelective(requestCtx, dto);	
		}
		
		EcrDetail ed= detailMapper.getSumQtys(dto);
		EcrInfluencedmaterial eim=new EcrInfluencedmaterial();
		eim.setKid(dto.getEcrItemKid());
		eim=ecrInfluencedmaterialMapper.selectByPrimaryKey(eim);
		if(ed.getPoQty()!=null) {
			eim.setPoQty(ed.getPoQty().toString());
		}
		if(ed.getSupplierOnhand()!=null) {
			eim.setSupplierOnhand(ed.getSupplierOnhand().toString());
		}
		if(ed.getSpecialOnhand()!=null) {
			eim.setSpecialOnhand(ed.getSpecialOnhand().toString());
		}
		if(ed.getCalculateOnhand()!=null) {
			eim.setCalculateOnhand(ed.getCalculateOnhand().toString());
		}
		ecrInfluencedmaterialService.updateByPrimaryKey(requestCtx, eim);	
		responseData.setSuccess(true);
		responseData.setMessage("保存成功");
		return responseData;
	}

    @Override
    public List<EcrDetailsVO>  stockInfoQuery(Long itemId) {
        //wms库存
        Long stockSum = detailMapper.stockQuery(itemId);
        //wms库存最后更新时间
        Date finalDate = detailMapper.finalDateQuery(itemId);
        EcrDetailsVO ecrDetailsVO = new EcrDetailsVO();
        ecrDetailsVO.setWmsOnhand(stockSum);
        ecrDetailsVO.setWmsLastUpdateDate(finalDate);
        List<EcrDetailsVO> ecrDetailList = new ArrayList<>();
        ecrDetailList.add(ecrDetailsVO);
        return ecrDetailList;
    }
}
