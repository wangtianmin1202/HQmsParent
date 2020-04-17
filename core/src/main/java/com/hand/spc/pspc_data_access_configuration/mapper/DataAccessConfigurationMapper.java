package com.hand.spc.pspc_data_access_configuration.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.pspc_data_access_configuration.dto.DataAccessConfiguration;

import java.util.List;

public interface DataAccessConfigurationMapper extends Mapper<DataAccessConfiguration> {

    /**
    * @Description 查询
    * @author hch
    * @date 2019/8/8 11:27
    * @Param []
    * @return java.util.List<demo.pspc_data_access_configuration.dto.DataAccessConfiguration>
    * @version 1.0
    */
    List<DataAccessConfiguration> selectData(DataAccessConfiguration dto);
}