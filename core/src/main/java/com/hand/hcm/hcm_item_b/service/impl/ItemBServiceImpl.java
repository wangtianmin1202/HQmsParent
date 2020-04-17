package com.hand.hcm.hcm_item_b.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.mail.dto.Message;
import com.hand.hap.mail.service.IEmailService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SoapPostUtil.Response;
import com.hand.hap.util.SoapPostUtil.ResponseSap;
import com.hand.hap.util.SpringContextUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.hand.hcm.hcm_item_b.dto.ConvertUnit;
import com.hand.hcm.hcm_item_b.dto.ItemB;
import com.hand.hcm.hcm_item_b.dto.ItemControl;
import com.hand.hcm.hcm_item_b.dto.ItemTl;
import com.hand.hcm.hcm_item_b.mapper.ConvertUnitMapper;
import com.hand.hcm.hcm_item_b.mapper.ItemBMapper;
import com.hand.hcm.hcm_item_b.mapper.ItemControlMapper;
import com.hand.hcm.hcm_item_b.mapper.ItemTlMapper;
import com.hand.hcm.hcm_item_b.service.IConvertUnitService;
import com.hand.hcm.hcm_item_b.service.IItemBService;
import com.hand.hcm.hcm_plant.dto.Plant;
import com.hand.hcm.hcm_plant.mapper.PlantMapper;
import com.hand.itf.itf_material.dto.Material;
import com.hand.itf.itf_material.mapper.MaterialMapper;
import com.hand.itf.itf_supplier_materials.mapper.SupplierMaterialsMapper;

import antlr.StringUtils;
import jodd.util.StringUtil;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ContextLoader;

@Service
@Transactional(rollbackFor = Exception.class)
public class ItemBServiceImpl extends BaseServiceImpl<ItemB> implements IItemBService {

	@Autowired
	ItemBMapper mapper;
	@Autowired
	PlantMapper plantMapper;
	@Autowired
	MaterialMapper materialMapper;
	@Autowired
	ItemTlMapper itemTlMapper;
	@Autowired
	ConvertUnitMapper convertUnitMapper;
	@Autowired
	ItemControlMapper itemControlMapper;

	@Autowired
	IEmailService iEmailService;
	
	@Override
	public List<?> teselect(IRequest requestContext, ItemB dto, int page, int pageSize) {
//		return mapper.teselect(666f);
//		Message me = new Message();
//		me.setMessageHost("mailhost.kohlerco.com");
//		try {
//			iEmailService.sendSingleEmailMessageWithFile(me, null);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		IConvertUnitService taskExecutor = (IConvertUnitService) SpringContextUtil.getBean("convertUnitServiceImpl");
		return taskExecutor.selectAll(requestContext);
	}

