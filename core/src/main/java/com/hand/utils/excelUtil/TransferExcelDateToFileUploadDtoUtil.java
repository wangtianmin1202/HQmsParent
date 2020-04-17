package com.hand.utils.excelUtil;

import oracle.core.lmx.CoreException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TransferExcelDateToFileUploadDtoUtil {

    public static final String TEMP_PATH ="D://temp";   //.xls文件暂存路径

    /**
     *
     * @Description 将Excel中前sheetCount个sheet页转换成List<FileUploadDto>
     *
     * @author yuchao.wang
     * @date 2018/10/22 20:45
     * @param request 文件流
     * @param startRowId 从第几行数据开始解析
     * @param colCount 列数
     * @param sheetCount 要解析的sheet页总数
     * @return java.util.List<com.hand.util.excel.dto.FileUploadDto>
     *
     */
    public static List<FileUploadDto> TransferExcelDateToFileUploadDtoList(HttpServletRequest request, int startRowId, int colCount, int sheetCount) throws Exception {
        //将excel文件流解析为InputStream流
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        List items = upload.parseRequest(request);
        InputStream inputStream = null;

        String excelType = "";
        if(!items.isEmpty()) {
            FileItem item = (FileItem)items.get(0);
            if(!item.isFormField()) {
                inputStream = item.getInputStream();

                //获得当前Excel格式
                String fileName = item.getName();
                excelType = fileName.substring(fileName.lastIndexOf(".") + 1);
            }
        }

        List<FileUploadDto> fileUploadDtoList = TransferExcelDateToFileUploadDtoUtil.transferExcelDateToArrayLists(inputStream, excelType, startRowId, colCount, sheetCount);

        return fileUploadDtoList;
    }

    /**
     *
     * @Description 将Excel中前sheetCount个sheet页转换成List<FileUploadDto>
     *
     * @author yuchao.wang
     * @date 2018/10/22 20:45
     * @param inputStream 文件流
     * @param startRowId 从第几行数据开始解析
     * @param colCount 列数
     * @param sheetCount 要解析的sheet页总数
     * @return java.util.List<com.hand.util.excel.dto.FileUploadDto>
     *
     */
    public static  List<FileUploadDto> transferExcelDateToArrayLists(InputStream inputStream, String excelType, int startRowId, int colCount, int sheetCount) throws Exception {
        Workbook workBook = null;
        List<FileUploadDto> fileUploadDtoList = new ArrayList<>();

        try {
            if (excelType.equals("xls")){
                workBook = new HSSFWorkbook(inputStream);
            } else {
                workBook = new XSSFWorkbook(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int sheetIndex=0; sheetIndex<sheetCount; sheetIndex++) {
            FileUploadDto fileUploadDto = new FileUploadDto();
            List<FileUploadBaseDto> list = new ArrayList<>();
            Sheet sheet = workBook.getSheetAt(sheetIndex);

            int rownum = sheet.getLastRowNum() + 1;
            for (int i = startRowId - 1; i < rownum; i++) {
                Row row = sheet.getRow(i);
                FileUploadBaseDto model = new FileUploadBaseDto();
                model.setRowNum(i + 1);

                for (int j = 0; j < colCount; j++) {
                    model = TransferExcelDateToFileUploadDtoUtil.setAttribute(model, row, j);
                }
                list.add(model);
            }

            //构造List<dto>
            fileUploadDto.setList(list);
            fileUploadDto.setColNum(colCount);
            fileUploadDtoList.add(fileUploadDto);
        }
        return fileUploadDtoList;
    }

    /*
     *  传入Excel文件流，返回改文件流解析后的list信息
     *  传参：request:文件流
     *  startRowId:从第几行数据开始解析
     *  colCount：列数
     *  sheetIndex：sheet索引
     *  返回参数：FileUploadDto
     */
    public static FileUploadDto TransferExcelDateToFileUploadDto(HttpServletRequest request, int startRowId, int colCount, int sheetIndex) throws Exception {
        //将excel文件流解析为InputStream流
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        List items = upload.parseRequest(request);
        InputStream inputStream = null;

        String excelType = "";
        if(!items.isEmpty()) {
            FileItem item = (FileItem)items.get(0);
            if(!item.isFormField()) {
                inputStream = item.getInputStream();

                //获得当前Excel格式
                String fileName = item.getName();
                excelType = fileName.substring(fileName.lastIndexOf(".") + 1);
            }
        }

        //将该fileInputStream解析为list对象
        FileUploadDto fileUploadDto = TransferExcelDateToFileUploadDtoUtil.transferExcelDateToArrayList(inputStream,excelType,startRowId,colCount,sheetIndex);

        return fileUploadDto;
    }

    /*
       将excel中的数据转换为java对象数组
       传参fileInputStream：excel的文件输入流
       返回：FileUploadDto
    */
    public static  FileUploadDto transferExcelDateToArrayList(InputStream inputStream, String excelType, int startRowId, int colCount, int sheetIndex) throws Exception {
        Sheet sheet;
        Workbook workBook = null;
        List<FileUploadBaseDto> list = new ArrayList<>();
        FileUploadDto fileUploadDto = new FileUploadDto();

        /*
         * 得到sheet
         */
        try {
            if (excelType.equals("xls")){
                workBook = new HSSFWorkbook(inputStream);
            } else {
                workBook = new XSSFWorkbook(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
         * 验证：导入模板数据时，如果有多个sheet如何选择？
         */
        int count = workBook.getNumberOfSheets();
        if(count>1){
            throw new Exception("导入模板中存在多页!");
        }

        //获得第一个sheet
        sheet = workBook.getSheetAt(sheetIndex);

        /*
        遍历所有行
         */
        int rownum = sheet.getLastRowNum() + 1;
        for (int i = startRowId-1; i < rownum; i++) {
            Row row = sheet.getRow(i);
            FileUploadBaseDto model = new FileUploadBaseDto();
            model.setRowNum(i+1);

            /*
            构造sheet中单行数据的dto
             */
            for(int j = 0;j < colCount; j++){
                model = TransferExcelDateToFileUploadDtoUtil.setAttribute(model,row,j);
            }
            list.add(model);
        }

        //返回
        fileUploadDto.setList(list);
        fileUploadDto.setColNum(colCount);
        return fileUploadDto;

    }

    /*
     * 将Row中第colNum列的信息放入FileUploadBaseDto中
     */
    public static FileUploadBaseDto setAttribute(FileUploadBaseDto fileUploadBaseDto,Row row,int colNum ){
        Cell cell = row.getCell(colNum);

        if(cell == null)
            return fileUploadBaseDto;

        if(cell.toString().equals("")){
            return fileUploadBaseDto;
        }

        Class clazz = FileUploadBaseDto.class;
        int count = colNum+1;
        try {
            String val = "";
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_STRING:
                    val = cell.getStringCellValue();
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    Boolean val1 = cell.getBooleanCellValue();
                    val = val1.toString();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                        Date theDate = cell.getDateCellValue();
                        SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        val = dff.format(theDate);
                    }else{
                        DecimalFormat df = new DecimalFormat("#.##");
                        val = df.format(cell.getNumericCellValue());
                    }
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    try {
                        val = String.valueOf(cell.getNumericCellValue());
                    } catch (IllegalStateException e) {
                        val = String.valueOf(cell.getRichStringCellValue());
                    }
                    break;
                case Cell.CELL_TYPE_BLANK:
                    break;
                default:
                    throw new CoreException("数据类型配置不正确");
            }
            Method method = clazz.getDeclaredMethod("setAttribute" + count,String.class);
            method.invoke(fileUploadBaseDto,val);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //一旦有值则dto不为空
        fileUploadBaseDto.setEmpty(false);

        return fileUploadBaseDto;
    }

}
