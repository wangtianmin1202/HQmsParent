package com.hand.utils.recording_changes_basic_data.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.utils.recording_changes_basic_data.dto.ChangesBasicData;

public interface ChangesBasicDataMapper extends Mapper<ChangesBasicData>{
	public List<ChangesBasicData> selectChangeDatas(ChangesBasicData changesBasicData);
}