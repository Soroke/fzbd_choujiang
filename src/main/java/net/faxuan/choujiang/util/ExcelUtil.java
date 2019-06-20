package net.faxuan.choujiang.util;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

/**
 * Created by song on 2019/06/06.
 */
public class ExcelUtil {

    private static XSSFWorkbook workbook = null;
    /**
     * 创建新excel.
     * @param fileDir  excel的路径
     * @param sheetNames 要创建的表格索引
     * @param titleRow excel的第一行即表格头
     */
    public static void createExcel(String fileDir,List<String> sheetNames,List<String> titleRow) throws Exception{
        //创建workbook
        workbook = new XSSFWorkbook();
        FileOutputStream out = null;
        for (String sheetName:sheetNames) {
            //添加Worksheet（不添加sheet时生成的xls文件打开时会报错)
            XSSFSheet sheet1 = workbook.createSheet(sheetName);
            try {
                //添加表头
                XSSFRow row = workbook.getSheet(sheetName).createRow(0);    //创建第一行
                for(short i = 0;i < titleRow.size();i++){
                    XSSFCell cell = row.createCell(i);
                    cell.setCellValue(titleRow.get(i));
                }
            } catch (Exception e) {
                throw e;
            }
        }
        try {
            out = new FileOutputStream(fileDir);
            workbook.write(out);
        }catch (Exception e) {
            throw e;
        }finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 往excel中写入(已存在的数据无法写入).
     * @param fileDir    文件路径
     * @param sheetName  表格索引
     * @throws Exception
     */
    public static void writeToExcel(String fileDir, String sheetName, List<String[]> dataList) throws Exception{
        //创建workbook
        File file = new File(fileDir);
        try {
            workbook = new XSSFWorkbook(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //流
        FileOutputStream out = null;
        XSSFSheet sheet = workbook.getSheet(sheetName);
        // 获取表格的总行数
        // int rowCount = sheet.getLastRowNum() + 1; // 需要加一
        // 获取表头的列数
        int columnCount = sheet.getRow(0).getLastCellNum();

        try {
            // 获得表头行对象
            XSSFRow titleRow = sheet.getRow(0);
            if(titleRow!=null){
                for(int rowId=0;rowId<dataList.size();rowId++){
                    String[] data = dataList.get(rowId);
                    XSSFRow newRow=sheet.createRow(rowId+1);
                    for (short columnIndex = 0; columnIndex < columnCount; columnIndex++) {
                        XSSFCell cell = newRow.createCell(columnIndex);
                        cell.setCellValue(data[columnIndex]==null ? null : data[columnIndex]);
                    }
                }
            }
            out = new FileOutputStream(fileDir);
            workbook.write(out);
        }catch (Exception e) {
            throw e;
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}
