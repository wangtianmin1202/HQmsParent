package com.hand.hqm.hqm_qc_task.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.StringUtil;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.core.impl.ServiceRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IPromptService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SoapPostUtil.Response;
import com.hand.hap.util.SoapPostUtil.ResponseSap;
import com.hand.hap.webservice.ws.idto.TaskInComeInspect;
import com.hand.hap.util.SystemApiMethod;
import com.hand.hcm.hcm_item.dto.Item;
import com.hand.hcm.hcm_item.mapper.ItemMapper;
import com.hand.hcm.hcm_item_category_assign.dto.ItemCategoryAssign;
import com.hand.hcm.hcm_item_category_assign.mapper.ItemCategoryAssignMapper;
import com.hand.hcm.hcm_plant.dto.Plant;
import com.hand.hcm.hcm_plant.mapper.PlantMapper;
import com.hand.hcs.hcs_supplier_site.dto.SupplierSite;
import com.hand.hcs.hcs_supplier_site.mapper.SupplierSiteMapper;
import com.hand.hcs.hcs_suppliers.dto.Suppliers;
import com.hand.hcs.hcs_suppliers.mapper.SuppliersMapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.hqm.hqm_iqc_inspection_h.dto.IqcInspectionH;
import com.hand.hqm.hqm_iqc_inspection_h.mapper.IqcInspectionHMapper;
import com.hand.hqm.hqm_iqc_inspection_h.service.IIqcInspectionHService;
import com.hand.hqm.hqm_iqc_inspection_template_h.dto.IqcInspectionTemplateH;
import com.hand.hqm.hqm_iqc_inspection_template_h.mapper.IqcInspectionTemplateHMapper;
import com.hand.hqm.hqm_item_control.dto.ItemControlQms;
import com.hand.hqm.hqm_item_control.mapper.ItemControlQmsMapper;
import com.hand.hqm.hqm_item_type_tests.dto.ItemTypeTests;
import com.hand.hqm.hqm_item_type_tests.mapper.ItemTypeTestsMapper;
import com.hand.hqm.hqm_qc_task.dto.IqcTask;
import com.hand.hqm.hqm_qc_task.mapper.IqcTaskMapper;
import com.hand.hqm.hqm_qc_task.service.IIqcTaskService;
import com.hand.sys.sys_user.dto.UserSys;
import com.hand.sys.sys_user.mapper.UserSysMapper;

