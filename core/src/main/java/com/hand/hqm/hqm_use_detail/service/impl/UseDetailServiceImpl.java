package com.hand.hqm.hqm_use_detail.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.hqm.hqm_sample_use.dto.SampleUse;
import com.hand.hqm.hqm_use_detail.dto.UseDetail;
import com.hand.hqm.hqm_use_detail.mapper.UseDetailMapper;
import com.hand.hqm.hqm_use_detail.service.IUseDetailService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class UseDetailServiceImpl extends BaseServiceImpl<UseDetail> implements IUseDetailService{
	 @Autowired
	 UseDetailMapper   useDetailMapper;
	 @Autowired
	 IUseDetailService   UseDetailService ; 
	 /**
	     * 新建行表
	     * @param dto 查询内容
	     * @param request 请求
	     * @return 结果集
	     */
	 public ResponseData addline(UseDetail dto,IRequest requestCtx, HttpServletRequest request) {  
	    	//获取当前的 登记人
		 ResponseData r  =new ResponseData();
		 UseDetail res =new UseDetail();
		 int n = useDetailMapper.selectCount(dto);
		 if(n>0)
		 {
			 r.setSuccess(false);
			 r.setMessage("该样品已存在");
			 return r ;
		 }
		 UseDetailService.insertSelective(requestCtx, dto) ;		 
		 return r;
	 } 
	 /**
	     * 删除
	     * @param dto 查询内容
	     * @return 结果集
	     */
	 @Override
		public void deleteRow(UseDetail dto) {

			// 查询后代所有需要删除的附着对象
		/*	List<PfmeaLevel> delList = new ArrayList<>();
			delList.add(dto);
			queryDownAttachment(delList, Collections.singletonList(dto));
			// 删除
			self().batchDelete(delList);*/
			self().deleteByPrimaryKey(dto);
		}
	 /**
	     * 页面查询
	     * @param dto 查询内容
	     * @param page 页码
	     * @param pageSize 页大小
	     * @param request 请求
	     * @return 结果集
	     */
	 @Override
	    public List<UseDetail> myselect(IRequest requestContext, UseDetail dto, int page, int pageSize) {
	        PageHelper.startPage(page, pageSize);
	        return useDetailMapper.myselect(dto);
		 }
	 /**
	     * 样品查询
	     * @param dto 查询内容
	     * @param page 页码
	     * @param pageSize 页大小
	     * @param request 请求
	     * @return 结果集
	     */
	 @Override
	    public List<UseDetail> selectforsample(IRequest requestContext, UseDetail dto, int page, int pageSize) {
	        PageHelper.startPage(page, pageSize);
	        return useDetailMapper.selectforsample(dto);
		 }
}