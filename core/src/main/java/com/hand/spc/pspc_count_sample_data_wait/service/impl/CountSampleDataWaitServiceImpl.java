package com.hand.spc.pspc_count_sample_data_wait.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hand.hap.cache.impl.SysCodeCache;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.Code;
import com.hand.hap.system.dto.CodeValue;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.constants.SpcConstants;
import com.hand.spc.pspc_classify.dto.Classify;
import com.hand.spc.pspc_classify.mapper.ClassifyMapper;
import com.hand.spc.pspc_count_sample_data_class.dto.CountSampleDataClass;
import com.hand.spc.pspc_count_sample_data_class.mapper.CountSampleDataClassMapper;
import com.hand.spc.pspc_count_sample_data_extend.dto.CountSampleDataExtend;
import com.hand.spc.pspc_count_sample_data_extend.mapper.CountSampleDataExtendMapper;
import com.hand.spc.pspc_count_sample_data_wait.dto.CountSampleDataWait;
import com.hand.spc.pspc_count_sample_data_wait.mapper.CountSampleDataWaitMapper;
import com.hand.spc.pspc_count_sample_data_wait.service.ICountSampleDataWaitService;
import com.hand.utils.excelUtil.FileUploadBaseDto;
import com.hand.utils.excelUtil.FileUploadDto;
import com.hand.utils.excelUtil.TransferExcelDateToFileUploadDtoUtil;

@Service
@Transactional(rollbackFor = Exception.class)
public class CountSampleDataWaitServiceImpl extends BaseServiceImpl<CountSampleDataWait> implements ICountSampleDataWaitService,SpcConstants {
    private static final String EXTEND_ATTR_CODE = "PSPC.DATA.EXTRA.ATTRITUBE";

    @Autowired
    private CountSampleDataWaitMapper countSampleDataWaitMapper;
    @Autowired
    private ClassifyMapper classifyMapper;
    @Autowired
    private CountSampleDataClassMapper countSampleDataClassMapper;
    @Autowired
    private CountSampleDataExtendMapper countSampleDataExtendMapper;
    @Autowired
    private SysCodeCache sysCodeCache;
    @Override
    public ResponseData importExcel(IRequest requestContext, HttpServletRequest request, Long ceGroupId,
                                    Long ceParameterId, Long attachmentGroupId, Long classifyExtendCount, Long attributeExtendCount) throws NoSuchFieldException, IllegalAccessException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        ResponseData responseData = new ResponseData();
        List<FileUploadDto> fileUploadDtos = null;
        try {
            fileUploadDtos = TransferExcelDateToFileUploadDtoUtil.TransferExcelDateToFileUploadDtoList(
                    request, 1, 40, 1);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        FileUploadDto fileUploadDto = fileUploadDtos.get(0);
        List<FileUploadBaseDto> baseDtos = fileUploadDto.getList();


        //固定列
        int baseColumnCount = 3;
        //导入的数据总列数
        int allCount = 0;
        //所有的标题列
        List<String> titleList = new ArrayList<>();

        //判断是否有数据
        if(baseDtos.size() == 0){
            throw new RuntimeException("请填充数据");
        }
        //先获得第一行的标题,最大支持40列
        for (int i = 0; i < 40; i++) {
            String s = baseDtos.get(0).get("attribute" + (i + 1));
            if(StringUtils.isEmpty(s)){
                break;
            }
            titleList.add(s);
            allCount++;
        }
        Code cacheValue = sysCodeCache.getValue(EXTEND_ATTR_CODE+ "." + zh_CN);
        int count = 0;
        for (FileUploadBaseDto baseDto:baseDtos) {
            if(count == 0){
                count++;
                continue;
            }
            //存_WAIT表
            CountSampleDataWait countSampleDataWait = new CountSampleDataWait();
            countSampleDataWait.setTenantId(-1L);
            countSampleDataWait.setSiteId(-1L);
            countSampleDataWait.setAttachmentGroupId(attachmentGroupId);
            countSampleDataWait.setCeGroupId(ceGroupId);
            countSampleDataWait.setCeParameterId(ceParameterId);
            countSampleDataWait.setSampleValueCount(Long.valueOf(baseDto.getAttribute2()));
            try {
                Date sampleTime = simpleDateFormat.parse(baseDto.getAttribute1());
                countSampleDataWait.setSampleTime(sampleTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            countSampleDataWait.setUnqualifiedQuantity(Long.valueOf(baseDto.getAttribute3()));
            countSampleDataWaitMapper.insertSelective(countSampleDataWait);

            //excel中拓展属性列数
            int extendColumnsCount = 0;
            //存拓展属性
            for (int i = baseColumnCount; i < 40; i++) {
                int n = i;
                //能匹配到快码是拓展属性
                CodeValue codeValue = cacheValue.getCodeValues().stream()
                        .filter(code -> code.getMeaning().equals(titleList.get(n)))
                        .findAny().orElse(null);
                if(null == codeValue){
                    break;
                }
                CountSampleDataExtend countSampleDataExtend = new CountSampleDataExtend();
                countSampleDataExtend.setTenantId(-1L);
                countSampleDataExtend.setSiteId(-1L);
                countSampleDataExtend.setCountSampleDataId(countSampleDataWait.getCountSampleDataWaitId());
                countSampleDataExtend.setExtendAttribute(codeValue.getValue());
                countSampleDataExtend.setExtendValue(baseDto.get("attribute"+(baseColumnCount+1)));
                countSampleDataExtendMapper.insertSelective(countSampleDataExtend);
                extendColumnsCount++;
            }

            //存分类项
            for (int i = baseColumnCount+extendColumnsCount; i < titleList.size(); i++) {
                Classify classify = new Classify();
                CountSampleDataClass countSampleDataClass = new CountSampleDataClass();
                countSampleDataClass.setTenantId(-1L);
                countSampleDataClass.setSiteId(-1L);
                countSampleDataClass.setCountSampleDataId(countSampleDataWait.getCountSampleDataWaitId());

                //根据分类项编码查询id
                classify.setClassifyCode(titleList.get(i));
                List<Classify> classifies = classifyMapper.select(classify);
                if(classifies.size() == 0){
                    throw new RuntimeException("分类项不存在");
                }

                countSampleDataClass.setClassifyId(classifies.get(0).getClassifyId());
                countSampleDataClass.setClassifyCountValue(Double.valueOf(baseDto.get("attribute"+(i+1))));
                countSampleDataClassMapper.insertSelective(countSampleDataClass);
            }
        }
        return new ResponseData(true);
    }
}