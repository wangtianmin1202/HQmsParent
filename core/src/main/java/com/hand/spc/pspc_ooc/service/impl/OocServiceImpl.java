package com.hand.spc.pspc_ooc.service.impl;

import com.hand.dimension.hqm_dimension_order.dto.DimensionOrder;
import com.hand.dimension.hqm_dimension_root_cause.dto.DimensionRootCause;
import com.hand.dimension.hqm_dimension_root_cause.service.IDimensionRootCauseService;
import com.hand.dimension.hqm_dimension_step.dto.DimensionStep;
import com.hand.dimension.hqm_dimension_step.mapper.DimensionStepMapper;
import com.hand.dimension.hqm_dimension_step.service.IDimensionStepService;
import com.hand.hap.cache.impl.SysCodeCache;
import com.hand.hap.core.IRequest;
import com.hand.hap.mybatis.common.Criteria;
import com.hand.hap.mybatis.common.query.WhereField;
import com.hand.hap.system.dto.Code;
import com.hand.hap.system.dto.CodeValue;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.constants.SpcConstants;
import com.hand.spc.hqm_d_order.dto.DProblemDescription;
import com.hand.spc.hqm_d_order.dto.EightDOrder;
import com.hand.spc.hqm_d_order.service.IDProblemDescriptionService;
import com.hand.spc.hqm_d_order.service.IEightDOrderService;
import com.hand.spc.pspc_attachment.dto.SpcAttachment;
import com.hand.spc.pspc_attachment.mapper.SpcAttachmentMapper;
import com.hand.spc.pspc_attachment_group.dto.AttachmentGroup;
import com.hand.spc.pspc_attachment_group.mapper.AttachmentGroupMapper;
import com.hand.spc.pspc_attachment_relation.dto.AttachmentRelation;
import com.hand.spc.pspc_attachment_relation.mapper.AttachmentRelationMapper;
import com.hand.spc.pspc_ce_group.dto.CeGroup;
import com.hand.spc.pspc_ce_group.service.ICeGroupService;
import com.hand.spc.pspc_ce_parameter.dto.CeParameter;
import com.hand.spc.pspc_ce_parameter.mapper.CeParameterMapper;
import com.hand.spc.pspc_chart.dto.Chart;
import com.hand.spc.pspc_chart.dto.ChartDetail;
import com.hand.spc.pspc_chart.mapper.ChartDetailMapper;
import com.hand.spc.pspc_chart.mapper.ChartMapper;
import com.hand.spc.pspc_entirety_statistic.dto.EntiretyStatistic;
import com.hand.spc.pspc_entirety_statistic.mapper.EntiretyStatisticMapper;
import com.hand.spc.pspc_entity.dto.Entity;
import com.hand.spc.pspc_entity.mapper.EntityMapper;
import com.hand.spc.pspc_entity.service.IEntityService;
import com.hand.spc.pspc_judgement_group.dto.Judgement;
import com.hand.spc.pspc_judgement_group.service.IJudgementService;
import com.hand.spc.pspc_ooc.mapper.OocMapper;
import com.hand.spc.pspc_ooc.view.OocReportVO;
import com.hand.spc.utils.SpcUtil;

import org.activiti.engine.impl.util.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.owasp.esapi.util.CollectionsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.spc.pspc_ooc.dto.Ooc;
import com.hand.spc.pspc_ooc.service.IOocService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@Service
@Transactional(rollbackFor = Exception.class)
public class OocServiceImpl extends BaseServiceImpl<Ooc> implements IOocService, SpcConstants {
	
