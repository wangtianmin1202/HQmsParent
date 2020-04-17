package com.hand.itf.itf_transaction_records.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.ICodeService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SoapPostUtil;
import com.hand.hap.webservice.ws.wsi.DataReceptionFromSap;
import com.hand.itf.itf_function_info.dto.FunctionInfo;
import com.hand.itf.itf_function_info.service.IFunctionInfoService;
import com.hand.itf.itf_transaction_records.mapper.TransactionRecordsMapper;
import com.hand.sys.sys_if_invoke_outbound.dto.IfInvokeOutbound;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.itf.itf_transaction_records.dto.TransactionRecords;
import com.hand.itf.itf_transaction_records.service.ITransactionRecordsService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class TransactionRecordsServiceImpl extends BaseServiceImpl<TransactionRecords> implements ITransactionRecordsService{

    @Autowired
    TransactionRecordsMapper transactionRecordsMapper;
    @Autowired
    IFunctionInfoService functionInfoService;
    @Autowired
    ICodeService codeService;

    @Resource(name = "serviceFromSap")
    DataReceptionFromSap sap;

    @Override
    public List<TransactionRecords> queryTransactionRecords(IRequest request, TransactionRecords t, int pageNum, int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        return transactionRecordsMapper.queryTransactionRecords(t);
    }

    @Override
    public void transactionResend(IRequest request,float id,int page,int pageSize){
        TransactionRecords t=new TransactionRecords();
        t.setTransId(id);
        TransactionRecords transactionRecords =transactionRecordsMapper.selectOne(t);
        if(transactionRecords !=null){
            //新生成一条履历
            TransactionRecords transactionRecordsNew =new TransactionRecords();
            transactionRecordsNew.setSystemCode(transactionRecords.getSystemCode());
            transactionRecordsNew.setTransactionType(transactionRecords.getTransactionType());
            transactionRecordsNew.setItfCode(transactionRecords.getItfCode());
            transactionRecordsNew.setTransactionTime(new Date());
            transactionRecordsNew.setTransactionData(transactionRecords.getTransactionData());
            String str2=null;
            try {
                String str1 = transactionRecords.getTransactionAscend().substring(0, transactionRecords.getTransactionAscend().indexOf("-"));
                str2 = transactionRecords.getTransactionAscend().substring(str1.length() + 1, transactionRecords.getTransactionAscend().length());
                transactionRecordsNew.setTransactionAscend(transactionRecords.getTransactionAscend() + "-" + transactionRecords.getTransId() + "-" + (Integer.parseInt(str2) +1));
            }
            catch (Exception e){
                if(StringUtils.isEmpty(str2)){
                    transactionRecordsNew.setTransactionAscend(transactionRecords.getTransactionAscend() + "-" + transactionRecords.getTransId() + "-1");
                }
            }
            //获取接口方法
            FunctionInfo functionInfo=new FunctionInfo();
            functionInfo.setSystemCode(transactionRecords.getSystemCode());
            functionInfo.setTransactionType(transactionRecords.getTransactionType());
            functionInfo.setItfCode(transactionRecords.getItfCode());
            List<FunctionInfo> infoList=functionInfoService.select(request,functionInfo,page,pageSize);
            if(infoList.size()>0){
                String method=infoList.get(0).getFunctionName();
                //重传调用接口
                String resJson=null;
                //接收
                if(transactionRecords.getTransactionType().equals("RECEIVE")) {
                    resJson = sap.SRMGlobalFunc(method, transactionRecords.getTransactionData());
                    transactionRecordsNew.setTransactionReturn(resJson);

                    JSONObject object = JSON.parseObject(resJson);
                    String result=object.getString("result");
                    if(result.equals("0")) {
                        transactionRecordsNew.setTransactionFlag("success");
                    }
                    else {
                        transactionRecordsNew.setTransactionFlag("failure");
                    }
                }
                //发送
                else if(transactionRecords.getTransactionType().equals("SEND")){
                    String url=codeService.getCodeValueByMeaning(request,"HAP.SYSTEM",transactionRecords.getSystemCode());
                    if(transactionRecords.getSystemCode().equals("WMS")) {
                        SoapPostUtil.Response response=SoapPostUtil.ticketSrmToWms(infoList.get(0).getFunctionName(),transactionRecords.getTransactionData(),new IfInvokeOutbound(),url);
                        transactionRecordsNew.setTransactionReturn(JSON.toJSONString(response));
                        if(response.getResult()){
                            transactionRecordsNew.setTransactionFlag("success");
                        }
                        else{
                            transactionRecordsNew.setTransactionFlag("failure");
                        }
                    }
                }
            }
            else{
                transactionRecordsNew.setTransactionFlag("failure");
            }
            transactionRecordsMapper.insert(transactionRecordsNew);
        }
    }

    @Override
    public String transactionDetail(IRequest request,float id){
        TransactionRecords t=new TransactionRecords();
        t.setTransId(id);
        TransactionRecords transactionRecords =transactionRecordsMapper.selectOne(t);

        return transactionRecords.getTransactionData();
    }

}