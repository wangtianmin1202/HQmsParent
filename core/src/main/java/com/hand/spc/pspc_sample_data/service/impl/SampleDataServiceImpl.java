package com.hand.spc.pspc_sample_data.service.impl;

//import static com.hand.spc.SpcConstants.zh_CN;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.hand.hap.cache.impl.SysCodeCache;
import com.hand.hap.core.BaseConstants;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.Code;
import com.hand.hap.system.dto.CodeValue;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.pspc_sample_data.dto.SampleData;
import com.hand.spc.pspc_sample_data.dto.SampleDataExtend;
import com.hand.spc.pspc_sample_data.dto.SampleDataWait;
import com.hand.spc.pspc_sample_data.mapper.SampleDataMapper;
import com.hand.spc.pspc_sample_data.service.ISampleDataExtendService;
import com.hand.spc.pspc_sample_data.service.ISampleDataService;
import com.hand.spc.pspc_sample_data.service.ISampleDataWaitService;
import com.hand.spc.pspc_sample_data.view.SampleDataQueryVO;
import com.hand.utils.excelUtil.FileUploadBaseDto;
import com.hand.utils.excelUtil.FileUploadDto;
import com.hand.utils.excelUtil.TransferExcelDateToFileUploadDtoUtil;

@Service
@Transactional(rollbackFor = Exception.class)
public class SampleDataServiceImpl extends BaseServiceImpl<SampleData> implements ISampleDataService, BaseConstants {

    @Autowired
    private SampleDataMapper sampleDataMapper;

    @Autowired
    private ISampleDataExtendService sampleDataExtendService;

    @Autowired
    private ISampleDataWaitService sampleDataWaitService;

    Map<String, Integer> sampleDataExtendMap = new HashMap<>(20);

    @Autowired
    private SysCodeCache sysCodeCache;

    /**
     * @param iRequest          基本参数
     * @param sampleDataQueryVO 限制条件
     * @param page              页数
     * @param pageSize          页数大小
     * @return : java.util.List<com.hand.spc.pspc_sample_data.dto.SampleData>
     * @Description: 基础数据查询(限制查询拓展表)
     * @author: ywj
     * @date 2019/8/14 17:32
     * @version 1.0
     */
    @Override
    public List<SampleDataQueryVO> queryBaseData(IRequest iRequest, SampleDataQueryVO sampleDataQueryVO, int page, int pageSize) {

        // 分页
        PageHelper.startPage(page, pageSize);

        // 数据查询
        List<SampleDataQueryVO> sampleDataQueryVOList = sampleDataMapper.queryBaseData(sampleDataQueryVO);

        // 根据查询的数据进行拓展
        SampleDataExtend sampleDataExtend = new SampleDataExtend();
        for (SampleDataQueryVO queryVO : sampleDataQueryVOList) {
            sampleDataExtend.setSampleDataId(queryVO.getSampleDataId());
            List<SampleDataExtend> sampleDataExtendList = sampleDataExtendService.select(iRequest, sampleDataExtend, 0, 0);
            returnSampleDataQueryVOResolveExtend(sampleDataExtendList, queryVO);
        }

        List<SampleDataQueryVO> sampleDataQueryVOListRet = new ArrayList<>();
        // 将集合的拓展相同数据放入同一个属性中
        for (SampleDataQueryVO queryVO : sampleDataQueryVOList) {
            if (sampleDataQueryVOListRet.size() != 0) {
                sortData(queryVO);
            }
            sampleDataQueryVOListRet.add(queryVO);
        }

        return sampleDataQueryVOList;
    }