	int _index  ;
    private static final String thirdStatus = "remarked";
    @Autowired
    private OocMapper oocMapper;
    @Autowired
    private IEightDOrderService eightDOrderService;
    @Autowired
    private IEntityService entityService;
    @Autowired
    private ICeGroupService ceGroupService;
    @Autowired
    private IJudgementService judgementService;
    @Autowired
    private SysCodeCache sysCodeCache;
    @Autowired
    private IDProblemDescriptionService problemDescriptionService;
    @Autowired
    CeParameterMapper ceParameterMapper;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private ChartMapper chartMapper;
    @Autowired
    private ChartDetailMapper chartDetailMapper;
    @Autowired
    AttachmentRelationMapper attachmentRelationMapper;
    @Autowired
    SpcAttachmentMapper spcAttachmentMapper;
    @Autowired
    AttachmentGroupMapper attachmentGroupMapper;
    @Autowired
    private EntiretyStatisticMapper entiretyStatisticMapper;
	@Autowired
	IDimensionStepService iDimensionStepService;
	@Autowired
	DimensionStepMapper dimensionStepMapper;
	@Autowired
	IDimensionRootCauseService iDimensionRootCauseService;
    @Override
    public List<Ooc> saveAndChangeStatus(IRequest requestCtx, List<Ooc> dtos) {
    	
    	

        //更新时更新状态
        dtos.forEach(dto -> {
            if(dto.get__status().equals(ADD.toLowerCase())) {
                Chart chart = new Chart();
                ChartDetail chartDetail = new ChartDetail();
                EntiretyStatistic entiretyStatistic = new EntiretyStatistic();

                Long chartId = dto.getChartId();
                chart.setChartId(Float.valueOf(chartId));
                Chart chart1 = chartMapper.selectByPrimaryKey(chart);
                if(null == chart1){
                    throw new RuntimeException("未在chart表找到数据");
                }
                chartDetail.setChartId(Float.valueOf(dto.getChartId()));
                List<ChartDetail> chartDetails = chartDetailMapper.select(chartDetail);
                if(CollectionUtils.isEmpty(chartDetails)){
                    throw new RuntimeException("未在PSPC_CHART_DETAIL表找到数据");
                }

                entiretyStatistic.setSampleSubgroupId(dto.getSampleSubgroupId());
                entiretyStatistic.setEntityCode(dto.getEntityCode());
                entiretyStatistic.setEntityVersion(dto.getEntityVersion());
                List<EntiretyStatistic> entiretyStatistics = entiretyStatisticMapper.select(entiretyStatistic);
                if(CollectionUtils.isEmpty(entiretyStatistics)){
                    throw new RuntimeException("未在PSPC_ENTIRETY_STATISTIC表找到数据");
                }

                dto.setMaxPlotPoints(chart1.getMaxPlotPoints());
                dto.setTickLabelX(chart1.getTickLabelX());
                dto.setAxisLabelX(chartDetails.get(0).getAxisLabelX());
                dto.setAxisLabelY(chartDetails.get(0).getAxisLabelY());
                dto.setUpperControlLimit(entiretyStatistics.get(0).getEntiretyUcl());
                dto.setCenterLine(entiretyStatistics.get(0).getEntiretyCl());
                dto.setLowerControlLimit(entiretyStatistics.get(0).getEntiretyLcl());
                dto.setUpperSpecLimit(entiretyStatistics.get(0).getEntiretyUsl());
                dto.setLowerSpecLimit(entiretyStatistics.get(0).getEntiretyLsl());
            }else{
                if (null == dto.getClassifyGroupId() && null == dto.getClassifyId() && StringUtils.isEmpty(dto.getRemark())) {
                    dto.setOocStatus(UNPROCESSED);
                } else {
                    dto.setOocStatus(PROCESSED);
                }
            }
        });
        return self().batchUpdate(requestCtx,dtos);
    }

    /**
     *
     * @Description OOC报表查询
     *
     * @author yuchao.wang
     * @date 2019/8/29 22:10
     * @param requestContext
     * @param dto
     * @param page
     * @param pageSize
     * @return java.util.List<com.hand.spc.pspc_ooc.view.OocReportVO>
     *
     */
    @Override
    public List<OocReportVO> queryOocReport(IRequest requestContext, OocReportVO dto, int page, int pageSize) {
        //分页查询
        //PageHelper.startPage(page, pageSize);
        List<OocReportVO> oocReportVOList = oocMapper.queryOocReport(dto);

        return oocReportVOList;
    }

