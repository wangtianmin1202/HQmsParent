package com.hand.npi.npi_route.service.impl;

import com.codahale.metrics.Histogram;
import com.google.common.collect.Maps;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.DocSequence;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IDocSequenceService;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.drools.lang.DRLExpressions.neg_operator_key_return;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.npi.npi_route.dto.TechnologyRoute;
import com.hand.npi.npi_route.dto.TechnologyRouteHis;
import com.hand.npi.npi_route.dto.TechnologyWorkingProcedure;
import com.hand.npi.npi_route.dto.TechnologyWorkingProcedureHis;
import com.hand.npi.npi_route.dto.TechnologyWpAction;
import com.hand.npi.npi_route.dto.TechnologyWpActionEquipDetail;
import com.hand.npi.npi_route.dto.TechnologyWpMaterielRef;
import com.hand.npi.npi_route.dto.TechnologyWpSpecDetail;
import com.hand.npi.npi_route.dto.TechnologyWpStandardActionDetail;
import com.hand.npi.npi_route.mapper.TechnologyRouteHisMapper;
import com.hand.npi.npi_route.mapper.TechnologyRouteMapper;
import com.hand.npi.npi_route.mapper.TechnologyWorkingProcedureHisMapper;
import com.hand.npi.npi_route.mapper.TechnologyWorkingProcedureMapper;
import com.hand.npi.npi_route.mapper.TechnologyWpActionEquipDetailMapper;
import com.hand.npi.npi_route.mapper.TechnologyWpActionMapper;
import com.hand.npi.npi_route.mapper.TechnologyWpMaterielRefMapper;
import com.hand.npi.npi_route.mapper.TechnologyWpSpecDetailMapper;
import com.hand.npi.npi_route.mapper.TechnologyWpStandardActionDetailMapper;
import com.hand.npi.npi_route.service.ITechnologyRouteService;
import com.hand.npi.npi_technology.dto.EbomDetail;
import com.hand.npi.npi_technology.dto.QuickTechRouteDto;
import com.hand.wfl.util.DropDownListDto;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class TechnologyRouteServiceImpl extends BaseServiceImpl<TechnologyRoute> implements ITechnologyRouteService {

	@Autowired
	IDocSequenceService iDocSequenceService;
	@Autowired
	TechnologyRouteMapper technologyRouteMapper;
	@Autowired
	TechnologyWorkingProcedureMapper workingProcedureMapper;
	@Autowired
	TechnologyWpActionMapper wpActionMapper;
	@Autowired
	TechnologyWpMaterielRefMapper wpMaterielRefMapper;
	@Autowired
	TechnologyRouteHisMapper technologyRouteHisMapper;
	@Autowired
	TechnologyWorkingProcedureHisMapper workingProcedureHisMapper;
	@Autowired
	TechnologyWpStandardActionDetailMapper tActionDetailMapper;
	@Autowired
	TechnologyWpActionEquipDetailMapper tEquipDetailMapper;
	@Autowired
	TechnologyWpSpecDetailMapper tDetailMapper;

	@Override
	public List<TechnologyRoute> addData(IRequest request, List<TechnologyRoute> list) {
		// 新增工艺路径 新增的版本都是V
		for (TechnologyRoute dto : list) {
			//新增时 该sku的生产线不能重复
			List<TechnologyRoute> select = technologyRouteMapper.select(dto);
			if (select.size()>0) {
				return null;
			}
			dto.setRouteVersion("V1");
			dto.setStatus("unsubmit");
			// 工艺路径编码的生成规则是 LJ+yyyyMMdd0001
			String trCode = getNewRouteNumber(request);
			dto.setRouteNumber(trCode);
		}
		List<TechnologyRoute> batchUpdate = self().batchUpdate(request, list);
		return batchUpdate;
	}
	
	public String getNewRouteNumber(IRequest request) {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
		String time = dateFormat.format(date);
		DocSequence docSequence = new DocSequence();
		docSequence.setDocType("GYZD" + time);
		String trCode = iDocSequenceService.getSequence(request, docSequence, "", 4, 1L);
		return docSequence.getDocType()+trCode;
	}

	@Override
	public List<DropDownListDto> queryOldSku(IRequest request, QuickTechRouteDto dto) {
		// TODO 查询参照版本SKU 获取的数据是工艺路径表+历史表 的所有sku
		return technologyRouteMapper.queryOldSku(dto);
	}

	@Override
	public List<DropDownListDto> queryRouteVersion(IRequest request, QuickTechRouteDto dto) {
		// TODO 查询工艺路径版本 获取这个sku的所有版本(包括历史版本)
		return technologyRouteMapper.queryRouteVersion(dto);
	}

	@Override
	public List<DropDownListDto> queryNewSku(IRequest request, QuickTechRouteDto dto) {
		// TODO 查询相同品类的sku
		return technologyRouteMapper.queryNewSku(dto);
	}

	@Override
	public List<EbomDetail> queryOldEbom(IRequest request, QuickTechRouteDto dto) {
		// TODO 根据选择的参考工艺路径的sku和ebom版本 查询bom信息

		return technologyRouteMapper.queryOldEbom(dto);
	}

	@Override
	public List<EbomDetail> queryNewEbom(IRequest request, QuickTechRouteDto dto) {
		// TODO 根据新的sku 版本为1 来选择ebom信息
		return technologyRouteMapper.queryNewEbom(dto);
	}

	@Override
	public Map<String, Object> checkData(IRequest request, QuickTechRouteDto dto) {
		// TODO 获取可以对比的数据
		List<EbomDetail> oldMinusNew = technologyRouteMapper.getOldMinusNew(dto);
		List<EbomDetail> newMinOld = technologyRouteMapper.getNewMinOld(dto);
		Map<String, Object> resultMap = Maps.newHashMap();
		resultMap.put("oldMinusNew", oldMinusNew);
		resultMap.put("newMinOld", newMinOld);
		return resultMap;
	}

	@Override
	public ResponseData copyData(IRequest request, QuickTechRouteDto dto) {
		ResponseData responseData = new ResponseData();
		//TODO 历史表字段还没有修改 这里代码先不做修改
		/**
		 * 系统将复制选择的SKU对应的工艺路径、工序和绑定的SOP动画等全部复制后绑定到新的SKU上。
		 * 注：复制时，新的SKU中
		 * 1、工艺路线编码系统生成  2、版本为V1 3、SKU为填写的”新版本SKU“ 4、LINE为选择的值、
		 * 5、E-BOM版本为该SKU最新的E-BOM版本 6、审批状态为”新增“ 7、项目名称：根据填写的"新版本SKU"查询SKU主数据表自动带出
		 * 8、标准总工时、设备工时、人工工时都和被复制的工艺路径保持一致
		 * 9、工序详细信息与被复制的工艺路径保持一致，其中E-BOM比对时有差异部分标红
		 * 10、copy来的数据  工序要打上标签 copyFlag=1
		 */
		//由于数据可能来源于历史表和最新数据表，所以对不同的数据来源进行不同的处理  涉及的表 工艺路径表、工序表、工艺动作表、工艺动作物料属性表
		//复制的校验逻辑 复制出来的工艺路径 在原来的数据中不能存在一样的数据
		TechnologyRoute drRoute=new TechnologyRoute();
		drRoute.setSkuId(dto.getNewSku());
		drRoute.setLine(dto.getNewLine());
		List<TechnologyRoute> checkList = technologyRouteMapper.select(drRoute);
		if (checkList.size()!=0) {
			//存在重复组合数据 不允许保存
			responseData.setSuccess(false);
			responseData.setMessage("SKU和产线组合重复");
			return responseData;
		}
		
		drRoute.setRouteVersion(dto.getRouteVersion());
		drRoute.setSkuId(dto.getOldSku());
		drRoute.setLine(dto.getOldLine());
		List<TechnologyRoute> select = technologyRouteMapper.select(drRoute);
		if (select.isEmpty()) {
			//说明选择的是历史表数据
			TechnologyRouteHis ts=new TechnologyRouteHis();
			ts.setOldVersionNumber(dto.getRouteVersion());
			ts.setSkuId(dto.getOldSku());
			ts.setLine(dto.getOldLine());
			List<TechnologyRouteHis> routeHis = technologyRouteHisMapper.select(ts);
			for (TechnologyRouteHis technologyRouteHis : routeHis) {
				//新增一条工艺路径 部分信息由原数据复制而来
				TechnologyRoute newTr = new TechnologyRoute();
				newTr.setSkuId(dto.getNewSku());
				newTr.setLine(dto.getNewLine());
				newTr.setRouteVersion("V1");
				String newRouteNumber = getNewRouteNumber(request);
				newTr.setRouteNumber(newRouteNumber);
				newTr.setStandardWorkingHoursTotal(technologyRouteHis.getStandardWorkingHoursTotal());
				newTr.setStatus("unsubmit");
				newTr.setCopyFromRn(technologyRouteHis.getRouteNumber());
				newTr.setCopyFromRv(technologyRouteHis.getOldVersionNumber());
				// TODO ebom版本要最新的  从NPI_EBOM_MAIN表获取最新日期的数据的版本
				String newEbomVersion = technologyRouteMapper.getNewEbomVersion(dto.getNewSku());
				newTr.seteBomVersion(newEbomVersion);
				technologyRouteMapper.insertSelective(newTr);
				//查询出工序历史
				TechnologyWorkingProcedureHis twphis =new TechnologyWorkingProcedureHis();
				twphis.setRouteNumber(technologyRouteHis.getRouteNumber());
				twphis.setRouteVersion(technologyRouteHis.getOldVersionNumber());
				List<TechnologyWorkingProcedureHis> twphisList = workingProcedureHisMapper.select(twphis);
				for (TechnologyWorkingProcedureHis hisdto : twphisList) {
					//新增一条工序  部分信息由原数据复制而来
					TechnologyWorkingProcedure newTwp = new TechnologyWorkingProcedure();
					newTwp.setRouteId(newTr.getRouteId());
					newTwp.setSerialNumber(hisdto.getSerialNumber());
					newTwp.setStandardWorkingHours(hisdto.getStandardWorkingHours());
					newTwp.setPreWorkingProcedure(hisdto.getPreWorkingProcedure());
					newTwp.setInOutMake(hisdto.getInOutMake());
					//TODO newTwp.setWpCode(hisdto.get); todo
					//newTwp.setSopId(hisdto.getSopId());
					newTwp.setLineType(hisdto.getLineType());
					newTwp.setLineCode(hisdto.getLineCode());
					newTwp.setAutoFlag(hisdto.getAutoFlag());
					newTwp.setStatus(hisdto.getStatus());
					newTwp.setCopyFlag("1");
					//查询出该条历史数据下的工艺动作
					workingProcedureMapper.insertSelective(newTwp);
					TechnologyWpAction twpc=new TechnologyWpAction();
					twpc.setRouteWpRefId(newTwp.getRouteWpRefId());
					List<TechnologyWpAction> twpcList = wpActionMapper.select(twpc);
					for (TechnologyWpAction wpaDto : twpcList) {
						//新增工艺动作
						Float routeWpRefId = wpaDto.getRouteWpRefId();
						wpaDto.setRouteWpRefId(routeWpRefId);
						wpActionMapper.insert(wpaDto);
						//查询该条动作之下的物料属性
						TechnologyWpMaterielRef twpmr=new TechnologyWpMaterielRef();
						//twpmr.setWpId(wpId);
						List<TechnologyWpMaterielRef> twpmrList = wpMaterielRefMapper.select(twpmr);
						for (TechnologyWpMaterielRef refDto : twpmrList) {
							//refDto.setWpId(wpaDto.getId());
							wpMaterielRefMapper.insertSelective(refDto);
						}
						//2020年3月26日17:27:22 复制工装设备信息
						TechnologyWpActionEquipDetail twed=new TechnologyWpActionEquipDetail();
						//twed.setWpActionId(wpId);
						List<TechnologyWpActionEquipDetail> equipDetails = tEquipDetailMapper.select(twed);
						for (TechnologyWpActionEquipDetail refDto : equipDetails) {
							//refDto.setWpActionId(wpaDto.getId());
							tEquipDetailMapper.insertSelective(refDto);
						}
					}
					//复制标准动作
					TechnologyWpStandardActionDetail detail = new TechnologyWpStandardActionDetail();
					//detail.setRouteWpRefId(hisdto);
					List<TechnologyWpStandardActionDetail> standardActionDetails = tActionDetailMapper.select(detail);
					for (TechnologyWpStandardActionDetail wpaDto : standardActionDetails) {
						//新增标准工艺动作
						Float wpId = wpaDto.getWpStdActDetailId();
						tActionDetailMapper.insert(wpaDto);
						//2020年3月26日17:27:22 复制工装设备信息
						TechnologyWpActionEquipDetail twed=new TechnologyWpActionEquipDetail();
						twed.setWpActionId(wpId);
						List<TechnologyWpActionEquipDetail> equipDetails = tEquipDetailMapper.select(twed);
						for (TechnologyWpActionEquipDetail refDto : equipDetails) {
							refDto.setWpActionId(wpaDto.getWpStdActDetailId());
							tEquipDetailMapper.insertSelective(refDto);
						}
					}
					//复制spec要求
					TechnologyWpSpecDetail specDetail=new TechnologyWpSpecDetail();
					/*specDetail.setWpCode(hisdto.getOldWpCode());
					specDetail.setWpVersion(hisdto.getRouteVersion());
					specDetail.setRouteCode(hisdto.getRouteNumber());
					specDetail.setRouteVersion(hisdto.getRouteVersion());*/
					List<TechnologyWpSpecDetail> specDetails = tDetailMapper.select(specDetail);
					for (TechnologyWpSpecDetail dSpecDetail : specDetails) {
						/*dSpecDetail.setWpCode(newTwp.getWpCode());
						dSpecDetail.setWpVersion(newTwp.getRouteVersion());
						dSpecDetail.setRouteCode(newTr.getRouteNumber());
						dSpecDetail.setRouteVersion(newTr.getRouteVersion());*/
						tDetailMapper.insertSelective(dSpecDetail);
					}
				}
			}
		}else {
			//说明选择的是最新的表的数据
			TechnologyRoute ts=new TechnologyRoute();
			ts.setRouteVersion(dto.getRouteVersion());
			ts.setSkuId(dto.getOldSku());
			ts.setLine(dto.getOldLine());
			List<TechnologyRoute> routeHis = technologyRouteMapper.select(ts);
			for (TechnologyRoute technologyRoute : routeHis) {
				Float routeIdOld = technologyRoute.getRouteId();
				//新增一条工艺路径 部分信息由原数据复制而来
				TechnologyRoute newTr = new TechnologyRoute();
				newTr.setSkuId(dto.getNewSku());
				newTr.setLine(dto.getNewLine());
				newTr.setRouteVersion("V1");
				String newRouteNumber = getNewRouteNumber(request);
				newTr.setRouteNumber(newRouteNumber);
				newTr.setStandardWorkingHoursTotal(technologyRoute.getStandardWorkingHoursTotal());
				newTr.setStatus("unsubmit");
				newTr.setCopyFromRn(technologyRoute.getRouteNumber());
				newTr.setCopyFromRv(technologyRoute.getRouteVersion());
				// TODO ebom版本要最新的
				String newEbomVersion = technologyRouteMapper.getNewEbomVersion(dto.getNewSku());
				newTr.seteBomVersion(newEbomVersion);
				technologyRouteMapper.insertSelective(newTr);
				//查询出工序信息
				TechnologyWorkingProcedure twphis =new TechnologyWorkingProcedure();
				twphis.setRouteId(routeIdOld);
				List<TechnologyWorkingProcedure> twphisList = workingProcedureMapper.select(twphis);
				for (TechnologyWorkingProcedure hisdto : twphisList) {
					//新增一条工序  部分信息由原数据复制而来
					Float routeWpRefIdOld = hisdto.getRouteWpRefId();
					hisdto.setCopyFlag("1");
					hisdto.setRouteId(newTr.getRouteId());
					//查询出该条历史数据下的工艺动作
					workingProcedureMapper.insertSelective(hisdto);
					TechnologyWpAction twpc=new TechnologyWpAction();
					twpc.setRouteWpRefId(routeWpRefIdOld);
					List<TechnologyWpAction> twpcList = wpActionMapper.select(twpc);
					for (TechnologyWpAction wpaDto : twpcList) {
						//新增工艺动作
						Float wpId = wpaDto.getWpAuxId();
						wpaDto.setRouteWpRefId(hisdto.getRouteWpRefId());
						wpActionMapper.insert(wpaDto);
						//查询该条动作之下的物料属性
						TechnologyWpMaterielRef twpmr=new TechnologyWpMaterielRef();
						twpmr.setWpAuxId(wpId);
						List<TechnologyWpMaterielRef> twpmrList = wpMaterielRefMapper.select(twpmr);
						for (TechnologyWpMaterielRef refDto : twpmrList) {
							refDto.setWpAuxId(wpaDto.getWpAuxId());
							wpMaterielRefMapper.insertSelective(refDto);
						}
						TechnologyWpActionEquipDetail twed=new TechnologyWpActionEquipDetail();
						twed.setWpActionId(wpId);
						List<TechnologyWpActionEquipDetail> equipDetails = tEquipDetailMapper.select(twed);
						for (TechnologyWpActionEquipDetail refDto : equipDetails) {
							refDto.setWpActionId(wpaDto.getWpAuxId());
							tEquipDetailMapper.insertSelective(refDto);
						}
					}
					//复制标准动作
					TechnologyWpStandardActionDetail detail = new TechnologyWpStandardActionDetail();
					detail.setRouteWpRefId(routeWpRefIdOld);
					List<TechnologyWpStandardActionDetail> standardActionDetails = tActionDetailMapper.select(detail);
					for (TechnologyWpStandardActionDetail wpaDto : standardActionDetails) {
						//新增标准工艺动作
						Float wpId = wpaDto.getWpStdActDetailId();
						wpaDto.setRouteWpRefId(hisdto.getRouteWpRefId());
						tActionDetailMapper.insert(wpaDto);
						//2020年3月26日17:27:22 复制工装设备信息
						TechnologyWpActionEquipDetail twed=new TechnologyWpActionEquipDetail();
						twed.setWpActionId(wpId);
						List<TechnologyWpActionEquipDetail> equipDetails = tEquipDetailMapper.select(twed);
						for (TechnologyWpActionEquipDetail refDto : equipDetails) {
							refDto.setWpActionId(wpaDto.getWpStdActDetailId());
							tEquipDetailMapper.insertSelective(refDto);
						}
					}
					//复制spec要求
					TechnologyWpSpecDetail specDetail=new TechnologyWpSpecDetail();
					specDetail.setRouteWpRefId(routeWpRefIdOld);
					List<TechnologyWpSpecDetail> specDetails = tDetailMapper.select(specDetail);
					for (TechnologyWpSpecDetail dSpecDetail : specDetails) {
						dSpecDetail.setRouteWpRefId(hisdto.getRouteWpRefId());
						tDetailMapper.insertSelective(dSpecDetail);
					}
				}
			}
		}
		return responseData;
	}

}