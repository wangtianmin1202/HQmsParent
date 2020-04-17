package com.hand.hcs.hcs_station.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.DTOStatus;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hcs.hcs_station.dto.SupplierItemStation;
import com.hand.hcs.hcs_station.mapper.SupplierItemStationMapper;
import com.hand.hcs.hcs_station.service.ISupplierItemStationService;
import com.hand.hcs.hcs_station.view.ItemStationV0;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class SupplierItemStationServiceImpl extends BaseServiceImpl<SupplierItemStation> implements ISupplierItemStationService{

    @Autowired
    private SupplierItemStationMapper stationMapper;

    @Override
    public List<SupplierItemStation> query(IRequest request, SupplierItemStation dto) {
        return stationMapper.headQuery(dto);
    }

    @Override
    public ResponseData inserMuli(IRequest request, List<SupplierItemStation> dtos) {
        ResponseData responseData = new ResponseData();
        if(CollectionUtils.isNotEmpty(dtos)){
            SupplierItemStation dto = dtos.get(0);

            List<ItemStationV0> v0List = dto.getV0List();
            if(CollectionUtils.isNotEmpty(v0List)){
                for (ItemStationV0 stationV0 : v0List) {
                    SupplierItemStation supplierItemStation = new SupplierItemStation();
                    supplierItemStation.setSupplierId(dto.getSupplierId());
                    supplierItemStation.setItemId(dto.getItemId());
                    Long aLong = stationMapper.maxSequence(supplierItemStation);
                    supplierItemStation.setStationSequence(aLong+10L);
                    supplierItemStation.setStationName(stationV0.getStationName());
                    supplierItemStation.setEnableFlag("Y");
                    self().insert(request,supplierItemStation);
                }
            }else {
                dto.setEnableFlag("Y");
                self().insert(request,dto);
            }
        }
        responseData.setMessage("保存成功");
        responseData.setSuccess(true);
        return responseData;
    }

    @Override
    public ResponseData enable(IRequest request, Float id, String flag) {
        ResponseData responseData = new ResponseData();
        stationMapper.enable(id,flag);
        responseData.setMessage("修改成功");
        responseData.setSuccess(true);
        return responseData;
    }

    @Override
    public ResponseData add(IRequest request, SupplierItemStation dto) {
        ResponseData responseData = new ResponseData();
        SupplierItemStation supplierItemStation = new SupplierItemStation();
        supplierItemStation.setSupplierId(dto.getSupplierId());
        supplierItemStation.setItemId(dto.getItemId());
        Long aLong = stationMapper.maxSequence(supplierItemStation);
        dto.setStationSequence(aLong+10L);
        dto.setEnableFlag("Y");
        self().insert(request,dto);
        responseData.setMessage("保存成功");
        responseData.setSuccess(true);
        return responseData;
    }
}