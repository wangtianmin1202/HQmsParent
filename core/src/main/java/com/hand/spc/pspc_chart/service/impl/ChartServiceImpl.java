package com.hand.spc.pspc_chart.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.his.dto.*;
import com.hand.spc.his.service.*;
import com.hand.spc.pspc_chart.dto.ChartDetail;
import com.hand.spc.pspc_chart.mapper.ChartMapper;
import com.hand.spc.pspc_chart.service.IChartDetailService;
import com.hand.spc.pspc_count_ooc.dto.CountOoc;
import com.hand.spc.pspc_count_ooc.service.ICountOocService;
import com.hand.spc.pspc_count_statistic.dto.CountStatistic;
import com.hand.spc.pspc_count_statistic.service.ICountStatisticService;
import com.hand.spc.pspc_entirety_statistic.dto.EntiretyStatistic;
import com.hand.spc.pspc_entirety_statistic.service.IEntiretyStatisticService;
import com.hand.spc.pspc_entity.dto.Entity;
import com.hand.spc.pspc_entity.service.IEntityService;
import com.hand.spc.pspc_entity_role_relation.dto.EntityRoleRelation;
import com.hand.spc.pspc_entity_role_relation.service.IEntityRoleRelationService;
import com.hand.spc.pspc_message.dto.MessageL;
import com.hand.spc.pspc_message.service.IMessageServiceL;
import com.hand.spc.pspc_message_detail.dto.MessageDetail;
import com.hand.spc.pspc_message_detail.service.IMessageDetailService;
import com.hand.spc.pspc_ooc.dto.Ooc;
import com.hand.spc.pspc_ooc.service.IOocService;
import com.hand.spc.pspc_sample_subgroup.dto.SampleSubgroup;
import com.hand.spc.pspc_sample_subgroup.service.ISampleSubgroupService;
import com.hand.spc.pspc_sample_subgroup_rel.dto.SampleSubgroupRel;
import com.hand.spc.pspc_sample_subgroup_rel.service.ISampleSubgroupRelService;
import com.hand.spc.pspc_subgroup_statistic.dto.SubgroupStatistic;
import com.hand.spc.pspc_subgroup_statistic.service.ISubgroupStatisticService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.spc.pspc_chart.dto.Chart;
import com.hand.spc.pspc_chart.service.IChartService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Collections;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class ChartServiceImpl extends BaseServiceImpl<Chart> implements IChartService {

    @Autowired
    private ChartMapper chartMapper;

    @Autowired
    private IChartDetailService chartDetailService;

    @Autowired
    private IEntityService entityService;

    @Autowired
    private IEntityHisService entityHisService;

    @Autowired
    private IEntityRoleRelationHisService entityRoleRelationHisService;

    @Autowired
    private IOocHisService oocHisService;

    @Autowired
    private ISampleSubgroupHisService sampleSubgroupHisService;

    @Autowired
    private ISampleSubgroupRelHisService sampleSubgroupRelHisService;

    @Autowired
    private ISubgroupStatisticHisService subgroupStatisticHisService;

    @Autowired
    private IEntiretyStatisticHisService entiretyStatisticHisService;

    @Autowired
    private IMessageHisService messageHisService;

    @Autowired
    private IMessageDetailHisService messageDetailHisService;

    @Autowired
    private ICountOocHisService countOocHisService;

    @Autowired
    private ICountStatisticHisService countStatisticHisService;

    @Autowired
    private IEntityRoleRelationService entityRoleRelationService;

    @Autowired
    private IOocService oocService;

    @Autowired
    private ISampleSubgroupService sampleSubgroupService;

    @Autowired
    private ISampleSubgroupRelService sampleSubgroupRelService;

    @Autowired
    private ISubgroupStatisticService subgroupStatisticService;

    @Autowired
    private IEntiretyStatisticService entiretyStatisticService;

    @Autowired
    private IMessageServiceL messageService;

    @Autowired
    private IMessageDetailService messageDetailService;

    @Autowired
    private ICountOocService countOocService;

    @Autowired
    private ICountStatisticService countStatisticService;

    /**
     * @param iRequest 基本参数
     * @param chart    限制条件
     * @param page     页数
     * @param pageSize 页数大小
     * @return : java.util.List<com.hand.spc.pspc_chart.dto.Chart>
     * @Description: 基础数据查询
     * @author: ywj
     * @date 2019/8/19 9:54
     * @version 1.0
     */
    @Override
    public List<Chart> queryBaseData(IRequest iRequest, Chart chart, int page, int pageSize) {

        // 分页
        PageHelper.startPage(page, pageSize);

        // 数据查询
        return chartMapper.queryBaseData(chart);
    }


    /**
     * @param requestCtx 基本参数
     * @param chartList  传入参数
     * @return : com.hand.hap.system.dto.ResponseData
     * @Description: 数据保存
     * @author: ywj
     * @date 2019/8/19 10:32
     * @version 1.0
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseData saveBaseData(IRequest requestCtx, List<Chart> chartList) {

        ResponseData responseData = new ResponseData();

        for (Chart item : chartList) {
            if(item.getChartId()!=null){
                item.set__status("update");
            }else {
                item.set__status("add");
            }

            // 数据更新判断
            responseData = checkSubGroup(requestCtx, item);
            if(!responseData.isSuccess()){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return  responseData;
            }

            // 判断 控制图赋值是否有值
            if (item.getChartTypeControl() != null) {

                // 判断复制的类型和当前的类型是否相同 ，不同则报错
                if (!item.getChartType().equals(item.getControlChartType())) {

                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    responseData.setSuccess(false);
                    responseData.setMessage("控制图类型不同，无法复制！");
                    return responseData;

                } else {

                    // 将要复制数据的明细全部复制到新建/修改的数据上
                    ChartDetail chartDetail = new ChartDetail();
                    chartDetail.setChartId(item.getChartTypeControl());
                    List<ChartDetail> chartDetailList = chartDetailService.select(requestCtx, chartDetail, 0, 0);

                    //  先更新数据
                    item = batchUpdate(requestCtx, Collections.singletonList(item)).get(0);

                    // 再 插入 明细数据 (此处因为 控制图复制时无法在修改的时候使用 因此不考虑修改逻辑 只有插入)
                    Chart finalItem = item;
                    chartDetailList.forEach(detail -> {
                        detail.setChartId(finalItem.getChartId());
                        detail.setChartDetailId(null);
                        chartDetailService.insertSelective(requestCtx, detail);
                    });

                }
                item.setChartTypeControl(null);
                item.setEnableFlagSub(null);
                item.setEnableFlagType(null);

            } else {
                // 直接更新数据
                batchUpdate(requestCtx, Collections.singletonList(item)).get(0);
                item.setChartTypeControl(null);
                item.setEnableFlagSub(null);
                item.setEnableFlagType(null);
            }
        }

        responseData.setSuccess(true);
        responseData.setRows(chartList);

        return responseData;
    }


    /**
     * @param chart 限制条件
     * @return : void
     * @Description: 判断子组大小是否更新
     * @author: ywj
     * @date 2019/8/19 17:54
     * @version 1.0
     */
    private ResponseData checkSubGroup(IRequest request, Chart chart) {

        ResponseData responseData = new ResponseData();
        //  只找更新数据
        if ("update".equals(chart.get__status())) {
            Chart chartRes = new Chart();
            chartRes.setChartId(chart.getChartId());
            chartRes = chartMapper.selectByPrimaryKey(chartRes);
            if (!chartRes.getSubgroupSize().equals(chart.getSubgroupSize())) {
                // 判断entity表是否绑定了该数据
                Entity entity = new Entity();
                entity.setChartId(chart.getChartId().longValue());
                List<Entity> entityList = entityService.select(request, entity, 0, 0);


                //校验数据
                if (!"Y".equals(chart.getEnableFlagSub())) {
                    if (entityList.size() > 0) {

                        responseData.setSuccess(false);
                        responseData.setMessage("A");
                        return responseData;
                    }
                } else {

                    // 记录历史
                    recordHis(request, entityList.get(0));
                }

            }

            if (!chartRes.getChartType().equals(chart.getChartType())) {

                // 判断entity表是否绑定了该数据
                Entity entity = new Entity();
                entity.setChartId(chart.getChartId().longValue());
                List<Entity> entityList = entityService.select(request, entity, 0, 0);

                //校验数据
                if (!"Y".equals(chart.getEnableFlagType())) {

                    // 判断是否跨域
                    if ("nP".equals(chartRes.getChartType()) || "P".equals(chartRes.getChartType()) || "C".equals(chartRes.getChartType()) || "U".equals(chartRes.getChartType())) {

                        //如果跨域则报错 设置类型为C
                        if (!"nP".equals(chart.getChartType()) && !"P".equals(chart.getChartType()) && !"C".equals(chart.getChartType()) && !"U".equals(chart.getChartType())) {

                            responseData.setSuccess(false);
                            responseData.setMessage("C");
                            return responseData;
                        }
                    } else {
                        if ("nP".equals(chart.getChartType()) || "P".equals(chart.getChartType()) || "C".equals(chart.getChartType()) || "U".equals(chart.getChartType())) {

                            //如果跨域则报错 设置类型为C
                            responseData.setSuccess(false);
                            responseData.setMessage("C");
                            return responseData;
                        }
                    }

                        responseData.setSuccess(false);
                        responseData.setMessage("B");
                        return responseData;
                } else {
                    // 记录历史
                    if(entityList.size()>0) {
                        recordHis(request, entityList.get(0));
                    }
                }
            }
        }

        responseData.setSuccess(true);
        return responseData;

    }

    /**
     * @param requestCtx 基本参数
     * @param entity     传入参数
     * @return : void
     * @Description: 历史记录
     * @author: ywj
     * @date 2019/8/20 10:13
     * @version 1.0
     */
    private void recordHis(IRequest requestCtx, Entity entity) {
        //  复制原有数据
        Entity entityHis = new Entity();
        BeanUtils.copyProperties(entity, entityHis);

        // 删除数据
        entityService.deleteByPrimaryKey(entityHis);

        // 新增数据 将版本加1
        entity.setEntityVersion(String.valueOf(Long.valueOf(entity.getEntityVersion()) + 1));
        entityService.insertSelective(requestCtx, entity);

        // 记录历史
        EntityHis his = new EntityHis();
        BeanUtils.copyProperties(entityHis, his);
        his.setEntityId(entityHis.getEntityId().floatValue());
        his.setTenantId(entityHis.getTenantId().floatValue());
        his.setSiteId(entityHis.getSiteId().floatValue());
        his.setAttachmentGroupId(entityHis.getAttachmentGroupId().floatValue());
        his.setCeParameterId(entityHis.getCeParameterId().floatValue());
        his.setCeGroupId(entityHis.getCeGroupId().floatValue());
        his.setChartId(entityHis.getChartId().floatValue());

        entityHisService.insertSelective(requestCtx, his);

         entity.setEntityVersion(String.valueOf(Long.valueOf(entity.getEntityVersion()) - 1));
//        EntityRoleRelation  entityRoleRelation = new EntityRoleRelation();
//        entityRoleRelation.setEntityCode(entity.getEntityCode());
//        entityRoleRelation.setEntityVersion(entity.getEntityVersion());
//        List<EntityRoleRelation> entityRoleRelationList = entityRoleRelationService.select(requestCtx,entityRoleRelation,0,0);
//
//        for (int i = 0; i < entityRoleRelationList.size(); i++) {
//
//            entityRoleRelation = new EntityRoleRelation();
//            entityRoleRelation.setObjectVersionNumber(null);
//            entityRoleRelation.setEntityRoleRelationId(entityRoleRelationList.get(i).getEntityRoleRelationId());
//            // 删除数据
//            entityRoleRelationService.deleteByPrimaryKey(entityRoleRelation);
//
//            // 记录历史
//            EntityRoleRelationHis entityRoleRelationHis = new EntityRoleRelationHis();
//            BeanUtils.copyProperties(entityRoleRelation, entityRoleRelationHis);
//            entityRoleRelationHisService.insertSelective(requestCtx, entityRoleRelationHis);
//
//        }

        Ooc ooc = new Ooc();
        ooc.setEntityCode(entity.getEntityCode());
        ooc.setEntityVersion(entity.getEntityVersion());
        List<Ooc> oocList = oocService.select(requestCtx,ooc,0,0);

        for (int i = 0; i < oocList.size(); i++) {

            ooc = new Ooc();
            ooc.setOocId(oocList.get(i).getOocId());
            // 删除数据
            oocService.deleteByPrimaryKey(ooc);

            // 记录历史
            OocHis oocHis = new OocHis();
            BeanUtils.copyProperties(oocList.get(i), oocHis);
            oocHis.setOocId(oocList.get(i).getOocId());
            oocHisService.insertSelective(requestCtx, oocHis);

        }

        SampleSubgroup sampleSubgroup = new SampleSubgroup();
        sampleSubgroup.setEntityCode(entity.getEntityCode());
        sampleSubgroup.setEntityVersion(entity.getEntityVersion());
        List<SampleSubgroup> sampleSubgroupList = sampleSubgroupService.select(requestCtx,sampleSubgroup,0,0);

        for (int i = 0; i < sampleSubgroupList.size(); i++) {

            sampleSubgroup = new SampleSubgroup();
            sampleSubgroup.setSampleSubgroupId(sampleSubgroupList.get(i).getSampleSubgroupId());

            // 删除数据
            sampleSubgroupService.deleteByPrimaryKey(sampleSubgroup);

            // 记录历史
            SampleSubgroupHis sampleSubgroupHis = new SampleSubgroupHis();
            BeanUtils.copyProperties(sampleSubgroupList.get(i), sampleSubgroupHis);
            sampleSubgroupHis.setSampleSubgroupId(sampleSubgroupList.get(i).getSampleSubgroupId().floatValue());
            sampleSubgroupHis.setTenantId(sampleSubgroupList.get(i).getTenantId().floatValue());
            sampleSubgroupHis.setSiteId(sampleSubgroupList.get(i).getSiteId().floatValue());
            sampleSubgroupHis.setSubgroupNum(sampleSubgroupList.get(i).getSubgroupNum().floatValue());
            sampleSubgroupHis.setExistNum(sampleSubgroupList.get(i).getExistNum().floatValue());

            sampleSubgroupHisService.insertSelective(requestCtx, sampleSubgroupHis);

        }

        SampleSubgroupRel sampleSubgroupRel = new SampleSubgroupRel();
        sampleSubgroupRel.setEntityCode(entity.getEntityCode());
        sampleSubgroupRel.setEntityVersion(entity.getEntityVersion());
        List<SampleSubgroupRel> sampleSubgroupRelList = sampleSubgroupRelService.select(requestCtx,sampleSubgroupRel,0,0);

        for (int i = 0; i < sampleSubgroupRelList.size(); i++) {

            sampleSubgroupRel = new SampleSubgroupRel();
            sampleSubgroupRel.setSampleSubgroupRelationId(sampleSubgroupRelList.get(i).getSampleSubgroupRelationId());

            // 删除数据
            sampleSubgroupRelService.deleteByPrimaryKey(sampleSubgroupRel);

            // 记录历史
            SampleSubgroupRelHis sampleSubgroupRelHis = new SampleSubgroupRelHis();
            BeanUtils.copyProperties(sampleSubgroupRelList.get(i), sampleSubgroupRelHis);
            sampleSubgroupRelHis.setSampleSubgroupRelId(sampleSubgroupRelList.get(i).getSampleSubgroupId());
            sampleSubgroupRelHisService.insertSelective(requestCtx, sampleSubgroupRelHis);
        }

        SubgroupStatistic subgroupStatistic = new SubgroupStatistic();
        subgroupStatistic.setEntityCode(entity.getEntityCode());
        subgroupStatistic.setEntityVersion(entity.getEntityVersion());
        List<SubgroupStatistic> subgroupStatisticList = subgroupStatisticService.select(requestCtx,subgroupStatistic,0,0);

        for (int i = 0; i < subgroupStatisticList.size(); i++) {

            subgroupStatistic = new SubgroupStatistic();
            subgroupStatistic.setSubgroupStatisticId(subgroupStatisticList.get(i).getSubgroupStatisticId());
            // 删除数据
            subgroupStatisticService.deleteByPrimaryKey(subgroupStatistic);

            // 记录历史
            SubgroupStatisticHis subgroupStatisticHis = new SubgroupStatisticHis();
            BeanUtils.copyProperties(subgroupStatisticList.get(i), subgroupStatisticHis);
            subgroupStatisticHisService.insertSelective(requestCtx, subgroupStatisticHis);
        }

        EntiretyStatistic entiretyStatistic = new EntiretyStatistic();
        entiretyStatistic.setEntityCode(entity.getEntityCode());
        entiretyStatistic.setEntityVersion(entity.getEntityVersion());
        List<EntiretyStatistic> entiretyStatisticList = entiretyStatisticService.select(requestCtx,entiretyStatistic,0,0);

        for (int i = 0; i < entiretyStatisticList.size(); i++) {

            entiretyStatistic = new EntiretyStatistic();
            entiretyStatistic.setEntiretyStatisticId(entiretyStatisticList.get(i).getEntiretyStatisticId());
            // 删除数据
            entiretyStatisticService.deleteByPrimaryKey(entiretyStatistic);

            // 记录历史
            EntiretyStatisticHis entiretyStatisticHis = new EntiretyStatisticHis();
            BeanUtils.copyProperties(entiretyStatisticList.get(i), entiretyStatisticHis);
            entiretyStatisticHisService.insertSelective(requestCtx, entiretyStatisticHis);
        }


        MessageL message = new MessageL();
        message.setEntityCode(entity.getEntityCode());
        message.setEntityVersion(entity.getEntityVersion());
        List<MessageL> messageList = messageService.select(requestCtx,message,0,0);

        for (int i = 0; i < messageList.size(); i++) {

            message = new MessageL();
            message.setMessageId(messageList.get(i).getMessageId());
            // 删除数据
            messageService.deleteByPrimaryKey(message);

            // 记录历史
            MessageHis messageHis = new MessageHis();
            BeanUtils.copyProperties(messageList.get(i), messageHis);
            messageHisService.insertSelective(requestCtx, messageHis);
        }

        MessageDetail messageDetail = new MessageDetail();
        messageDetail.setEntityCode(entity.getEntityCode());
        messageDetail.setEntityVersion(entity.getEntityVersion());
        List<MessageDetail> messageDetailList = messageDetailService.select(requestCtx,messageDetail,0,0);

        for (int i = 0; i < messageDetailList.size(); i++) {

            messageDetail = new MessageDetail();
            messageDetail.setMessageDetailId(messageDetailList.get(i).getMessageDetailId());
            // 删除数据
            messageDetailService.deleteByPrimaryId(messageDetail);

            // 记录历史
            MessageDetailHis messageDetailHis = new MessageDetailHis();
            BeanUtils.copyProperties(messageDetailList.get(i), messageDetailHis);
            messageDetailHisService.insertSelective(requestCtx, messageDetailHis);
        }

        CountOoc countOoc = new CountOoc();
        countOoc.setEntityCode(entity.getEntityCode());
        countOoc.setEntityVersion(entity.getEntityVersion());
        List<CountOoc> countOocList = countOocService.select(requestCtx,countOoc,0,0);

        for (int i = 0; i < countOocList.size(); i++) {

            countOoc = new CountOoc();
            countOoc.setCountOocId(countOocList.get(i).getCountOocId());
            // 删除数据
            countOocService.deleteByPrimaryKey(countOoc);

            // 记录历史
            CountOocHis countOocHis = new CountOocHis();
            BeanUtils.copyProperties(countOocList.get(i), countOocHis);
            countOocHisService.insertSelective(requestCtx, countOocHis);
        }

        CountStatistic countStatistic = new CountStatistic();
        countStatistic.setEntityCode(entity.getEntityCode());
        countStatistic.setEntityVersion(entity.getEntityVersion());
        List<CountStatistic> countStatisticList = countStatisticService.select(requestCtx,countStatistic,0,0);

        for (int i = 0; i < countStatisticList.size(); i++) {

            countStatistic = new CountStatistic();
            countStatistic.setCountStatisticId(countStatisticList.get(i).getCountStatisticId());
            // 删除数据
            countStatisticService.deleteByPrimaryKey(countStatistic);

            // 记录历史
            CountStatisticHis countStatisticHis = new CountStatisticHis();
            BeanUtils.copyProperties(countStatisticList.get(i), countStatisticHis);
            countStatisticHisService.insertSelective(requestCtx, countStatisticHis);

        }

    }

    /**
     * @param requestCtx 基本参数
     * @param chartList  传入参数
     * @return : com.hand.hap.system.dto.ResponseData
     * @Description: 数据删除
     * @author: ywj
     * @date 2019/8/19 19:47
     * @version 1.0
     */
    @Override
    public ResponseData deleteData(IRequest requestCtx, List<Chart> chartList) {

        // 此处加入校验:其他数据是否用到该数据
        chartList.forEach(chart -> {
            if (chart.getChartId() != null) {

                Entity entity = new Entity();
                entity.setChartId(chart.getChartId().longValue());
                List<Entity> entityList = entityService.select(requestCtx, entity, 0, 0);

                if (entityList.size() > 0) {
                    throw new RuntimeException("该控制图已维护实体控制图关系，无法删除!");
                }

                // 删除明细
                ChartDetail chartDetail = new ChartDetail();
                chartDetail.setChartId(chart.getChartId());
                List<ChartDetail> chartDetailList = chartDetailService.select(requestCtx, chartDetail, 0, 0);
                for (ChartDetail detail : chartDetailList) {
                    chartDetailService.deleteByPrimaryKey(detail);
                }

            }
        });

        // 主数据删除
        self().batchDelete(chartList);

        return new ResponseData();
    }
}