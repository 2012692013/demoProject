package com.qyh.demo.excel;

//这里可自行定义一个excelException进行错误抛出
import com.qyh.demo.exceptions.ExcelException;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;

public class ExcelImport {
    public ExcelImport() {
    }
    /**
     * 读EXCEL文件，获取信息集合
     *
     * @param mFile
     * @return
     */
    public static <T> List<T> getExcelInfo(MultipartFile mFile,Class<T> clazz) {
        String fileName = mFile.getOriginalFilename();// 获取文件名
        try {
            if (!validateExcel(fileName)) {// 验证文件名是否合格
                return null;
            }
            boolean isExcel2003 = true;// 根据文件名判断文件是2003版本还是2007版本
            if (isExcel2007(fileName) || isExcelCsv(fileName)) {
                isExcel2003 = false;
            }
            return createExcel(mFile.getInputStream(), isExcel2003,clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据excel里面的内容读取客户信息
     *
     * @param is          输入流
     * @param isExcel2003 excel是2003还是2007版本
     * @return
     * @throws IOException
     */
    private static  <T> List<T> createExcel(InputStream is, boolean isExcel2003,Class<T> clazz) {
        try {
            Workbook wb = null;
            if (isExcel2003) {// 当excel是2003时,创建excel2003
                wb = new HSSFWorkbook(is);
            } else {// 当excel是2007时,创建excel2007
                wb = new XSSFWorkbook(is);
            }
            return readExcelValue(wb,clazz);// 读取Excel里面客户的信息
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取Excel
     *
     * @param wb
     * @return
     */
    private static  <T> List<T> readExcelValue(Workbook wb,Class<T> clazz) {
        try {
            ExcelImporter excelReader = clazz.getAnnotation(ExcelImporter.class);
//            // 预留于多表文件上传
//            String value = excelReader.value();
            String sheetName = excelReader.sheetName();
            int titleNum = excelReader.titleRow();
            int startNum = excelReader.startRow();
            Sheet sheet = null;
            if (StringUtils.isBlank(sheetName))
                sheet = wb.getSheetAt(0);
            else
                sheet = wb.getSheet(sheetName);
            // 得到Excel的行数
            int totalRows = sheet.getPhysicalNumberOfRows();

            int totalCells = 0;
            // 得到Excel的列数(前提是有行数)
            if (totalRows > 1 && sheet.getRow(titleNum) != null) {
                totalCells = sheet.getRow(titleNum).getLastCellNum();
            }
            Row titleRow = sheet.getRow(titleNum);
            // 解析表头
            Map<String, Integer> titleMap = new HashMap<>();
            for (int i = 0; i < titleRow.getLastCellNum(); i++) {
                Cell cell = titleRow.getCell(i);
                if (cell == null || StringUtils.isBlank(cell.getStringCellValue()))
                    continue;
                String cellValue = cell.getStringCellValue();
                cellValue = cellValue.trim();
                titleMap.put(cellValue, i);
            }
            // 解析实体类
            Map<String, String> beanMap = new HashMap<>();
            // 需要解析的列
            List<Integer> parseCell = new ArrayList<>();
            // 解析的节点
            Map<Integer, String> parseBean = new HashMap<>();
            // 翻译节点
            Map<String, Map<String,String>> transMap = new HashMap<>();
            Field[] declaredFields = clazz.getDeclaredFields();
            for (int i = 0; i < declaredFields.length; i++) {
                Field field = declaredFields[i];
                boolean present = field.isAnnotationPresent(ExcelCellImporter.class);
                if (present) {
                    ExcelCellImporter reader = field.getAnnotation(ExcelCellImporter.class);
                    String title = reader.value();
                    if (titleMap.get(title) == null && reader.required()) {
                        System.err.println("Excel内找不到约定对应字段:" + title + "\r\n\tfrom:" + clazz.getName());
                        //这里可自行定义一个excelException进行错误抛出
                        throw new ExcelException("Excel内找不到约定对应字段:" + title);
                    }
                    beanMap.put(title, field.getName());
                    parseCell.add(titleMap.get(title));
                    parseBean.put(titleMap.get(title), field.getName());
                    // 加入解析器
                    ExcelTrans[] trans = reader.trans();
                    if (trans != null && trans.length > 0) {
                        Map<String, String> propertyTransMap = new HashMap<>();
                        for (int j = 0; j < trans.length; j++) {
                            ExcelTrans tran = trans[j];
                            String bean = tran.bean();
                            String excel = tran.excel();
                            propertyTransMap.put(excel, bean);
                        }
                        transMap.put(field.getName(),propertyTransMap);
                    }
                } else continue;
            }


            List<T> exportList = new ArrayList<>();

            if (parseCell.size() < 1)
                return null;
            // 循环Excel行数
            for (int r = startNum; r < totalRows; r++) {
                Row row = sheet.getRow(r);
                if (row == null) {
                    continue;
                }
                T instance = clazz.newInstance();
                //有效数据行
                int validData = 0;
                for (int c = 0; c < parseCell.size(); c++) {
                    Integer cellNum = parseCell.get(c);
                    if (cellNum == null)
                        continue;
                    Cell cell = row.getCell(cellNum);
                    if (cell == null || cell.toString().trim().equals(""))
                        continue;
                    validData++;
                    cell.setCellType(CellType.STRING);
                    Field field = clazz.getDeclaredField(parseBean.get(cellNum));
                    Class<?> type = field.getType();
                    Object value = cell.getStringCellValue().trim();
                    Map<String, String> valueTransMap = transMap.get(field.getName());
                    if (valueTransMap != null && valueTransMap.get(value) != null)
                        value = valueTransMap.get(value);

                    if (type.equals(String.class))
                        value = String.valueOf(value);
                    if (type.equals(Integer.class))
                        value = Integer.valueOf(new Double(value.toString()).intValue());
                    if (type.equals(BigDecimal.class))
                        value = new BigDecimal(value.toString());
                    if (type.equals(Date.class))
                        value = new BigDecimal(String.valueOf(value));
                    if (type.equals(Double.class))
                        value = new Double(String.valueOf(value));
                    if (type.equals(Float.class))
                        value = new Float(String.valueOf(value));

                    field.setAccessible(true);
                    field.set(instance,value);
                    field.setAccessible(false);
                }
                if (validData > 0) {
                    if (instance instanceof ExcelImportReader) {
                        Method reconstruct = clazz.getDeclaredMethod("reconstruct", Object.class);
                        Object invoke = reconstruct.invoke(instance,instance);
                        instance = (T) invoke;
                    }
                    if (instance != null)
                        exportList.add(instance);
                }
            }
            return exportList;
        }catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return new ArrayList<T>();
    }

    /**
     * 验证EXCEL文件
     *
     * @param filePath
     * @return
     */
    private static boolean validateExcel(String filePath) {
        if (filePath == null || !(isExcel2003(filePath) || isExcel2007(filePath) || isExcelCsv(filePath))) {
            //文件名不是excel格式
            return false;
        }
        return true;
    }

    // @描述：是否是2003的excel，返回true是2003
    private static boolean isExcel2003(String filePath) {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }

    // @描述：是否是2007的excel，返回true是2007
    private static boolean isExcel2007(String filePath) {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }

    // @描述：是否是csv文件
    private static boolean isExcelCsv(String filePath) {
        return filePath.matches("^.+\\.(?i)(csv)$");
    }
}
