package com.hand.hqm.hqm_sample_scheme.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.BaseDTO;
import com.hand.hap.system.dto.DTOStatus;
import com.hand.hap.system.service.IPromptService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SystemApiMethod;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_sample_scheme.dto.SampleScheme;
import com.hand.hqm.hqm_sample_scheme.mapper.SampleSchemeMapper;
import com.hand.hqm.hqm_sample_scheme.service.ISampleSchemeService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class SampleSchemeServiceImpl extends BaseServiceImpl<SampleScheme> implements ISampleSchemeService {

	@Autowired
	SampleSchemeMapper mapper;
	@Autowired
	IPromptService iPromptService;

	/**
	 * 提交
	 * 
	 * @param dto     操作数据集
	 * @param result  结果参数
	 * @param request 请求
	 * @return 操作结果
	 */
	@Override
	public List<SampleScheme> rebatchUpdate(IRequest request, List<SampleScheme> list) {
		// TODO Auto-generated method stub
		for (SampleScheme t : list) {
			if (t.get__status() == null) {
				if (t.getSchemeId() == null) {
					self().insertSelective(request, t);
				} else {
					if (useSelectiveUpdate()) {
						self().updateByPrimaryKeySelective(request, t);
					} else {
						self().updateByPrimaryKey(request, t);
					}
				}
			} else {
				switch (t.get__status()) {
				case DTOStatus.ADD:
					self().insertSelective(request, t);
					break;
				case DTOStatus.UPDATE:
					if (useSelectiveUpdate()) {
						self().updateByPrimaryKeySelective(request, t);
					} else {
						self().updateByPrimaryKey(request, t);
					}
					break;
				case DTOStatus.DELETE:
					self().deleteByPrimaryKey(t);
					break;
				default:
					break;
				}
			}
		}

		list.forEach(p -> {
			if (p.getSampleProcedureType().equals("3")) {
				SampleScheme search = new SampleScheme();
				search.setSampleProcedureType("3");
				search.setSamplePlanType(p.getSamplePlanType());
				search.setAttribute3(p.getAttribute3());
				List<SampleScheme> re = mapper.select(search);
				if (re != null && re.size() > 1) {
					throw new RuntimeException(SystemApiMethod.getPromptDescription(request, iPromptService,
							"samples_cheme_update_error1"));
				}
			}
		});

		return list;
	}

	/**
	 * 页面查询
	 * 
	 * @param dto      查询内容
	 * @param page     页码
	 * @param pageSize 页大小
	 * @param request  请求
	 * @return 结果集
	 */
	@Override
	public List<SampleScheme> reSelect(IRequest requestContext, SampleScheme dto, int page, int pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, pageSize);
		return mapper.reSelect(dto);
	}

}