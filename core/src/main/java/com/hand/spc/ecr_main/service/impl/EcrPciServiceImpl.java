package com.hand.spc.ecr_main.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SystemApiMethod;
import com.hand.spc.ecr_main.dto.EcrPci;
import com.hand.spc.ecr_main.mapper.EcrPciMapper;
import com.hand.spc.ecr_main.service.IEcrPciService;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * description
 *
 * @author KOCDZX0 2020/03/11 5:49 PM
 */
@Service
public class EcrPciServiceImpl extends BaseServiceImpl<EcrPci> implements IEcrPciService {

    @Autowired
    private EcrPciMapper pciMapper;

    @Override
    public List<EcrPci> query(IRequest requestCtx, EcrPci dto) {
        return pciMapper.pciQuery(dto);
    }

    @Override
    public ResponseData fileUpload(IRequest requestCtx, HttpServletRequest request) throws IllegalStateException, IOException {
        String pciCode = request.getParameter("pciCode");
        Long pciId = Long.valueOf(request.getParameter("pciId"));
        ResponseData responseData = new ResponseData();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        // 获取文件map集合
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        String rootPath = "/apps/hap/resource";
        String endPath = "/ecr/pci/file_" + pciCode + "/";
        String path = rootPath + endPath;
        if (SystemApiMethod.getOsType().equals("window")) {
            rootPath = "C:/apps/hap/resource";
            endPath = "/ecr/pci/file_" + pciCode + "/";
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
            EcrPci ecrPci = new EcrPci();
            ecrPci.setPciId(pciId);
            ecrPci = self().selectByPrimaryKey(requestCtx, ecrPci);
            if (ecrPci != null && !StringUtils.isNotBlank(ecrPci.getPciAttachment())) {
                forModel.transferTo(file);
                ecrPci.setPciAttachment(endPath + entry.getValue().getOriginalFilename());
                self().updateByPrimaryKeySelective(requestCtx, ecrPci);
            } else {
                continue;
            }
            responseData.setMessage(entry.getValue().getOriginalFilename());
            break;
        }
        return responseData;
    }

    @Override
    public int updateAndDelFile(IRequest requestContext, List<EcrPci> dto) {
        String rootPath = "/apps/hap/resource";
        if (SystemApiMethod.getOsType().equals("window")) {
            rootPath = "C:/apps/hap/resource";
        }
        int i = 0;
        for (EcrPci t : dto) {
            t = self().selectByPrimaryKey(requestContext, t);
            if (t != null) {
                //删除文件
                File file = new File(rootPath + t.getPciAttachment());
                if (file.exists()) {
                    file.delete();
                }
                //清空数据
                t.setPciAttachment("");
                self().updateByPrimaryKeySelective(requestContext, t);
                i++;
            }
        }
        return i;
    }
}
