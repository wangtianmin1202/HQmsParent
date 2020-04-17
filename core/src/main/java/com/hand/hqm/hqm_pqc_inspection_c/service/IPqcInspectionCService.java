package com.hand.hqm.hqm_pqc_inspection_c.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_iqc_inspection_h.dto.IqcInspectionH;
import com.hand.hqm.hqm_iqc_inspection_l.dto.IqcInspectionL;
import com.hand.hqm.hqm_pqc_inspection_c.dto.PqcInspectionC;
import com.hand.hqm.hqm_pqc_inspection_l.dto.PqcInspectionL;

public interface IPqcInspectionCService extends IBaseService<PqcInspectionC>, ProxySelf<IPqcInspectionCService>{

	/**
	 * 基础查询
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<PqcInspectionC> myselect(IRequest requestContext, PqcInspectionC dto, int page, int pageSize);

	/**
	 * 明细保存
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param requestCtx
	 * @param request
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	List<PqcInspectionC> detailSave(IRequest requestCtx,HttpServletRequest request, List<PqcInspectionC> dto) throws Exception;
	
	
	List<PqcInspectionC> selectfornon(IRequest requestContext, PqcInspectionC dto, int page, int pageSize);
	
	/**
	 * 获取明细
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param requestCtx
	 * @param dtoh
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	List<PqcInspectionC> getDetail(IRequest requestCtx, PqcInspectionL dtoh) throws JsonGenerationException, JsonMappingException, IOException;
	
	/**
	 * 保存明细
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param requestCtx
	 * @param dto
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws Exception
	 */
	void saveDetail(IRequest requestCtx, List<PqcInspectionC> dto) throws JsonParseException, JsonMappingException, IOException, Exception;
	
	/**
	 * 提交
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param requestCtx
	 * @param dto
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws Exception
	 */
	void commitDetail(IRequest requestCtx, List<PqcInspectionC> dto) throws JsonParseException, JsonMappingException, IOException, Exception;
}