    @Override
    public ResponseData createDReport(IRequest requestContext, List<OocReportVO> dtos) {
    	//
    	Long item_id =null; 
    	String item_code=null;
    	String item_desc=null;
    	Long item_id_component =null;
    	String item_component_code=null;
    	String item_component_des=null;
    	Long prod_line_id = null;
    	String  Prodline_des=null;
    	String Prodline_code=null;
    	String jugdement=null;
    	String type =null;
    	String supplier_code =null;
    	String supplier_des =null;
        String orderStatus = "open";
        Date date = new Date();
        String format = "yyyyMMdd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        //当前流水号
        Long seqInter = eightDOrderService.getSeqInter();
        seqInter = null == seqInter?1L:seqInter;
        for (OocReportVO dto:dtos) {
            //存头表
            EightDOrder eightDOrder = new EightDOrder();
            eightDOrder.setOrderTheme(dto.getEntityCode());

            eightDOrder.setOrderCode("8D-"+ simpleDateFormat.format(date) + "-"+SpcUtil.getZeroNumber(seqInter,3));
            eightDOrder.setOrderStatus(orderStatus);
            eightDOrder.setOrderProcess("0");
            eightDOrder.setSourceType("5");
            eightDOrderService.insertSelective(requestContext,eightDOrder);

            //查询物料id和发生地点
            Entity entity = new Entity();
            entity.setEntityCode(dto.getEntityCode());
            entity.setEntityVersion(dto.getEntityVersion());
            List<Entity> entities = entityMapper.select(entity);
 
            if(CollectionUtil.isEmpty(entities)){
                throw new RuntimeException("未找到对应实体控制图信息，entityCode-"+dto.getEntityCode()+",entityVersion-"+dto.getEntityVersion());
            }
            CeGroup ceGroup = new CeGroup();
            ceGroup.setCeGroupId(entities.get(0).getCeGroupId());
            ceGroup = ceGroupService.selectByPrimaryKey(requestContext,ceGroup);
            if(ceGroup.getType()!=null)
            {     	
            	type=ceGroup.getType();
            }
            boolean  fg= ceGroup.getType().contains("QC");
            
            CeParameter ceParamenter_p =new CeParameter();
            ceParamenter_p.setCeParameterId(entities.get(0).getCeParameterId());
            ceParamenter_p =ceParameterMapper.selectByPrimaryKey(ceParamenter_p);
            if(ceParamenter_p.getCeParameter()!=null)
            {
            	jugdement =ceParamenter_p.getCeParameter();
            }
            if(ceParamenter_p.getCeParameterName()!=null)
            {
            	jugdement =ceParamenter_p.getCeParameter()+" /"+ceParamenter_p.getCeParameterName();
            }
           
            
            //判断type 来确定 ITEM_ID
            if(ceGroup.getType()==null)
            {
            	item_id = entities.get(0).getCeParameterId();
            }  
            else if(ceGroup.getType().equals("PQC")||ceGroup.getType().equals("MES"))
            {
            	item_id = entities.get(0).getCeParameterId();
            }
            else if(ceGroup.getType().equals("FQC"))
            {
            	item_id = ceGroup.getCeGroupId();
            }
            else 
            {       	
            	item_id =null;
            }
            //判断 item_code 和 desc
            if(item_id==null)
            {
            	item_code =null;
            	item_desc =null;
            }
            else if(ceGroup.getType()==null )
            {
            	CeParameter ceParamenter_i =new CeParameter();
            	ceParamenter_i.setCeParameterId(entities.get(0).getCeParameterId());
            	ceParamenter_i =ceParameterMapper.selectByPrimaryKey(ceParamenter_i);
            	item_code =ceParamenter_i.getCeParameter();
            	item_desc =ceParamenter_i.getCeParameterName();           	
            }
            else if( ceGroup.getType().equals("MES"))
            {
            	CeParameter ceParamenter_i =new CeParameter();
            	ceParamenter_i.setCeParameterId(entities.get(0).getCeParameterId());
            	ceParamenter_i =ceParameterMapper.selectByPrimaryKey(ceParamenter_i);
            	item_code =ceParamenter_i.getCeParameter();
            	item_desc =ceParamenter_i.getCeParameterName();           	
            }else if(fg)
            {
            	AttachmentGroup attachmentGroup_i = new AttachmentGroup();
            	attachmentGroup_i.setCeGroupId(entities.get(0).getCeGroupId());
            	List<AttachmentGroup> lCG =attachmentGroupMapper.select(attachmentGroup_i);
            	AttachmentRelation attachmentRelation_i =new AttachmentRelation();
            	attachmentRelation_i.setAttachmentGroupId(lCG.get(0).getAttachmentGroupId());
            	List<AttachmentRelation> lAR=attachmentRelationMapper.select(attachmentRelation_i);           	
            	SpcAttachment spcAttachment  =new SpcAttachment();
            	spcAttachment.setAttachmentId(lAR.get(0).getAttachmentId());
            	spcAttachment =spcAttachmentMapper.selectByPrimaryKey(lAR.get(0).getAttachmentId());
            	if(spcAttachment.getAttachmentType().equals("FQC"))
            	{
            		 String des = spcAttachment.getDescription();
            		 String coDe =spcAttachment.getAttachmentCode();
            		 item_desc=des.substring(des.indexOf(";"),des.indexOf(";",des.indexOf(";")+1));
            		 item_code=coDe.substring(coDe.indexOf("-"), coDe.indexOf("-",coDe.indexOf("-")+1));
            	}
            	else if(spcAttachment.getAttachmentType().equals("PQC"))
            	{
            		CeParameter ceParamenter_i =new CeParameter();
                	ceParamenter_i.setCeParameterId(entities.get(0).getCeParameterId());
                	ceParamenter_i =ceParameterMapper.selectByPrimaryKey(ceParamenter_i);
                	item_code =ceParamenter_i.getCeParameter();
                	item_desc =ceParamenter_i.getCeParameterName();         
            	}
            	else
            	{
            		item_code =null;
                	item_desc =null;
            	}
            }
            //判断item_id_component
            if(ceGroup.getType()==null)
            {
            	item_id_component = null;
            }  
            else  if( ceGroup.getType().equals("PQC")||ceGroup.getType().equals("MES")||ceGroup.getType().equals("FQC"))
            {
            	item_id_component = null;
            }
            else if(ceGroup.getType().equals("IQC"))
            {
            	item_id_component = ceGroup.getCeGroupId();
            }
            //判断item_CODE_component
            
            if(item_id_component == null)
            {
            	item_component_code =null;
            	item_component_des =null;
            }
            else if(fg)
            {
            	AttachmentGroup attachmentGroup_i = new AttachmentGroup();
            	attachmentGroup_i.setCeGroupId(entities.get(0).getCeGroupId());
            	List<AttachmentGroup> lCG =attachmentGroupMapper.select(attachmentGroup_i);
            	AttachmentRelation attachmentRelation_i =new AttachmentRelation();
            	attachmentRelation_i.setAttachmentGroupId(lCG.get(0).getAttachmentGroupId());
            	List<AttachmentRelation> lAR=attachmentRelationMapper.select(attachmentRelation_i);           	
            	SpcAttachment spcAttachment  =new SpcAttachment();
            	spcAttachment.setAttachmentId(lAR.get(0).getAttachmentId());
            	spcAttachment =spcAttachmentMapper.selectByPrimaryKey(lAR.get(0).getAttachmentId());
            	if(spcAttachment.getAttachmentType().equals("IQC"))
            	{
            		 String des = spcAttachment.getDescription();
            		 String coDe =spcAttachment.getAttachmentCode();
            		 item_component_des=des.substring(des.indexOf(";")+1,des.indexOf(";",des.indexOf(";")+1));
            		 item_component_code=coDe.substring(coDe.indexOf("-")+1, coDe.indexOf("-",coDe.indexOf("-")+1));
            	}
            	else
            	{
            		item_component_code =null;
            		item_component_des =null;
            	}
            }
            //判断PRODLINEid
            if(ceGroup.getType()==null)
            {
            	prod_line_id = null;
            }
            else if(ceGroup.getType().equals("PQC")||ceGroup.getType().equals("MES")||ceGroup.getType().equals("FQC"))
            {
            	prod_line_id = ceGroup.getCeGroupId();
            }
            else if(ceGroup.getType().equals("IQC"))
            {
            	prod_line_id  = null;
            }
        //判断prodlineCODE 
            
            if(prod_line_id==null)
            {
            	Prodline_des =null;
            	Prodline_code =null;
            }
            else if(ceGroup.getType()==null)
            {
            	AttachmentGroup attachmentGroup_i = new AttachmentGroup();
            	attachmentGroup_i.setCeGroupId(entities.get(0).getCeGroupId());
            	List<AttachmentGroup> lCG =attachmentGroupMapper.select(attachmentGroup_i);
            	AttachmentRelation attachmentRelation_i =new AttachmentRelation();
            	attachmentRelation_i.setAttachmentGroupId(lCG.get(0).getAttachmentGroupId());
            	List<AttachmentRelation> lAR=attachmentRelationMapper.select(attachmentRelation_i);           	
            	SpcAttachment spcAttachment  =new SpcAttachment();
            	spcAttachment.setAttachmentId(lAR.get(0).getAttachmentId());
            	spcAttachment =spcAttachmentMapper.selectByPrimaryKey(lAR.get(0).getAttachmentId());
            	String des = spcAttachment.getDescription();
            	String coDe =spcAttachment.getAttachmentCode();
            	Prodline_des=des.substring(des.indexOf(";")+1,des.indexOf(";",des.indexOf(";")+1));
            	Prodline_code=coDe.substring(coDe.indexOf("-")+1, coDe.indexOf("-",coDe.indexOf("-")+1));       	
            }
            else if(ceGroup.getType().equals("MES"))
            {
            	AttachmentGroup attachmentGroup_i = new AttachmentGroup();
            	attachmentGroup_i.setCeGroupId(entities.get(0).getCeGroupId());
            	Long iii =entities.get(0).getCeGroupId();
            	List<AttachmentGroup> lCG =attachmentGroupMapper.select(attachmentGroup_i);
            	AttachmentRelation attachmentRelation_i =new AttachmentRelation();
            	attachmentRelation_i.setAttachmentGroupId(lCG.get(0).getAttachmentGroupId());
            	List<AttachmentRelation> lAR=attachmentRelationMapper.select(attachmentRelation_i);           	
            	SpcAttachment spcAttachment  =new SpcAttachment();
            	spcAttachment.setAttachmentId(lAR.get(0).getAttachmentId());
            	spcAttachment =spcAttachmentMapper.selectByPrimaryKey(lAR.get(0).getAttachmentId());
            	String des = spcAttachment.getDescription();
            	String coDe =spcAttachment.getAttachmentCode();
            	Prodline_des=des.substring(des.indexOf(";")+1,des.indexOf(";",des.indexOf(";")+1));
            	Prodline_code=coDe.substring(coDe.indexOf("-")+1, coDe.indexOf("-",coDe.indexOf("-")+1));       	
            }else if(fg)
            {
            	AttachmentGroup attachmentGroup_i = new AttachmentGroup();
            	attachmentGroup_i.setCeGroupId(entities.get(0).getCeGroupId());
            	List<AttachmentGroup> lCG =attachmentGroupMapper.select(attachmentGroup_i);
            	AttachmentRelation attachmentRelation_i =new AttachmentRelation();
            	attachmentRelation_i.setAttachmentGroupId(lCG.get(0).getAttachmentGroupId());
            	List<AttachmentRelation> lAR=attachmentRelationMapper.select(attachmentRelation_i);           	
            	SpcAttachment spcAttachment  =new SpcAttachment();
            	spcAttachment.setAttachmentId(lAR.get(0).getAttachmentId());
            	spcAttachment =spcAttachmentMapper.selectByPrimaryKey(lAR.get(0).getAttachmentId());
            	if(spcAttachment.getAttachmentType().equals("PQC"))
            	{
            		 String des = spcAttachment.getDescription();
            		 String coDe =spcAttachment.getAttachmentCode();
            		 Prodline_des=des.substring(des.indexOf(";")+1,des.indexOf(";",des.indexOf(";")+1));
            		 Prodline_code=coDe.substring(coDe.indexOf("-")+1, coDe.indexOf("-",coDe.indexOf("-")+1));
            	}
            	else
            	{
            		Prodline_code =null;
            		Prodline_des =null;
            	}
            }
            
            if(ceGroup.getType().equals("IQC"))
            {
            	AttachmentGroup attachmentGroup_i = new AttachmentGroup();
            	attachmentGroup_i.setCeGroupId(entities.get(0).getCeGroupId());
            	List<AttachmentGroup> lCG =attachmentGroupMapper.select(attachmentGroup_i);
            	AttachmentRelation attachmentRelation_i =new AttachmentRelation();
            	attachmentRelation_i.setAttachmentGroupId(lCG.get(0).getAttachmentGroupId());
            	List<AttachmentRelation> lAR=attachmentRelationMapper.select(attachmentRelation_i);           	
            	SpcAttachment spcAttachment  =new SpcAttachment();
            	spcAttachment.setAttachmentId(lAR.get(0).getAttachmentId());
            	spcAttachment =spcAttachmentMapper.selectByPrimaryKey(lAR.get(0).getAttachmentId());
            	String des_s = spcAttachment.getDescription();
            	String coDe_S =spcAttachment.getAttachmentCode();
            	getindex( des_s,";");
            	supplier_des=des_s.substring( des_s.indexOf(";",des_s.indexOf(";")+1)+1,_index);
            	getindex(coDe_S ,"-");
            	
            	supplier_code=coDe_S.substring(  coDe_S.indexOf("-",coDe_S.indexOf("-")+1)+1,_index);       	
            }
            
            
            //查询问题描述
            Judgement judgement = new Judgement();
            judgement.setJudgementId(dto.getJudgementId());
            judgement = judgementService.selectByPrimaryKey(requestContext,judgement);
            if(null == judgement){
                throw new RuntimeException("未在judgement表中找到判断信息");
            }
            Code cacheValue = sysCodeCache.getValue("PSPC.JUDGEMENT" + "." + DEFAULT_LANG);

            
            Judgement finalJudgement = judgement;
            String meanning =type+" /"+ jugdement+" /"+cacheValue.getCodeValues().stream().filter(code -> code.getValue().equals(finalJudgement.getJudgementCode()))
                    .findAny().orElseThrow(() -> new RuntimeException("未在快码PSPC.JUDGEMENT找到对应含义，value-"+finalJudgement.getJudgementCode())).getMeaning();

            if(null == dto.getClassifyId()){
                //throw new RuntimeException("缺失数据-不良代码");
            }
            //存行明细表
            DProblemDescription problemDescription = new DProblemDescription();
            problemDescription.setOrderId(eightDOrder.getOrderId());
            problemDescription.setItemId(item_id);
            problemDescription.setItemCode(item_code);
            problemDescription.setItemCodeDes(item_desc);
            problemDescription.setItemIdComponent(item_id_component);
            problemDescription.setItemComponentCode(item_component_code);
            problemDescription.setItemComponentDes(item_component_des);
            problemDescription.setProdlineCode(Prodline_code);
            problemDescription.setProdlineDes(Prodline_des);                   
            problemDescription.setOccurTime(new Date());
            problemDescription.setOccurPlace(ceGroup.getDescription());
            problemDescription.setProdLineId(prod_line_id);
            problemDescription.setNgGroupId(dto.getCeGroupId());
            //problemDescription.setNgMemberId(dto.getClassifyId().longValue());
            problemDescription.setProblemDescription(meanning);
            problemDescription.setProblemSource("3");
            problemDescription.setSupplierCode(supplier_code);
            problemDescription.setSupplierDes(supplier_des);
            
            problemDescription= problemDescriptionService.insertSelective(requestContext,problemDescription);

            seqInter++;
            
            DimensionOrder d =new DimensionOrder();
            d.setOrderId(Float.valueOf(eightDOrder.getOrderId()));
            eightDOrder(requestContext,d);
            createDimensionRootCause(requestContext,d);
        }
       
    	
        return new ResponseData();
    }
    
