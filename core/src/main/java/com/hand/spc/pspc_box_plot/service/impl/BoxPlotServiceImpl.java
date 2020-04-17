package com.hand.spc.pspc_box_plot.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.pspc_box_plot.mapper.BoxPlotMapper;
import com.hand.spc.pspc_box_plot.service.IBoxPlotService;
import com.hand.spc.pspc_box_plot.view.BoxPlotVO;
import com.hand.spc.pspc_entity.mapper.EntityMapper;
import com.hand.spc.pspc_entity.service.IEntityService;
import com.hand.spc.pspc_entity.view.ScatterPlotVO;
import com.hand.utils.date.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional(rollbackFor = Exception.class)
public class BoxPlotServiceImpl implements IBoxPlotService {


    @Autowired
    private IEntityService iEntityService;


    @Autowired
    private EntityMapper entityMapper;

    @Autowired
    private BoxPlotMapper boxPlotMapper;
    private Object selectInfo;

    @Override
    public ResponseData queryBoxPlot(ResponseData responseData, IRequest requestContext, List<BoxPlotVO> dto) {
        /**
         * @Description //TODO  箱线图查询
         * @Author leizhe
         * @Date 17:37 2019/8/26
         * @Param
         * @return com.hand.hap.system.dto.ResponseData
         **/

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int size = dto.size();
        ArrayList<BoxPlotVO> result = new ArrayList<>(size);


        for (int i = 0; i < dto.size(); i++) {
            BoxPlotVO boxPlotVO = dto.get(i);
            BoxPlotVO boxPlotVO1 = new BoxPlotVO();

            List<BoxPlotVO> boxPlotVOS = null;
            //region校验时间
            if ((boxPlotVO.getStartDate() != null && boxPlotVO.getEndDate() == null)
                    || (boxPlotVO.getStartDate() == null && boxPlotVO.getEndDate() != null)) {
                responseData.setSuccess(false);
                responseData.setMessage("时间最长跨度为30天");
                return responseData;
            } else if (boxPlotVO.getStartDate() != null && boxPlotVO.getEndDate() != null) {
                //获取时间跨度
                long days = 0;
                try {
                    days = DateUtil.getDateInterval(simpleDateFormat.parse(boxPlotVO.getStartDate()), simpleDateFormat.parse(boxPlotVO.getEndDate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (days < 0 || days > 30) {
                    responseData.setSuccess(false);
                    responseData.setMessage("时间最长跨度为30天");
                    return responseData;
                }

                //查询基础数据
                boxPlotVOS = boxPlotMapper.querySampleValuesByTime(boxPlotVO);
                //endregion

            } else {
                //获取最大样本数
                long maxPlotPoints = entityMapper.queryMaxPlotPointsByEntityId(boxPlotVO.getEntityId());
                boxPlotVO.setMaxPlotPoints(maxPlotPoints);

                //查询基础数据
                boxPlotVOS = boxPlotMapper.querySampleValuesByMaxPoints(boxPlotVO);


            }


            //开始整理数据
            //存储查询出来的数据
            boxPlotVO1.setList(boxPlotVOS);
            //存储实体Code
            boxPlotVO1.setEntityCode(boxPlotVO.getEntityCode());

            //存储实体DESC
//            boxPlotVO1.setDescription(boxPlotVO.getDescription());
            boxPlotVO1.setDescription(boxPlotVO.getCeParameterName());

            //进行箱线图逻辑计算
            if (dto.size() == 1) {
                //如果只查询1个，需要分时间进行处理计算
                SimpleDateFormat sdfYmd = new SimpleDateFormat("yyyy/MM/dd");
                ArrayList<String> list = new ArrayList<>(boxPlotVOS.size());
                //region 获取所有的时间（不重复）
                for (int j = 0; j < boxPlotVOS.size(); j++) {
                    BoxPlotVO plotVO = boxPlotVOS.get(j);
                    Date sampleTime = plotVO.getSampleTime();
                    String format = sdfYmd.format(sampleTime);
                    list.add(format);
                }

                Set set = new HashSet(list);
                List listSet = new ArrayList(set);
                //endregion

                //region  循环不重复的时间，进行计算
                ArrayList<BoxPlotVO> boxPlotVOS1 = new ArrayList<>(listSet.size());
                for (int j = 0; j < listSet.size(); j++) {
                    String time = (String)listSet.get(j);
                    //找到当前time所对应的集合
                    List<BoxPlotVO> collect = boxPlotVOS.stream().filter(
                            t -> sdfYmd.format(t.getSampleTime()).equals(time)).collect(Collectors.toList());
                    BoxPlotVO boxPlotVO2 = calculateBoxPlot(collect);

                    if (boxPlotVO2 != null){
                        boxPlotVO2.setTimeStr(time);
                        boxPlotVOS1.add(boxPlotVO2);
                    }

                }
                //endregion
                boxPlotVO1.setBoxPlotVOSList(boxPlotVOS1);

            } else {
                //获取箱线图的数据
                BoxPlotVO boxPlotVO2 = calculateBoxPlot(boxPlotVOS);
                ArrayList<BoxPlotVO> boxPlotVOS1 = new ArrayList<>(1);
                boxPlotVOS1.add(boxPlotVO2);
                // 将箱线图数据存储到BoxPlotVOSList中
                boxPlotVO1.setBoxPlotVOSList(boxPlotVOS1);
            }

            result.add(boxPlotVO1);

        }
        //将结果返回
        responseData.setRows(result);
        responseData.setSuccess(true);

        return responseData;
    }

    // 根据这些数据进行
    private BoxPlotVO calculateBoxPlot(List<BoxPlotVO> boxPlotVOS) {

        int size = boxPlotVOS.size();

        long[] a = new long[size];
        for (int i = 0; i < size; i++) {
            BoxPlotVO boxPlotVO = boxPlotVOS.get(i);
            Long sampleValue = boxPlotVO.getSampleValue();
            a[i] = sampleValue;
        }




        int n = a.length;

        if (n < 3){
            return null;
        }
        //region	下四分位数Q1：
        // 	确定Q1所在位置：(n+1)/4，结果记为b，b的整数位记为c，b的小数位记为d；
        // 	Q1=a(c)+{a(c+1)-a(c)}*d。
        // 例子中，（n+1）/4= 7/4 =1.75，Q1在第一与第二个数字之间，Q1 =7+{15-7}*0.75 = 13。

        int c = (int) myDivide((n + 1) , 4,5);
        double d = myDivide(((n + 1) % 4),4,5);
        double Q1 = myAdd(a[c-1] ,myMultiply(mySubtract(a[c-1+1],a[c-1]),d));

        //endregion

        //region	中位数Q2：
        // 	若n为奇数，则Q2为中间位置对应的数值；若n为偶数，则Q2为中间两数的平均数；
        // 例子中，四分位数Q2为该组数列的中间两个数的平均数， Q2 = （36+39）/2= 37.5。

        double Q2 = 0d;
        if (n % 2 == 0){
            //    偶数
            int i = n / 2;
            // Q2 = (a[i - 1] + a[i]) / 2;
            Q2 = myDivide(myAdd(a[i - 1], a[i]), 2,5);
        }else {
            //    奇数
            int i = n / 2;
            Q2 = a[i];
        }
        //endregion

        //region 	上四分位数Q3：
        // 	确定Q3所在位置：3(n+1)/4，记结果为e，e的整数位记为f，e的小数位记为g；
        // 	Q3=a(f)+{a(f+1)-a(f)}*g
        // 例子中，3（n+1）/4= 21/4 =5.25, Q3在第五与第六个数字之间，Q3 =40+{41-40}*0.25= 0.25*41+0.75*40 = 40.25.

        double e = myDivide(myMultiply(myAdd(n, 1), 3), 4,5);
        String eStr = String.valueOf(e);
        String[] split = eStr.split("\\.");
        int f = Integer.parseInt(split[0]);
        double g = Double.parseDouble("0." + split[1]);
        double Q3 =  myAdd(a[f-1],myMultiply(mySubtract(a[f-1+1],a[f-1]),g));
        //endregion

        //region  	四分位距（IQR）：Q3-Q1，例子中，IQR=40.25-13=27.25
        double IQR = mySubtract(Q3,Q1);
        //endregion

        //region	内上限：Q3+1.5*IQR，例子中，40.25+1.5*27.25=81.125（保留三位小数）
        double IUL  = myAdd(Q3 ,myMultiply(1.5,IQR));
        IUL = get3DecimalFormat(IUL);
        //endregion

        //region	内下限：Q1-1.5IQR，例子中，13-1.5*27.25=-27.875（保留三位小数）
        double ILL = mySubtract(Q1,myMultiply(IQR,1.5));
        ILL = get3DecimalFormat(ILL);
        //endregion

        //region 	外上限：Q3+3IQR，例子中，40.25+3*27.25=122（保留三位小数）
        double EUL = myAdd(Q3 , myMultiply(3 , IQR));
        //endregion

        //region 	外下限：Q1－3IQR， 例子中，13-3*27.25=-68.75（保留三位小数）
        double ELL = mySubtract(Q1,myMultiply(3 , IQR));
        //endregion

        //region 	上边缘（最大值）：为一组数据点中小于等于内上限的最大值。例子中为41。
        double UMMax = getUMMax(a,IUL);
        //endregion

        //region  	下边缘（最小值）：为一组数据中大于等于内下限的最小值。例子中为7。
        double LMMin = getLMMax(a,ILL);
        //endregion

        //region  3.显示异常点：
        // 异常点：样本数据中大于内上限和小于内下限的值，为异常值；颜色用红色标记；

        //region 	位于上限与外上限之间以及位于下限与外下限之间的值为温和异常值，用●（红色）表示。
        List<Long> range1 = getRange(a, IUL, EUL, true);
        List<Long> range2 = getRange(a, ELL, ILL, true);
        range1.addAll(range2);
        //endregion

        //region // 	位于外下限的下面以及外上限的上面的值为极端异常值，用"*" （红色）表示。
        List<Long> range3 = getRange(a, ELL, true);
        List<Long> range4 = getRange(a, EUL, false);
        range3.addAll(range4);
        //endregion

        //endregion


        //假写
        /*if (range1.size() == 0){
            range1.add(121l);
            range1.add(10l);
            range1.add(100l);
            range1.add(20l);
        }

        if (range3.size() == 0){
            range3.add(1l);
            range3.add(201l);
            range3.add(212l);
            range3.add(223l);
        }*/

        //region 数据整理
        BoxPlotVO boxPlotVO = new BoxPlotVO();
        boxPlotVO.setQ1(Q1);
        boxPlotVO.setQ2(Q2);
        boxPlotVO.setQ3(Q3);
        boxPlotVO.setIqr(IQR);
        boxPlotVO.setIul(IUL);
        boxPlotVO.setIll(ILL);
        boxPlotVO.setEul(EUL);
        boxPlotVO.setEll(ELL);
        boxPlotVO.setUmMax(UMMax);
        boxPlotVO.setLmMin(LMMin);
        boxPlotVO.setRange1(range1);
        boxPlotVO.setRange2(range3);
        //endregion

        return boxPlotVO;
    }


    //bl = true a 中小于num的数据， bl=false a 中大于num的数据
    private static List<Long> getRange(long[] a, double num, boolean bl) {
        ArrayList<Long> list = new ArrayList<>();
        for (int i = 0; i < a.length; i++) {
            long l = a[i];
            if (bl){
                if (l < num) list.add(l);
            }else {
                if (l > num) list.add(l);
            }
        }
        return list;
    }

    //获取a内大于(等于)  min  小于(等于) max的所有值,bl = true,带有(等于) ，bl = false 不带(等于)
    private static List<Long> getRange(long[] a, double min, double max,boolean bl) {
        ArrayList<Long> list = new ArrayList<>();


        for (int i = 0; i < a.length; i++) {
            long l = a[i];
            if (bl){
                if (l <= max && l >= min){
                    list.add(l);
                }
            }else {
                if (l < max && l > min){
                    list.add(l);
                }
            }
        }
        return list;

    }

    //获取大于等于ill的最大值
    private static double getLMMax(long[] a, double ill) {
        ArrayList<Double> list = new ArrayList<>();
        for (int i = 0; i < a.length; i++) {
            long l = a[i];
            if (Double.parseDouble(String.valueOf(l)) >= ill ){
                list.add(Double.parseDouble(String.valueOf(l)));
            }
        }

        if (list.size()>0){
            double min = list.stream().filter(e -> e != null).min(Comparator.naturalOrder()).orElse(null);
            return min;
        }else {
            return 0;
        }

    }

    //获取小于等于iul的最大值
    private static double getUMMax(long[] a, double iul) {
        ArrayList<Double> list = new ArrayList<>();
        for (int i = 0; i < a.length; i++) {
            long l = a[i];
            if (Double.parseDouble(String.valueOf(l)) <= iul ){
                list.add(Double.parseDouble(String.valueOf(l)));
            }
        }
        if (list.size() > 0) {
            double max = list.stream().filter(e -> e != null).max(Comparator.naturalOrder()).orElse(null);
            return max;
        }else {
            return 0;
        }
    }


    //乘法
    private static double myMultiply(double a,double b){
        BigDecimal bigDecimal = new BigDecimal(String.valueOf(a));
        BigDecimal bigDecimal1 = new BigDecimal(String.valueOf(b));
        BigDecimal multiply = bigDecimal.multiply(bigDecimal1);
        String string = multiply.toString();
        return  Double.parseDouble(string);

    }

    //除法
    private static double myDivide(double a,double b,int retain){
        BigDecimal bigDecimal = new BigDecimal(String.valueOf(a));
        BigDecimal bigDecimal1 = new BigDecimal(String.valueOf(b));
        BigDecimal multiply = bigDecimal.divide(bigDecimal1,retain,BigDecimal.ROUND_HALF_UP);
        String string = multiply.toString();
        return  Double.parseDouble(string);
    }

    //减法
    private static double mySubtract(double a, double b){
        BigDecimal bigDecimal1 = new BigDecimal(a);
        BigDecimal bigDecimal2 = new BigDecimal(b);
        BigDecimal subtract = bigDecimal1.subtract(bigDecimal2);
        return Double.parseDouble(String.valueOf(subtract));
    }

    //加法
    private static double myAdd(double a, double b){
        BigDecimal bigDecimal1 = new BigDecimal(a);
        BigDecimal bigDecimal2 = new BigDecimal(b);
        BigDecimal subtract = bigDecimal1.add(bigDecimal2);
        return Double.parseDouble(String.valueOf(subtract));
    }

    //保留3位有效数字
    private static double get3DecimalFormat(double a){
        DecimalFormat df = new DecimalFormat("0.000");
        return Double.parseDouble(df.format(a));
    }

}