import net.logstash.logback.encoder.org.apache.commons.lang.ObjectUtils;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class IqcTaskServiceImpl extends BaseServiceImpl<IqcTask> implements IIqcTaskService {

	@Autowired
	IqcTaskMapper mapper;
	@Autowired
	IIqcInspectionHService iIqcInspectionHService;
	@Autowired
	SuppliersMapper suppliersMapper;
	@Autowired
	PlantMapper plantMapper;
	@Autowired
	UserSysMapper userSysMapper;
	@Autowired
	IPromptService iPromptService;
	@Autowired
	ItemMapper itemMapper;
	@Autowired
	SupplierSiteMapper supplierSiteMapper;
	@Autowired
	ItemControlQmsMapper itemControlQmsMapper;
	@Autowired
	ItemTypeTestsMapper itemTypeTestsMapper;
	@Autowired
	IqcInspectionHMapper iqcInspectionHMapper;
	@Autowired
	ItemCategoryAssignMapper itemCategoryAssignMapper;
	@Autowired
	IqcInspectionTemplateHMapper iqcInspectionTemplateHMapper;

	private Logger logger = LoggerFactory.getLogger(IqcTaskServiceImpl.class);

	@Override
	public Response transferIqcTask() {
		Response re = new Response();
		IqcTask te = new IqcTask();
//		((IqcTaskServiceImpl) AopContext.currentProxy()).generatorByInterface(te);
		try {
			generatorByInterface(te);
		} catch (Exception e) {
			re.setResult(false);
			re.setMessage(e.getMessage());
		}
		return re;
	}

	/**
	 * 
	 * @description 从wms检验任务传输接口的数据生成一个IQC检验单
	 * @author tianmin.wang
	 * @date 2019年11月18日
	 * @param transferList
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void generatorByInterface(IqcTask iqctask) {
		IqcInspectionH iqcinspectionh = new IqcInspectionH();
		iqcinspectionh.setSupplierId(iqctask.getSupplierId());
		iqcinspectionh.setPlantId(iqctask.getPlantId());
		iqcinspectionh.setPlantCode(getPlantById(iqcinspectionh.getPlantId()).getPlantCode());
		iqcinspectionh.setItemId(iqctask.getItemId());
		iqcinspectionh.setProductionBatch(iqctask.getProductionBatch());
		iqcinspectionh.setReceiveQty(iqctask.getReceiveQty());
		iqcinspectionh.setInspectionType("IQC");
		iqcinspectionh.setIsUrgency(iqctask.getIsUrgency());
		iqcinspectionh.setSourceOrder(iqctask.getSourceOrder());
		iqcinspectionh.setReceiveDate(iqctask.getReceiveDate());
		iqcinspectionh.setSourceType(iqctask.getSourceType());
		if (iqctask.getRecipientNum() != null && !"".equals(iqctask.getRecipientNum())) {
			iqcinspectionh.setRecipientId(String.valueOf(Float.valueOf(iqctask.getRecipientNum()).intValue()));
		}

		// sourceType 检验性质/来源类型
//		iqcinspectionh.setSourceType(
//				getSourceType(iqcinspectionh.getItemId(), iqcinspectionh.getPlantId(), iqcinspectionh.getSupplierId()));

		ResponseData rd = iIqcInspectionHService.addNewInspection(iqcinspectionh, null, null);
		iqctask.setInspectionNum(((IqcInspectionH) rd.getRows().get(0)).getInspectionNum());
	}

	public String getSourceType(Float itemId, Float plantId, Float supplierId) {
		IqcTask its = new IqcTask();
		its.setItemId(itemId);
		its.setPlantId(plantId);
		its.setSupplierId(supplierId);
		List<IqcTask> res = mapper.select(its);
		if (res != null && res.size() > 0) {
			return "0";// 正常检验

		} else {
			return "4";// VTP实验
		}
	}

	public Float getUserIdByName(String name) {
		UserSys us = new UserSys();
		us.setUserName(name);
		return userSysMapper.select(us).get(0).getUserId();
	}

	public Suppliers getSupplierById(Float kid) {
		Suppliers sus = new Suppliers();
		sus.setSupplierId(kid);
		return suppliersMapper.select(sus).get(0);
	}

	public Plant getPlantById(Float kid) {
		Plant sus = new Plant();
		sus.setPlantId(kid);
		return plantMapper.select(sus).get(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hand.hqm.hqm_iqc_task.service.IIqcTaskService#reSelect(com.hand.hap.core.
	 * IRequest, com.hand.hqm.hqm_iqc_task.dto.IqcTask, int, int)
	 */
	@Override
	public List<IqcTask> reSelect(IRequest requestContext, IqcTask dto, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return mapper.reSelect(dto);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hand.hqm.hqm_qc_task.service.IIqcTaskService#createIqc(java.util.List)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void createIqc(List<IqcTask> dto, HttpServletRequest request) {
		IRequest requestCtx;
		if (request == null) {
			ServiceRequest sr = new ServiceRequest();
			sr.setLocale("zh_CN");
			sr.setUserId(-1l);
			requestCtx = sr;
		} else {
			requestCtx = RequestHelper.createServiceRequest(request);
		}
		// TODO 生成报告
		ResponseData res = new ResponseData();
		List<IqcInspectionH> lis = new ArrayList();
		for (IqcTask iqcTask : dto) {
			IqcTask iqcTaskRes = mapper.selectByPrimaryKey(iqcTask);
			if (StringUtil.isNotEmpty(iqcTaskRes.getInspectionNum())) {
				throw new RuntimeException(SystemApiMethod.getPromptDescription(requestCtx, iPromptService,
						"error.hqm_iqc_order_create_20"));// 该检验通知已生成检验单
			}
			generatorByButton(iqcTaskRes, request);
			iqcTask.setInspectionNum(iqcTaskRes.getInspectionNum());
			IqcInspectionH search = new IqcInspectionH();
			search.setInspectionNum(iqcTaskRes.getInspectionNum());
			lis.addAll(iqcInspectionHMapper.select(search));
		}
		// 发送给 WMS的接口
		res.setRows(lis);
		iIqcInspectionHService.samplingToWms(res);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void createIqc(IqcTask dto, HttpServletRequest request) {
		IRequest requestCtx;
		if (request == null) {
			ServiceRequest sr = new ServiceRequest();
			sr.setLocale("zh_CN");
			sr.setUserId(-1l);
			requestCtx = sr;
		} else {
			requestCtx = RequestHelper.createServiceRequest(request);
		}
		// TODO 生成报告
		IqcTask iqcTaskRes = dto;
		generatorByButtonNoUpdate(iqcTaskRes, request);
		dto.setInspectionNum(iqcTaskRes.getInspectionNum());

		// 调用WMS接口
		ResponseData res = new ResponseData();
		IqcInspectionH search = new IqcInspectionH();
		search.setInspectionNum(iqcTaskRes.getInspectionNum());
		res.setRows(iqcInspectionHMapper.select(search));
		iIqcInspectionHService.samplingToWms(res);
	}

	/**
	 * mapper.updateByPrimaryKeySelective(iqctask);这个操作会导致 rollback异常
	 * 
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月20日
	 * @param iqctask
	 * @param request
	 */
	public void generatorByButtonNoUpdate(IqcTask iqctask, HttpServletRequest request) {
		IqcInspectionH iqcinspectionh = new IqcInspectionH();
		iqcinspectionh.setSupplierId(iqctask.getSupplierId());
		iqcinspectionh.setPlantId(iqctask.getPlantId());
		iqcinspectionh.setPlantCode(getPlantById(iqcinspectionh.getPlantId()).getPlantCode());
		iqcinspectionh.setItemId(iqctask.getItemId());
		iqcinspectionh.setProductionBatch(iqctask.getProductionBatch());
		iqcinspectionh.setReceiveQty(iqctask.getReceiveQty());
		iqcinspectionh.setInspectionType("IQC");
		iqcinspectionh.setIsUrgency(iqctask.getIsUrgency());
		iqcinspectionh.setSourceOrder(iqctask.getSourceOrder());
		iqcinspectionh.setReceiveDate(iqctask.getReceiveDate());
		iqcinspectionh.setTaskFrom(iqctask.getTaskFrom());
		iqcinspectionh.setItemEdition(iqctask.getItemVersion());
		if (iqctask.getRecipientNum() != null && !"".equals(iqctask.getRecipientNum())) {
			iqcinspectionh.setRecipientId(String.valueOf(Float.valueOf(iqctask.getRecipientNum()).intValue()));
		}

		// sourceType 检验性质/来源类型
		iqcinspectionh.setSourceType(
				getSourceType(iqcinspectionh.getItemId(), iqcinspectionh.getPlantId(), iqcinspectionh.getSupplierId()));
		iIqcInspectionHService.addNewInspection(iqctask, iqcinspectionh, request);
		iqctask.setInspectionNum(iqcinspectionh.getInspectionNum());
		// mapper.updateByPrimaryKeySelective(iqctask);
	}

	public void generatorByButton(IqcTask iqctask, HttpServletRequest request) {
		IqcInspectionH iqcinspectionh = new IqcInspectionH();
		iqcinspectionh.setSupplierId(iqctask.getSupplierId());
		iqcinspectionh.setPlantId(iqctask.getPlantId());
		iqcinspectionh.setPlantCode(getPlantById(iqcinspectionh.getPlantId()).getPlantCode());
		iqcinspectionh.setItemId(iqctask.getItemId());
		iqcinspectionh.setProductionBatch(iqctask.getProductionBatch());
		iqcinspectionh.setReceiveQty(iqctask.getReceiveQty());
		iqcinspectionh.setInspectionType("IQC");
		iqcinspectionh.setIsUrgency(iqctask.getIsUrgency());
		iqcinspectionh.setSourceOrder(iqctask.getSourceOrder());
		iqcinspectionh.setReceiveDate(iqctask.getReceiveDate());
		iqcinspectionh.setTaskFrom(iqctask.getTaskFrom());
		iqcinspectionh.setItemEdition(iqctask.getItemVersion());
		if (iqctask.getRecipientNum() != null && !"".equals(iqctask.getRecipientNum())) {
			iqcinspectionh.setRecipientId(String.valueOf(Float.valueOf(iqctask.getRecipientNum()).intValue()));
		}

		// sourceType 检验性质/来源类型
		iqcinspectionh.setSourceType(
				getSourceType(iqcinspectionh.getItemId(), iqcinspectionh.getPlantId(), iqcinspectionh.getSupplierId()));
		iIqcInspectionHService.addNewInspection(iqctask, iqcinspectionh, request);
		iqctask.setInspectionNum(iqcinspectionh.getInspectionNum());
		mapper.updateByPrimaryKeySelective(iqctask);
	}

	@Override
	public ResponseSap inspectWmsIncome(TaskInComeInspect dto, List<IqcTask> iqcTaskDto) throws Exception {
		ResponseSap response = new ResponseSap();
		IqcTask iqcTask = new IqcTask();

		// 查询工厂id
		Plant plant = new Plant();
		plant.setPlantCode(dto.getPlantCode());
		List<Plant> plantList = plantMapper.select(plant);
		if (plantList != null && plantList.size() > 0) {
			iqcTask.setPlantId(plantList.get(0).getPlantId());
		} else {
			response.setResult(false);
			response.setError_info("工厂编码在工厂表中找不到！");
			return response;
		}

		iqcTask.setSourceOrder(dto.getSourceOrder());
		iqcTask.setLineNum(dto.getLineNumber());

		// 查询物料id
		Item item = new Item();
		item.setItemCode(dto.getItemCode());
		item.setPlantCode(dto.getPlantCode());
		List<Item> itedtoist = itemMapper.select(item);
		if (itedtoist != null && itedtoist.size() > 0) {
			iqcTask.setItemId(itedtoist.get(0).getItemId());
		} else {
			response.setResult(false);
			response.setError_info("物料id在物料表中找不到！");
			return response;
		}

		iqcTask.setItemVersion(dto.getItemVersion());
		if (dto.getPoNumber() != null && !"".equals(dto.getPoNumber())) {
			iqcTask.setPoNumber(Float.parseFloat(dto.getPoNumber()));
		}

		// 判断该送货单行物料是否已报验
		List<IqcTask> iqcTaskList = mapper.select(iqcTask);
		if (iqcTaskList != null && iqcTaskList.size() > 0) {
			response.setResult(false);
			response.setError_info("该送货单行物料已报验！");
			return response;
		}

		// 判断是否保存ecr_number字段
		IqcInspectionTemplateH iqcInspectionTemplateH = new IqcInspectionTemplateH();
		iqcInspectionTemplateH.setPlantId(iqcTask.getPlantId());
		iqcInspectionTemplateH.setItemId(iqcTask.getItemId());
		iqcInspectionTemplateH.setSourceType("0");
		iqcInspectionTemplateH.setItemEdition(dto.getItemVersion());
		iqcInspectionTemplateH.setStatus("3");
		List<IqcInspectionTemplateH> iqcInspectionTemplateHList = iqcInspectionTemplateHMapper
				.select(iqcInspectionTemplateH);
		if (iqcInspectionTemplateHList != null && iqcInspectionTemplateHList.size() > 0) {
			for (IqcInspectionTemplateH iith : iqcInspectionTemplateHList) {
				iith.setStatus("4");
				iqcInspectionTemplateHMapper.updateByPrimaryKeySelective(iith);
			}
			iqcTask.setEcrNumber(iqcInspectionTemplateHList.get(0).getEcrNumber());
		}

		iqcTask.setSpreading(dto.getSpreading());
		iqcTask.setProductionBatch(dto.getLotNumber());

		// 查询供应商id
		Suppliers suppliers = new Suppliers();
		suppliers.setSupplierCode(dto.getSupplierCode());
		List<Suppliers> supplierList = suppliersMapper.select(suppliers);
		if (supplierList != null && supplierList.size() > 0) {
			iqcTask.setSupplierId(supplierList.get(0).getSupplierId());
		} else {
			response.setResult(false);
			response.setError_info("供应商id在供应商表中找不到！");
			return response;
		}

		iqcTask.setInspectionType("IQC");
		iqcTask.setReceiveQty(dto.getReceiveQty());

		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");// 日期格式
		Date date = format.parse(dto.getEventTime());
		iqcTask.setReceiveDate(date);

		iqcTask.setPackQty(dto.getPackQty());
		iqcTask.setPacketInfo(dto.getPacketInfo());

		// 查询物料检验地点
		ItemControlQms itemControlQms = new ItemControlQms();
		itemControlQms.setPlantCode(dto.getPlantCode());
		itemControlQms.setItemCode(dto.getItemCode());
		List<ItemControlQms> itemControlQmsList = itemControlQmsMapper.select(itemControlQms);
		if (itemControlQmsList != null && itemControlQmsList.size() > 0) {
			iqcTask.setInspectionPlace(itemControlQmsList.get(0).getCheckPlace());
		} else {
			response.setResult(false);
			response.setError_info("物料检验地点找不到！");
			return response;
		}

		iqcTask.setSolveStatus("N");
		iqcTask.setWareHouseType(dto.getWarehouseType());

		// 接口触发后，执行是否VTP
		iqcTaskDto = interfaceTrigger(iqcTask);

		response.setResult(true);

		return response;
	}

	// 报验借口触发后-逻辑
	public List<IqcTask> interfaceTrigger(IqcTask iqcTask) throws Exception {
		List<IqcTask> iqcTaskResList = new ArrayList<IqcTask>();

		IqcTask iqcTaskT = new IqcTask();
		iqcTaskT.setItemId(iqcTask.getItemId());
		List<IqcTask> iqcTasks = mapper.select(iqcTaskT);
		if (iqcTasks != null && iqcTasks.size() > 0) {
			// 不属于首次报验
			IqcTask iqcTaskFir = iqcTask;
			iqcTaskFir.setTaskId(null);
			iqcTaskFir.setInspectionNum(null);
			iqcTaskFir.setSourceType("0");
			iqcTaskFir.setCreationDate(new Date());
			iqcTaskFir.setLastUpdateDate(new Date());
			mapper.insertSelective(iqcTaskFir);

			iqcTaskResList.add(iqcTaskFir);

			boolean isexist = false;
			ItemTypeTests itemTypeTests = new ItemTypeTests();
			itemTypeTests.setPlantId(iqcTaskFir.getPlantId());
			itemTypeTests.setItemId(iqcTaskFir.getItemId());
			List<ItemTypeTests> itemTypeTestes = itemTypeTestsMapper.select(itemTypeTests);
			float totalQty = 0;
			float triQty = 0;
			float typeChangeTime = 0;
			Date lastTime = null;
			if (itemTypeTestes != null && itemTypeTestes.size() > 0) {
				for (ItemTypeTests itt : itemTypeTestes) {
					if ("1".equals(itt.getTestType())) {
						isexist = true;
						totalQty = itt.getTotalQty();
						triQty = itt.getTriggerNum();
						typeChangeTime = itt.getTypeChangeTime();
						lastTime = itt.getLastTime();
					}
				}
			}

			if (isexist) {
				float reQty = iqcTaskFir.getReceiveQty();
				if ((reQty + totalQty) >= triQty) {
					IqcTask iqcTaskType = iqcTask;
					iqcTaskType.setTaskId(null);
					iqcTaskType.setInspectionNum(null);
					iqcTaskType.setSourceType("2");
					iqcTaskType.setCreationDate(new Date());
					iqcTaskType.setLastUpdateDate(new Date());
					mapper.insertSelective(iqcTaskType);

					iqcTaskResList.add(iqcTaskType);

					if (itemTypeTestes != null && itemTypeTestes.size() > 0) {
						for (ItemTypeTests itt : itemTypeTestes) {
							itt.setTotalQty((float) 0);
							itemTypeTestsMapper.updateByPrimaryKeySelective(itt);
						}
					}

					ItemCategoryAssign itemCategoryAssign = new ItemCategoryAssign();
					itemCategoryAssign.setItemId(iqcTask.getItemId());
					List<ItemCategoryAssign> itemCategoryAssigns = itemCategoryAssignMapper.select(itemCategoryAssign);
					if (itemCategoryAssigns != null && itemCategoryAssigns.size() > 0) {
						ItemTypeTests itemTypeTestsCategory = new ItemTypeTests();
						itemTypeTestsCategory.setCategoryId(itemCategoryAssigns.get(0).getCategoryId());
						List<ItemTypeTests> itemTypeTestesCategory = itemTypeTestsMapper.select(itemTypeTestsCategory);
						if (itemTypeTestesCategory != null && itemTypeTestesCategory.size() > 0) {
							for (ItemTypeTests itt : itemTypeTestesCategory) {
								itt.setTotalQty((float) 0);
								itemTypeTestsMapper.updateByPrimaryKeySelective(itt);
							}
						}
					}
				} else {
					Calendar calendar = Calendar.getInstance();
					if (lastTime != null) {
						calendar.setTime(lastTime);
					}

					calendar.add(Calendar.DATE, (int) typeChangeTime);
					if (lastTime != null && calendar.getTime().compareTo(new Date()) > 0) {
						if (itemTypeTestes != null && itemTypeTestes.size() > 0) {
							for (ItemTypeTests itt : itemTypeTestes) {
								itt.setTotalQty(iqcTaskFir.getReceiveQty() + itt.getTotalQty());
								itemTypeTestsMapper.updateByPrimaryKeySelective(itt);
							}
						}

						ItemCategoryAssign itemCategoryAssign = new ItemCategoryAssign();
						itemCategoryAssign.setItemId(iqcTask.getItemId());
						List<ItemCategoryAssign> itemCategoryAssigns = itemCategoryAssignMapper
								.select(itemCategoryAssign);
						if (itemCategoryAssigns != null && itemCategoryAssigns.size() > 0) {
							ItemTypeTests itemTypeTestsCategory = new ItemTypeTests();
							itemTypeTestsCategory.setCategoryId(itemCategoryAssigns.get(0).getCategoryId());
							List<ItemTypeTests> itemTypeTestesCategory = itemTypeTestsMapper
									.select(itemTypeTestsCategory);
							if (itemTypeTestesCategory != null && itemTypeTestesCategory.size() > 0) {
								for (ItemTypeTests itt : itemTypeTestesCategory) {
									itt.setTotalQty(iqcTaskFir.getReceiveQty() + itt.getTotalQty());
									itemTypeTestsMapper.updateByPrimaryKeySelective(itt);
								}
							}
						}
					} else {
						boolean isexistB = false;
						ItemCategoryAssign itemCategoryAssign = new ItemCategoryAssign();
						itemCategoryAssign.setItemId(iqcTask.getItemId());
						List<ItemCategoryAssign> itemCategoryAssigns = itemCategoryAssignMapper
								.select(itemCategoryAssign);
						if (itemCategoryAssigns != null && itemCategoryAssigns.size() > 0) {
							ItemTypeTests itemTypeTestsCategory = new ItemTypeTests();
							itemTypeTestsCategory.setCategoryId(itemCategoryAssigns.get(0).getCategoryId());
							List<ItemTypeTests> itemTypeTestesCategory = itemTypeTestsMapper
									.select(itemTypeTestsCategory);
							if (itemTypeTestesCategory != null && itemTypeTestesCategory.size() > 0) {
								for (ItemTypeTests itt : itemTypeTestesCategory) {
									if ("2".equals(itt.getTestType())) {
										isexistB = true;
									}
								}
							}
						}

						if (!isexistB) {
							if (itemTypeTestes != null && itemTypeTestes.size() > 0) {
								for (ItemTypeTests itt : itemTypeTestes) {
									itt.setTotalQty(iqcTaskFir.getReceiveQty() + itt.getTotalQty());
									itemTypeTestsMapper.updateByPrimaryKeySelective(itt);
								}
							}
						} else {
							if (itemTypeTestes != null && itemTypeTestes.size() > 0) {
								for (ItemTypeTests itt : itemTypeTestes) {
									if ((iqcTaskFir.getReceiveQty() + itt.getTotalQty()) > itt.getTriggerNum()) {
										IqcTask iqcTaskType = iqcTask;
										iqcTaskType.setTaskId(null);
										iqcTaskType.setInspectionNum(null);
										iqcTaskType.setSourceType("2");
										iqcTaskType.setCreationDate(new Date());
										iqcTaskType.setLastUpdateDate(new Date());
										mapper.insertSelective(iqcTaskType);

										iqcTaskResList.add(iqcTaskType);

										itt.setTotalQty((float) 0);
										itemTypeTestsMapper.updateByPrimaryKeySelective(itt);
										if (itemCategoryAssigns != null && itemCategoryAssigns.size() > 0) {
											ItemTypeTests itemTypeTestsCategory = new ItemTypeTests();
											itemTypeTestsCategory
													.setCategoryId(itemCategoryAssigns.get(0).getCategoryId());
											List<ItemTypeTests> itemTypeTestesCategory = itemTypeTestsMapper
													.select(itemTypeTestsCategory);
											if (itemTypeTestesCategory != null && itemTypeTestesCategory.size() > 0) {
												for (ItemTypeTests ittB : itemTypeTestesCategory) {
													ittB.setTotalQty((float) 0);
													itemTypeTestsMapper.updateByPrimaryKeySelective(ittB);
												}
											}
										}
									} else {
										itt.setTotalQty(iqcTaskFir.getReceiveQty() + itt.getTotalQty());
										itemTypeTestsMapper.updateByPrimaryKeySelective(itt);

										if (itemCategoryAssigns != null && itemCategoryAssigns.size() > 0) {
											ItemTypeTests itemTypeTestsCategory = new ItemTypeTests();
											itemTypeTestsCategory
													.setCategoryId(itemCategoryAssigns.get(0).getCategoryId());
											List<ItemTypeTests> itemTypeTestesCategory = itemTypeTestsMapper
													.select(itemTypeTestsCategory);
											if (itemTypeTestesCategory != null && itemTypeTestesCategory.size() > 0) {
												for (ItemTypeTests ittB : itemTypeTestesCategory) {
													ittB.setTotalQty(iqcTaskFir.getReceiveQty() + ittB.getTotalQty());
													itemTypeTestsMapper.updateByPrimaryKeySelective(ittB);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			} else {
				ItemCategoryAssign itemCategoryAssign = new ItemCategoryAssign();
				itemCategoryAssign.setItemId(iqcTask.getItemId());
				List<ItemCategoryAssign> itemCategoryAssigns = itemCategoryAssignMapper.select(itemCategoryAssign);
				if (itemCategoryAssigns != null && itemCategoryAssigns.size() > 0) {
					ItemTypeTests itemTypeTestsCategory = new ItemTypeTests();
					itemTypeTestsCategory.setCategoryId(itemCategoryAssigns.get(0).getCategoryId());
					List<ItemTypeTests> itemTypeTestesCategory = itemTypeTestsMapper.select(itemTypeTestsCategory);
					if (itemTypeTestesCategory != null && itemTypeTestesCategory.size() > 0) {
						for (ItemTypeTests itt : itemTypeTestesCategory) {
							if ("2".equals(itt.getTestType())) {
								if ((iqcTaskFir.getReceiveQty() + itt.getTotalQty()) > itt.getTriggerNum()) {
									IqcTask iqcTaskType = iqcTask;
									iqcTaskType.setTaskId(null);
									iqcTaskType.setInspectionNum(null);
									iqcTaskType.setSourceType("2");
									iqcTaskType.setCreationDate(new Date());
									iqcTaskType.setLastUpdateDate(new Date());
									mapper.insertSelective(iqcTaskType);

									iqcTaskResList.add(iqcTaskType);

									itt.setTotalQty((float) 0);
									itemTypeTestsMapper.updateByPrimaryKeySelective(itt);
								} else {
									itt.setTotalQty(itt.getTotalQty() + iqcTaskFir.getReceiveQty());
									itemTypeTestsMapper.updateByPrimaryKeySelective(itt);
								}

							}
						}
					}
				}
			}
		} else {
			// 属于首次报验
			IqcTask iqcTaskVtp = iqcTask;
			iqcTaskVtp.setTaskId(null);
			iqcTaskVtp.setInspectionNum(null);
			iqcTaskVtp.setSourceType("4");
			iqcTaskVtp.setCreationDate(new Date());
			mapper.insertSelective(iqcTaskVtp);

			iqcTaskResList.add(iqcTaskVtp);

			IqcTask iqcTaskTyp = iqcTask;
			iqcTaskTyp.setTaskId(null);
			iqcTaskTyp.setInspectionNum(null);
			iqcTaskTyp.setSourceType("2");
			iqcTaskTyp.setCreationDate(new Date());
			mapper.insertSelective(iqcTaskTyp);

			iqcTaskResList.add(iqcTaskTyp);
		}

		return iqcTaskResList;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void createIqcInterface(List<IqcTask> dto, HttpServletRequest request) {
		IRequest requestCtx;
		if (request == null) {
			ServiceRequest sr = new ServiceRequest();
			sr.setLocale("zh_CN");
			sr.setUserId(-1l);
			requestCtx = sr;
		} else {
			requestCtx = RequestHelper.createServiceRequest(request);
		}
		// TODO 生成报告
		for (IqcTask iqcTask : dto) {
			IqcTask iqcTaskRes = mapper.selectByPrimaryKey(iqcTask);
			if (StringUtil.isNotEmpty(iqcTaskRes.getInspectionNum())) {
				throw new RuntimeException(SystemApiMethod.getPromptDescription(requestCtx, iPromptService,
						"error.hqm_iqc_order_create_20"));// 该检验通知已生成检验单
			}
			generatorByButton(iqcTaskRes, request);
			iqcTask.setInspectionNum(iqcTaskRes.getInspectionNum());

			growNum(iqcTaskRes);
		}
	}

	// 获取抽样数，生成抽样箱数
	public void growNum(IqcTask iqcTask) {
		Random random = new Random();

		IqcInspectionH iqcInspectionH = new IqcInspectionH();
		iqcInspectionH.setInspectionNum(iqcTask.getInspectionNum());
		List<IqcInspectionH> iqcInspectionHList = iqcInspectionHMapper.select(iqcInspectionH);
		if (iqcInspectionHList != null && iqcInspectionHList.size() > 0) {
			float length = iqcInspectionHList.get(0).getSampleSize();
			float receQty = iqcTask.getReceiveQty();
			float packQty = iqcTask.getPackQty();
			int[] anArray = new int[(int) length];
			for (int i = 0; i < (int) length; i++) {// 生成数组
				int rand = random.nextInt((int) receQty) + 1;
				int num = (int) Math.ceil(rand / packQty);
				anArray[i] = num;
			}
			// 去重
			HashSet<Integer> hashSet = new HashSet<Integer>();
			for (int i = 0; i < anArray.length; i++) {
				hashSet.add(anArray[i]);
			}
			int samplePackQty = hashSet.size();

			iqcTask.setSamplePackQty((float) samplePackQty);
			mapper.updateByPrimaryKeySelective(iqcTask);
		}
	}
}