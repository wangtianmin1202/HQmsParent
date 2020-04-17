package com.hand.hqm.hqm_iqc_inspection_l.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_iqc_inspection_h.dto.IqcInspectionH;
import com.hand.hqm.hqm_iqc_inspection_l.dto.IqcInspectionL;

public interface IIqcInspectionLService extends IBaseService<IqcInspectionL>, ProxySelf<IIqcInspectionLService>{

	/**
	 * 
	 * @description 基础查询
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<IqcInspectionL> reSelect(IRequest requestContext, IqcInspectionL dto, int page, int pageSize);
	
	
	List<IqcInspectionL> reSelectFornon(IRequest requestContext, IqcInspectionL dto, int page, int pageSize);
	
	/**
	 * 
	 * @description 获取明细
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param requestCtx
	 * @param dtoh
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	List<IqcInspectionL> getDetail(IRequest requestCtx, IqcInspectionH dtoh) throws JsonGenerationException, JsonMappingException, IOException;
	
	/**
	 * 
	 * @description 保存明细
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param requestCtx
	 * @param dto
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws Exception
	 */
	void saveDetail(IRequest requestCtx,HttpServletRequest request, List<IqcInspectionL> dto) throws JsonParseException, JsonMappingException, IOException, Exception;
	
	
	/**
	 * 
	 * @description 提交明细
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param requestCtx
	 * @param dto
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws Exception
	 */
	ResponseData commitDetail(IRequest requestCtx,HttpServletRequest request, List<IqcInspectionL> dto) throws JsonParseException, JsonMappingException, IOException, Exception;


	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月30日 
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<IqcInspectionL> selectAttribute(IRequest requestContext, IqcInspectionL dto, int page, int pageSize);


	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月30日 
	 * @param requestCtx
	 * @param dto
	 * @return
	 */
	List<IqcInspectionL> batchAttributeUpdate(IRequest requestCtx, List<IqcInspectionL> dto);


	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月30日 
	 * @param dto
	 */
	void batchAttributeDelete(List<IqcInspectionL> dto);


	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月31日 
	 * @param request
	 * @param t
	 */
	IqcInspectionL updateByPrimaryKeySelectiveAttribute(IRequest request, IqcInspectionL t);


	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月31日 
	 * @param request
	 * @param t
	 */
	IqcInspectionL insertSelectiveAttribute(IRequest request, IqcInspectionL t);
	
	

}