package com.hand.plm.product_func_attr_basic.mapper;

import com.hand.plm.product_func_attr_basic.view.UpdateDetailVO;
import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.plm.product_func_attr_basic.dto.ProductFuncAttrDetail;
import com.hand.plm.product_func_attr_basic.view.ProductFuncAttrDetailVO;
import org.apache.ibatis.annotations.Param;

public interface ProductFuncAttrDetailMapper extends Mapper<ProductFuncAttrDetail> {
    /**
     * 查询功能产品内容
     * @param dto
     * @return
     */
    List<ProductFuncAttrDetailVO> queryDetail(ProductFuncAttrDetailVO dto);


    /**
     * 查询要修改的功能产品内容
     * @param vo
     * @return
     */
    List<UpdateDetailVO> getUpdateDetailData(UpdateDetailVO vo);
}