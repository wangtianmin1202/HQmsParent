package com.hand.hcs.hcs_certificate_file_manage.service.impl;


import com.hand.hap.activiti.service.IActivitiService;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.system.dto.DTOStatus;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SystemApiMethod;
import com.hand.hcs.hcs_certificate_file_manage.dto.Certificate;
import com.hand.hcs.hcs_certificate_file_manage.dto.CertificateHistory;
import com.hand.hcs.hcs_certificate_file_manage.mapper.CertificateHistoryMapper;
import com.hand.hcs.hcs_certificate_file_manage.mapper.CertificateMapper;
import com.hand.hcs.hcs_certificate_file_manage.service.ICerApproval;
import com.hand.hcs.hcs_certificate_file_manage.service.ICertificateService;
import com.hand.hcs.hcs_certificate_file_manage.service.IPerApproval;
import com.hand.hcs.hcs_certificate_file_manage.service.IPpapApproval;
import com.hand.wfl.util.ActException;
import com.hand.wfl.util.ActivitiConstants;
import com.hand.wfl.util.ActivitiUtil;
import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.rest.service.api.engine.variable.RestVariable;
import org.activiti.rest.service.api.runtime.process.ProcessInstanceCreateRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.ValidationException;

@Service(value = "CertificateServiceImpl")
@Transactional(rollbackFor = Exception.class)
public class CertificateServiceImpl extends BaseServiceImpl<Certificate> implements ICertificateService, IPpapApproval, IPerApproval, ICerApproval {

    @Autowired
    private CertificateMapper certificateMapper;

    @Autowired
    private CertificateHistoryMapper historyMapper;


    @Autowired
    private IActivitiService activitiService;

    private Logger logger = LoggerFactory.getLogger(CertificateServiceImpl.class);

    @Override
    public List<Certificate> querySupplier(IRequest request, Certificate dto) {
        return certificateMapper.querySupplier(dto);
    }

    @Override
    public List<Certificate> queryFirstCategory(IRequest request, Long supplierId) {
        return certificateMapper.queryFirstCategory(supplierId);
    }

    @Override
    public List<Certificate> querySecondCategory(IRequest request, Long supplierId, String firstCategory) {
        return certificateMapper.querySecondCategory(supplierId, firstCategory);
    }

    @Override
    public List<Certificate> currentQuery(Certificate dto) {
        return certificateMapper.currentQuery(dto);
    }

    @Override
    public ResponseData itemStart(IRequest request, Certificate dto) {
        ResponseData responseData = new ResponseData();
        if (DTOStatus.ADD.equals(dto.get__status())) {
            //二级分类为证书时,前端复选框返回多个值时，每个值插入一条数据
            String secondCategory = dto.getSecondCategory();
            String secondCategory1 = dto.getSecondCategory1();
            String secondCategory2 = dto.getSecondCategory2();
            String secondCategory3 = dto.getSecondCategory3();
            String secondCategory4 = dto.getSecondCategory4();

            Certificate setCerCategory = setCerCategory(secondCategory, dto);
            if (isHistory(request, setCerCategory)) {
                self().insertSelective(request, setCerCategory);
            }

            Certificate setCerCategory1 = setCerCategory(secondCategory1, dto);
            if (isHistory(request, setCerCategory1)) {
                self().insertSelective(request, setCerCategory1);
            }

            Certificate setCerCategory2 = setCerCategory(secondCategory2, dto);
            if (isHistory(request, setCerCategory2)) {
                self().insertSelective(request, setCerCategory2);
            }

            Certificate setCerCategory3 = setCerCategory(secondCategory3, dto);
            if (isHistory(request, setCerCategory3)) {
                self().insertSelective(request, setCerCategory3);
            }

            Certificate setCerCategory4 = setCerCategory(secondCategory4, dto);
            if (isHistory(request, setCerCategory4)) {
                self().insertSelective(request, setCerCategory4);
            }


            //二级分类为PPAP时
            //PPAP级别
            String levels = dto.getLevels();
            //PPAP版本
            String value = dto.getTypeValue();
            if (StringUtils.isNotBlank(levels) && StringUtils.isNotBlank(value)) {
                Certificate certificate = new Certificate();
                certificate.setSupplierId(dto.getSupplierId());
                certificate.setFirstCategory(dto.getItemCode());
                certificate.setPlantId(dto.getPlantId());
                certificate.setItemId(dto.getItemId());
                certificate.setCertificateType("PPAP");
                certificate.setSecondCategory("PPAP");
                certificate.setApprovalStatus("U");

                List<Certificate> certificates = certificateMapper.select(certificate);
                //若存在PPAP类型的数据则更新相应值
                if (CollectionUtils.isNotEmpty(certificates)) {
                    certificate.setLevels(levels);
                    certificate.setTypeValue(value);
                    //获取已存在PPAP类型证书的id
                    for (Certificate certificate1 : certificates) {
                        certificate.setCertificateId(certificate1.getCertificateId());
                        //如果为已审批状态的证书，更新数据之后，再插入历史表
                        if (isHistory(request, certificate)) {
                            //非审批状态的直接更新值
                            self().updateByPrimaryKey(request, certificate);
                        }
                    }
                } else {
                    //若不存在则直接插入新数据
                    certificate.setTypeValue(value);
                    certificate.setLevels(levels);
                    self().insertSelective(request, certificate);
                }
            }


            //二级分类为人员认证时
            Certificate setPerCategory = setPerCategory(dto.getSecondCategory5(), dto);
            if (isHistory(request, setPerCategory)) {
                self().insert(request, setPerCategory);
            }
            Certificate setPerCategory1 = setPerCategory(dto.getSecondCategory6(), dto);
            if (isHistory(request, setPerCategory1)) {
                self().insert(request, setPerCategory1);
            }

        }
        startCertificateWorkFlow(request, dto.getSupplierId(), dto.getItemId());
        responseData.setMessage("保存成功");
        responseData.setSuccess(true);
        return responseData;
    }

