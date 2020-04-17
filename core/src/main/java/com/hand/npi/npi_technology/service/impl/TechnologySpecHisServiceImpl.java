package com.hand.npi.npi_technology.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.npi.npi_technology.dto.TechnologySparePartDetails;
import com.hand.npi.npi_technology.dto.TechnologySpec;
import com.hand.npi.npi_technology.dto.TechnologySpecDetail;
import com.hand.npi.npi_technology.dto.TechnologySpecHis;
import com.hand.npi.npi_technology.dto.TechnologySpecMatDetail;
import com.hand.npi.npi_technology.mapper.TechnologySparePartDetailsMapper;
import com.hand.npi.npi_technology.mapper.TechnologySpecDetailMapper;
import com.hand.npi.npi_technology.mapper.TechnologySpecHisMapper;
import com.hand.npi.npi_technology.mapper.TechnologySpecMapper;
import com.hand.npi.npi_technology.mapper.TechnologySpecMatDetailMapper;
import com.hand.npi.npi_technology.service.ITechnologySpecHisService;
import com.hand.npi.npi_technology.utils.GetNextVersion;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class TechnologySpecHisServiceImpl extends BaseServiceImpl<TechnologySpecHis> implements ITechnologySpecHisService{
	@Autowired
	TechnologySpecHisMapper technologySpecHisMapper;
	@Autowired
	TechnologySpecMatDetailMapper technologySpecMatDetailMapper;
	@Autowired
	TechnologySpecDetailMapper technologySpecDetailMapper;
	@Autowired
	TechnologySpecMapper technologySpecMapper;
	@Autowired
	TechnologySparePartDetailsMapper technologySparePartDetailsMapper;

	/**
	 * @author likai 2020.03.21
	 * @description 查询组装动作历史数据
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @param requestCtx
	 * @param request
	 * @return
	 */
	@Override
	public List<TechnologySpecHis> selectByLastUpdateDate(TechnologySpecHis dto, int page, int pageSize, IRequest requestCtx,
			HttpServletRequest request) {
		PageHelper.startPage(page, pageSize);
		return technologySpecHisMapper.selectByLastUpdateDate(dto);
	}
	
	/**
	 * @author likai 2020.03.23
	 * @description 发起历史变更 新增或修改
	 * @param request
	 * @param list
	 * @return
	 */
	@Override
	public ResponseData addOrEditData(IRequest request, List<TechnologySpecHis> list) {
		ResponseData responseData=new ResponseData();
		for(TechnologySpecHis dto: list) {
			TechnologySpec technologySpecFind = new TechnologySpec();
			technologySpecFind.setSpecId(dto.getSpecId());
			List<TechnologySpec> technologySpecFindList = technologySpecMapper.select(technologySpecFind);
			String status = "";
			if(technologySpecFindList != null && technologySpecFindList.size() > 0) {
				status = technologySpecFindList.get(0).getStatus();
			}
			if("1".equals(status)) {//变更修改
				TechnologySpecHis technologySpecHis = new TechnologySpecHis();
				
				technologySpecHis.setSpecId(dto.getSpecId());
				technologySpecHis.setSpecVersion(dto.getSpecVersion());
				List<TechnologySpecHis> listFind = technologySpecHisMapper.select(technologySpecHis);
				dto.setHisSpecId(listFind.get(0).getHisSpecId());
				technologySpecHisMapper.updateByPrimaryKeySelective(dto);
				
				TechnologySpecMatDetail technologySpecMatDetail = new TechnologySpecMatDetail();
				technologySpecMatDetail.setSpecId(dto.getSpecId());
				technologySpecMatDetail.setSpecVersion(dto.getSpecVersion());
				technologySpecMatDetailMapper.delete(technologySpecMatDetail);
				//新增Mat行信息
				if("1".equals(dto.getSpecActionType())) {
					List<TechnologySpecMatDetail> matList = dto.getMatList();
					if (matList !=null) {
						for (int i = matList.size() - 1; i > -1; i --) {
							matList.get(i).setSpecId(dto.getSpecId());
							matList.get(i).setSpecVersion(dto.getSpecVersion());
							technologySpecMatDetailMapper.insertSelective(matList.get(i));
						}
					}
				}else if("2".equals(dto.getSpecActionType())) {
					TechnologySpecMatDetail matDto = new TechnologySpecMatDetail();
					if(dto.getSparePartId() != null) {
						TechnologySparePartDetails technologySparePartDetails = new TechnologySparePartDetails();
						technologySparePartDetails.setDetailsCode(dto.getSparePartId());
						List<TechnologySparePartDetails> spareList = technologySparePartDetailsMapper.select(technologySparePartDetails);
						if(spareList != null && spareList.size() > 0) {
							matDto.setMaterielAttributeNumber(String.valueOf(spareList.get(0).getSparePartDetailsId()));
						}
						
						matDto.setSpecId(dto.getSpecId());
						matDto.setSpecVersion(dto.getSpecVersion());
						technologySpecMatDetailMapper.insertSelective(matDto);
					}
				}
				
				TechnologySpecDetail technologySpecDetail = new TechnologySpecDetail();
				technologySpecDetail.setSpecId(dto.getSpecId());
				technologySpecDetail.setSpecVersion(dto.getSpecVersion());
				technologySpecDetailMapper.delete(technologySpecDetail);
				//新增Detail行信息
				List<TechnologySpecDetail> detailList = dto.getDetailList();
				if (detailList !=null) {
					for (int i = detailList.size() - 1; i > -1; i --) {
						detailList.get(i).setSpecId(dto.getSpecId());
						detailList.get(i).setSpecVersion(dto.getSpecVersion());
						technologySpecDetailMapper.insertSelective(detailList.get(i));
					}
				}
			} else if("3".equals(status)) {//新增变更
				//新增头信息
				//版本号递增 获取新的版本号
				dto.setHisSpecId(null);
				String versionCode = GetNextVersion.getVersionCode(dto.getSpecVersion());
				dto.setSpecVersion(versionCode);
				technologySpecHisMapper.insertSelective(dto);
				
				//新增Mat行信息
				if("1".equals(dto.getSpecActionType())) {
					List<TechnologySpecMatDetail> matList = dto.getMatList();
					if (matList !=null) {
						for (int i = matList.size() - 1; i > -1; i --) {
							matList.get(i).setSpecId(dto.getSpecId());
							matList.get(i).setSpecVersion(dto.getSpecVersion());
							technologySpecMatDetailMapper.insertSelective(matList.get(i));
						}
					}
				}else if("2".equals(dto.getSpecActionType())) {
					TechnologySpecMatDetail matDto = new TechnologySpecMatDetail();
					if(dto.getSparePartId() != null) {
						TechnologySparePartDetails technologySparePartDetails = new TechnologySparePartDetails();
						technologySparePartDetails.setDetailsCode(dto.getSparePartId());
						List<TechnologySparePartDetails> spareList = technologySparePartDetailsMapper.select(technologySparePartDetails);
						if(spareList != null && spareList.size() > 0) {
							matDto.setMaterielAttributeNumber(String.valueOf(spareList.get(0).getSparePartDetailsId()));
						}
						
						matDto.setSpecId(dto.getSpecId());
						matDto.setSpecVersion(dto.getSpecVersion());
						technologySpecMatDetailMapper.insertSelective(matDto);
					}
				}
				
				//新增Detail行信息
				List<TechnologySpecDetail> detailList = dto.getDetailList();
				if (detailList !=null) {
					for (int i = detailList.size() - 1; i > -1; i --) {
						detailList.get(i).setSpecId(dto.getSpecId());
						detailList.get(i).setSpecVersion(dto.getSpecVersion());
						technologySpecDetailMapper.insertSelective(detailList.get(i));
					}
				}
				
				//更新单据状态为待提交
				TechnologySpec technologySpecUpdate = new TechnologySpec();
				technologySpecUpdate.setSpecId(dto.getSpecId());
				technologySpecUpdate.setStatus("1");
				technologySpecMapper.updateByPrimaryKeySelective(technologySpecUpdate);
			} else {
				responseData.setSuccess(false);
				responseData.setMessage("动作要求数据找不到！");
			}
		}
		
		responseData.setRows(list);
		return responseData;
	}
	
}