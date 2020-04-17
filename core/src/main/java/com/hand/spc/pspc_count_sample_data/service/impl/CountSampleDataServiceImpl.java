package com.hand.spc.pspc_count_sample_data.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.constants.SpcConstants;
import com.hand.spc.pspc_classify.dto.Classify;
import com.hand.spc.pspc_classify.mapper.ClassifyMapper;
import com.hand.spc.pspc_count_sample_data.dto.CountSampleData;
import com.hand.spc.pspc_count_sample_data.dto.CountSampleDataVO;
import com.hand.spc.pspc_count_sample_data.mapper.CountSampleDataMapper;
import com.hand.spc.pspc_count_sample_data.service.ICountSampleDataService;
import com.hand.spc.pspc_count_sample_data_class.dto.CountSampleDataClass;
import com.hand.spc.pspc_count_sample_data_class.mapper.CountSampleDataClassMapper;
import com.hand.spc.pspc_count_sample_data_extend.dto.CountSampleDataExtend;
import com.hand.spc.pspc_count_sample_data_extend.mapper.CountSampleDataExtendMapper;
import com.hand.spc.pspc_count_sample_data_wait.dto.CountSampleDataWait;
import com.hand.spc.pspc_count_sample_data_wait.mapper.CountSampleDataWaitMapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class CountSampleDataServiceImpl extends BaseServiceImpl<CountSampleData> implements ICountSampleDataService,SpcConstants {
    @Autowired
    private CountSampleDataMapper countSampleDataMapper;
    @Autowired
    private ClassifyMapper classifyMapper;
    @Autowired
    private CountSampleDataClassMapper countSampleDataClassMapper;
    @Autowired
    private CountSampleDataExtendMapper countSampleDataExtendMapper;
    @Autowired
    private CountSampleDataWaitMapper countSampleDataWaitMapper;

    Pattern pattern = Pattern.compile("^[0-9]+(.[0-9]+)?$");

    @Override
    public List<Map<String, String>> queryCountData(IRequest requestContext, CountSampleData dto, int page, int pageSize){
        //查询基本列
        List<CountSampleDataVO> countSampleDataVOS = countSampleDataMapper.selectCountData(dto);
        List<Map<String,String>> mapList = new ArrayList<>();
        List<Map<String, String>> finalMapList = new ArrayList<>();
        countSampleDataVOS.stream().forEach(item -> {
            Map<String,String> map = new HashMap<>();
            try {
                map = BeanUtils.describe(item);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            finalMapList.add(map);
        });
        mapList= finalMapList;
        //添加扩展列
        mapList = mapList.stream().map(item -> {
            //添加分类项扩展列
            setClassifyColumns(item);
            //添加拓展属性列
            setExtAttributeColumns(item);
            return item;
        }).collect(Collectors.toList());
        return mapList;
    }

    @Override
    public ResponseData saveCountData(IRequest requestCtx, List<Map<String, String>> dtos) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        //固定的列数
        int baseCount = 7;
        dtos.forEach(dto -> {
            int count = 0;
            Map<Integer,String> tempMap = new HashMap<>();
            //因为map没有下标，考虑先全部取出放入有下标的map中
            for (Map.Entry<String, String> entry: dto.entrySet()) {
                count++;
                tempMap.put(count,entry.getKey());
                if(StringUtils.isEmpty(entry.getValue())){
                  throw new RuntimeException("数据不能为空");
                }
            }
            //存_WAIT表
            CountSampleDataWait countSampleDataWait = new CountSampleDataWait();
            countSampleDataWait.setTenantId(-1L);
            countSampleDataWait.setSiteId(-1L);
            countSampleDataWait.setAttachmentGroupId(Long.valueOf(dto.get("attachmentGroupId")));
            countSampleDataWait.setCeGroupId(Long.valueOf(dto.get("ceGroupId")));
            countSampleDataWait.setCeParameterId(Long.valueOf(dto.get("ceParameterId")));
            countSampleDataWait.setSampleValueCount(Long.valueOf(dto.get("sampleValueCount")));
            countSampleDataWait.setLastUpdatedBy(requestCtx.getUserId());
            countSampleDataWait.setCreatedBy(requestCtx.getUserId());
            countSampleDataWait.setLastUpdateLogin(requestCtx.getUserId());
            try {
                Date sampleTime = simpleDateFormat.parse(dto.get("sampleTime"));
                countSampleDataWait.setSampleTime(sampleTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            countSampleDataWait.setUnqualifiedQuantity(Long.valueOf(dto.get("unqualifiedQuantity")));
            countSampleDataWaitMapper.insertSelective(countSampleDataWait);
            //存classify表
            int classifyCount = Integer.parseInt(dto.get(CLASSIFY_EXTEND_COUNT));
            //后classifyCount列数据都是分类项
            for (int i = 0; i < classifyCount; i++) {
                Classify classify = new Classify();
                CountSampleDataClass countSampleDataClass = new CountSampleDataClass();
                countSampleDataClass.setTenantId(-1L);
                countSampleDataClass.setSiteId(-1L);
                countSampleDataClass.setCountSampleDataId(countSampleDataWait.getCountSampleDataWaitId());
                String classifyCode = tempMap.get(baseCount + i + 1);
                //根据分类项编码查询id
                classify.setClassifyCode(classifyCode.replaceAll(SPEC_CHAC.get("-"),"-"));
                List<Classify> classifies = classifyMapper.select(classify);
                if(classifies.size() == 0){
                    throw new RuntimeException("保存错误，未找到分类项");
                }
                classify = classifies.get(0);

                countSampleDataClass.setClassifyId(classify.getClassifyId());
                if(!pattern.matcher(dto.get(tempMap.get(baseCount+i+1))).matches()){
                    throw new RuntimeException("分类项需为数字");
                }
                countSampleDataClass.setClassifyCountValue(Double.valueOf(dto.get(tempMap.get(baseCount+i+1))));

                countSampleDataClass.setLastUpdatedBy(requestCtx.getUserId());
                countSampleDataClass.setCreatedBy(requestCtx.getUserId());
                countSampleDataClass.setLastUpdateLogin(requestCtx.getUserId());
                countSampleDataClassMapper.insertSelective(countSampleDataClass);
            }

            //存 extend表
            int extendCount = Integer.parseInt(dto.get(ATTRIBUTE_EXTEND_COUNT));
            for (int i = 0; i < extendCount; i++) {
                CountSampleDataExtend countSampleDataExtend = new CountSampleDataExtend();
                countSampleDataExtend.setTenantId(-1L);
                countSampleDataExtend.setSiteId(-1L);
                countSampleDataExtend.setCountSampleDataId(countSampleDataWait.getCountSampleDataWaitId());
                countSampleDataExtend.setExtendAttribute(tempMap.get(baseCount+classifyCount+i+1));
                countSampleDataExtend.setExtendValue(dto.get(tempMap.get(baseCount+classifyCount+i+1)));

                countSampleDataExtend.setLastUpdatedBy(requestCtx.getUserId());
                countSampleDataExtend.setCreatedBy(requestCtx.getUserId());
                countSampleDataExtend.setLastUpdateLogin(requestCtx.getUserId());
                countSampleDataExtendMapper.insertSelective(countSampleDataExtend);
            }
        });

        return new ResponseData(true);
    }

    @Override
    public ResponseData deleteCountSamleDate(List<CountSampleData> dtos) {
        for (CountSampleData countSampleData:dtos) {
            countSampleDataMapper.deleteByPrimaryKey(countSampleData);

            CountSampleDataWait countSampleDataWait = new CountSampleDataWait();
            countSampleDataWait.setCountSampleDataWaitId(countSampleData.getCountSampleDataId());
            countSampleDataWaitMapper.deleteByPrimaryKey(countSampleDataWait);

            CountSampleDataExtend countSampleDataExtend = new CountSampleDataExtend();
            countSampleDataExtend.setCountSampleDataId(countSampleData.getCountSampleDataId());
            countSampleDataExtendMapper.delete(countSampleDataExtend);

            CountSampleDataClass countSampleDataClass = new CountSampleDataClass();
            countSampleDataClass.setCountSampleDataId(countSampleData.getCountSampleDataId());
            countSampleDataClassMapper.delete(countSampleDataClass);
        }
        return new ResponseData(true);
    }

    /**
     * @Author han.zhang
     * @Description 添加分类项扩展列
     * @Date 12:25 2019/8/14
     * @Param []
     */
    public void setClassifyColumns(Map<String,String> map){
        //根据样本数据id在计数分类项里查询维护的分类项
        CountSampleDataClass countSampleDataClass = new CountSampleDataClass();
        countSampleDataClass.setCountSampleDataId(Long.valueOf(map.get("countSampleDataId")));
        List<CountSampleDataClass> countSampleDataClasses = countSampleDataClassMapper.select(countSampleDataClass);
        map.put(CLASSIFY_EXTEND_COUNT, String.valueOf(countSampleDataClasses.size()));
        //遍历维护的分类项将其插入到返回的结果中
        countSampleDataClasses.stream().forEach(dataClass -> {
            Classify classify = new Classify();
            classify.setClassifyId(dataClass.getClassifyId().longValue());
            classify = classifyMapper.selectByPrimaryKey(classify);
            map.put(classify.getClassifyCode().replaceAll("-",SPEC_CHAC.get("-")), String.valueOf(dataClass.getClassifyCountValue()));
        });
    }
    /**
     * @Author han.zhang
     * @Description 添加拓展属性列
     * @Date 11:43 2019/8/14
     * @Param []
     */
    public void setExtAttributeColumns(Map<String,String> map){
        List<CountSampleDataExtend> countSampleDataExtendList =
                countSampleDataExtendMapper.getExtAttribute(Long.valueOf(map.get("countSampleDataId")));
        map.put(ATTRIBUTE_EXTEND_COUNT, String.valueOf(countSampleDataExtendList.size()));
        countSampleDataExtendList.stream().forEach(extend -> map.put(extend.getExtendAttribute(),extend.getExtendValue()));
    }
}