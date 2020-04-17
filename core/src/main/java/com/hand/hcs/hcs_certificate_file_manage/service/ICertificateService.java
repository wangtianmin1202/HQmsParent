package com.hand.hcs.hcs_certificate_file_manage.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hcs.hcs_certificate_file_manage.dto.Certificate;
import com.hand.wfl.util.ActException;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.ValidationException;

public interface ICertificateService extends IBaseService<Certificate>, ProxySelf<ICertificateService> {

    /**
     * 查询根节点-供应商
     *
     * @param request
     * @param dto
     * @return
     */
    List<Certificate> querySupplier(IRequest request, Certificate dto);

    /**
     * 树二级节点------一级分类查询
     *
     * @param request
     * @param supplierId 供应商Id
     * @return
     */
    List<Certificate> queryFirstCategory(IRequest request, Long supplierId);

    /**
     * 查询三级节点---证书
     *
     * @param request
     * @param supplierId
     * @param firstCategory
     * @return
     */
    List<Certificate> querySecondCategory(IRequest request, Long supplierId, String firstCategory);


    /**
     * 当前版本证书查询,传入参数为三级节点certificateId
     *
     * @param dto
     * @return
     */
    List<Certificate> currentQuery(Certificate dto);

    /**
     * 物料上传发起
     *
     * @param request
     * @param dto
     * @return
     */
    ResponseData itemStart(IRequest request, Certificate dto);

    /**
     * 供应商上传发起
     *
     * @param request
     * @param dto
     * @return
     */
    ResponseData supplierStart(IRequest request, Certificate dto);

    /**
     * 下拉搜索-供应商
     *
     * @param request
     * @param dto
     * @return
     */
    List<Certificate> queryMuliSupplier(IRequest request, Certificate dto);

    /**
     * 下拉搜索-物料
     *
     * @param request
     * @param dto
     * @return
     */
    List<Certificate> queryMuliItem(IRequest request, Certificate dto);

    /**
     * 物料发起校验
     *
     * @param dto
     * @return 非已审批状态的证书数据
     */
    List<String> validData(Certificate dto);

    /**
     * 校验是否为已审批状态，true则同时插入历史表
     *
     * @param request
     * @param dto
     * @return
     */
    Boolean isHistory(IRequest request, Certificate dto);

    /**
     * 开启审批流程（cer---证书    per---人员认证   ppap---ppap）
     *
     * @param request
     * @param dto
     * @return
     * @throws ValidationException
     */
    List<Certificate> approve(IRequest request, Certificate dto) throws ActException, ValidationException;


    /**
     * @param supplierId
     * @param itemId
     * @description 供应商 物料 开启文件流程
     * @author tianmin.wang
     * @date 2020年3月24日
     */
    void startCertificateWorkFlow(IRequest request, Long supplierId, Long itemId);

    /**
     * @param requestCtx
     * @param dto
     * @return
     * @description
     * @author tianmin.wang
     * @date 2020年3月26日
     */
    List<Certificate> getAllRows(IRequest requestCtx, Certificate dto);

    /**
     * 上传文件
     * @param requestCtx
     * @param request
     * @return
     * @throws IllegalStateException
     * @throws IOException
     * @throws ValidationException
     */
    ResponseData upload(IRequest requestCtx,HttpServletRequest request) throws IllegalStateException, IOException,ValidationException;

    /**
     * 删除文件
     * @param requestContext
     * @param dto
     * @return
     */
    int updateAndDelFile(IRequest requestContext, List<Certificate> dto);

    /**
     * 上传文件并提交审批
     * @param requestCtx
     * @param request
     * @param certificateId
     * @param issueDate
     * @param disabledDate
     * @return
     * @throws IllegalStateException
     * @throws IOException
     * @throws ValidationException
     * @throws ActException
     */
    ResponseData submitUpload(IRequest requestCtx,HttpServletRequest request,Long certificateId,String issueDate,String disabledDate) throws IllegalStateException, IOException, ValidationException, ActException;

    /**
     * 根据id更新单据
     * @param request
     * @param dto
     * @return
     */
    ResponseData update(IRequest request,Certificate dto);
}