    public void getindex(String des,String s)
    {
    int count=0;
    for(int i=0;i<des.length();i++)
    {
        if(s.equals(String.valueOf(des.charAt(i))))
       {
          count++;
          if(count==3)
          {
        	  _index= i;
        	  break;
          }
          else
          {
        	  _index=  des.length()-1;
          }
       }

    }
	

    }
    
    public void eightDOrder(IRequest requestContext, DimensionOrder dto) {
		for (float i = 0; i < 9; i++) {
			if (i == 0) {
				DimensionStep adder = new DimensionStep();
				adder.setOrderId(dto.getOrderId());
				adder.setStatus("2");
				adder.setStep(i);
				iDimensionStepService.insertSelective(requestContext, adder);
				continue;
			}
			DimensionStep adder = new DimensionStep();
			adder.setOrderId(dto.getOrderId());
			adder.setStatus("0");
			adder.setStep(i);
			iDimensionStepService.insertSelective(requestContext, adder);
		}
	}
    
    public void createDimensionRootCause(IRequest requestContext, DimensionOrder dto) {
		DimensionRootCause dimen = new DimensionRootCause();
		dimen.setOrderId(dto.getOrderId());
		String rootCause = "Question\n" + "-人/机\n"  + "-料\n" + "-法\n" + "-环\n" ;
		dimen.setRootCause(rootCause);
		iDimensionRootCauseService.insertSelective(requestContext, dimen);
	}
}