    /**
     * @param sampleDataExtendList 传入参数
     * @param sampleDataQueryVO    返回参数
     * @return : com.hand.spc.pspc_sample_data.view.SampleDataQueryVO
     * @Description: 拓展表数据赋入
     * @author: ywj
     * @date 2019/8/14 17:56
     * @version 1.0
     */
    private SampleDataQueryVO returnSampleDataQueryVOResolveExtend(List<SampleDataExtend> sampleDataExtendList, SampleDataQueryVO sampleDataQueryVO) {


        // 此处笨方法 共15处
        for (int i = 0; i < sampleDataExtendList.size(); i++) {

            // 用于 后面拓展字段数据同步 做铺垫
            if (sampleDataExtendMap.get(sampleDataExtendList.get(i).getExtendAttribute()) == null) {
                sampleDataExtendMap.put(sampleDataExtendList.get(i).getExtendAttribute(), sampleDataExtendMap.size() + 1);
            }

            if (sampleDataQueryVO.getAttribute1() == null) {
                sampleDataQueryVO.setAttribute1Title(sampleDataExtendList.get(i).getExtendAttribute());
                sampleDataQueryVO.setAttribute1(sampleDataExtendList.get(i).getExtendValue());
            } else {
                if (sampleDataQueryVO.getAttribute2() == null) {
                    sampleDataQueryVO.setAttribute2Title(sampleDataExtendList.get(i).getExtendAttribute());
                    sampleDataQueryVO.setAttribute2(sampleDataExtendList.get(i).getExtendValue());
                } else {
                    if (sampleDataQueryVO.getAttribute3() == null) {
                        sampleDataQueryVO.setAttribute3Title(sampleDataExtendList.get(i).getExtendAttribute());
                        sampleDataQueryVO.setAttribute3(sampleDataExtendList.get(i).getExtendValue());
                    } else {
                        if (sampleDataQueryVO.getAttribute4() == null) {
                            sampleDataQueryVO.setAttribute4Title(sampleDataExtendList.get(i).getExtendAttribute());
                            sampleDataQueryVO.setAttribute4(sampleDataExtendList.get(i).getExtendValue());
                        } else {
                            if (sampleDataQueryVO.getAttribute5() == null) {
                                sampleDataQueryVO.setAttribute5Title(sampleDataExtendList.get(i).getExtendAttribute());
                                sampleDataQueryVO.setAttribute5(sampleDataExtendList.get(i).getExtendValue());
                            } else {
                                if (sampleDataQueryVO.getAttribute6() == null) {
                                    sampleDataQueryVO.setAttribute6Title(sampleDataExtendList.get(i).getExtendAttribute());
                                    sampleDataQueryVO.setAttribute6(sampleDataExtendList.get(i).getExtendValue());
                                } else {
                                    if (sampleDataQueryVO.getAttribute7() == null) {
                                        sampleDataQueryVO.setAttribute7Title(sampleDataExtendList.get(i).getExtendAttribute());
                                        sampleDataQueryVO.setAttribute7(sampleDataExtendList.get(i).getExtendValue());
                                    } else {
                                        if (sampleDataQueryVO.getAttribute8() == null) {
                                            sampleDataQueryVO.setAttribute8Title(sampleDataExtendList.get(i).getExtendAttribute());
                                            sampleDataQueryVO.setAttribute8(sampleDataExtendList.get(i).getExtendValue());
                                        } else {
                                            if (sampleDataQueryVO.getAttribute9() == null) {
                                                sampleDataQueryVO.setAttribute9Title(sampleDataExtendList.get(i).getExtendAttribute());
                                                sampleDataQueryVO.setAttribute9(sampleDataExtendList.get(i).getExtendValue());
                                            } else {
                                                if (sampleDataQueryVO.getAttribute10() == null) {
                                                    sampleDataQueryVO.setAttribute10Title(sampleDataExtendList.get(i).getExtendAttribute());
                                                    sampleDataQueryVO.setAttribute10(sampleDataExtendList.get(i).getExtendValue());
                                                } else {
                                                    if (sampleDataQueryVO.getAttribute11() == null) {
                                                        sampleDataQueryVO.setAttribute11Title(sampleDataExtendList.get(i).getExtendAttribute());
                                                        sampleDataQueryVO.setAttribute11(sampleDataExtendList.get(i).getExtendValue());
                                                    } else {
                                                        if (sampleDataQueryVO.getAttribute12() == null) {
                                                            sampleDataQueryVO.setAttribute12Title(sampleDataExtendList.get(i).getExtendAttribute());
                                                            sampleDataQueryVO.setAttribute12(sampleDataExtendList.get(i).getExtendValue());
                                                        } else {
                                                            if (sampleDataQueryVO.getAttribute13() == null) {
                                                                sampleDataQueryVO.setAttribute13Title(sampleDataExtendList.get(i).getExtendAttribute());
                                                                sampleDataQueryVO.setAttribute13(sampleDataExtendList.get(i).getExtendValue());
                                                            } else {
                                                                if (sampleDataQueryVO.getAttribute14() == null) {
                                                                    sampleDataQueryVO.setAttribute14Title(sampleDataExtendList.get(i).getExtendAttribute());
                                                                    sampleDataQueryVO.setAttribute14(sampleDataExtendList.get(i).getExtendValue());
                                                                } else {
                                                                    sampleDataQueryVO.setAttribute15Title(sampleDataExtendList.get(i).getExtendAttribute());
                                                                    sampleDataQueryVO.setAttribute15(sampleDataExtendList.get(i).getExtendValue());
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }

        return sampleDataQueryVO;
    }


    /**
     * @param sampleDataQueryVO 1传入参数
     * @return : void
     * @Description: 数据排序
     * @author: ywj
     * @date 2019/8/15 10:03
     * @version 1.0
     */
    private void sortData(SampleDataQueryVO sampleDataQueryVO) {

        SampleDataQueryVO dataQueryVO = new SampleDataQueryVO();
        BeanUtils.copyProperties(sampleDataQueryVO, dataQueryVO);
        dataQueryVO.setAttribute1(null);
        dataQueryVO.setAttribute2(null);
        dataQueryVO.setAttribute3(null);
        dataQueryVO.setAttribute4(null);
        dataQueryVO.setAttribute5(null);
        dataQueryVO.setAttribute6(null);
        dataQueryVO.setAttribute7(null);
        dataQueryVO.setAttribute8(null);
        dataQueryVO.setAttribute9(null);
        dataQueryVO.setAttribute10(null);
        dataQueryVO.setAttribute11(null);
        dataQueryVO.setAttribute12(null);
        dataQueryVO.setAttribute13(null);
        dataQueryVO.setAttribute14(null);
        dataQueryVO.setAttribute15(null);
        dataQueryVO.setAttribute1Title(null);
        dataQueryVO.setAttribute2Title(null);
        dataQueryVO.setAttribute3Title(null);
        dataQueryVO.setAttribute4Title(null);
        dataQueryVO.setAttribute5Title(null);
        dataQueryVO.setAttribute6Title(null);
        dataQueryVO.setAttribute7Title(null);
        dataQueryVO.setAttribute8Title(null);
        dataQueryVO.setAttribute9Title(null);
        dataQueryVO.setAttribute10Title(null);
        dataQueryVO.setAttribute11Title(null);
        dataQueryVO.setAttribute12Title(null);
        dataQueryVO.setAttribute13Title(null);
        dataQueryVO.setAttribute14Title(null);
        dataQueryVO.setAttribute15Title(null);

        if (sampleDataQueryVO.getAttribute1Title() != null) {
            setDataVO(sampleDataQueryVO.getAttribute1Title(), sampleDataQueryVO.getAttribute1(), sampleDataExtendMap.get(sampleDataQueryVO.getAttribute1Title()), dataQueryVO);
        }
        if (sampleDataQueryVO.getAttribute2Title() != null) {
            setDataVO(sampleDataQueryVO.getAttribute2Title(), sampleDataQueryVO.getAttribute2(), sampleDataExtendMap.get(sampleDataQueryVO.getAttribute2Title()), dataQueryVO);
        }
        if (sampleDataQueryVO.getAttribute3Title() != null) {
            setDataVO(sampleDataQueryVO.getAttribute3Title(), sampleDataQueryVO.getAttribute3(), sampleDataExtendMap.get(sampleDataQueryVO.getAttribute3Title()), dataQueryVO);
        }
        if (sampleDataQueryVO.getAttribute4Title() != null) {
            setDataVO(sampleDataQueryVO.getAttribute4Title(), sampleDataQueryVO.getAttribute4(), sampleDataExtendMap.get(sampleDataQueryVO.getAttribute4Title()), dataQueryVO);
        }
        if (sampleDataQueryVO.getAttribute5Title() != null) {
            setDataVO(sampleDataQueryVO.getAttribute5Title(), sampleDataQueryVO.getAttribute5(), sampleDataExtendMap.get(sampleDataQueryVO.getAttribute5Title()), dataQueryVO);
        }
        if (sampleDataQueryVO.getAttribute6Title() != null) {
            setDataVO(sampleDataQueryVO.getAttribute6Title(), sampleDataQueryVO.getAttribute6(), sampleDataExtendMap.get(sampleDataQueryVO.getAttribute6Title()), dataQueryVO);
        }
        if (sampleDataQueryVO.getAttribute7Title() != null) {
            setDataVO(sampleDataQueryVO.getAttribute7Title(), sampleDataQueryVO.getAttribute7(), sampleDataExtendMap.get(sampleDataQueryVO.getAttribute7Title()), dataQueryVO);
        }
        if (sampleDataQueryVO.getAttribute8Title() != null) {
            setDataVO(sampleDataQueryVO.getAttribute8Title(), sampleDataQueryVO.getAttribute8(), sampleDataExtendMap.get(sampleDataQueryVO.getAttribute8Title()), dataQueryVO);
        }
        if (sampleDataQueryVO.getAttribute9Title() != null) {
            setDataVO(sampleDataQueryVO.getAttribute9Title(), sampleDataQueryVO.getAttribute9(), sampleDataExtendMap.get(sampleDataQueryVO.getAttribute9Title()), dataQueryVO);
        }
        if (sampleDataQueryVO.getAttribute10Title() != null) {
            setDataVO(sampleDataQueryVO.getAttribute10Title(), sampleDataQueryVO.getAttribute10(), sampleDataExtendMap.get(sampleDataQueryVO.getAttribute10Title()), dataQueryVO);
        }
        if (sampleDataQueryVO.getAttribute11Title() != null) {
            setDataVO(sampleDataQueryVO.getAttribute11Title(), sampleDataQueryVO.getAttribute11(), sampleDataExtendMap.get(sampleDataQueryVO.getAttribute11Title()), dataQueryVO);
        }
        if (sampleDataQueryVO.getAttribute12Title() != null) {
            setDataVO(sampleDataQueryVO.getAttribute12Title(), sampleDataQueryVO.getAttribute12(), sampleDataExtendMap.get(sampleDataQueryVO.getAttribute12Title()), dataQueryVO);
        }
        if (sampleDataQueryVO.getAttribute13Title() != null) {
            setDataVO(sampleDataQueryVO.getAttribute13Title(), sampleDataQueryVO.getAttribute13(), sampleDataExtendMap.get(sampleDataQueryVO.getAttribute13Title()), dataQueryVO);
        }
        if (sampleDataQueryVO.getAttribute14Title() != null) {
            setDataVO(sampleDataQueryVO.getAttribute14Title(), sampleDataQueryVO.getAttribute14(), sampleDataExtendMap.get(sampleDataQueryVO.getAttribute14Title()), dataQueryVO);
        }
        if (sampleDataQueryVO.getAttribute15Title() != null) {
            setDataVO(sampleDataQueryVO.getAttribute15Title(), sampleDataQueryVO.getAttribute15(), sampleDataExtendMap.get(sampleDataQueryVO.getAttribute15Title()), dataQueryVO);
        }

    }

    /**
     * @param title             传入标题
     * @param value             传入值
     * @param num               放置位置
     * @param sampleDataQueryVO 数据更改
     * @return : void
     * @Description: 排序的数据
     * @author: ywj
     * @date 2019/8/15 10:09
     * @version 1.0
     */
    private void setDataVO(String title, String value, int num, SampleDataQueryVO sampleDataQueryVO) {
        switch (sampleDataExtendMap.get(title)) {
            case 1:
                sampleDataQueryVO.setAttribute1(value);
                sampleDataQueryVO.setAttribute1Title(title);
                break;
            case 2:
                sampleDataQueryVO.setAttribute1(value);
                sampleDataQueryVO.setAttribute1Title(title);
                break;
            case 3:
                sampleDataQueryVO.setAttribute1(value);
                sampleDataQueryVO.setAttribute1Title(title);
                break;
            case 4:
                sampleDataQueryVO.setAttribute1(value);
                sampleDataQueryVO.setAttribute1Title(title);
                break;
            case 5:
                sampleDataQueryVO.setAttribute1(value);
                sampleDataQueryVO.setAttribute1Title(title);
                break;
            case 6:
                sampleDataQueryVO.setAttribute1(value);
                sampleDataQueryVO.setAttribute1Title(title);
                break;
            case 7:
                sampleDataQueryVO.setAttribute1(value);
                sampleDataQueryVO.setAttribute1Title(title);
                break;
            case 8:
                sampleDataQueryVO.setAttribute1(value);
                sampleDataQueryVO.setAttribute1Title(title);
                break;
            case 9:
                sampleDataQueryVO.setAttribute1(value);
                sampleDataQueryVO.setAttribute1Title(title);
                break;
            case 10:
                sampleDataQueryVO.setAttribute1(value);
                sampleDataQueryVO.setAttribute1Title(title);
                break;
            case 11:
                sampleDataQueryVO.setAttribute1(value);
                sampleDataQueryVO.setAttribute1Title(title);
                break;
            case 12:
                sampleDataQueryVO.setAttribute1(value);
                sampleDataQueryVO.setAttribute1Title(title);
                break;
            case 13:
                sampleDataQueryVO.setAttribute1(value);
                sampleDataQueryVO.setAttribute1Title(title);
                break;
            case 14:
                sampleDataQueryVO.setAttribute1(value);
                sampleDataQueryVO.setAttribute1Title(title);
                break;
            case 15:
                sampleDataQueryVO.setAttribute1(value);
                sampleDataQueryVO.setAttribute1Title(title);
                break;

            default:
                return;

        }
    }

    /**
     * @param iRequest              基本参数
     * @param sampleDataQueryVOList 传入参数
     * @return : com.hand.hap.system.dto.ResponseData
     * @Description: 数据保存
     * @author: ywj
     * @date 2019/8/15 16:36
     * @version 1.0
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseData saveBaseData(IRequest iRequest, List<SampleDataQueryVO> sampleDataQueryVOList) {

        // 快码
        Code code = sysCodeCache.getValue("PSPC.DATA.EXTRA.ATTRITUBE" + "." + DEFAULT_LANG);

        // 遍历
        for (SampleDataQueryVO dataQueryVO : sampleDataQueryVOList) {

            SampleDataWait sampleDataWait = new SampleDataWait();

            // 保存投数据
            BeanUtils.copyProperties(dataQueryVO, sampleDataWait);
            if (dataQueryVO.getSampleDataId() == null) {
                sampleDataWait.set__status("add");
            } else {
                sampleDataWait.set__status("update");
            }
            sampleDataWait = sampleDataWaitService.batchUpdate(iRequest, Collections.singletonList(sampleDataWait)).get(0);

            // 赋值主键
            dataQueryVO.setSampleDataId(sampleDataWait.getSampleDataWaitId());

            // 拓展字段 的新增和更新
            if (dataQueryVO.getAttribute1() != null) {
                saveExtendData(iRequest, dataQueryVO.getSampleDataId(), code, dataQueryVO.getAttribute1Title(), dataQueryVO.getAttribute1());
            }
            if (dataQueryVO.getAttribute2() != null) {
                saveExtendData(iRequest, dataQueryVO.getSampleDataId(), code, dataQueryVO.getAttribute2Title(), dataQueryVO.getAttribute2());
            }
            if (dataQueryVO.getAttribute3() != null) {
                saveExtendData(iRequest, dataQueryVO.getSampleDataId(), code, dataQueryVO.getAttribute3Title(), dataQueryVO.getAttribute3());
            }
            if (dataQueryVO.getAttribute4() != null) {
                saveExtendData(iRequest, dataQueryVO.getSampleDataId(), code, dataQueryVO.getAttribute4Title(), dataQueryVO.getAttribute4());
            }
            if (dataQueryVO.getAttribute5() != null) {
                saveExtendData(iRequest, dataQueryVO.getSampleDataId(), code, dataQueryVO.getAttribute5Title(), dataQueryVO.getAttribute5());
            }
            if (dataQueryVO.getAttribute6() != null) {
                saveExtendData(iRequest, dataQueryVO.getSampleDataId(), code, dataQueryVO.getAttribute6Title(), dataQueryVO.getAttribute6());
            }
            if (dataQueryVO.getAttribute7() != null) {
                saveExtendData(iRequest, dataQueryVO.getSampleDataId(), code, dataQueryVO.getAttribute7Title(), dataQueryVO.getAttribute7());
            }
            if (dataQueryVO.getAttribute8() != null) {
                saveExtendData(iRequest, dataQueryVO.getSampleDataId(), code, dataQueryVO.getAttribute8Title(), dataQueryVO.getAttribute8());
            }
            if (dataQueryVO.getAttribute9() != null) {
                saveExtendData(iRequest, dataQueryVO.getSampleDataId(), code, dataQueryVO.getAttribute9Title(), dataQueryVO.getAttribute9());
            }
            if (dataQueryVO.getAttribute10() != null) {
                saveExtendData(iRequest, dataQueryVO.getSampleDataId(), code, dataQueryVO.getAttribute10Title(), dataQueryVO.getAttribute10());
            }
            if (dataQueryVO.getAttribute11() != null) {
                saveExtendData(iRequest, dataQueryVO.getSampleDataId(), code, dataQueryVO.getAttribute11Title(), dataQueryVO.getAttribute11());
            }
            if (dataQueryVO.getAttribute12() != null) {
                saveExtendData(iRequest, dataQueryVO.getSampleDataId(), code, dataQueryVO.getAttribute12Title(), dataQueryVO.getAttribute12());
            }
            if (dataQueryVO.getAttribute13() != null) {
                saveExtendData(iRequest, dataQueryVO.getSampleDataId(), code, dataQueryVO.getAttribute13Title(), dataQueryVO.getAttribute13());
            }
            if (dataQueryVO.getAttribute14() != null) {
                saveExtendData(iRequest, dataQueryVO.getSampleDataId(), code, dataQueryVO.getAttribute14Title(), dataQueryVO.getAttribute14());
            }
            if (dataQueryVO.getAttribute15() != null) {
                saveExtendData(iRequest, dataQueryVO.getSampleDataId(), code, dataQueryVO.getAttribute15Title(), dataQueryVO.getAttribute15());
            }


        }

        // 结果返回
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(true);
        responseData.setRows(sampleDataQueryVOList);
        return responseData;
    }


    /**
     * @param iRequest        基本参数
     * @param sampleDataId    头主键
     * @param code            快码
     * @param extendAttribute 拓展属性
     * @param extendValue     拓展值
     * @return : void
     * @Description: 更新或者新增拓展字段
     * @author: ywj
     * @date 2019/8/15 17:27
     * @version 1.0
     */
    private void saveExtendData(IRequest iRequest, Float sampleDataId, Code code, String extendAttribute, String extendValue) {
        // 通过描述获取含义
        List<CodeValue> codeValueList = code.getCodeValues().stream().filter(item -> extendAttribute.equals(item.getDescription())).collect(Collectors.toList());

        if (codeValueList.size() != 0) {

            SampleDataExtend sampleDataExtend = new SampleDataExtend();
            // 查询 拓展字段所以数据
            sampleDataExtend.setSampleDataId(sampleDataId);
            List<SampleDataExtend> sampleDataExtendList = sampleDataExtendService.select(iRequest, sampleDataExtend, 0, 0);
            sampleDataExtendList = sampleDataExtendList.stream().filter(item -> codeValueList.get(0).getValue().equals(item.getExtendAttribute())).collect(Collectors.toList());

            sampleDataExtend.setExtendAttribute(codeValueList.get(0).getValue());
            sampleDataExtend.setExtendValue(extendValue);

            // 找不到新增 找到 更新
            if (sampleDataExtendList.size() == 0) {
                sampleDataExtendService.insertSelective(iRequest, sampleDataExtend);
            } else {
                sampleDataExtendService.updateByPrimaryKeySelective(iRequest, sampleDataExtend);
            }
        }
    }

    /**
     * @param iRequest          基本参数
     * @param request           文本传入参数
     * @param ceGroupId         控制要素组主键
     * @param ceParameterId     控件要素主键
     * @param attachmentGroupId 附件要素组主键
     * @return : com.hand.hap.system.dto.ResponseData
     * @Description: 数据导入
     * @author: ywj
     * @date 2019/8/16 9:13
     * @version 1.0
     */
    @Override
    public ResponseData importExcel(IRequest iRequest, HttpServletRequest request, Long ceGroupId, Long ceParameterId, Long attachmentGroupId) throws NoSuchFieldException, IllegalAccessException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

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
        int baseColumnCount = 2;
        //导入的数据总列数
        int allCount = 0;
        //所有的标题列
        List<String> titleList = new ArrayList<>();

        //判断是否有数据
        if (baseDtos.size() == 0) {
            throw new RuntimeException("请填充数据");
        }
        //先获得第一行的标题,最大支持40列
        for (int i = 0; i < 40; i++) {
            String s = baseDtos.get(0).get("attribute" + (i + 1));
            if (StringUtils.isEmpty(s)) {
                break;
            }
            titleList.add(s);
            allCount++;
        }
        Code cacheValue = sysCodeCache.getValue("PSPC.DATA.EXTRA.ATTRITUBE" + "." + DEFAULT_LANG);
        int count = 0;
        for (FileUploadBaseDto baseDto : baseDtos) {
            if (count == 0) {
                count++;
                continue;
            }
            //存_WAIT表
            SampleDataWait sampleDataWait = new SampleDataWait();
            sampleDataWait.setAttachmentGroupId(attachmentGroupId.floatValue());
            sampleDataWait.setCeGroupId(ceGroupId.floatValue());
            sampleDataWait.setCeParameterId(ceParameterId.floatValue());
            sampleDataWait.setSampleValue(Float.valueOf(baseDto.getAttribute2()));
            try {
                Date sampleTime = simpleDateFormat.parse(baseDto.getAttribute1());
                sampleDataWait.setSampleTime(sampleTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            sampleDataWait = sampleDataWaitService.insertSelective(iRequest, sampleDataWait);

            //excel中拓展属性列数
            //存拓展属性
            for (int i = baseColumnCount; i < titleList.size(); i++) {
                int n = i;
                //能匹配到快码是拓展属性
                CodeValue codeValue = cacheValue.getCodeValues().stream()
                        .filter(code -> code.getMeaning().equals(titleList.get(n)))
                        .findAny().orElse(null);
                if (null == codeValue) {
                    break;
                }
                SampleDataExtend sampleDataExtend = new SampleDataExtend();
                sampleDataExtend.setSampleDataId(sampleDataWait.getSampleDataWaitId());
                sampleDataExtend.setExtendAttribute(codeValue.getValue());
                sampleDataExtend.setExtendValue(baseDto.get("attribute" + (baseColumnCount + 1)));
                sampleDataExtendService.insertSelective(iRequest, sampleDataExtend);
            }
        }
        return new ResponseData(true);
    }
}