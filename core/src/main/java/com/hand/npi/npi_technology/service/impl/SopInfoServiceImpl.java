package com.hand.npi.npi_technology.service.impl;

import com.hand.hap.attachment.dto.SysFile;
import com.hand.hap.attachment.mapper.SysFileMapper;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SystemApiMethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.npi.npi_technology.dto.SopInfo;
import com.hand.npi.npi_technology.mapper.SopInfoMapper;
import com.hand.npi.npi_technology.service.ISopInfoService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class SopInfoServiceImpl extends BaseServiceImpl<SopInfo> implements ISopInfoService{

	@Autowired
	private SysFileMapper sysFileMapper;
	@Autowired
	private SopInfoMapper sopInfoMapper;
	
	@Override
	public String queryFilePath(SopInfo dto) {
		String queryFileId = sopInfoMapper.queryFileId(dto);
		SysFile sysFile = sysFileMapper.selectByPrimaryKey(queryFileId);
		String filePath="";
		if(SystemApiMethod.getOsType().equals("window")) {
			filePath=sysFile.getFilePath().replace("C:/apps/hap/resource", "");
		}else {
			filePath=sysFile.getFilePath().replace("/apps/hap/resource", "");
		}
		return filePath+sysFile.getFileName();
	}

}