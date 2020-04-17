package com.hand.npi.npi_route.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.DocSequence;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IDocSequenceService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.npi.npi_route.dto.*;
import com.hand.npi.npi_route.mapper.*;
import com.hand.npi.npi_route.service.ITechnologyWorkingProcedureService;
import com.hand.npi.npi_technology.dto.*;
import com.hand.npi.npi_technology.mapper.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class TechnologyWorkingProcedureServiceImpl extends BaseServiceImpl<TechnologyWorkingProcedure>
		implements ITechnologyWorkingProcedureService {
    @Autowired
    TechnologyRouteMapper technologyRouteMapper;
	@Autowired
	TechnologyWorkingProcedureMapper technologyWorkingProcedureMapper;
	@Autowired
	TechnologyWpActionMapper technologyWpActionMapper;
	@Autowired
	TechnologyWpMaterielRefMapper technologyWpMaterielRefMapper;
	@Autowired
	TechnologyWpActionEquipDetailMapper technologyWpActionEquipDetailMapper;
	@Autowired
	TechnologyWpStandardActionDetailMapper tWpStandardActionDetailMapper;
	@Autowired
	IDocSequenceService iDocSequenceService;
	@Autowired
	EbomDetailMapper ebomDetailMapper;
	@Autowired
	EbomMainMapper ebomMainMapper;
	@Autowired
	TechnologyWpSpecDetailMapper technologyWpSpecDetailMapper;
	@Autowired
	TechnologyWorkProcedureStoreMapper technologyWorkProcedureStoreMapper;
	@Autowired
	TechnologyWpStoreDetailMapper technologyWpStoreDetailMapper;
	@Autowired
	TechnologyWpStoreMaterielRefMapper technologyWpStoreMaterielRefMapper;
	@Autowired
	private ComposeProductMapper composeProductMapper;
	@Autowired
	private ComposeProductMaterielMapper composeProductMaterielMapper;
	@Autowired
	private ComposeProductMaterielAttrMapper composeProductMaterielAttrMapper;

	@Override
	public ResponseData addData(IRequest request, TechnologyWorkingProcedure dto) {
		ResponseData responseData = new ResponseData(true);

		responseData = checkSku(dto);
		if (!responseData.isSuccess()) {
			return responseData;
		}

		// 首先新增工序 然后新增工序的工艺动作 然后新增工艺动作的物料属性表
		// 2020年3月30日14:09:46 由于表结构调整 代码的逻辑发生变化   工序有了自己的库表  插入时 先插入库表中 然后 插入自己的关联表中
		TechnologyWorkProcedureStore twps=new TechnologyWorkProcedureStore();
		TechnologyWpMaterielRef tMaterielRef = new TechnologyWpMaterielRef();
		dto.setVersion("V1");
		String wpCode = getNewWpCode(request);
		twps.setWpCode(wpCode);
		twps.setWpName(dto.getWpName());
		technologyWorkProcedureStoreMapper.insertSelective(twps);
		dto.setWpId(twps.getWpId());
		//新增时设置工序的序号 为当前最大的序号+1
		Float serialNumber = technologyWorkingProcedureMapper.getMaxSerialNumber(dto);
		dto.setSerialNumber(serialNumber);
		// 新增工序
		technologyWorkingProcedureMapper.insertSelective(dto);
		List<TechnologyWpAction> matList = dto.getMatList();
		for (TechnologyWpAction technologyWpAction : matList) {
			// 新增工艺动作
			technologyWpAction.setRouteWpRefId(dto.getRouteWpRefId());
			technologyWpActionMapper.insertSelective(technologyWpAction);
			String materielIds = technologyWpAction.getMaterielIds();
			if (StringUtils.isNotBlank(materielIds)) {
				String[] split = materielIds.split(",");
				for (String id : split) {
					// 新增工艺动作物料属性
					tMaterielRef.setWpAuxId(technologyWpAction.getWpAuxId());
					tMaterielRef.setMatAttrCode(id);
					technologyWpMaterielRefMapper.insertSelective(tMaterielRef);
				}
			}
			// 新增它的工装设备夹具
			List<TechnologyWpActionEquipDetail> equipList = technologyWpAction.getEquipList();
			if (equipList != null) {
				for (TechnologyWpActionEquipDetail detaildto : equipList) {
					// technologyWpActionEquipDetailMapper.insertSelective(detaildto);
					if ("add".equals(detaildto.getStatus())) {
						detaildto.setWpActionId(technologyWpAction.getWpAuxId());
						detaildto.setActionType("aux");
						technologyWpActionEquipDetailMapper.updateData(detaildto);
					} else {
						detaildto.setWpActionId(technologyWpAction.getWpAuxId());
						detaildto.setActionType("aux");
						technologyWpActionEquipDetailMapper.insertSelective(detaildto);
					}
				}
			}
		}
		// 新增标准动作行表
		TechnologyWpStandardActionDetail staDetail = dto.getStaDetail();
		staDetail.setRouteWpRefId(dto.getRouteWpRefId());
		tWpStandardActionDetailMapper.insertSelective(staDetail);
		// 标准动作有自己的工装设备夹具
		List<TechnologyWpActionEquipDetail> equipList = staDetail.getEquipList();
		if (equipList != null) {
			for (TechnologyWpActionEquipDetail detaildto : equipList) {
				if ("add".equals(detaildto.getStatus())) {
					detaildto.setWpActionId(staDetail.getWpStdActDetailId());
					detaildto.setActionType("str");
					technologyWpActionEquipDetailMapper.updateData(detaildto);
				} else {
					detaildto.setWpActionId(staDetail.getWpStdActDetailId());
					detaildto.setActionType("str");
					technologyWpActionEquipDetailMapper.insertSelective(detaildto);
				}
			}
		}
		// 新增工序的spec要求表
		List<TechnologyWpSpecDetail> specDetail = dto.getSpecDetail();
		if (specDetail != null) {
			for (TechnologyWpSpecDetail technologyWpSpecDetail : specDetail) {
				technologyWpSpecDetail.setRouteWpRefId(dto.getRouteWpRefId());
				technologyWpSpecDetailMapper.insertSelective(technologyWpSpecDetail);
			}
		}
        changeRouteSWHT(dto);
		//2020年4月1日16:30:48 自动存为组合件
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String time = dateFormat.format(date);
		DocSequence docSequence = new DocSequence();
		docSequence.setDocType("ZHJ"+time);
		String assItemCode = iDocSequenceService.getSequence(request, docSequence, "ZHJ"+time, 4, 1L);
		ComposeProduct comDto = new ComposeProduct();
		comDto.setAssItemCode(assItemCode);
		comDto.setRouteId(dto.getRouteId());
		TechnologyRoute technologyRoute = technologyRouteMapper.selectByPrimaryKey(dto.getRouteId());
		comDto.setRouteVersion(technologyRoute.getRouteVersion());
		comDto.setWpId(dto.getRouteWpRefId());
		comDto.setWpVersion(dto.getVersion());
		composeProductMapper.insertSelective(comDto);
		ComposeProductMateriel composeProductMateriel=new ComposeProductMateriel();
		ComposeProductMaterielAttr composeProductMaterielAttr=new ComposeProductMaterielAttr();
		for (TechnologyWpAction technologyWpAction : matList) {
			composeProductMateriel.setAssItemId(comDto.getAssItemId());
			composeProductMateriel.setItemId(technologyWpAction.getItemId());
			composeProductMateriel.setItemDetailVersion(technologyWpAction.getItemDetailVersion());
			composeProductMateriel.setMatType(technologyWpAction.getMatType());
			composeProductMaterielMapper.insertSelective(composeProductMateriel);
			String materielIds = technologyWpAction.getMaterielIds();
			String[] split = materielIds.split(",");
			for (String id : split) {
				composeProductMaterielAttr.setMatAttrId(Float.valueOf(id));
				composeProductMaterielAttr.setAssMatRefId(composeProductMateriel.getAssMatRefId());
				composeProductMaterielAttrMapper.insertSelective(composeProductMaterielAttr);
			}
		}
        return responseData;
	}

    private void changeRouteSWHT(TechnologyWorkingProcedure dto) {
        //2020年4月1日14:47:21 工序保存之后 工艺路径的标准工时小计需要更新
        TechnologyRoute technologyRoute = technologyRouteMapper.selectByPrimaryKey(dto.getRouteId());
        float total = technologyRoute.getStandardWorkingHoursTotal() + dto.getStandardWorkingHours();
        technologyRoute.setStandardWorkingHoursTotal(total);
        technologyRouteMapper.updateByPrimaryKey(technologyRoute);
    }

    @Override
	public EbomMain queryEBomVersion(IRequest request, EbomMain dto) {
		// 查询最新的sku的ebom版本
		EbomMain queryNewEBomVersion = ebomMainMapper.queryNewEBomVersion(dto);
		return queryNewEBomVersion;
	}

	@Override
	public List<EbomDetail> qeuryEBomPart(IRequest request, EbomMain dto, int pageNum, int pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(pageNum, pageSize);
		List<EbomDetail> qeuryEBomPart = ebomDetailMapper.qeuryEBomPart(dto);
		return qeuryEBomPart;
	}

	@Override
	public List<TechnologyWorkingProcedure> selectWpInfo(IRequest request, TechnologyWorkingProcedure dto, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		return technologyWorkingProcedureMapper.queryWpInfo(dto);
	}

	@Override
	public ResponseData updateData(IRequest request, TechnologyWorkingProcedure dto) {
		// 修改复制过来的工序的信息内容如下时：主表：工序名称、标准动作 明细表：物料编码、数量 属性表：物料属性
		// 保存时首先校验上述的各种值有没有变化 如果有那么还要往库表中添加新的数据 如果没有 那么只需要更新下原来的表就可以
		// 2020年3月30日18:19:58 逻辑变化 如果没有改变 那么不需要插入到库表中  如果有 那么需要先插入库表中 然后再插入到工艺路径的各个表中
		boolean isSaveFlag = checkIsSaveStore(dto);
		ResponseData responseData = new ResponseData(true);
		if (isSaveFlag) {
			// 需要插入到库表中
			responseData = checkSku(dto);
			if (!responseData.isSuccess()) {
				return responseData;
			}
			// 往库表中插入数据
			TechnologyWorkProcedureStore twps=new TechnologyWorkProcedureStore();
			//生成新的工序编码 	
			String wpCode = getNewWpCode(request);
			twps.setWpCode(wpCode);
			String versionNumber = dto.getVersion();
			twps.setWpName(dto.getWpName());
			twps.setWpVersion(versionNumber);
			twps.setStandardActionId(dto.getStaDetail().getStandardActionId());
			technologyWorkProcedureStoreMapper.insertSelective(twps);
			TechnologyWpStoreDetail twpsd=new TechnologyWpStoreDetail();
			twpsd.setWpId(twps.getWpId());
			TechnologyWpStoreMaterielRef twpsr=new TechnologyWpStoreMaterielRef();
			List<TechnologyWpAction> matList = dto.getMatList();
			for (TechnologyWpAction technologyWpAction : matList) {
				twpsd.setMatType(technologyWpAction.getMatType());
				twpsd.setQty(technologyWpAction.getQty());
				twpsd.setItemId(technologyWpAction.getItemId());
				twpsd.setItemVersion(technologyWpAction.getItemDetailVersion());
				technologyWpStoreDetailMapper.insertSelective(twpsd);
				String materielIds = technologyWpAction.getMaterielIds();
				if (StringUtils.isNotBlank(materielIds)) {
					String[] split = materielIds.split(",");
					for (String id : split) {
						// 新增工艺动作物料属性
						twpsr.setMatAttrId(Float.parseFloat(id));
						twpsr.setWpStoreMatRefId(twpsd.getWpStoreMatRefId());
						technologyWpStoreMaterielRefMapper.insertSelective(twpsr);
					}
				}
			}
			changeLocal( request, dto,isSaveFlag,twps.getWpId());
		} else {
			// 不需要插入到库表中
			responseData = checkSku(dto);
			if (!responseData.isSuccess()) {
				return responseData;
			}
			changeLocal( request, dto,isSaveFlag,0F);
		}

		return responseData;
	}

	private boolean checkIsSaveStore(TechnologyWorkingProcedure dto) {
		boolean isSaveFlag = false;
		TechnologyWpMaterielRef tMaterielRef = new TechnologyWpMaterielRef();
		// 校验工序名称是否改变
		TechnologyWorkingProcedure pkDto = technologyWorkingProcedureMapper.selectByPrimaryKey(dto.getRouteWpRefId());
		TechnologyWorkProcedureStore technologyWorkProcedureStore = technologyWorkProcedureStoreMapper.selectByPrimaryKey(dto.getWpId());
        String wpName = technologyWorkProcedureStore.getWpName();
        if (!wpName.equals(dto.getWpName())) {
			isSaveFlag = true;
		}
		// 校验工艺动作的标准动作
		TechnologyWpStandardActionDetail staDetail = dto.getStaDetail();
		List<TechnologyWpStandardActionDetail> staDetailList = tWpStandardActionDetailMapper.select(staDetail);
		if (staDetailList.isEmpty()) {
			isSaveFlag = true;
		}
		// 校验工艺动作的物料编码和数量 还有物料属性
		List<TechnologyWpAction> matList = dto.getMatList();
		if (!isSaveFlag) {
			boolean flag = false;
			for (TechnologyWpAction technologyWpAction : matList) {
				if (flag) {
					break;
				}
				List<TechnologyWpAction> select = technologyWpActionMapper.select(technologyWpAction);
				if (select.isEmpty()) {
					isSaveFlag = true;
					break;
				} else {
					String materielIds = technologyWpAction.getMaterielIds();
					// 查询物料明细
					if (StringUtils.isNotBlank(materielIds)) {
						String[] split = materielIds.split(",");
						for (String id : split) {
							// 新增工艺动作物料属性
							tMaterielRef.setWpAuxId(technologyWpAction.getWpAuxId());
							tMaterielRef.setMatAttrCode(id);
							List<TechnologyWpMaterielRef> refs = technologyWpMaterielRefMapper.select(tMaterielRef);
							if (refs.isEmpty()) {
								isSaveFlag = true;
								flag = true;
								break;
							}
						}
					}
				}
			}
		}
		return isSaveFlag;
	}

	private ResponseData checkSku(TechnologyWorkingProcedure dto) {
		ResponseData responseData = new ResponseData(true);
		String sku = dto.getSkuId();
		// TODO 保存时要校验物料是否还够
		List<TechnologyWpAction> matListCheck = dto.getMatList();
		for (TechnologyWpAction technologyWpAction : matListCheck) {
			String materielNumber = technologyWpAction.getMaterielNumber();
			List<EbomDetail> checkMatQty = ebomDetailMapper.checkMatQty(sku, materielNumber);
			for (EbomDetail ebomDetail : checkMatQty) {
				if (technologyWpAction.getQty() > ebomDetail.getQuantity()) {
					// 数量超出 保存失败
					responseData.setSuccess(false);
					responseData.setMessage("物料" + materielNumber + "超出Ebom总数");
					return responseData;
				}
			}
		}
		return responseData;
	}

	private void changeLocal(IRequest request,TechnologyWorkingProcedure dto,boolean isSaveFlag,Float newWpId) {
		// 首先更新工序编码 然后新增工序的工艺动作 然后新增工艺动作的物料属性表
		TechnologyWpMaterielRef tMaterielRef = new TechnologyWpMaterielRef();
		TechnologyWpActionEquipDetail tEquipDetail = new TechnologyWpActionEquipDetail();
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String time = dateFormat.format(date);
		dto.setVersion("V1");
		Float wpIdOld = dto.getWpId();
		if (isSaveFlag) {
			dto.setWpId(newWpId);
		}
		// 更新工序
		technologyWorkingProcedureMapper.updateByPrimaryKey(dto);
		List<TechnologyWpAction> matList = dto.getMatList();
		// 删除旧数据 删除的时候也按照层级结构从最后一层开始删除
		// 首先通过工艺动作删除所有的物料属性和装配明细信息
		for (TechnologyWpAction technologyWpAction : matList) {
			technologyWpAction.setRouteWpRefId(dto.getRouteWpRefId());
			// 删除工艺动作的行表信息 1. 删除物料 2.删除明细
			tMaterielRef.setWpAuxId(technologyWpAction.getWpAuxId());
			tEquipDetail.setWpActionId(technologyWpAction.getWpAuxId());
			tEquipDetail.setActionType("aux");
			technologyWpMaterielRefMapper.delete(tMaterielRef);
			technologyWpActionEquipDetailMapper.delete(tEquipDetail);
			// 新增工艺动作
			technologyWpAction.setRouteWpRefId(dto.getRouteWpRefId());
			if (technologyWpAction.getWpAuxId() == null) {
				technologyWpActionMapper.insertSelective(technologyWpAction);
			}else {
				technologyWpActionMapper.updateByPrimaryKeySelective(technologyWpAction);
			}
			String materielIds = technologyWpAction.getMaterielIds();
			// String materielIds = "1,2";
			if (StringUtils.isNotBlank(materielIds)) {
				String[] split = materielIds.split(",");
				for (String id : split) {
					// 新增工艺动作物料属性
					tMaterielRef.setWpAuxId(technologyWpAction.getWpAuxId());
					tMaterielRef.setMatAttrCode(id);
					technologyWpMaterielRefMapper.insertSelective(tMaterielRef);
				}
			}
			// 新增它的工装设备夹具
			List<TechnologyWpActionEquipDetail> equipList = technologyWpAction.getEquipList();
			if (equipList != null) {
				for (TechnologyWpActionEquipDetail detaildto : equipList) {
					// technologyWpActionEquipDetailMapper.insertSelective(detaildto);
					if ("add".equals(detaildto.getStatus()) && null == detaildto.getActionEquipId()) {
						detaildto.setWpActionId(technologyWpAction.getWpAuxId());
						detaildto.setActionType("aux");
						technologyWpActionEquipDetailMapper.updateData(detaildto);
					} else {
						detaildto.setWpActionId(technologyWpAction.getWpAuxId());
						// detaildto.setActionType("aux");
						technologyWpActionEquipDetailMapper.insertSelective(detaildto);
					}
				}
			}
		}
		// 新增标准动作行表
		TechnologyWpStandardActionDetail staDetail = dto.getStaDetail();
		staDetail.setRouteWpRefId(dto.getRouteWpRefId());
		tWpStandardActionDetailMapper.updateByPrimaryKey(staDetail);
		// 删除工装信息
		tEquipDetail.setWpActionId(staDetail.getWpStdActDetailId());
		tEquipDetail.setActionType("str");
		technologyWpActionEquipDetailMapper.delete(tEquipDetail);
		// 标准动作有自己的工装设备夹具
		List<TechnologyWpActionEquipDetail> equipList = staDetail.getEquipList();
		if (equipList != null) {
			for (TechnologyWpActionEquipDetail detaildto : equipList) {
				if ("add".equals(detaildto.getStatus()) && null == detaildto.getActionEquipId()) {
					detaildto.setWpActionId(staDetail.getWpStdActDetailId());
					detaildto.setActionType("str");
					technologyWpActionEquipDetailMapper.updateData(detaildto);
				} else {
					detaildto.setWpActionId(staDetail.getWpStdActDetailId());
					// detaildto.setActionType("str");
					technologyWpActionEquipDetailMapper.insertSelective(detaildto);
				}
			}
		}
		// 删除spec要求
		TechnologyWpSpecDetail specDetail = new TechnologyWpSpecDetail();
		specDetail.setRouteWpRefId(dto.getRouteWpRefId());
		technologyWpSpecDetailMapper.delete(specDetail);
		// 新增工序的spec要求表
		List<TechnologyWpSpecDetail> specDetailList = dto.getSpecDetail();
		if (specDetail != null) {
			for (TechnologyWpSpecDetail technologyWpSpecDetail : specDetailList) {
                technologyWpSpecDetail.setRouteWpRefId(dto.getRouteWpRefId());
				technologyWpSpecDetailMapper.insertSelective(technologyWpSpecDetail);
			}
		}
		
		List<TechnologyWpActionEquipDetail> equipDetail = dto.getEquipDetail();
		if (equipDetail != null) {
			for (TechnologyWpActionEquipDetail technologyWpActionEquipDetail : equipDetail) {
				if(technologyWpActionEquipDetail.getActionEquipId() != null) {
					technologyWpActionEquipDetailMapper.insertSelective(technologyWpActionEquipDetail);
				}
			}
		}
        changeRouteSWHT(dto);
	}

	private  String getNewWpCode(IRequest request) {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String time = dateFormat.format(date);
		DocSequence docSequence = new DocSequence();
		docSequence.setDocType("wpCodeSeq" + time);
		return iDocSequenceService.getSequence(request, docSequence, "ZY" + time, 4, 1L);
	}
}