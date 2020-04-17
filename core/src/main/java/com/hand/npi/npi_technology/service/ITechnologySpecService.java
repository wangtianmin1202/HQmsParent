package com.hand.npi.npi_technology.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.core.annotation.StdWho;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.npi.npi_technology.dto.TechnologySpec;
import com.hand.wfl.util.DropDownListDto;

public interface ITechnologySpecService extends IBaseService<TechnologySpec>, ProxySelf<ITechnologySpecService>{
	
	ResponseData addNewTechnologySpec(TechnologySpec dto, IRequest requestCtx, HttpServletRequest request);
	
	ResponseData addData(List<TechnologySpec> dto, IRequest requestCtx, HttpServletRequest request);
	
	List<DropDownListDto> queryStandardActionName(String standardActionName);
	
	/**
	 * @author likai 2020.03.20 
	 * @description 测试动作下拉列表查询
	 * @param standardActionName
	 * @return List<DropDownListDto>
	 */
	List<DropDownListDto> queryTestActionName(String standardActionName);
	
	List<DropDownListDto> queryAuxiliaryActionName(String standardActionName);
	
	List<DropDownListDto> queryMaterielName(String materielName);
	
	List<DropDownListDto> queryCharacteristicName(String characteristicName);
	
	
	List<TechnologySpec> queryTechnologySpecList(TechnologySpec dto,int page,int pageSize, IRequest requestCtx, HttpServletRequest request);
	
	List<TechnologySpec> querySpecData(TechnologySpec dto,int page,int pageSize, IRequest requestCtx, HttpServletRequest request);
	List<TechnologySpec> querySpecInfo(TechnologySpec dto,int page,int pageSize, IRequest requestCtx, HttpServletRequest request);
	List<TechnologySpec> queryHisData(TechnologySpec dto,int page,int pageSize, IRequest requestCtx, HttpServletRequest request);
	
	
	List<TechnologySpec> submitData(IRequest request, List<TechnologySpec> list);
	
	int delData(List<TechnologySpec> list);

	/**
	 * @description 保存测试动作要求
	 * @author likai 2020.03.20
	 * @param dtos
	 * @param requestCtx
	 * @param request
	 * @return
	 */
	ResponseData addTestData(List<TechnologySpec> dtos, IRequest requestCtx, HttpServletRequest request);
	
	/**
	 * @description 修改测试动作后执行保存
	 * @author likai 2020.03.20
	 * @param request
	 * @param list
	 * @return
	 */
	List<TechnologySpec> submitTestData(IRequest request, List<TechnologySpec> list);
	
	/**
	 * @description 执行审批，启动流程
	 * @author likai 2020.03.21
	 * @param dtos
	 * @param requestCtx
	 * @return
	 */
	void audit(List<TechnologySpec> dtos, IRequest requestCtx);
	

	/**
	 * @description 删除数据
	 * @author likai 2020.04.01
	 * @param dtos
	 * @param requestCtx
	 * @return
	 */
	void deleteData(List<TechnologySpec> dtos, IRequest requestCtx);

}