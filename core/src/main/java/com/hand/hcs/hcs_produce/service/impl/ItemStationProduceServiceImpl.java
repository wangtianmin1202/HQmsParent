package com.hand.hcs.hcs_produce.service.impl;

import com.hand.hap.activiti.service.IActivitiService;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.system.dto.DTOStatus;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hcs.hcs_certificate_file_manage.service.impl.CertificateServiceImpl;
import com.hand.hcs.hcs_produce.dto.ItemStationProduce;
import com.hand.hcs.hcs_produce.dto.ItemStationProduceLine;
import com.hand.hcs.hcs_produce.mapper.ItemStationProduceLineMapper;
import com.hand.hcs.hcs_produce.mapper.ItemStationProduceMapper;
import com.hand.hcs.hcs_produce.service.IItemStationProduceService;
import com.hand.hcs.hcs_produce.service.IProduceApproval;
import com.hand.wfl.util.ActException;
import com.hand.wfl.util.ActivitiConstants;
import com.hand.wfl.util.ActivitiUtil;
import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.rest.service.api.engine.variable.RestVariable;
import org.activiti.rest.service.api.runtime.process.ProcessInstanceCreateRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.xml.bind.ValidationException;

@Service(value = "ItemStationProduceServiceImpl")
@Transactional(rollbackFor = Exception.class)
public class ItemStationProduceServiceImpl extends BaseServiceImpl<ItemStationProduce> implements IItemStationProduceService, IProduceApproval {

    @Autowired
    private ItemStationProduceMapper produceMapper;

    @Autowired
    private ItemStationProduceLineMapper lineMapper;

    @Autowired
    private IActivitiService activitiService;

    private Logger logger = LoggerFactory.getLogger(CertificateServiceImpl.class);

    @Override
    public List<ItemStationProduce> listQuery(IRequest request, ItemStationProduce dto) {
        return produceMapper.listQuery(dto);
    }

    @Override
    public ResponseData insertMutiData(IRequest request, List<ItemStationProduce> dtos) {
        ResponseData responseData = new ResponseData();
        for (ItemStationProduce produce : dtos) {
            //校验制程编号
            ItemStationProduce stationProduce = new ItemStationProduce();
            stationProduce.setProduceNumber(produce.getProduceNumber());
            List<ItemStationProduce> itemStationProduces = produceMapper.select(stationProduce);
            if(CollectionUtils.isNotEmpty(itemStationProduces)){
                responseData.setMessage("该制程编号已存在，请重新输入！");
                responseData.setSuccess(false);
                return responseData;
            }

            produce.setStatus("N");
            produceMapper.insertSelective(produce);
            Long produceId = produce.getProduceId();
            if(Objects.isNull(produceId)){
                responseData.setMessage("头信息不存在，无法保存！");
                responseData.setSuccess(false);
                return responseData;
            }
            List<ItemStationProduceLine> produceLineList = produce.getLineList();
            if (CollectionUtils.isNotEmpty(produceLineList)) {
                for (ItemStationProduceLine line : produceLineList) {
                    //校验不合格项目
                    ItemStationProduceLine produceLine = new ItemStationProduceLine();
                    produceLine.setNgProject(line.getNgProject());
                    produceLine.setProduceId(produceId);
                    List<ItemStationProduceLine> select = lineMapper.select(produceLine);
                    if(CollectionUtils.isNotEmpty(select)){
                        responseData.setMessage("该不合格项目已存在，请重新输入！");
                        responseData.setSuccess(false);
                        return responseData;
                    }
                    line.setProduceId(produceId);
                    line.setStatus("PENDING");
                    lineMapper.insertSelective(line);
                }
            }
        }
        responseData.setRows(dtos);
        responseData.setMessage("保存成功");
        responseData.setSuccess(true);
        return responseData;
    }

    @Override
    public ResponseData add(IRequest request, ItemStationProduce dto) {
        ResponseData responseData = new ResponseData();
        if (DTOStatus.ADD.equals(dto.get__status())) {
            self().insertSelective(request, dto);
        } else if (DTOStatus.UPDATE.equals(dto.get__status())) {
            self().updateByPrimaryKeySelective(request, dto);
        }
        responseData.setMessage("保存成功");
        responseData.setSuccess(true);
        return responseData;

    }

    @Override
    public List<ItemStationProduce> approve(IRequest request, ItemStationProduce dto) throws ActException, ValidationException {
        if (Objects.isNull(dto.getProduceId())) {
            throw new ValidationException("Document is not found");
        }

        try {
            ProcessInstanceCreateRequest processInstanceCreateRequest = new ProcessInstanceCreateRequest();
            processInstanceCreateRequest.setProcessDefinitionKey(ActivitiConstants.SUPPLIER_WORKFLOW_PRODUCE);


            processInstanceCreateRequest.setBusinessKey(dto.getProduceId().toString()+":"+dto.getProduceNumber());

            List<RestVariable> restVariables = new ArrayList<>();
            //构建工作流参数
            restVariables.add(ActivitiUtil.getRestVariable(ItemStationProduce.FIELD_PRODUCE_ID, dto.getProduceId().toString()));
            restVariables.add(ActivitiUtil.getRestVariable(ItemStationProduce.FIELD_PRODUCE_NUMBER, dto.getProduceNumber()));
            restVariables.add(ActivitiUtil.getRestVariable("DESC", "供应商制程监控"+"-"+dto.getProduceNumber()));
            restVariables.add(ActivitiUtil.getRestVariable("beanName", "ItemStationProduceServiceImpl"));

            processInstanceCreateRequest.setVariables(restVariables);

            //设置单据为审批中状态
            ItemStationProduce produce = produceMapper.selectByPrimaryKey(dto.getProduceId());
            produce.setStatus("P");
            self().updateByPrimaryKey(request, produce);

            activitiService.startProcess(request, processInstanceCreateRequest);
        } catch (ActivitiIllegalArgumentException e) {
            logger.error(e.getClass().getName(), e);
            throw new ActException("KEY为" + ActivitiConstants.SUPPLIER_WORKFLOW_PRODUCE + "的工作流没有定义或发布！");
        }
        return Stream.of(dto).collect(Collectors.toList());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void produceApproveResult(Long id, String approveResult) throws ActException {
        ItemStationProduce stationProduce = produceMapper.selectByPrimaryKey(id);
        if(Objects.nonNull(stationProduce)){
            if("APPROVED".equals(approveResult)){
                stationProduce.setStatus("A");
            }else if("REJECTED".equals(approveResult)){
                stationProduce.setStatus("S");
            }
            IRequest request = RequestHelper.newEmptyRequest();
            self().updateByPrimaryKey(request,stationProduce);
        }
    }
}