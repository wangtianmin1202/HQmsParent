package com.hand.hcs.hcs_out_barcode_control.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hcs.hcs_out_barcode_control.dto.OutBarcodeControl;
import com.hand.hcs.hcs_out_barcode_control.service.IOutBarcodeControlService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class OutBarcodeControlServiceImpl extends BaseServiceImpl<OutBarcodeControl> implements IOutBarcodeControlService{

}