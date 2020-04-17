package com.hand.spc.ecr_main.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SystemApiMethod;
import com.hand.spc.ecr_main.dto.EcrSample;
import com.hand.spc.ecr_main.mapper.EcrSampleMapper;
import com.hand.spc.ecr_main.service.IEcrSampleService;
import com.hand.spc.ecr_main.view.EcrSampleV0;
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
public class EcrSampleServiceImpl extends BaseServiceImpl<EcrSample> implements IEcrSampleService{
	
	
	@Autowired
	private EcrSampleMapper ecrSampleMapper;
	
	public List<EcrSampleV0> baseQuery(EcrSample dto , int page, int pageSize){
		PageHelper.startPage(page, pageSize);
		return ecrSampleMapper.baseQuery(dto);
	}

    @Override
    public ResponseData fileUpload(IRequest requestCtx, HttpServletRequest request) throws IllegalStateException, IOException {
        Float kid = Float.valueOf(request.getParameter("kid"));
        String code = request.getParameter("sampleNumber");
        ResponseData responseData = new ResponseData();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        // 获取文件map集合
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        String rootPath = "/apps/hap/resource";
        String endPath = "/ecr/sample/file_" + code + "/";
        String path = rootPath + endPath;
        if (SystemApiMethod.getOsType().equals("window")) {
            rootPath = "C:/apps/hap/resource";
            endPath = "/ecr/sample/file_" + code + "/";
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
            EcrSample ecrSample = new EcrSample();
            ecrSample.setKid(kid);
            ecrSample = self().selectByPrimaryKey(requestCtx, ecrSample);
            if (ecrSample != null && !StringUtils.isNotBlank(ecrSample.getAttachment())) {
                forModel.transferTo(file);
                ecrSample.setAttachment(endPath + entry.getValue().getOriginalFilename());
                self().updateByPrimaryKeySelective(requestCtx, ecrSample);
            } else {
                continue;
            }
            responseData.setMessage(endPath +entry.getValue().getOriginalFilename());
            break;
        }
        return responseData;
    }

    @Override
    public int updateAndDelFile(IRequest requestContext, List<EcrSample> dto) {
        String rootPath = "/apps/hap/resource";
        if (SystemApiMethod.getOsType().equals("window")) {
            rootPath = "C:/apps/hap/resource";
        }
        int i = 0;
        for (EcrSample t : dto) {
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