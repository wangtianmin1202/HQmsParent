package com.hand.spc.ecr_main.service;

import java.util.Date;
import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.ecr_main.dto.EcrProjectTracking;

public interface IEcrProjectTrackingService extends IBaseService<EcrProjectTracking>, ProxySelf<IEcrProjectTrackingService>{

	/**
	 * 查询本表信息
	 * @param request
	 * @param ecrno
	 * @return
	 */
	List<EcrProjectTracking> selectGrid(IRequest requestContext, EcrProjectTracking dto, int page, int pageSize);
	
	/**
	 * 查询项目跟踪信息基本信息（根据ECR编码）
	 * @param request
	 * @param ecrno
	 * @return
	 */
	List<EcrProjectTracking> selectInfo(IRequest request, String ecrno);

    /**
     *- 新建数据-根据ECR编码
     * @param request
     * @param ecrno
     * @return
     */
	List<EcrProjectTracking> insertInfo(IRequest request, String ecrno);

	/**
	 * ECR受影响物料价格趋势查询报表 
	 * @param request
	 * @param skuId
	 * @return
	 */
	List<EcrProjectTracking> selectInfluence(IRequest request, String skuId);

    /**
     * SKU 实际成本计算
     * @param ecrno
     * @param skuId
     * @param request
     * @return
     */
	void calculateActualCost(IRequest requestContext, String ecrno, String skuId);

    /**
     * QTP任务写到QTP任务表
     * @param request
     * @return
     */
	Boolean qtp(IRequest request,String ecrno);

    /**
     * QTP 任务负责人分配
     * @param request
     * @return
     */
	void qtpMainDuty(IRequest request, String ecrno, String itemId);
	

    /**
     * VTP任务写到VTP任务表
     * @param request
     * @return
     */
	Boolean vtp(IRequest request,String ecrno);

    /**
     * VTP 任务负责人分配
     * @param request
     * @return
     */
	void vtpMainDuty(IRequest request, String ecrno, String itemId);


    /**
     * PCI任务写到VTP任务表
     * @param request
     * @return
     */
	Boolean pci(IRequest requestContext, String ecrno);

	/**
	 * 查询项目跟踪的要求完成时间
	 * @param ecrno
	 * @param type
	 * @return
	 */
	Date selectDueDate(String ecrno, String type);

}