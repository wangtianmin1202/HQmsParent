package com.hand.utils.recording_changes_basic_data.service;

import java.util.List;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.utils.recording_changes_basic_data.dto.ChangesBasicData;

public interface IChangesBasicDataService extends IBaseService<ChangesBasicData>, ProxySelf<IChangesBasicDataService>{
	public List<ChangesBasicData> selectChangeDatas(ChangesBasicData changesBasicData);
}