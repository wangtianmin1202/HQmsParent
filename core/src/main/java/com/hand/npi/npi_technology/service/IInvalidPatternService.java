package com.hand.npi.npi_technology.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.npi.npi_technology.dto.InvalidPattern;
import com.hand.npi.npi_technology.dto.TechnologySpec;

public interface IInvalidPatternService extends IBaseService<InvalidPattern>, ProxySelf<IInvalidPatternService>{
	
	ResponseData addNewInvalidPattern(InvalidPattern dto, IRequest requestCtx, HttpServletRequest request);
	
	void deleteRow(InvalidPattern dto) throws Exception;
	void deleteDatas(List<InvalidPattern> dto);
	
	
	ResponseData addInvalidPatterns(List<InvalidPattern> dtos, IRequest requestCtx, HttpServletRequest request);
	List<InvalidPattern> editInvalidPatterns(List<InvalidPattern> dtos, IRequest requestCtx, HttpServletRequest request);
	
	List<InvalidPattern> queryData(IRequest request, InvalidPattern condition, int pageNum, int pageSize);
	
	List<InvalidPattern> queryHisData(InvalidPattern dto,int page,int pageSize, IRequest requestCtx, HttpServletRequest request);

	/**
	 * @description 执行审批，启动流程
	 * @author likai 2020.03.26
	 * @param dtos
	 * @param requestCtx
	 * @return
	 */
	void audit(List<InvalidPattern> dtos, IRequest requestCtx);
	
	/**
	 * @author likai 2020.03.26
	 * @description 发起历史变更 新增或修改
	 * @param request
	 * @param list
	 * @return
	 */
	ResponseData addOrEditData(IRequest request, List<InvalidPattern> list);

}