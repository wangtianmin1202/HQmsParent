package com.hand.hcm.hcm_operation_sequences.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hcm.hcm_operation_sequences.dto.OperationSequences;
import com.hand.hcm.hcm_operation_sequences.service.IOperationSequencesService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class OperationSequencesServiceImpl extends BaseServiceImpl<OperationSequences> implements IOperationSequencesService{

}