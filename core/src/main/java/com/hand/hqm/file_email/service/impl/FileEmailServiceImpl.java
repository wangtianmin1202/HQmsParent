package com.hand.hqm.file_email.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hqm.file_email.dto.FileEmail;
import com.hand.hqm.file_email.mapper.FileEmailMapper;
import com.hand.hqm.file_email.service.IFileEmailService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class FileEmailServiceImpl extends BaseServiceImpl<FileEmail> implements IFileEmailService{

	@Autowired
	private FileEmailMapper fileEmailMapper;
	
	@Override
	public List<FileEmail> queryByCondition(IRequest requestContext, FileEmail dto, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return fileEmailMapper.queryByCondition(dto);
	}

}