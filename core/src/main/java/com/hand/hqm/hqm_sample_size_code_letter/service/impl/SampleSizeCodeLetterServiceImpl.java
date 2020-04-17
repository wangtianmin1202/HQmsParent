package com.hand.hqm.hqm_sample_size_code_letter.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.annotation.StdWho;
import com.hand.hap.system.dto.BaseDTO;
import com.hand.hap.system.dto.DTOStatus;
import com.hand.hap.system.service.IBaseService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hqm.hqm_sample_size_code_letter.mapper.SampleSizeCodeLetterMapper;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_sample_size_code_letter.dto.SampleSizeCodeLetter;
import com.hand.hqm.hqm_sample_size_code_letter.service.ISampleSizeCodeLetterService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class SampleSizeCodeLetterServiceImpl extends BaseServiceImpl<SampleSizeCodeLetter> implements ISampleSizeCodeLetterService {
    @Autowired
    SampleSizeCodeLetterMapper sampleSizeCodeLetterMapper;

    @Override
    public List<SampleSizeCodeLetter> myselect(IRequest requestContext, SampleSizeCodeLetter dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return sampleSizeCodeLetterMapper.myselect(dto);
    }

    @Override
    public List<SampleSizeCodeLetter> LevelUpdate(IRequest request,@StdWho List<SampleSizeCodeLetter> list) {
//        IBaseService<SampleSizeCodeLetter> self = ((IBaseService<SampleSizeCodeLetter>) AopContext.currentProxy());
        for (SampleSizeCodeLetter t : list) {
            String[] sampleString = {t.getInspectionLevelsOne(), t.getInspectionLevelsTwo(), t.getInspectionLevelsThree(), t.getInspectionLevelsFour(), t.getInspectionLevelsFive(), t.getInspectionLevelsSix(), t.getInspectionLevelsSeven()};
            switch (((BaseDTO) t).get__status()) {
                case DTOStatus.ADD:
//                    self.insertSelective(request, t);
                    for (int i = 0; i < 7; i++) {
                        SampleSizeCodeLetter thisDto = new SampleSizeCodeLetter();
                        thisDto.setInspectionLevels(String.valueOf(i));
                        thisDto.setSizeCodeLetter(sampleString[i]);
                        thisDto.setSampleProcedureType(t.getSampleProcedureType());
                        thisDto.setLotSizeFrom(t.getLotSizeFrom());
                        thisDto.setLotSizeTo(t.getLotSizeTo());
                        thisDto.setEnableFlag(t.getEnableFlag());
                        self().insertSelective(request, thisDto);
                        t.setLetterId(thisDto.getLetterId());
                    }
                    break;
                case DTOStatus.UPDATE:
                    SampleSizeCodeLetter searchModel = new SampleSizeCodeLetter();
                    searchModel.setLotSizeFrom(t.getLotSizeFrom());
                    searchModel.setLotSizeTo(t.getLotSizeTo());
                    if (useSelectiveUpdate()) {
                        for (SampleSizeCodeLetter ssl : select(request, searchModel, 1, 100)) {
                            self().updateByPrimaryKeySelective(request, ssl);
                            t.setEnableFlag(ssl.getEnableFlag());
                        }
                    }
                    break;
                case DTOStatus.DELETE:
                    self().deleteByPrimaryKey(t);
                    break;
                default:
                    break;
            }
        }
        return list;
    }

	@Override
	public List<SampleSizeCodeLetter> selectSandardCode() {
		// TODO Auto-generated method stub
		return sampleSizeCodeLetterMapper.selectSandardCode();
	}

	@Override
	public List<SampleSizeCodeLetter> selectCodeLevel(SampleSizeCodeLetter dto) {
		// TODO Auto-generated method stub
		return sampleSizeCodeLetterMapper.selectCodeLevel(dto);
	}
}