    /**
     * 二级分类是证书时初始化值
     *
     * @param category
     * @param dto
     * @return
     */
    private Certificate setCerCategory(String category, Certificate dto) {
        Certificate certificate = new Certificate();
        if (StringUtils.isNotBlank(category)) {
            certificate.setSupplierId(dto.getSupplierId());
            certificate.setItemId(dto.getItemId());
            certificate.setPlantId(dto.getPlantId());
            certificate.setCertificateType("CER");
            certificate.setFirstCategory(dto.getItemCode());
            certificate.setApprovalStatus("U");
            certificate.setSecondCategory(category);
        }
        return certificate;
    }

    /**
     * 二级分类是人员认证时初始化值
     *
     * @param category
     * @param dto
     * @return
     */
    private Certificate setPerCategory(String category, Certificate dto) {
        Certificate certificate = new Certificate();
        if (StringUtils.isNotBlank(category)) {
            certificate.setSupplierId(dto.getSupplierId());
            certificate.setPlantId(dto.getPlantId());
            certificate.setFirstCategory(dto.getItemCode());
            certificate.setSecondCategory("PER");
            certificate.setCertificateType("PER");
            certificate.setApprovalStatus("U");
            certificate.setTypeValue(category);
        }
        return certificate;
    }

    @Override
    public ResponseData supplierStart(IRequest request, Certificate dto) {
        ResponseData responseData = new ResponseData();
        if (DTOStatus.ADD.equals(dto.get__status())) {
            dto.setFirstCategory("SYS");
            dto.setCertificateType("CER");
            dto.setApprovalStatus("U");
            self().insertSelective(request, dto);
        }
        startCertificateWorkFlow(request, dto.getSupplierId(), null);
        responseData.setMessage("保存成功");
        responseData.setSuccess(true);
        return responseData;
    }

    @Override
    public List<Certificate> queryMuliSupplier(IRequest request, Certificate dto) {
        return certificateMapper.queryMuliSupplier(dto);
    }

    @Override
    public List<Certificate> queryMuliItem(IRequest request, Certificate dto) {
        return certificateMapper.queryMuliItem(dto);
    }

    @Override
    public List<String> validData(Certificate dto) {
        Long supplierId = dto.getSupplierId();
        Long itemId = dto.getItemId();
        Float plantId = dto.getPlantId();

        Certificate certificate = new Certificate();
        certificate.setSupplierId(supplierId);
        certificate.setPlantId(plantId);
        certificate.setItemId(itemId);

        //根据供应商id和物料id查询证书数据
        List<Certificate> select = certificateMapper.select(certificate);
        List<String> list = new ArrayList<>(5);
        //查询出不是已审批状态的证书
        for (Certificate certificate1 : select) {
            String category = null;
            if (!"A".equals(certificate1.getApprovalStatus())) {
                category = certificate1.getSecondCategory();
            }
            if (StringUtils.isNotBlank(category)) {
                list.add(category);
            }
        }
        return list;
    }

