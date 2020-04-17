package com.hand.hqm.hqm_fqc_inspection_l.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_fqc_inspection_h.dto.FqcInspectionH;
import com.hand.hqm.hqm_fqc_inspection_l.dto.FqcInspectionL;

public interface IFqcInspectionLService extends IBaseService<FqcInspectionL>, ProxySelf<IFqcInspectionLService>{
	/**
	 * FQC检验单明细查询
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<FqcInspectionL> query(IRequest requestContext,FqcInspectionL dto,int page,int pageSize); 
	
	
	List<FqcInspectionL> queryfornon(IRequest requestContext,FqcInspectionL dto,int page,int pageSize);
	
	/**
	 * 
	 * @description 获取D表明细
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param requestCtx
	 * @param dtoh
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	List<FqcInspectionL> getDetail(IRequest requestCtx, FqcInspectionH dtoh) throws JsonGenerationException, JsonMappingException, IOException;
	
	/**
	 * 
	 * @description 保存
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param requestCtx
	 * @param request 
	 * @param dto
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws Exception
	 */
	void saveDetail(IRequest requestCtx, HttpServletRequest request, List<FqcInspectionL> dto) throws JsonParseException, JsonMappingException, IOException, Exception;
	
	/**
	 * 
	 * @description 提交
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param requestCtx
	 * @param request 
	 * @param dto
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws Exception
	 */
	ResponseData commitDetail(IRequest requestCtx, HttpServletRequest request, List<FqcInspectionL> dto) throws JsonParseException, JsonMappingException, IOException, Exception;


	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月31日 
	 * @param dto
	 */
	void batchAttributeDelete(List<FqcInspectionL> dto);


	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月31日 
	 * @param requestCtx
	 * @param dto
	 * @return
	 */
	List<FqcInspectionL> batchAttributeUpdate(IRequest requestCtx, List<FqcInspectionL> dto);


	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月31日 
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<FqcInspectionL> selectAttribute(IRequest requestContext, FqcInspectionL dto, int page, int pageSize);


	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月31日 
	 * @param request
	 * @param t
	 */
	FqcInspectionL insertSelectiveAttribute(IRequest request, FqcInspectionL t);


	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月31日 
	 * @param request
	 * @param t
	 */
	FqcInspectionL updateByPrimaryKeySelectiveAttribute(IRequest request, FqcInspectionL t);
}