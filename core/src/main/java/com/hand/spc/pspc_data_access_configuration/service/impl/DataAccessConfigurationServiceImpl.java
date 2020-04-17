package com.hand.spc.pspc_data_access_configuration.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.pspc_data_access_configuration.dto.DataAccessConfiguration;
import com.hand.spc.pspc_data_access_configuration.mapper.DataAccessConfigurationMapper;
import com.hand.spc.pspc_data_access_configuration.service.IDataAccessConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class DataAccessConfigurationServiceImpl extends BaseServiceImpl<DataAccessConfiguration> implements IDataAccessConfigurationService {

    @Autowired
    private IDataAccessConfigurationService iDataAccessConfigurationService;
    @Autowired
    private DataAccessConfigurationMapper dataAccessConfigurationMapper;

    /**
     * @return com.hand.hap.system.dto.ResponseData
     * @Description 保存
     * @author hch
     * @date 2019/8/7 15:25
     * @Param [requestCtx, dto]
     * @version 1.0
     */
    @Override
    public ResponseData updateAndSubmit(IRequest iRequest, DataAccessConfiguration dto) {
        ResponseData responseData = new ResponseData();

        //判断：如果有头id则存表，没有则更新
        if (dto.getDataAccessConfigurationId() == null) {
            try {
                iDataAccessConfigurationService.insertSelective(iRequest, dto);
                responseData.setSuccess(true);
                responseData.setMessage("保存成功！");
                return responseData;
            } catch (Exception e) {
                responseData.setSuccess(false);
                return responseData;
            }
        } else {
            try {
                iDataAccessConfigurationService.updateByPrimaryKeySelective(iRequest, dto);
                responseData.setSuccess(true);
                responseData.setMessage("保存更新成功！");
                return responseData;
            } catch (Exception e) {
                responseData.setSuccess(false);
                return responseData;
            }
        }
    }

    /**
     * @return com.hand.hap.system.dto.ResponseData
     * @Description 删除
     * @author hch
     * @date 2019/8/7 18:06
     * @Param [dto]
     * @version 1.0
     */
    @Override
    public ResponseData remove(DataAccessConfiguration dto) {
        ResponseData responseData = new ResponseData();
        try {
            mapper.deleteByPrimaryKey(dto);
            responseData.setSuccess(true);
            responseData.setMessage("删除成功！");
            return responseData;
        } catch (Exception e) {
            responseData.setSuccess(false);
            return responseData;
        }
    }

    /**
     * @return java.util.List<demo.pspc_data_access_configuration.dto.DataAccessConfiguration>
     * @Description 查询
     * @author hch
     * @date 2019/8/8 11:26
     * @Param [requestContext, dto, page, pageSize]
     * @version 1.0
     */
    @Override
    public List<DataAccessConfiguration> selectData(IRequest requestContext, DataAccessConfiguration dto, int page, int pageSize) {
        return dataAccessConfigurationMapper.selectData(dto);
    }


    /**
     * @return com.hand.hap.system.dto.ResponseData
     * @Description 校验数据（过滤下限不能超过过滤上限，唯一性校验）
     * @author hch
     * @date 2019/8/28 16:43
     * @Param [requestCtx, DataAccessConfigurationList]
     * @version 1.0
     */
    @Override
    public ResponseData validate(IRequest requestCtx, List<DataAccessConfiguration> DataAccessConfigurationList) {
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(true);
        for (DataAccessConfiguration list : DataAccessConfigurationList) {

            //唯一性校验
            //新增和修改的时候校验数据唯一性
            if ("add".equals(list.get__status())) {
                DataAccessConfiguration dataAccessConfigurationDto = new DataAccessConfiguration();
                dataAccessConfigurationDto.setTenantId(list.getTenantId());
                dataAccessConfigurationDto.setSiteId(list.getSiteId());
                dataAccessConfigurationDto.setCeGroupId(list.getCeGroupId());
                dataAccessConfigurationDto.setAttachmentGroupId(list.getAttachmentGroupId());
                dataAccessConfigurationDto.setCeParameterId(list.getCeParameterId());
                List<DataAccessConfiguration> returnList = this.dataAccessConfigurationMapper.select(dataAccessConfigurationDto);
                if (returnList != null && returnList.size() > 0) {
                    responseData.setSuccess(false);
                    responseData.setMessage("数据重复！");
                    return responseData;
                }
            }

            //校验过滤下限不能超过过滤上限
            if (list.getDataLowerLimit() != null && list.getDataUpperLimit() != null) {
                Long dataLowerLimit = list.getDataLowerLimit();
                Long dataUpperLimit = list.getDataUpperLimit();
                if (dataLowerLimit > dataUpperLimit) {
                    responseData.setSuccess(false);
                    responseData.setMessage("过滤下限不能超过过滤上限,请重新输入再保存！");
                    return responseData;
                }
            }
        }


        return responseData;
    }
}