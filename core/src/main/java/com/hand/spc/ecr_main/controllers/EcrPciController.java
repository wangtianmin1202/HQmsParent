package com.hand.spc.ecr_main.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.ecr_main.dto.EcrPci;
import com.hand.spc.ecr_main.service.IEcrPciService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 * description
 *
 * @author KOCDZX0 2020/03/11 5:49 PM
 */
@Controller
public class EcrPciController extends BaseController {

    @Autowired
    private IEcrPciService pciService;

    private static final String PCI_COMPLETED_STATUS = "COMPLETED";

    @RequestMapping(value = "/hpm/ecr/pci/query")
    @ResponseBody
    public ResponseData query(EcrPci dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(pciService.query(requestContext, dto));
    }


    @RequestMapping(value = "/hpm/ecr/pci/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<EcrPci> dto, BindingResult result, HttpServletRequest request) {
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        dto.forEach(ecrPci->{
            //默认负责人为当前创建人
            ecrPci.setCreatedBy(requestCtx.getUserId());
            //若状态为已完成，完成时间为保存记录的时间
            if(PCI_COMPLETED_STATUS.equals(ecrPci.getStatus())){
                ecrPci.setActFinishedDate(new Date());
            }
        });
        return new ResponseData(pciService.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hpm/ecr/pci/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<EcrPci> dto) {
        pciService.batchDelete(dto);
        return new ResponseData();
    }

    @RequestMapping(value = "/hpm/ecr/pci/upload")
    @ResponseBody
    public ResponseData fileUpload(HttpServletRequest request) {
        ResponseData responseData = new ResponseData();
        IRequest requestCtx = createRequestContext(request);
        try {
            responseData = pciService.fileUpload(requestCtx, request);
        } catch (IllegalStateException | IOException e) {
            responseData.setMessage(e.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @RequestMapping(value = "/hpm/ecr/pci/delfile")
    @ResponseBody
    public ResponseData updaeDataAndDelFile(HttpServletRequest request, @RequestBody List<EcrPci> dto) {
        IRequest requestCtx = createRequestContext(request);
        pciService.updateAndDelFile(requestCtx, dto);
        return new ResponseData();
    }

}
