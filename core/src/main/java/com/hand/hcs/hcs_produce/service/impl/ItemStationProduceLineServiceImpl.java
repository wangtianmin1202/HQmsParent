package com.hand.hcs.hcs_produce.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.DTOStatus;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SystemApiMethod;
import com.hand.hcs.hcs_produce.dto.ItemStationProduceLine;
import com.hand.hcs.hcs_produce.mapper.ItemStationProduceLineMapper;
import com.hand.hcs.hcs_produce.service.IItemStationProduceLineService;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

@Service
@Transactional(rollbackFor = Exception.class)
public class ItemStationProduceLineServiceImpl extends BaseServiceImpl<ItemStationProduceLine> implements IItemStationProduceLineService{

    @Autowired
    private ItemStationProduceLineMapper lineMapper;

    @Override
    public List<ItemStationProduceLine> listQuery(IRequest request, ItemStationProduceLine dto) {
        return lineMapper.listQuery(dto);
    }

    @Override
    public ResponseData add(IRequest request, ItemStationProduceLine dto) {
        ResponseData responseData = new ResponseData();
        if (DTOStatus.ADD.equals(dto.get__status())) {
            Long maxLineNum = lineMapper.maxLineNum();
            dto.setLineNumber(maxLineNum+1L);
            dto.setStatus("PENDING");
            self().insertSelective(request, dto);
        } else if (DTOStatus.UPDATE.equals(dto.get__status())) {
            self().updateByPrimaryKeySelective(request, dto);
        }
        responseData.setMessage("保存成功");
        responseData.setSuccess(true);
        return responseData;
    }

    @Override
    public ResponseData fileUpload(IRequest requestCtx, HttpServletRequest request) throws IllegalStateException, IOException {
        long millis = System.currentTimeMillis();
        String code = String.valueOf(millis);
        Long produceLineId = Long.valueOf(request.getParameter("produceLineId"));
        ResponseData responseData = new ResponseData();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        // 获取文件map集合
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        String rootPath = "/apps/hap/resource";
        String endPath = "/hcs/produce/file_" + code + "/";
        String path = rootPath + endPath;
        if (SystemApiMethod.getOsType().equals("window")) {
            rootPath = "C:/apps/hap/resource";
            endPath = "/hcs/produce/file_" + code + "/";
            path = rootPath + endPath;
        }
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        List<String> rows = new ArrayList<String>();
        for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
            rows.add(entry.getValue().getOriginalFilename());
            MultipartFile forModel = entry.getValue();
            File file = new File(path, forModel.getOriginalFilename());
            //是否已经存在文件
            ItemStationProduceLine itemStationProduceLine = new ItemStationProduceLine();
            itemStationProduceLine.setProduceLineId(produceLineId);
            itemStationProduceLine = self().selectByPrimaryKey(requestCtx, itemStationProduceLine);
            if (itemStationProduceLine != null && !StringUtils.isNotBlank(itemStationProduceLine.getAttachment())) {
                forModel.transferTo(file);
                itemStationProduceLine.setAttachment(endPath + entry.getValue().getOriginalFilename());
                self().updateByPrimaryKeySelective(requestCtx, itemStationProduceLine);
            } else {
                continue;
            }
            responseData.setMessage(entry.getValue().getOriginalFilename());
            break;
        }
        return responseData;
    }

    @Override
    public int updateAndDelFile(IRequest requestContext, List<ItemStationProduceLine> dto) {
        String rootPath = "/apps/hap/resource";
        if (SystemApiMethod.getOsType().equals("window")) {
            rootPath = "C:/apps/hap/resource";
        }
        int i = 0;
        for (ItemStationProduceLine t : dto) {
            t = self().selectByPrimaryKey(requestContext, t);
            if (t != null) {
                //删除文件
                File file = new File(rootPath + t.getAttachment());
                if (file.exists()) {
                    file.delete();
                }
                //清空数据
                t.setAttachment("");
                self().updateByPrimaryKeySelective(requestContext, t);
                i++;
            }
        }
        return i;
    }
}