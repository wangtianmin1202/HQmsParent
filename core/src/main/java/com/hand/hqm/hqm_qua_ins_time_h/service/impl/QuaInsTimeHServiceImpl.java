package com.hand.hqm.hqm_qua_ins_time_h.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_qua_ins_time_h.dto.QuaInsTimeH;
import com.hand.hqm.hqm_qua_ins_time_h.mapper.QuaInsTimeHMapper;
import com.hand.hqm.hqm_qua_ins_time_h.service.IQuaInsTimeHService;


import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class QuaInsTimeHServiceImpl extends BaseServiceImpl<QuaInsTimeH> implements IQuaInsTimeHService{
	 @Autowired
	 QuaInsTimeHMapper quaInsTimeHMapper;
	 @Autowired
	 IQuaInsTimeHService QuaInsTimeHServic;
	
	 /**
	     * 头表数据查询
	     * @param dto 查询内容
	     * @param page 页码
	     * @param pageSize 页大小
	     * @param request 请求
	     * @return 结果集
	     */
	@Override
    public List<QuaInsTimeH> myselect(IRequest requestContext, QuaInsTimeH dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return quaInsTimeHMapper.myselect(dto);
 
	 }
}