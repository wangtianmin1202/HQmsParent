package com.hand.spc.repository.service.impl;
/*package com.hand.spc.test.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.test.dto.CountOoc;
import com.hand.spc.test.dto.CountOocRequestDTO;
import com.hand.spc.test.dto.CountOocResponseDTO;
import com.hand.spc.test.dto.MessageTypeDetail;
import com.hand.spc.test.dto.Ooc;
import com.hand.spc.test.dto.OocRequestDTO;
import com.hand.spc.test.dto.OocResponseDTO;
import com.hand.spc.test.dto.OocUpdateDto;
import com.hand.spc.test.mapper.CountOocMapper;
import com.hand.spc.test.mapper.OocMapper;
import com.hand.spc.test.service.IMessageTypeDetailService;
import com.hand.spc.test.service.IOocService;
import com.hand.spc.utils.CommonException;
import com.hand.spc.utils.LovFeignClient;
import com.hand.spc.utils.LovValueDTO;
import com.hand.spc.utils.RedisHelper;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class OocServiceImpl extends BaseServiceImpl<Ooc> implements IOocService {

    private Logger logger = LoggerFactory.getLogger(OocServiceImpl.class);

    @Autowired
    private OocMapper oocRepository;
    @Autowired
    private LovFeignClient lovFeignClient;
    @Autowired
    private RedisHelper redisHelper;
    @Autowired
    private CountOocMapper countOocRepository;

    @Override
    public Page<OocResponseDTO> listOOC(OocRequestDTO requestDTO, PageRequest pageRequest) {

        //logger.info("查询参数：" + redisHelper.toJson(requestDTO));
        //logger.info("分页参数：" + redisHelper.toJson(pageRequest));

        //基于子组查找OOC时不校验时间为空
        if (requestDTO.getSubgroupIdList() == null || requestDTO.getSubgroupIdList().size() <= 0) {
            //日期处理
            if (requestDTO.getCreationDateFrom() == null || requestDTO.getCreationDateTo() == null) {
                throw new CommonException("pspc.error.date.null");//时间为空
            }
        }

        Page<OocResponseDTO> page = PageHelper.doPage(pageRequest.getPage(), pageRequest.getSize(), () -> oocMapper.listOOC(requestDTO));
        if (page.getContent().size() > 0) {
            //获取消息类型值集值
            List<LovValueDTO> messageTypeList = lovFeignClient.queryLovValue("PSPC.MESSAGE.TYPE", requestDTO.getTenantId());
            //获取OOC状态值集值
            List<LovValueDTO> oocStatusList = lovFeignClient.queryLovValue("PSPC.OOC.STATUS", requestDTO.getTenantId());
            //获取判异规则值集
            List<LovValueDTO> judgementCodeList = lovFeignClient.queryLovValue("PSPC.JUDGEMENT", requestDTO.getTenantId());

            for (OocResponseDTO dto : page.getContent()) {
                //处理消息类型描述
                dto.setMessageTypeDesc(messageTypeList.stream().filter(lovValueDTO -> lovValueDTO.getValue().equals(dto.getMessageTypeCode())).collect(Collectors.toList()).get(0).getMeaning());

                //OOC状态描述
                dto.setOocStatusDesc(oocStatusList.stream().filter(lovValue -> dto.getOocStatus().equals(lovValue.getValue())).collect(Collectors.toList()).get(0).getMeaning());

                //判异规则描述
                dto.setJudgementDesc(judgementCodeList.stream().filter(lovValueDTO -> dto.getJudgementCode().equals(lovValueDTO.getValue())).collect(Collectors.toList()).get(0).getMeaning());
                //处理 # $
                dto.setJudgementDesc(dto.getJudgementDesc().replace("#", dto.getLengthData().toString()).replace("$", dto.getLimitData().toString()));

                //前端要求必须有remark key
                if (dto.getRemark() == null) {
                    dto.setRemark("");
                }
            }
        }

        return page;
    }

    *//**
     * OOC修改
     *
     * @param oocUpdateDto
     * @return
     *//*
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Ooc updateOOC(OocUpdateDto oocUpdateDto) {

        logger.info("OOC修改,参数：" + redisHelper.toJson(oocUpdateDto));

        Ooc ooc = new Ooc();
        ooc.setOocId(oocUpdateDto.getOocId());
        ooc = oocRepository.selectByPrimaryKey(null, ooc);
        if (ooc == null) {
            throw new CommonException("pspc.error.ooc.not_exists");//OOC不存在
        }

        ooc.setClassifyGroupId(oocUpdateDto.getClassifyGroupId());
        ooc.setClassifyId(oocUpdateDto.getClassifyId());
        ooc.setRemark(oocUpdateDto.getRemark());
        ooc.setObjectVersionNumber(oocUpdateDto.getObjectVersionNumber());
        //状态处理
        if (oocUpdateDto.getClassifyGroupId() == null && oocUpdateDto.getClassifyId() == null && StringUtils.isEmpty(oocUpdateDto.getRemark())) {
            ooc.setOocStatus("UNPROCESSED");
        } else {
            ooc.setOocStatus("PROCESSED");
        }

        oocRepository.updateByPrimaryKey(ooc);

        logger.info("OOC修改结束，时间：" + new Date());

        return ooc;
    }

    @Override
    public List<CountOocResponseDTO> listCountOocByStatisticList(CountOocRequestDTO countOocRequestDTO) {

        //通过查询条件找到判异规则和OOC
        List<CountOocResponseDTO> oocResponseDTOList = countOocRepository.CountOOCById(countOocRequestDTO);

        //获取OOC状态值集值
        List<LovValueDTO> oocStatusList = lovFeignClient.queryLovValue("PSPC.OOC.STATUS", countOocRequestDTO.getTenantId());
        //获取判异规则值集
        List<LovValueDTO> judgementCodeList = lovFeignClient.queryLovValue("PSPC.JUDGEMENT", countOocRequestDTO.getTenantId());

        for(CountOocResponseDTO dto : oocResponseDTOList){
            //OOC状态描述
            dto.setOocStatusDesc(oocStatusList.stream().filter(lovValue -> dto.getOocStatus().equals(lovValue.getValue())).collect(Collectors.toList()).get(0).getMeaning());

            //判异规则描述
            dto.setJudgementDesc(judgementCodeList.stream().filter(lovValueDTO -> dto.getJudgementCode().equals(lovValueDTO.getValue())).collect(Collectors.toList()).get(0).getMeaning());
            //处理 # $
            dto.setJudgementDesc(dto.getJudgementDesc().replace("#", dto.getLengthData().toString()).replace("$", dto.getLimitData().toString()));

            //前端要求必须有remark key
            if (dto.getRemark() == null) {
                dto.setRemark("");
            }
        }

        return oocResponseDTOList;
    }

    @Override
    public CountOoc CountupdateOoc(CountOoc countOoc) {
        CountOoc countOoc1 = new CountOoc();
        countOoc1.setCountOocId(countOoc.getCountOocId());
        countOoc1.setClassifyGroupId(countOoc.getClassifyGroupId());
        countOoc1.setClassifyId(countOoc.getClassifyId());
        countOoc1.setRemark(countOoc.getRemark());
        countOoc1.setObjectVersionNumber(countOoc.getObjectVersionNumber());
        countOoc1.setTenantId(countOoc.getTenantId());
        countOoc1.setSiteId(countOoc.getSiteId());
        //状态处理
        if (countOoc.getClassifyGroupId() == null && countOoc.getClassifyId() == null && StringUtils.isEmpty(countOoc.getRemark())) {
            countOoc1.setOocStatus("UNPROCESSED");
        } else {
            countOoc1.setOocStatus("PROCESSED");
        }

        countOocRepository.updateByPrimaryKeySelective(countOoc1);
        return countOoc1;
    }
}
*/