    @Override
    public Boolean isHistory(IRequest request, Certificate dto) {
        //查询是否有已审批状态的相同类型证书
        dto.setApprovalStatus("A");

        List<Certificate> select = certificateMapper.select(dto);
        if (CollectionUtils.isNotEmpty(select)) {
            for (Certificate certificate : select) {
                //如果存在已审批状态的相同类型证书，则更新证书表的颁发日期、失效日期、附件地址为空

                certificate.setStartDate(null);
                certificate.setEndDate(null);
                certificate.setAttachment("");
                self().updateByPrimaryKey(request, certificate);

                //并同时插入到历史表
                CertificateHistory history = new CertificateHistory();
                history.setCertificateId(certificate.getCertificateId());
                history.setAttachment(certificate.getAttachment());
                history.setStartDate(certificate.getStartDate());
                history.setEndDate(certificate.getEndDate());
                history.setStatus(certificate.getApprovalStatus());
                history.setSubmitName(certificate.getCreatedBy());
                history.setSubmitDate(certificate.getLastUpdateDate());
                history.setPpapLevels(certificate.getLevels());
                history.setPpapVersion(certificate.getTypeValue());
                historyMapper.insert(history);


            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public List<Certificate> approve(IRequest request, Certificate dto) throws ActException, ValidationException {
        if (dto.getCertificateId() == null) {
            throw new ValidationException("Document is not found");
        }

        String key = null;
        String desc = null;
        try {
            ProcessInstanceCreateRequest processInstanceCreateRequest = new ProcessInstanceCreateRequest();
            if("PPAP".equals(dto.getCertificateType())){
                //ppap审批
                processInstanceCreateRequest.setProcessDefinitionKey(ActivitiConstants.CERFICATE_WORKFLOW_PPAP);
                key = ActivitiConstants.CERFICATE_WORKFLOW_PPAP;
                desc="供应商-PPAP审批";
            }else if("PER".equals(dto.getCertificateType())){
                //人员认证审批
                processInstanceCreateRequest.setProcessDefinitionKey(ActivitiConstants.CERFICATE_WORKFLOW_PER);
                key = ActivitiConstants.CERFICATE_WORKFLOW_PER;
                desc="供应商-人员认证审批";
            }else if("CER".equals(dto.getCertificateType())){
                //证书审批
                processInstanceCreateRequest.setProcessDefinitionKey(ActivitiConstants.CERFICATE_WORKFLOW_CER);
                key = ActivitiConstants.CERFICATE_WORKFLOW_CER;
                desc="供应商-证书审批";
            }
            //传入单据id和类型，类型用于判断前段显示PPAP（PPAP）、人员认证(PER)、证书(CER)
            processInstanceCreateRequest.setBusinessKey(dto.getCertificateId().toString()+":"+dto.getCertificateType());

            List<RestVariable> restVariables = new ArrayList<>();
            //构建工作流参数
            restVariables.add(ActivitiUtil.getRestVariable(Certificate.FIELD_CERTIFICATE_ID, dto.getCertificateId().toString()));
            restVariables.add(ActivitiUtil.getRestVariable(Certificate.FIELD_CERTIFICATE_TYPE, dto.getCertificateType()));
            restVariables.add(ActivitiUtil.getRestVariable(Certificate.FIELD_FIRST_CATEGORY, dto.getFirstCategory()));
            restVariables.add(ActivitiUtil.getRestVariable(Certificate.FIELD_SECOND_CATEGORY, dto.getSecondCategory()));
            //流程描述
            restVariables.add(ActivitiUtil.getRestVariable("DESC", desc));
            restVariables.add(ActivitiUtil.getRestVariable("beanName", "CertificateServiceImpl"));

            processInstanceCreateRequest.setVariables(restVariables);

            //设置单据为审批中状态
            Certificate certificate = certificateMapper.selectByPrimaryKey(dto.getCertificateId());
            certificate.setApprovalStatus("S");
            self().updateByPrimaryKey(request, certificate);

            activitiService.startProcess(request, processInstanceCreateRequest);
        } catch (ActivitiIllegalArgumentException e) {
            logger.error(e.getClass().getName(), e);
            throw new ActException("KEY为" + key + "的工作流没有定义或发布！");
        }
        return Stream.of(dto).collect(Collectors.toList());
    }

    @Override
    public void startCertificateWorkFlow(IRequest request, Long supplierId, Long itemId) {
        // itemId的存在性决定供应商上传还是物料上传的两种类型
        ProcessInstanceCreateRequest instanceCreateRequest = new ProcessInstanceCreateRequest();
        instanceCreateRequest.setBusinessKey(String.valueOf(supplierId) + "-" + itemId == null ? "null"
                : String.valueOf(itemId.intValue()));
        instanceCreateRequest.setProcessDefinitionKey("SupplierFileUpLoad");
        // 添加流程变量
        List<RestVariable> variables = new ArrayList<>();
        RestVariable variableSupplierId = new RestVariable();
        variableSupplierId.setName("supplierId");
        variableSupplierId.setType("string");
        variableSupplierId.setValue(String.valueOf(supplierId.intValue()));
        variables.add(variableSupplierId);
        RestVariable variableItemId = new RestVariable();
        variableItemId.setName("itemId");
        variableItemId.setType("string");
        variableItemId.setValue(String.valueOf(itemId.intValue()));
        variables.add(variableItemId);
        instanceCreateRequest.setVariables(variables);
        activitiService.startProcess(request, instanceCreateRequest);
    }

	/* (non-Javadoc)
	 * @see com.hand.hcs.hcs_certificate_file_manage.service.ICertificateService#getAllRows(com.hand.hap.core.IRequest, com.hand.hcs.hcs_certificate_file_manage.dto.Certificate)
	 */
	@Override
	public List<Certificate> getAllRows(IRequest requestCtx, Certificate dto) {
		// TODO Auto-generated method stub
		return certificateMapper.selectAllRows(dto);
	}

    /**
     * ppap审批结果处理
     * @param id
     * @param certificateType
     * @param approveResult
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void ppapApproveResult(Long id, String certificateType, String approveResult) {
        Certificate certificate = certificateMapper.selectByPrimaryKey(id);
        if("APPROVED".equals(approveResult)){
            certificate.setApprovalStatus("A");
        }else if("REJECTED".equals(approveResult)){
            certificate.setApprovalStatus("R");
        }
        IRequest request = RequestHelper.newEmptyRequest();
        this.self().updateByPrimaryKeySelective(request, certificate);
    }

    /**
     * 人员认证审批结果处理
     * @param id
     * @param certificateType
     * @param approveResult
     * @throws ActException
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void perApproveResult(Long id, String certificateType, String approveResult) throws ActException {
        Certificate certificate = certificateMapper.selectByPrimaryKey(id);
        if("APPROVED".equals(approveResult)){
            certificate.setApprovalStatus("A");
        }else if("REJECTED".equals(approveResult)){
            certificate.setApprovalStatus("R");
        }
        IRequest request = RequestHelper.newEmptyRequest();
        this.self().updateByPrimaryKeySelective(request, certificate);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void cerApproveResult(Long id, String certificateType, String approveResult) throws ActException {
        Certificate certificate = certificateMapper.selectByPrimaryKey(id);
        if("APPROVED".equals(approveResult)){
            certificate.setApprovalStatus("A");
        }else if("REJECTED".equals(approveResult)){
            certificate.setApprovalStatus("R");
        }
        IRequest request = RequestHelper.newEmptyRequest();
        this.self().updateByPrimaryKeySelective(request, certificate);
    }

    @Override
    public ResponseData upload(IRequest requestCtx,HttpServletRequest request) throws IllegalStateException, IOException,ValidationException {
        /*1.先上传文件*/
        String fileCode = String.valueOf(System.currentTimeMillis()) ;
        Long certificateId = Long.valueOf(request.getParameter("certificateId"));
        ResponseData responseData = new ResponseData();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        // 获取文件map集合
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        String rootPath = "/apps/hap/resource";
        String endPath = "/suppliers/approve/file_" + fileCode + "/";
        String path = rootPath + endPath;
        if (SystemApiMethod.getOsType().equals("window")) {
            rootPath = "C:/apps/hap/resource";
            endPath = "/suppliers/approve/file_" + fileCode + "/";
            path = rootPath + endPath;
        }
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        List<String> rows = new ArrayList<String>();
        Certificate certificate = new Certificate();
        certificate.setCertificateId(certificateId);
        certificate = self().selectByPrimaryKey(requestCtx, certificate);
        for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
            rows.add(entry.getValue().getOriginalFilename());
            MultipartFile forModel = entry.getValue();
            File file = new File(path, forModel.getOriginalFilename());
            //是否已经存在文件，存在文件则更新文件
            if (certificate != null && !net.logstash.logback.encoder.org.apache.commons.lang.StringUtils.isNotBlank(certificate.getAttachment())) {
                forModel.transferTo(file);
                certificate.setAttachment(endPath + entry.getValue().getOriginalFilename());
                self().updateByPrimaryKeySelective(requestCtx, certificate);
            } else {
                continue;
            }
            responseData.setMessage(entry.getValue().getOriginalFilename());
            break;
        }

        responseData.setMessage("提交成功");
        responseData.setSuccess(true);
        return responseData;
    }

    @Override
    public ResponseData submitUpload(IRequest requestCtx, HttpServletRequest request, Long certificateId, String issueDate, String disabledDate) throws IllegalStateException, IOException, ValidationException, ActException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date formatIssueDate = null;
        Date formatDisabledDate=null;
        try {
             formatIssueDate = sdf.parse(issueDate);
             formatDisabledDate = sdf.parse(disabledDate);
        } catch (ParseException e) {
            logger.error("日期转化错误");
        }

        /*1.先上传文件*/
        String fileCode = String.valueOf(System.currentTimeMillis()) ;
        ResponseData responseData = new ResponseData();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        // 获取文件map集合
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        if(fileMap.isEmpty()){
            responseData.setMessage("请先上传文件！");
            responseData.setSuccess(false);
            return responseData;
        }
        String rootPath = "/apps/hap/resource";
        String endPath = "/suppliers/approve/file_" + fileCode + "/";
        String path = rootPath + endPath;
        if (SystemApiMethod.getOsType().equals("window")) {
            rootPath = "C:/apps/hap/resource";
            endPath = "/suppliers/approve/file_" + fileCode + "/";
            path = rootPath + endPath;
        }
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        List<String> rows = new ArrayList<String>();
        Certificate certificate = new Certificate();
        certificate.setCertificateId(certificateId);
        certificate = self().selectByPrimaryKey(requestCtx, certificate);
        for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
            rows.add(entry.getValue().getOriginalFilename());
            MultipartFile forModel = entry.getValue();
            File file = new File(path, forModel.getOriginalFilename());
            //是否已经存在文件，存在文件则更新文件
            if (certificate != null && !net.logstash.logback.encoder.org.apache.commons.lang.StringUtils.isNotBlank(certificate.getAttachment())) {
                forModel.transferTo(file);
                certificate.setAttachment(endPath + entry.getValue().getOriginalFilename());
                certificate.setStartDate(formatIssueDate);
                certificate.setEndDate(formatDisabledDate);
                self().updateByPrimaryKeySelective(requestCtx, certificate);
            } else {
                continue;
            }
            responseData.setMessage(entry.getValue().getOriginalFilename());
            break;
        }

