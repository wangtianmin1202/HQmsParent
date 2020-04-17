package com.hand.hcs.hcs_supply_plan.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hcs.hcs_supply_demand.dto.SupplyDemand;
import com.hand.hcs.hcs_supply_plan.dto.SupplyPlan;

public interface ISupplyPlanService extends IBaseService<SupplyPlan>, ProxySelf<ISupplyPlanService> {

	/**
	 * 
	 * @description jit文件excel导入
	 * @author tianmin.wang
	 * @date 2019年11月13日
	 * @param requestCtx
	 * @param forModel
	 * @return
	 * @throws IOException
	 * @throws RuntimeException
	 */
	List<SupplyDemand> jitExcelImport(IRequest requestCtx, MultipartFile forModel) throws IOException, RuntimeException;

	/**
	 * 
	 * @description st文件excel导入
	 * @author tianmin.wang
	 * @date 2019年11月13日
	 * @param requestCtx
	 * @param forModel
	 * @return
	 * @throws IOException
	 * @throws RuntimeException
	 */
	List<SupplyDemand> stExcelImport(IRequest requestCtx, MultipartFile forModel) throws IOException, RuntimeException;

	/**
	 * 
	 * @description 查询
	 * @author tianmin.wang
	 * @date 2019年11月13日
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<SupplyPlan> reSelect(IRequest requestContext, SupplyPlan dto, int page, int pageSize);

	/**
	 * 
	 * @description 批量更新
	 * @author tianmin.wang
	 * @date 2019年11月13日
	 * @param requestCtx
	 * @param dto
	 * @return
	 */
	List<SupplyPlan> reBatchUpdate(IRequest requestCtx, List<SupplyPlan> dto);

	/**
	 * 批次和行号下对应的最大行分支号
	 * 
	 * @param requestContext
	 * @param dto
	 * @return
	 */
	List<SupplyPlan> maxLineNumSelect(IRequest requestContext, SupplyPlan dto);

	/**
	 * 
	 * @description 生成操作
	 * @author tianmin.wang
	 * @date 2019年11月13日
	 * @param requestCtx
	 * @param list
	 */
	void generate(IRequest requestCtx, List<SupplyPlan> list);

	/**
	 * 
	 * @description 取消操作
	 * @author tianmin.wang
	 * @date 2019年11月13日
	 * @param requestCtx
	 * @param list
	 */
	void cancel(IRequest requestCtx, List<SupplyPlan> list);

	/**
	 * 
	 * @description 提交操作
	 * @author tianmin.wang
	 * @date 2019年11月13日
	 * @param requestCtx
	 * @param list
	 */
	void confirm(IRequest requestCtx, List<SupplyPlan> list);

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年1月15日 
	 * @param supplyPlanNum
	 * @param supplyPlanLineNum
	 * @return
	 */
	Integer getSumSupplierdeliveryQty(String supplyPlanNum, String supplyPlanLineNum);

}