package com.hand.hcs.hcs_certificate_file_manage.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hcs.hcs_certificate_file_manage.dto.Certificate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CertificateMapper extends Mapper<Certificate>{


    List<Certificate> querySupplier(Certificate dto);

    List<Certificate> querySecondCategory(@Param("supplierId") Long supplierId,@Param("firstCategory") String firstCategory);

    List<Certificate> queryFirstCategory(@Param("supplierId") Long supplierId);

    List<Certificate> currentQuery(Certificate dto);

    List<Certificate> queryMuliSupplier(Certificate dto);

    List<Certificate> queryMuliItem(Certificate dto);

    /**
     * 获取研发工程师
     * @param itemId
     * @return
     */
    List<String> getRDengineer(@Param("itemId") Long itemId);

    /**
     * 获取有效sqe人员
     * @param supplierId
     * @return
     */
    List<String> getSQE(@Param("supplierId") Long supplierId);

/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月26日 
	 * @param dto
	 * @return
	 */
	List<Certificate> selectAllRows(Certificate dto);

}