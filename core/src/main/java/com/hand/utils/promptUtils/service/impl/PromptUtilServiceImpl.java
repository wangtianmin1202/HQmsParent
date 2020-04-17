package com.hand.utils.promptUtils.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.Prompt;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.mapper.PromptMapper;
import com.hand.utils.excelUtil.FileUploadBaseDto;
import com.hand.utils.excelUtil.FileUploadDto;
import com.hand.utils.excelUtil.TransferExcelDateToFileUploadDtoUtil;
import com.hand.utils.promptUtils.dto.CreateReportFile;
import com.hand.utils.promptUtils.dto.PromptUtil;
import com.hand.utils.promptUtils.mapper.PromptUtilMapper;
import com.hand.utils.promptUtils.service.IPromptUtilService;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileWriter;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Auther:lkj
 * @Date:2018/6/19 11:04
 * @E-mail:kejin.liu@hand-china.com
 * @Description:多语言的导入导出
 */
@Transactional
@Service
public class PromptUtilServiceImpl implements IPromptUtilService {

    @Autowired
    private PromptUtilMapper mapper;

    @Autowired
    private PromptMapper promptMapper;

    private String DATABASE_TYPE = "O";// M 代表mysql  O 代表oracle

    @Override
    public void selectTableColumn(String tableName, IRequest iRequest, HttpServletRequest request, HttpServletResponse response)throws Exception {
        /**
         *
         * 功能描述: 查询表列名和注释并且导出
         *
         * @auther:lkj
         * @date:2018/6/19 上午11:25
         * @param:[iRequest, dto]
         * @return:java.util.List<com.hand.util.promptUtils.dto.PromptUtil>
         *
         */
        OutputStream out = null;
        XSSFWorkbook workbook = null;
        tableName = tableName.toUpperCase();
        try{
            List<PromptUtil> newData = new ArrayList<>();
            String[] split = tableName.split(",");
            for(int x=0;x<split.length;x++){
                String sql = "";
                if(DATABASE_TYPE.equals("O")){
                    sql += "select a.column_name as columnName,a.comments as columnDesc from user_col_comments a ";
                    sql += " inner join user_tab_columns b on (a.table_name = b.table_name and a.column_name = b.column_name)";
                    sql += " where a.table_name = ";
                    sql += "'"+split[x]+"'";
                }else if(DATABASE_TYPE.equals("M")){
                    sql += "SELECT DISTINCT COLUMN_NAME AS columnName, column_comment AS columnDesc FROM ";
                    sql += "INFORMATION_SCHEMA.COLUMNS  WHERE table_name = '"+split[x]+"'";
                }

                List<PromptUtil> columns = mapper.mySql(sql);
                //查看表第一个出现 下划线坐标，然后截取余下的字符串
                int indexOf = tableName.indexOf("_")+1;
                String newTableName = split[x].substring(indexOf,split[x].length()).replace("_","");
                if(columns.size() > 0){
                    for(int i=0;i<columns.size();i++){
                        String column = columns.get(i).getColumnName().replace("_","");
                        columns.get(i).setCode(newTableName+"."+column);
                        newData.add(columns.get(i));
                    }
                }else{
                    System.out.println(split[x]+"：该表不存在");
                }
            }

            workbook = new XSSFWorkbook();
            XSSFCellStyle style = workbook.createCellStyle();
            //设置单元格居中
            style.setAlignment(HorizontalAlignment.CENTER);
            //设置单元格垂直居中
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            XSSFFont font2 = workbook.createFont();
            font2.setFontHeightInPoints((short) 12);
            style.setFont(font2);
            XSSFSheet sheet = workbook.createSheet("多语言维护");
            //写入标题
            XSSFRow titleRow = sheet.createRow(0);
            XSSFCell titleCell = titleRow.createCell(0);
            sheet.setColumnWidth(0,20*512);
            sheet.setColumnWidth(1,20*512);
            sheet.setColumnWidth(2,20*512);
            titleCell.setCellStyle(style);
            titleCell.setCellValue("代码");
            XSSFCell titleCell2 = titleRow.createCell(1);
            titleCell2.setCellStyle(style);
            titleCell2.setCellValue("中文描述");
            XSSFCell titleCell3 = titleRow.createCell(2);
            titleCell3.setCellStyle(style);
            titleCell3.setCellValue("英文描述");
            //写入内容
            XSSFCellStyle style2 = workbook.createCellStyle();
            style2.setAlignment(HorizontalAlignment.CENTER);
            int dataRow = 0;
            for(PromptUtil promptUtil:newData){
                //系统字段不导出
                if(promptUtil.getColumnName() == "LAST_UPDATED_BY" || "LAST_UPDATED_BY".equals(promptUtil.getColumnName())
                        || promptUtil.getColumnName() == "CREATED_BY" || "CREATED_BY".equals(promptUtil.getColumnName())
                        || promptUtil.getColumnName() == "CREATION_DATE" || "CREATION_DATE".equals(promptUtil.getColumnName())
                        || promptUtil.getColumnName() == "OBJECT_VERSION_NUMBER" || "OBJECT_VERSION_NUMBER".equals(promptUtil.getColumnName())
                        || promptUtil.getColumnName() == "REQUEST_ID" || "REQUEST_ID".equals(promptUtil.getColumnName())
                        || promptUtil.getColumnName() == "PROGRAM_ID" || "PROGRAM_ID".equals(promptUtil.getColumnName())
                        || promptUtil.getColumnName() == "LAST_UPDATE_DATE" || "LAST_UPDATE_DATE".equals(promptUtil.getColumnName())
                        || promptUtil.getColumnName() == "ATTRIBUTE_CATEGORY" || "ATTRIBUTE_CATEGORY".equals(promptUtil.getColumnName())
                        || promptUtil.getColumnName() == "PROGRAM_APPLICATION_ID" || "PROGRAM_APPLICATION_ID".equals(promptUtil.getColumnName())
                        || promptUtil.getColumnName() == "PROGRAM_UPDATE_DATE" || "PROGRAM_UPDATE_DATE".equals(promptUtil.getColumnName())
                        || promptUtil.getColumnName() == "LAST_UPDATE_LOGIN" || "LAST_UPDATE_LOGIN".equals(promptUtil.getColumnName())){
                    continue;
                }

                dataRow++;
                XSSFRow row = sheet.createRow(dataRow);
                row.setRowStyle(style2);
                //代码
                XSSFCell cell1 = row.createCell(0);
                cell1.setCellStyle(style2);
                cell1.setCellValue(promptUtil.getCode().toLowerCase());
                //中文描述
                XSSFCell cell2 = row.createCell(1);
                cell2.setCellStyle(style2);
                cell2.setCellValue(promptUtil.getColumnDesc());
                //英文描述
                XSSFCell cell3 = row.createCell(2);
                cell3.setCellStyle(style2);
                /*if(promptUtil.getColumnName() == "LAST_UPDATED_BY" || "LAST_UPDATED_BY".equals(promptUtil.getColumnName())){
                    cell3.setCellValue("LastUpdatedBy");
                } else if(promptUtil.getColumnName() == "CREATED_BY" || "CREATED_BY".equals(promptUtil.getColumnName())){
                    cell3.setCellValue("CreatedBy");
                } else if(promptUtil.getColumnName() == "CREATION_DATE" || "CREATION_DATE".equals(promptUtil.getColumnName())){
                    cell3.setCellValue("CreationDate");
                } else if(promptUtil.getColumnName() == "OBJECT_VERSION_NUMBER" || "OBJECT_VERSION_NUMBER".equals(promptUtil.getColumnName())){
                    cell3.setCellValue("ObjectVersionNumber");
                } else if(promptUtil.getColumnName() == "REQUEST_ID" || "REQUEST_ID".equals(promptUtil.getColumnName())){
                    cell3.setCellValue("RequestId");
                } else if(promptUtil.getColumnName() == "PROGRAM_ID" || "PROGRAM_ID".equals(promptUtil.getColumnName())){
                    cell3.setCellValue("ProgramId");
                } else if(promptUtil.getColumnName() == "LAST_UPDATE_DATE" || "LAST_UPDATE_DATE".equals(promptUtil.getColumnName())){
                    cell3.setCellValue("LastUpdateDate");
                } else if(promptUtil.getColumnName() == "LAST_UPDATE_LOGIN" || "LAST_UPDATE_LOGIN".equals(promptUtil.getColumnName())){
                    cell3.setCellValue("LastUpdateLogin");
                }*/
                try {
                    cell3.setCellValue(promptUtil.getCode().toLowerCase().split("\\.")[1]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //cell3.setCellValue("");
            }
            response.addHeader("Content-Disposition",
                    "attachment; filename=\""+"list.xlsx"+"\"");
            response.setContentType("application/vnd.ms-excelUtil" + ";charset=" + "UTF-8");
            out = response.getOutputStream();
            // 写入excel文件
            workbook.write(out);
            out.close();
            workbook.close();

        }catch (Exception e){
            e.printStackTrace();
            out.close();
            workbook.close();
            System.out.println(e.getMessage());
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseData importPromptData(HttpServletRequest request, HttpServletResponse response, IRequest requestContext)throws Exception {
        /**
         *
         * 功能描述: 描述维护数据导入
         *
         * @auther:lkj
         * @date:2018/6/19 下午8:20
         * @param:[request, response, requestContext]
         * @return:com.hand.hap.system.dto.ResponseData
         *
         */
        ResponseData responseData = new ResponseData();
        FileUploadDto fileUploadDto = TransferExcelDateToFileUploadDtoUtil.TransferExcelDateToFileUploadDto(
                request, 2, 3, 0);
        List<FileUploadBaseDto> baseDtos = fileUploadDto.getList();
        List<Prompt> prompts = new ArrayList<>();
        for(int i=0;i<baseDtos.size();i++){
            Prompt prompt = new Prompt();
            Prompt prompt2 = new Prompt();
            if(baseDtos.get(i).getAttribute1()==null || "".equals(baseDtos.get(i).getAttribute1())){
                responseData.setSuccess(false);
                responseData.setMessage("第 "+(i+2)+" 行代码不可为空");
                return responseData;
            }
            prompt.setPromptCode(baseDtos.get(i).getAttribute1());
            List<Prompt> select = promptMapper.select(prompt);
            if(select.size()>0){
                responseData.setSuccess(false);
                responseData.setMessage("第 "+(i+2)+" 行代码已经存在");
                return responseData;
            }
            if(baseDtos.get(i).getAttribute2()==null || "".equals(baseDtos.get(i).getAttribute2())){
                responseData.setSuccess(false);
                responseData.setMessage("第 "+(i+2)+" 行中文描述不可为空");
                return responseData;
            }
            prompt.setDescription(baseDtos.get(i).getAttribute2());
            prompt.setLang("zh_CN");
            prompts.add(prompt);
            prompt.setPromptId(null);
            if(baseDtos.get(i).getAttribute3()==null || "".equals(baseDtos.get(i).getAttribute3())){
                responseData.setSuccess(false);
                responseData.setMessage("第 "+(i+2)+" 行英文描述不可为空");
                return responseData;
            }
            prompt2.setPromptCode(baseDtos.get(i).getAttribute1());
            prompt2.setDescription(baseDtos.get(i).getAttribute3());
            prompt2.setLang("en_GB");
            prompts.add(prompt2);
        }
        for(Prompt prompt:prompts){
            promptMapper.insertSelective(prompt);
        }
        return responseData;
    }

    @Override
    public ResponseData importCreateTable(HttpServletRequest request, HttpServletResponse response, IRequest requestContext) throws Exception {
        /**
         *
         * 功能描述: excelUtil 建表
         *
         * @auther:lkj
         * @date:2018/7/11 下午7:24
         * @param:[request, response, requestContext]
         * @return:com.hand.hap.system.dto.ResponseData
         *
         */
        String errorMsg = "";
        ResponseData responseData = new ResponseData();
        FileUploadDto fileUploadDto = TransferExcelDateToFileUploadDtoUtil.TransferExcelDateToFileUploadDto(
                request, 2, 8, 0);
        List<FileUploadBaseDto> baseDtos = fileUploadDto.getList();
        if(!checkString(baseDtos.get(0).getAttribute7())){
            responseData.setMessage("第二行第七列请输入表名");
            responseData.setSuccess(false);
            return responseData;
        }
        String sql = "create table "+baseDtos.get(0).getAttribute7()+"(";
        for(int i=0;i<baseDtos.size();i++){
            // 字段名
            if(checkString(baseDtos.get(i).getAttribute1())){
                sql += baseDtos.get(i).getAttribute1()+" ";
            }else{
                errorMsg += "第 "+(i+2)+" 行字段不可为空<br>";
            }
            // 字段类型
            if(checkString(baseDtos.get(i).getAttribute2())){
                sql += baseDtos.get(i).getAttribute2()+" (";
            }else{
                errorMsg += "第 "+(i+2)+" 行字段类型不可为空<br>";
            }
            // 字段长度
            if(checkString(baseDtos.get(i).getAttribute3())){
                sql += baseDtos.get(i).getAttribute3()+") ";
            }else{
                errorMsg += "第 "+(i+2)+" 行字段长度不可为空<br>";
            }
            // 主键
            if(i==0){
                if(checkString(baseDtos.get(i).getAttribute6())){
                    sql += baseDtos.get(i).getAttribute6()+" ";
                }
            }
            // 是否为空
            if(checkString(baseDtos.get(i).getAttribute4())){
                if(baseDtos.get(i).getAttribute4().equals("N")){
                    sql += " NOT NULL ";
                }else{

                }
            }else{
                errorMsg += "第 "+(i+2)+" 行是否为空不可为空<br>";
            }

            // 字段描述
            if(checkString(baseDtos.get(i).getAttribute5())){
                sql +="COMMENT '"+baseDtos.get(i).getAttribute5()+"',";
            }else{
                errorMsg += "第 "+(i+2)+" 行字段描述不可为空<br>";
            }

        }
        sql += "REQUEST_ID BIGINT NOT NULL DEFAULT '1' COMMENT '请求',";
        sql += "PROGRAM_ID BIGINT NOT NULL DEFAULT '1' COMMENT '架构字段',";
        sql += "OBJECT_VERSION_NUMBER BIGINT NOT NULL DEFAULT '1' COMMENT '版本号',";
        sql += "CREATED_BY BIGINT NOT NULL DEFAULT '1' COMMENT '创建人',";
        sql += "CREATION_DATE DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',";
        sql += "LAST_UPDATED_BY BIGINT NOT NULL DEFAULT '1' COMMENT '最后更新人',";
        sql += "LAST_UPDATE_DATE DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',";
        sql += "LAST_UPDATE_LOGIN BIGINT NOT NULL DEFAULT '1' COMMENT '最后更新登陆人'";
        if(!checkString(baseDtos.get(0).getAttribute8())){
            responseData.setMessage("第二行第八列请输入表注释");
            responseData.setSuccess(false);
            return responseData;
        }

        // 表注释
        sql +=") COMMENT='"+baseDtos.get(0).getAttribute8()+"'; ";
        if(checkString(errorMsg)){
            responseData.setMessage(errorMsg);
            responseData.setSuccess(false);
            return responseData;
        }
        // 将sql转成大写
        sql = sql.toUpperCase();
        mapper.createTable(sql);
        return responseData;
    }

    @Override
    public ResponseData createReportFile(IRequest iRequest, CreateReportFile dto) {
        /**
         *
         * 功能描述: 创建报表文件
         *
         * @auther:lkj
         * @date:2018/9/3 上午11:50
         * @param:[iRequest, dto]
         * @return:com.hand.hap.system.dto.ResponseData
         *
         */
        ResponseData responseData = new ResponseData();
        try{
            String projectUrl = dto.getProjectUrl();
            String packageName = dto.getPackageUrl();
            String fileName = dto.getCreateFileName();
            String sql = dto.getQuerySql();
            // 根据SQL 创建DTO IO方式
            // 先分成列
            String[] clomuns = sql.toUpperCase().split("FROM")[0].split("SELECT")[1].split(",");

            File file;
            file = new File(projectUrl);
            if(!judeDirExists(file)){
                System.out.println(projectUrl+"工程文件夹不存在");
                responseData.setSuccess(false);
                responseData.setMessage(projectUrl+"工程文件夹不存在");
                return responseData;
            }
            String packageUrl = packageName;
            file = new File(projectUrl+"/"+packageUrl);
            if(!judeDirExists(file)){
                file.mkdir();
            }
            // 创建DTO Mapper Service Controller 文件夹
            String dtoUrl = packageUrl+"/dto/";
            String mapperUrl = packageUrl+"/mapper/";
            String serviceUrl = packageUrl+"/service/";
            String serviceImplUrl = packageUrl+"/service/impl/";
            String controllerlUrl = packageUrl+"/controllers/";
            String htmlUrl = projectUrl.split("java")[0]+"webapp/WEB-INF/view/"+packageName+"/";
            for(int i=1;i<7;i++){
                if(i==1){
                    file = new File(projectUrl+"/"+dtoUrl);
                }else if(i==2){
                    file = new File(projectUrl+"/"+mapperUrl);
                }else if(i==3){
                    file = new File(projectUrl+"/"+serviceUrl);
                }else if(i==4){
                    file = new File(projectUrl+"/"+serviceImplUrl);
                }else if(i==5){
                    file = new File(projectUrl+"/"+controllerlUrl);
                }else if(i==6){
                    file = new File(htmlUrl);
                }
                file.mkdir();
            }
            // 创建DTO Mapper Service Controller .java文件
            // 创建Dto
            String newFileDto = projectUrl+"/"+dtoUrl+fileName+".java";
            file = new File(newFileDto);
            System.out.println("DTO文件创建中...");
            file.createNewFile();
            // 创建Mapper
            String newFileMapper = projectUrl+"/"+mapperUrl+fileName+"Mapper.java";
            file = new File(newFileMapper);
            System.out.println("Mapper文件创建中...");
            file.createNewFile();
            // 创建Mapper XML
            String newFileMapperXML = projectUrl+"/"+mapperUrl+fileName+"CalendarMapper.xml";
            file = new File(newFileMapperXML);
            System.out.println("Mapper.xml文件创建中...");
            file.createNewFile();
            // 创建Service
            String newFileService = projectUrl+"/"+serviceUrl+"/I"+fileName+"Service.java";
            file = new File(newFileService);
            System.out.println("Service文件创建中...");
            file.createNewFile();
            // 创建Service
            String newFileServiceImpl = projectUrl+"/"+serviceImplUrl+fileName+"ServiceImpl.java";
            file = new File(newFileServiceImpl);
            System.out.println("ServiceImpl文件创建中...");
            file.createNewFile();
            // 创建Controller
            String newFileController = projectUrl+"/"+controllerlUrl+fileName+"Controller.java";
            file = new File(newFileController);
            System.out.println("Controller文件创建中...");
            file.createNewFile();
            // 创建HTML
            String newFileHTML = htmlUrl+fileName.toLowerCase()+".html";
            file = new File(newFileHTML);
            System.out.println("HTML文件创建中...");
            File file1 = new File(htmlUrl);
            if (!file1.exists()) {
                file1.mkdirs();
            }
            file.createNewFile();
            System.out.println("文件创建完毕");
            // 创建输入流
            FileWriter writer;
            // 写入文件DTO
            StringBuilder dtoContext = new StringBuilder();
            // 导入包
            dtoContext.append("package "+dtoUrl.substring(0,dtoUrl.length()-1).replace("/",".")+";\n");
            // 注释
            dtoContext.append("/**\n *@Create By：ReportGenerator\n *@Create Date："+new Date()+"\n *@Desc：\n */\n");
            dtoContext.append("import com.hand.hap.system.dto.BaseDTO;\n\n");
            // Class 名称
            dtoContext.append("public class "+fileName+" extends BaseDTO {\n\n");
            List<String> clomunList = new ArrayList<>();
            // 写入列
            for(int i=0;i<clomuns.length;i++){
                String[] nameLen = clomuns[i].split(" AS ");
                String lowerCaseStr;
                if(nameLen.length == 1){
                    lowerCaseStr = nameLen[0].toLowerCase().replace(" ","");
                }else{
                    lowerCaseStr = nameLen[1].toLowerCase().replace(" ","");;
                }
                String clomun = lowerCaseStr.substring(lowerCaseStr.indexOf(".")+1,lowerCaseStr.length());
                System.out.println(replaceUnderlineAndfirstToUpper(clomun,"_",""));
                clomunList.add(replaceUnderlineAndfirstToUpper(clomun,"_",""));
                dtoContext.append("    private String "+replaceUnderlineAndfirstToUpper(clomun,"_","")+";\n\n");
            }
            // 添加 GET SET 方法
            for(int i=0;i<clomunList.size();i++){
                // get 方法
                dtoContext.append("    public String get"+firstCharacterToUpper(clomunList.get(i))+"() {\n");
                dtoContext.append("        return "+clomunList.get(i)+";\n");
                dtoContext.append("    }\n\n");
                // set 方法
                dtoContext.append("    public void set"+firstCharacterToUpper(clomunList.get(i))+"(String "+clomunList.get(i)+") {\n");
                dtoContext.append("        this."+clomunList.get(i)+" = "+clomunList.get(i)+";\n");
                dtoContext.append("    }\n\n");
            }
            dtoContext.append("}");
            writer = new FileWriter(newFileDto);
            writer.write(dtoContext.toString());
            writer.flush();
            writer.close();
            // 写入Mapper
            StringBuilder mapperContext = new StringBuilder();
            // 导入包
            mapperContext.append("package "+mapperUrl.substring(0,mapperUrl.length()-1).replace("/",".")+";\n\n");
            // 注释
            mapperContext.append("/**\n *@Create By：ReportGenerator\n *@Create Date："+new Date()+"\n *@Desc：\n */\n\n");

            mapperContext.append("import "+dtoUrl.replace("/",".")+fileName+";\n");
            mapperContext.append("import java.util.List;\n\n");
            // Class 名称
            mapperContext.append("public interface "+fileName+"Mapper{\n\n");
            mapperContext.append("    List<"+fileName+"> select("+fileName+" dto);\n\n");
            mapperContext.append("}");
            writer = new FileWriter(newFileMapper);
            writer.write(mapperContext.toString());
            writer.flush();
            writer.close();

            // 写入Mapper XML
            StringBuilder mapperXMLContext = new StringBuilder();
            // 导入包
            mapperXMLContext.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n\n");
            mapperXMLContext.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >\n");
            mapperXMLContext.append("<mapper namespace=\""+mapperUrl.replace("/",".")+fileName+"Mapper\">\n");
            mapperXMLContext.append("    <resultMap id=\"BaseResultMap\" type=\""+dtoUrl.replace("/",".")+fileName+"\">\n");
            for(int i=0;i<clomuns.length;i++){
                String[] nameLen = clomuns[i].split(" AS ");
                String lowerCaseStr;
                if(nameLen.length == 1){
                    lowerCaseStr = nameLen[0].toLowerCase().replace(" ","");
                }else{
                    lowerCaseStr = nameLen[1].toLowerCase().replace(" ","");;
                }
                String upperCaseStr = lowerCaseStr.toUpperCase();
                String clomun = upperCaseStr.substring(upperCaseStr.indexOf(".")+1,upperCaseStr.length());
                mapperXMLContext.append("        <result column=\""+clomun+"\" property=\""+clomunList.get(i)+"\" jdbcType=\"VARCHAR\" />\n");
            }
            mapperXMLContext.append("    </resultMap>\n");
            mapperXMLContext.append("    <select id=\"select\" resultMap=\"BaseResultMap\" parameterType=\""+dtoUrl.replace("/",".")+fileName+"\">\n");
            mapperXMLContext.append("       "+sql+"\n");
            mapperXMLContext.append("    </select>\n");
            mapperXMLContext.append("</mapper>\n");
            writer = new FileWriter(newFileMapperXML);
            writer.write(mapperXMLContext.toString());
            writer.flush();
            writer.close();
            // 写入ServiceImpl
            StringBuilder serviceImplContext = new StringBuilder();
            // 导入包
            serviceImplContext.append("package "+serviceImplUrl.substring(0,serviceImplUrl.length()-1).replace("/",".")+";\n\n");
            // 注释
            serviceImplContext.append("/**\n *@Create By：ReportGenerator\n *@Create Date："+new Date()+"\n *@Desc：\n */\n\n");

            serviceImplContext.append("import "+dtoUrl.replace("/",".")+fileName+";\n");
            serviceImplContext.append("import "+mapperUrl.replace("/",".")+fileName+"Mapper;\n");
            serviceImplContext.append("import org.springframework.beans.factory.annotation.Autowired;\n");
            serviceImplContext.append("import org.springframework.stereotype.Service;\n");
            serviceImplContext.append("import com.hand.hap.core.IRequest;\n");
            serviceImplContext.append("import "+serviceUrl.replace("/",".")+"I"+fileName+"Service;\n");
            serviceImplContext.append("import org.springframework.transaction.annotation.Transactional;\n");
            serviceImplContext.append("import java.util.List;\n\n");
            // Class 名称
            serviceImplContext.append("@Service\n");
            serviceImplContext.append("@Transactional(rollbackFor = Exception.class)\n");
            serviceImplContext.append("public class "+fileName+"ServiceImpl implements I"+fileName+"Service{\n\n");
            serviceImplContext.append("    @Autowired\n");
            serviceImplContext.append("    private "+fileName+"Mapper mapper;\n\n");
            serviceImplContext.append("    @Override\n");
            serviceImplContext.append("    public List<"+fileName+"> select("+fileName+" dto, IRequest iRequest)throws Exception {\n\n");
            serviceImplContext.append("        List<"+fileName+"> list = mapper.select(dto);\n");
            serviceImplContext.append("        return list;\n");
            serviceImplContext.append("    }\n\n");
            serviceImplContext.append("}");
            writer = new FileWriter(newFileServiceImpl);
            writer.write(serviceImplContext.toString());
            writer.flush();
            writer.close();
            // 写入IService
            StringBuilder serviceContext = new StringBuilder();
            // 导入包
            serviceContext.append("package "+serviceUrl.substring(0,serviceUrl.length()-1).replace("/",".")+";\n");

            // 注释
            serviceContext.append("/**\n *@Create By：ReportGenerator\n *@Create Date："+new Date()+"\n *@Desc：\n */\n");

            serviceContext.append("import com.hand.hap.core.IRequest;\n");
            serviceContext.append("import "+dtoUrl.replace("/",".")+fileName+";\n");
            serviceContext.append("import java.util.List;\n\n");
            // Class 名称
            serviceContext.append("public interface I"+fileName+"Service{\n\n");
            serviceContext.append("    List<"+fileName+"> select("+fileName+" dto, IRequest iRequest) throws Exception;\n\n");
            serviceContext.append("}");
            writer = new FileWriter(newFileService);
            writer.write(serviceContext.toString());
            writer.flush();
            writer.close();
            // 写入Controllers
            StringBuilder controllerContext = new StringBuilder();
            // 导入包
            controllerContext.append("package "+controllerlUrl.substring(0,controllerlUrl.length()-1).replace("/",".")+";\n\n");
            // 注释
            controllerContext.append("/**\n *@Create By：ReportGenerator\n *@Create Date："+new Date()+"\n *@Desc：\n */\n");
            controllerContext.append("import "+dtoUrl.replace("/",".")+fileName+";\n");
            controllerContext.append("import "+serviceUrl.replace("/",".")+"I"+fileName+"Service;\n");
            controllerContext.append("import com.hand.hap.core.IRequest;\n");
            controllerContext.append("import org.springframework.stereotype.Controller;\n");
            controllerContext.append("import javax.servlet.http.HttpServletRequest;\n");
            controllerContext.append("import com.hand.hap.system.dto.ResponseData;\n");
            controllerContext.append("import com.hand.hap.system.controllers.BaseController;\n");
            controllerContext.append("import org.springframework.web.bind.annotation.ResponseBody;\n");
            controllerContext.append("import org.springframework.web.bind.annotation.RequestMapping;\n");
            controllerContext.append("import org.springframework.beans.factory.annotation.Autowired;\n");
            controllerContext.append("import java.util.List;\n\n");
            // Class 名称
            controllerContext.append("@Controller\n");
            controllerContext.append("public class "+fileName+"Controller extends BaseController {\n\n");
            controllerContext.append("    @Autowired\n");
            controllerContext.append("    private I"+fileName+"Service service;\n\n");
            controllerContext.append("    @RequestMapping(\"/"+fileName.toLowerCase()+"/select\")\n");
            controllerContext.append("    @ResponseBody\n");
            controllerContext.append("    public ResponseData select("+fileName+" dto, HttpServletRequest request)throws Exception {\n\n");
            controllerContext.append("        IRequest requestCtx = createRequestContext(request);\n");
            controllerContext.append("        List<"+fileName+"> list = service.select(dto,requestCtx);\n");
            controllerContext.append("        return new ResponseData(list);\n\n");
            controllerContext.append("    }\n\n");
            controllerContext.append("}");
            writer = new FileWriter(newFileController);
            writer.write(controllerContext.toString());
            writer.flush();
            writer.close();
            // 创建html
            StringBuilder htmlContext = new StringBuilder();
            htmlContext.append("<!--\n");
            htmlContext.append("    @Create By：ReportGenerator\n");
            htmlContext.append("    @Create Date："+new Date()+"\n");
            htmlContext.append("    @Desc：\n");
            htmlContext.append("-->\n");
            htmlContext.append("<#include \"../include/header.html\">\n");
            htmlContext.append("<script type=\"text/javascript\">\n");
            htmlContext.append("    var viewModel = Hap.createGridViewModel(\"#grid\");\n");
            htmlContext.append("</script>\n");
            htmlContext.append("<body>\n");
            htmlContext.append("<div id=\"page-content\">\n");
            htmlContext.append("        <div class=\"pull-right\" id=\"query-form\" style=\"padding-bottom:10px;\">\n");
            for(int i=0;i<3;i++){
                if((clomunList.size()-1) == i){
                    break;
                }
                htmlContext.append("            <input type=\"text\" data-role=\"maskedtextbox\" style=\"float:left;width:150px;margin-right:5px;\" placeholder='<@spring.message \""+fileName.toLowerCase()+"."+clomunList.get(i).toLowerCase()+"\"/>' data-bind=\"value:model."+clomunList.get(i)+"\" class=\"k-textbox\">\n");
            }

            htmlContext.append("            <span class=\"btn btn-primary\" style=\"float:left;width:70px\" data-bind=\"click:query\" type=\"submit\"><@spring.message \"hap.query\"/></span>\n");
            htmlContext.append("            <div style=\"clear:both\"></div>\n");
            htmlContext.append("    </div>\n");
            htmlContext.append("    <script>kendo.bind($('#query-form'), viewModel);</script>\n");
            htmlContext.append("    <div style=\"clear:both\"><div id=\"grid\"></div></div>\n");
            htmlContext.append("</div>\n");
            htmlContext.append("<script type=\"text/javascript\">\n");
            htmlContext.append("    Hap.initEnterQuery('#query-form', viewModel.query);\n");
            htmlContext.append("    var BaseUrl = _basePath;\n");
            htmlContext.append("    var dataSource = new kendo.data.DataSource({\n");
            htmlContext.append("        transport: {\n");
            htmlContext.append("            read: {\n");
            htmlContext.append("                url: BaseUrl + \"/"+fileName.toLowerCase()+"/select\",\n");
            htmlContext.append("                type: \"POST\",\n");
            htmlContext.append("                dataType: \"json\" \n");
            htmlContext.append("            },\n");
            htmlContext.append("            parameterMap: function (options, type) {\n");
            htmlContext.append("                if (type !== \"read\" && options.models) {\n");
            htmlContext.append("                    var datas = Hap.prepareSubmitParameter(options, type);\n");
            htmlContext.append("                    return kendo.stringify(datas);\n");
            htmlContext.append("                } else if (type === \"read\") {\n");
            htmlContext.append("                    return Hap.prepareQueryParameter(viewModel.model.toJSON(), options);\n");
            htmlContext.append("                }\n");
            htmlContext.append("            },\n");
            htmlContext.append("        },\n");
            htmlContext.append("        batch: true,\n");
            htmlContext.append("        serverPaging: true,\n");
            htmlContext.append("        pageSize: 10,\n");
            htmlContext.append("        schema: {\n");
            htmlContext.append("            data: 'rows',\n");
            htmlContext.append("            total: 'total',\n");
            htmlContext.append("            model: {\n");
            htmlContext.append("                id: \"\",\n");
            htmlContext.append("                fields: {},\n");
            htmlContext.append("            },\n");
            htmlContext.append("        },\n");
            htmlContext.append("    });\n");
            htmlContext.append("    $(\"#grid\").kendoGrid({\n");
            htmlContext.append("        dataSource: dataSource,\n");
            htmlContext.append("        resizable: true,\n");
            htmlContext.append("        scrollable: true,\n");
            htmlContext.append("        sortable: true,\n");
            htmlContext.append("        navigatable: false,\n");
            htmlContext.append("        //selectable: 'multiple, rowbox',\n");
            htmlContext.append("        pageable: {\n");
            htmlContext.append("            pageSizes: [5, 10, 20, 50],\n");
            htmlContext.append("            refresh: true,\n");
            htmlContext.append("            buttonCount: 5\n");
            htmlContext.append("        },\n");
            htmlContext.append("        columns: [\n");
            for(int i=0;i<clomunList.size();i++){
                htmlContext.append("            {\n");
                htmlContext.append("                field: '"+clomunList.get(i)+"',\n");
                htmlContext.append("                title: '<@spring.message \""+fileName.toLowerCase()+"."+clomunList.get(i).toLowerCase()+"\"/>',\n");
                htmlContext.append("                width: 120,\n");
                htmlContext.append("                attributes: {style: \"text-align:center\"},\n");
                htmlContext.append("                headerAttributes: {style: \"text-align:center\"},\n");
                htmlContext.append("             },\n");
            }
            htmlContext.append("        ],\n");
            htmlContext.append("        editable: false\n");
            htmlContext.append("    });\n");
            htmlContext.append("</script>\n");
            htmlContext.append("</body>\n");
            htmlContext.append("</html>\n");

            writer = new FileWriter(newFileHTML);
            writer.write(htmlContext.toString());
            writer.flush();
            writer.close();
            responseData.setMessage("创建成功");
            return responseData;
        }catch (Exception e){
            e.printStackTrace();
            responseData.setMessage(e.getMessage());
            responseData.setSuccess(false);
            return responseData;
        }
    }

    private boolean checkString(String s){
        if(s == null || "".equals(s) || s == ""){
            return false;
        }
        return true;
    }

    // 判断文件夹是否存在
    public static boolean judeDirExists(File file) {
        /**
         *
         * 功能描述: 判断文件夹是否存在
         *
         * @auther:lkj
         * @date:2018/9/3 下午1:23
         * @param:[file]
         * @return:boolean
         *
         */
        if (file.exists()) {
            return true;
        } else {
            return false;
        }

    }

    public String replaceUnderlineAndfirstToUpper(String srcStr, String org, String ob) {
        /**
         *
         * 功能描述: 替换字符串并让它的下一个字母为大写
         *
         * @auther:lkj
         * @date:2018/9/3 下午1:24
         * @param:[srcStr, org, ob]
         * @return:java.lang.String
         *
         */
        String newString = "";
        int first = 0;
        while (srcStr.indexOf(org) != -1) {
            first = srcStr.indexOf(org);
            if (first != srcStr.length()) {
                newString = newString + srcStr.substring(0, first) + ob;
                srcStr = srcStr.substring(first + org.length(), srcStr.length());
                srcStr = firstCharacterToUpper(srcStr);
            }
        }
        newString = newString + srcStr;
        return newString;
    }

    public String firstCharacterToUpper(String srcStr) {
        /**
         *
         * 功能描述: 首字母变大写
         *
         * @auther:lkj
         * @date:2018/9/3 下午1:23
         * @param:[srcStr]
         * @return:java.lang.String
         *
         */
        return srcStr.substring(0, 1).toUpperCase() + srcStr.substring(1);
    }
}
