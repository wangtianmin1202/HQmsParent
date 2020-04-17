package com.hand.utils.recording_changes_basic_data.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.utils.recording_changes_basic_data.dto.ChangesBasicData;
import com.hand.utils.recording_changes_basic_data.mapper.ChangesBasicDataMapper;
import com.hand.utils.recording_changes_basic_data.service.IChangesBasicDataService;

@Service
@Transactional(rollbackFor = Exception.class)
public class ChangesBasicDataServiceImpl extends BaseServiceImpl<ChangesBasicData> implements IChangesBasicDataService{

	@Autowired
	private ChangesBasicDataMapper changesBasicDataMapper;
	@Override
	public List<ChangesBasicData> selectChangeDatas(ChangesBasicData changesBasicData) {
		return changesBasicDataMapper.selectChangeDatas(changesBasicData);
	}

}