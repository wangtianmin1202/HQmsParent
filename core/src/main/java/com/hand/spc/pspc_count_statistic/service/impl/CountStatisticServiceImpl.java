package com.hand.spc.pspc_count_statistic.service.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.pspc_count_statistic.dto.CountStatistic;
import com.hand.spc.pspc_count_statistic.mapper.CountStatisticMapper;
import com.hand.spc.pspc_count_statistic.service.ICountStatisticService;

import sun.misc.BASE64Decoder;

@Service
@Transactional(rollbackFor = Exception.class)
public class CountStatisticServiceImpl extends BaseServiceImpl<CountStatistic> implements ICountStatisticService{

    @Autowired
    private CountStatisticMapper countStatisticMapper;

    @Override
    public ResponseData queryReport(IRequest requestContext, CountStatistic dto, int page, int pageSize) {
        /**
         * @Description //TODO   柏拉图展示查询
         * @Author leizhe
         * @Date 16:00 2019/8/19
         * @Param
         * @return java.util.List<com.hand.spc.pspc_count_statistic.dto.CountStatistic>
         **/

        ResponseData responseData = new ResponseData();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String releaseTimeBegin = dto.getReleaseTimeBegin();
        String relaeseTimeEnd = dto.getQueryRelaeseTimeEnd();
        //region  校验

        //region  校验时间不超过7天
        if (!StringUtils.isEmpty(releaseTimeBegin) || !StringUtils.isEmpty(relaeseTimeEnd) ){
            try {
                if (StringUtils.isEmpty(releaseTimeBegin)){
                    responseData.setSuccess(false);
                    responseData.setMessage("录入时间范围从未填值");
                    return responseData;
                }

                if (StringUtils.isEmpty(relaeseTimeEnd)){
                    responseData.setSuccess(false);
                    responseData.setMessage("录入时间范围至未填值");
                    return responseData;
                }

                Date begin = simpleDateFormat.parse(releaseTimeBegin);
                Date end = simpleDateFormat.parse(relaeseTimeEnd);
                long days = (end.getTime() - begin.getTime()) / 1000;
                if (days>= (60*60*24*7)) {
                    responseData.setSuccess(false);
                    responseData.setMessage("查询时间范围不能超过7天");
                    return responseData;
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        //endregion

        //region  判断查询LIMit
        if (StringUtils.isEmpty(releaseTimeBegin) && StringUtils.isEmpty(relaeseTimeEnd) ){
            String s = countStatisticMapper.queryLimit(dto);
            dto.setMyLimit(s);
        }
        //endregion

        //region 校验  该控制图类型不支持柏拉图

        String charType = countStatisticMapper.queryCharType(dto);
        String[] type = {"XBAR-R","XBAR-S","Me-R","X-Rm"};
        for (int i = 0; i < type.length; i++) {
            String s = type[i];
            if (s.equals(charType)){
                responseData.setSuccess(false);
                responseData.setMessage("该控制图类型不支持柏拉图");
                return responseData;
            }
        }


        //endrgeion
        //endregion

        //endregion

        // 主要数据查询
        List<CountStatistic> countStatistics = countStatisticMapper.queryReport(dto);

        //用于存储不合格类型的数据
        HashMap<String, String> stringDoubleHashMap = null;
        // if (countStatistics.size() > 0) {
        //     int count = countStatistics.get(0).getWc1().split(",").length + countStatistics.get(0).getWc2().split(",").length;
        stringDoubleHashMap = new HashMap<>();
        // }

        //处理OOC信息
        for (int i = 0; i < countStatistics.size(); i++) {
            CountStatistic countStatistic = countStatistics.get(i);

            //处理状态
            String occStatus = getOccStatus(countStatistic);
            countStatistic.setOccStatus(occStatus);

            //region处理动态列1
            ArrayList<String> list1 = getWc(countStatistic.getWc1(),false);
            countStatistic.setListWc1(list1);

            //将list存储在stringDoubleHashMap，用于图表展示的数据
            addMap(stringDoubleHashMap,list1);
            //endregion

            //处理动态列2
            ArrayList<String> list2 = getWc(countStatistic.getWc2(), true);
            countStatistic.setListWc2(list2);
        }

        // 将不合格信息进行处理
        if (countStatistics.size() > 0) {
            int sum = stringDoubleHashMap.size() * 2;
            ArrayList<String> list = new ArrayList<>(sum);
            Iterator<Map.Entry<String, String>> iterator = stringDoubleHashMap.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry<String, String> next = iterator.next();
                String key = next.getKey();
                String value = next.getValue();
                list.add(key);
                list.add(value);
            }

            //region给list重新排序

            int size = list.size()/2;
            //用于存储未排序的list
            ArrayList<Double> compList = new ArrayList<>(size);
            for (int i = 0; i < list.size(); i++) {
                if (i % 2 != 0){
                    double parseDouble = Double.parseDouble(list.get(i));
                    compList.add(parseDouble);
                }
            }


            //
            Collections.sort(compList);

            //用于存储排序完成后的list
            ArrayList<String> resultList = new ArrayList<>(size);

            //遍历数据
            for (int i = compList.size() - 1; i >= 0; i--) {
                double comp = compList.get(i);

                for (int j = 0; j < list.size(); j++) {
                    String s = list.get(j);

                    if (j % 2 != 0){
                        double parseDouble = Double.parseDouble(s);
                        if (comp == parseDouble) {
                            String s1 = list.get(j - 1);
                            resultList.add(s1);
                            resultList.add(s);
                        }
                    }

                }
            }
            //endregion
            countStatistics.get(0).setListUnqualified(resultList);
        }


        responseData.setSuccess(true);
        responseData.setRows(countStatistics);

        return responseData;
    }

    @Override
    public void exportExcel(CountStatistic dto,
                            IRequest requestContext,
                            HttpServletRequest request,
                            HttpServletResponse response,
                            List<CountStatistic> rows,
                            String img) {
        /**
         * @Description //TODO  导出excel
         * @Author leizhe
         * @Date 14:23 2019/9/10
         * @Param [dto, requestContext, request, response, rows, img]
         * @return void
         **/


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            if (rows.size() > 0){

                XSSFWorkbook wb = new XSSFWorkbook();
                XSSFCellStyle style = wb.createCellStyle();
                //设置单元格居中
                style.setAlignment(HorizontalAlignment.CENTER);
                //创建第一页
                XSSFSheet sheet1 = wb.createSheet("sheet1");

                String filePath = request.getContextPath() + "/temp";
                File file = new File(filePath);
                if (!file.exists()) {
                    file.mkdir();
                }
                String fileName = filePath + "\\" + System.currentTimeMillis() + ".png";

                InputStream inputStream = createImage(fileName,img);

                //将图片插入进去
                //循环读取图片插入到excel
                //画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）
                XSSFDrawing patriarch = sheet1.createDrawingPatriarch();
                int rowBegin = 0;//这里也可以动态，用上边的list的长度再加个2或者是3就可以实现动态图片的初始行
                int rowEnd = 23;
                int colBegin = 1;
                int colEnd = 18;
                ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
                BufferedImage bufferImg = ImageIO.read(inputStream);
                ImageIO.write(bufferImg, "png", byteArrayOut);
                //anchor主要用于设置图片的属性
                XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 255, 255,colBegin, rowBegin, colEnd, rowEnd);
                //插入图片
                patriarch.createPicture(anchor, wb.addPicture(byteArrayOut.toByteArray(), XSSFWorkbook.PICTURE_TYPE_PNG));



                //  表格开始行
                int rowNum = 26;
                XSSFRow RowOne = sheet1.createRow(rowNum);

                XSSFCell oneCel = RowOne.createCell(0);
                oneCel.setCellValue("序号");
                oneCel.setCellStyle(style);

                XSSFCell oneCel2 = RowOne.createCell(1);
                oneCel2.setCellValue("样本时间");
                oneCel2.setCellStyle(style);

                XSSFCell oneCel3 = RowOne.createCell(2);
                oneCel3.setCellValue("抽检数");
                oneCel3.setCellStyle(style);

                XSSFCell oneCel4 = RowOne.createCell(3);
                oneCel4.setCellValue("不合格数");
                oneCel4.setCellStyle(style);


                List<String> listWc1 = (List<String>) rows.get(0).getListWc1();

                //动态列1
                int cellNum = 3;
                for (int i = 0; i < listWc1.size(); i++) {
                    if (i%2 == 0){
                        cellNum++;
                        String s =  listWc1.get(i);
                        XSSFCell oneCel5 = RowOne.createCell(cellNum);
                        oneCel5.setCellValue(s);
                        oneCel5.setCellStyle(style);
                    }
                }
                //动态列2
                List<String> listWc2 = (List<String>)rows.get(0).getListWc2();
                for (int i = 0; i < listWc2.size(); i++) {
                    if (i%2 == 0){
                        cellNum++;
                        String s1 =  listWc2.get(i);
                        XSSFCell oneCel5 = RowOne.createCell(cellNum);
                        oneCel5.setCellValue(s1);
                        oneCel5.setCellStyle(style);
                    }
                }
                XSSFCell oneCel6 = RowOne.createCell(cellNum+1);
                oneCel6.setCellValue("状态");
                oneCel6.setCellStyle(style);
                XSSFCell oneCel7 = RowOne.createCell(cellNum+2);
                oneCel7.setCellValue("不合格率%");
                oneCel7.setCellStyle(style);


                for (int i = 0; i < rows.size(); i++) {
                    CountStatistic countStatistic = rows.get(i);
                    XSSFRow row = sheet1.createRow(rowNum+i+1);

                    //序号
                    XSSFCell cell0 = row.createCell(0);
                    cell0.setCellValue((i+1));
                    cell0.setCellStyle(style);

                    //样本时间
                    XSSFCell cell1 = row.createCell(1);
                    Date sampleTime = countStatistic.getSampleTime();
                    String format = simpleDateFormat.format(sampleTime);
                    cell1.setCellValue(format);
                    cell1.setCellStyle(style);

                    //抽检数
                    XSSFCell cell2 = row.createCell(2);
                    cell2.setCellValue(countStatistic.getSampleValueCount());
                    cell2.setCellStyle(style);

                    //不合格数
                    XSSFCell cell3 = row.createCell(3);
                    cell3.setCellValue(countStatistic.getUnqualifiedQuantity());
                    cell3.setCellStyle(style);

                    //动态列的下标
                    int currentCellNum = 3;
                    //动态列1
                    List<String> listWc11 = (List<String>) countStatistic.getListWc1();
                    for (int j = 0; j < listWc11.size(); j++) {
                        String s = listWc11.get(j);
                        if (j%2 != 0){
                            currentCellNum++;
                            XSSFCell oneCelC = row.createCell(currentCellNum);
                            oneCelC.setCellValue(s);
                            oneCelC.setCellStyle(style);
                        }
                    }
                    //动态列2
                    List<String> listWc21 = (List<String>)countStatistic.getListWc2();
                    for (int j = 0; j < listWc21.size(); j++) {
                        String s = listWc21.get(j);
                        if (j%2 != 0){
                            currentCellNum++;
                            XSSFCell oneCelC = row.createCell(currentCellNum);
                            oneCelC.setCellValue(s);
                            oneCelC.setCellStyle(style);
                        }
                    }

                    //状态
                    XSSFCell cell41 = row.createCell(currentCellNum+1);
                    cell41.setCellValue(countStatistic.getOccStatus());
                    cell41.setCellStyle(style);


                    //不合格率
                    XSSFCell cell51 = row.createCell(currentCellNum+2);
                    cell51.setCellValue(countStatistic.getUnqualifiedPercent());
                    cell51.setCellStyle(style);


                }




                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                response.addHeader("Content-Disposition",
                        "attachment; filename=\""+"list"+sdf.format(new Date())+".xlsx"+"\"");
                //这里定义文件的名字，后面跟了个日期
                response.setContentType("application/vnd.ms-excel" + ";charsets=" + "UTF-8");
                OutputStream out = response.getOutputStream();
                // 写入excel文件
                wb.write(out);
                out.close();
                wb.close();









            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    //  创建图片
    private InputStream createImage(String fileName, String img) {
        try {
            String[] url = img.split(",");
            String u = url[1];
            // Base64解码
            byte[] b = new BASE64Decoder().decodeBuffer(u);
            InputStream input = new ByteArrayInputStream(b);
            return input;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    //将list存储在stringDoubleHashMap，用于图表展示的数据
    private void addMap(HashMap<String, String> stringDoubleHashMap, ArrayList<String> list1) {
        for (int i = 0; i < list1.size(); i+=2) {
            String key = list1.get(i);
            String value = list1.get(i + 1);
            if (stringDoubleHashMap.containsKey(key)) {
                //值相加
                String addResult = addCal(stringDoubleHashMap.get(key), value);
                stringDoubleHashMap.put(key,addResult);
            }else {
                stringDoubleHashMap.put(key,value);
            }
        }
    }

    private String addCal(String v, String value) {
        BigDecimal bigDecimal = new BigDecimal(v);
        BigDecimal bigDecimal2 = new BigDecimal(value);

        BigDecimal add = bigDecimal.add(bigDecimal2);

        return String.valueOf(add);
    }

    //根据count_sample_data_id判断status
    private String getOccStatus(CountStatistic countStatistic) {
        List<String> list = countStatisticMapper.queryOccStatus(String.valueOf(countStatistic.getCountSampleDataId()));
        if (list.size() == 0){
            return "正常";
        }
        boolean enableDeal = true;//是否已处理
        //list.size>0时 list中只要有一个UNPROCESSED 就是失控,否则为已处理
        for (int i = 0; i < list.size(); i++) {
            String s = list.get(i);
            if (s.equals("UNPROCESSED")){
                enableDeal = false;
            }
        }
        if (enableDeal){
            return "已处理";
        }else {
            return "失控";
        }
    }

    //处理动态列,enableUseFD->是否使用快速编码
    public ArrayList<String> getWc(String wc1,boolean enableUseFD) {

        if (StringUtils.isEmpty(wc1)){
            ArrayList<String> list = new ArrayList<>();
            return list;
        }

        String[] split = wc1.split(",");
        ArrayList<String> list1 = new ArrayList<>(split.length * 2);

        List<CountStatistic> countStatistics = null;

        //如果需要使用快速编码
        if (enableUseFD){
            CountStatistic countStatistic = new CountStatistic();
            countStatistic.setFastCode("PSPC.DATA.EXTRA.ATTRITUBE");
            countStatistics = countStatisticMapper.queryFastCode(countStatistic);
        }

        for (int j = 0; j < split.length; j++) {
            String s = split[j];
            String[] split1 = s.split("\\+");
            //此处用于判断是否需要使用快速编码
            if (enableUseFD){
                CountStatistic cs = countStatistics.stream().filter(t -> t.getValue().equals(split1[0])).findFirst().orElse(null);
                if (cs != null) {
                    String meaning = cs.getMeaning();
                    list1.add(meaning);
                } else {
                    list1.add(" ");
                }
            }else {
                list1.add(split1[0]);
            }
            list1.add(split1[1]);
        }
        return list1;
    }

}