        /*2、发起工作流*/
        self().approve(requestCtx,certificate);

        responseData.setMessage("提交成功");
        responseData.setSuccess(true);
        return responseData;
    }

    @Override
    public ResponseData update(IRequest request, Certificate dto) {
        ResponseData responseData = new ResponseData();
        Certificate certificate = certificateMapper.selectByPrimaryKey(dto.getCertificateId());
        certificate.setStartDate(dto.getStartDate());
        certificate.setEndDate(dto.getEndDate());
        self().updateByPrimaryKey(request,certificate);
        responseData.setMessage("修改成功");
        responseData.setSuccess(true);
        return responseData;
    }

    @Override
    public int updateAndDelFile(IRequest requestContext, List<Certificate> dto) {
        String rootPath = "/apps/hap/resource";
        if (SystemApiMethod.getOsType().equals("window")) {
            rootPath = "C:/apps/hap/resource";
        }
        int i = 0;
        for (Certificate t : dto) {
            t = self().selectByPrimaryKey(requestContext, t);
            if (t != null) {
                //删除文件
                File file = new File(rootPath + t.getAttachment());
                if (file.exists()) {
                    file.delete();
                }
                //清空数据
                t.setAttachment("");
                self().updateByPrimaryKeySelective(requestContext, t);
                i++;
            }
        }
        return i;
    }
}