	@Override
	public ResponseSap transferMaterial(List<Material> mli) {
		Date now = new Date();
		ResponseSap response = new ResponseSap();
		Map<String, Float> pm = getAllPlant();//Map<工厂编码, 工厂ID> pm
		// refreshEnableFlag("N");
		//List<ItemB> allItem = mapper.selectAll();
		for (Material material : mli) {
			material.setProcessStatus("Y");
			material.setProcessTime(now);
			try {
				ItemB ibsearch = new ItemB();
				ibsearch.setItemCode(material.getMatnr());
				ibsearch.setPlantId(pm.get(material.getWerks()));
				List<ItemB> iblist = mapper.interfaceSelect(ibsearch);
				if (iblist != null && iblist.size() > 0) {// 物料存在的情况
					ItemB ibu = new ItemB();
//					allItem.removeIf((p) -> {
//						return p.getItemId().intValue() == iblist.get(0).getItemId().intValue()
//								&& p.getPlantId().intValue() == iblist.get(0).getPlantId().intValue();
//					});
					ibu.setEnableFlag("Y");
					ibu.setLastUpdateDate(now);
					ibu.setLastUpdatedBy(-1l);
					ibu.setPrimaryUom(material.getMeins());
					ibu.setItemType(material.getMtart());
					ibu.setMakeBuyCode(material.getBeskz());
					if (!StringUtil.isEmpty(material.getPlifz())) {
						ibu.setPurchaseLeadTime(Float.valueOf(material.getPlifz()));
					}
					if (!StringUtil.isEmpty(material.getBstrf())) {
						ibu.setPackQty(Float.valueOf(material.getBstrf()));
					}
					if (!StringUtil.isEmpty(material.getStprs())) {
						ibu.setUnitPrice(Float.valueOf(material.getStprs()));
					}
					ibu.setPriceUnit(material.getPeinh());
					ibu.setItemId(iblist.get(0).getItemId());
					ibu.setPlantId(iblist.get(0).getPlantId());

					mapper.updateByItemPlantId(ibu);
					ItemTl itu = new ItemTl();
					itu.setItemId(iblist.get(0).getItemId());
					itu.setPlantId(iblist.get(0).getPlantId());
					itu.setDescriptions(material.getMaktx());
					itemTlMapper.updateByItemPlantId(itu);

					refreshConvertUnit(iblist.get(0).getItemId(), iblist.get(0).getPlantId(), material.getMeinh(),
							true);

				} else {// 物料不存在的情况
					ItemB ibu = new ItemB();
					ibu.setEnableFlag("Y");
					ibu.setLastUpdateDate(now);
					ibu.setLastUpdatedBy(-1l);
					ibu.setPrimaryUom(material.getMeins());
					ibu.setItemType(material.getMtart());
					ibu.setMakeBuyCode(material.getBeskz());
					ibu.setItemCode(material.getMatnr());
					if (!StringUtil.isEmpty(material.getPlifz())) {
						ibu.setPurchaseLeadTime(Float.valueOf(material.getPlifz()));
					}
					if (!StringUtil.isEmpty(material.getBstrf())) {
						ibu.setPackQty(Float.valueOf(material.getBstrf()));
					}
					if (!StringUtil.isEmpty(material.getStprs())) {
						ibu.setUnitPrice(Float.valueOf(material.getStprs()));
					}
					ibu.setPriceUnit(material.getPeinh());
					ibu.setItemId(mapper.getSequence());// 获取item_id的新序列
					ibu.setPlantId(pm.get(material.getWerks()));

					mapper.insertSelective(ibu);

					ItemControl icu = new ItemControl();
					icu.setItemId(ibu.getItemId());
					icu.setPlantId(ibu.getPlantId());
					icu.setScheduleRegionId(21f);
					itemControlMapper.insertSelective(icu);

					ItemTl itu = new ItemTl();
					itu.setItemId(ibu.getItemId());
					itu.setPlantId(ibu.getPlantId());
					itu.setDescriptions(material.getMaktx());
					itemTlMapper.insertSelective(itu);

					refreshConvertUnit(ibu.getItemId(), ibu.getPlantId(), material.getMeinh(), false);

				}
			} catch (Exception e) {
				material.setProcessStatus("E");
				material.setMessage(e.getMessage());
			}
			materialMapper.updateByPrimaryKeySelective(material);
		}
//		try {
//			for (ItemB ib : allItem) {
//				ib.setEnableFlag("N");
//				ib.setLastUpdateDate(now);
//				ib.setLastUpdatedBy(-1l);
//				mapper.disableByItemPlantId(ib);
//			}
//		} catch (Exception e) {
//			response.setMessage(e.getMessage());
//		}
		return response;
	}

	/**
	 * 
	 * @description ConvertUnit表数据的更新
	 * @author tianmin.wang
	 * @date 2019年12月4日
	 * @param itemId 物料ID
	 * @param plantId 工厂ID
	 * @param meinh 单位数量组 String
	 * @param delete 是否删除已存在的物料工厂下的数据
	 */
	private void refreshConvertUnit(Float itemId, Float plantId, String meinh, boolean delete) {
		if(StringUtil.isEmpty(meinh)) {
			return;
		}
		ConvertUnit del = new ConvertUnit();
		del.setItemId(itemId);
		del.setPlantId(plantId);
		if (delete) {
			convertUnitMapper.deleteByItemPlantId(del);
		}
		for (String convert : meinh.split(";")) {
			del.setUomQty(Float.valueOf(convert.split("=")[0].split(" ")[0]));
			del.setUom(convert.split("=")[0].split(" ")[1]);
			del.setConvertUomQty(Float.valueOf(convert.split("=")[1].split(" ")[0]));
			del.setConvertUom(convert.split("=")[1].split(" ")[1]);
			convertUnitMapper.reInsert(del);
		}
	}

	/**
	 * 
	 * @description 更新有效性
	 * @author tianmin.wang
	 * @date 2019年12月4日 
	 * @param enableFlag
	 */
	public void refreshEnableFlag(String enableFlag) {
		ItemB ibsearch = new ItemB();
		ibsearch.setEnableFlag(enableFlag);
		mapper.refreshEnableFlag(ibsearch);
	}

	/**
	 * 获取所有工厂
	 * 
	 * @return
	 */
	public Map<String, Float> getAllPlant() {
		Map<String, Float> re = new HashMap<String, Float>();
		List<Plant> plantList = plantMapper.selectAll();
		for (Plant plant : plantList) {
			re.put(plant.getPlantCode(), plant.getPlantId());
		}
		return re;
	}

	/**
	 * 
	 * @description 通过工厂编号 获取 工厂ID
	 * @author tianmin.wang
	 * @date 2019年12月4日 
	 * @param plantCode
	 * @return
	 */
	public Float getPlantId(String plantCode) {
		Plant se = new Plant();
		se.setPlantCode(plantCode);
		return plantMapper.select(se).get(0).getPlantId();
	}

}