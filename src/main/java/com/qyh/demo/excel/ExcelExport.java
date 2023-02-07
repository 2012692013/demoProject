package com.qyh.demo.excel;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author mars_q
 * @date 2019/5/6 14:25
 * create by 2012692013@qq.com
 */
public class ExcelExport {

    private static String FILE_TYPE = ".xlsx";

    public static <T> void export(HttpServletResponse response, List<T> data) {
        if (data == null || data.isEmpty())
            return;
        //获取模板
        Class<?> clazz = data.get(0).getClass();
        //只支持模板型创建
        if (!clazz.isAnnotationPresent(ExcelExporter.class))
            return;
        ExcelExporter exporter = clazz.getAnnotation(ExcelExporter.class);
        String fileSimpleName = StringUtils.isNotBlank(exporter.value())?exporter.value():clazz.getSimpleName();
        String fileName = fileSimpleName + FILE_TYPE;
        String sheetName = fileSimpleName;

        Field[] fields = clazz.getDeclaredFields();
        // 排序列
        List<ExportCellOrder> exportCellOrders = new ArrayList<>();
        // 翻译节点
        Map<String, Map<String,String>> transMap = new HashMap<>();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (!field.isAnnotationPresent(ExcelCellExporter.class))
                continue;
            ExcelCellExporter cellExporter = field.getAnnotation(ExcelCellExporter.class);
            int order = cellExporter.order();
            String cellName = cellExporter.value();
            String property = field.getName();
            exportCellOrders.add(new ExportCellOrder() {{
                setOrder(order);
                setCellName(cellName);
                setProperty(property);
            }});
            // 加入解析器
            ExcelTrans[] trans = cellExporter.trans();
            if (trans != null && trans.length > 0) {
                Map<String, String> propertyTransMap = new HashMap<>();
                for (int j = 0; j < trans.length; j++) {
                    ExcelTrans tran = trans[j];
                    String bean = tran.bean();
                    String excel = tran.excel();
                    propertyTransMap.put(bean, excel);
                }
                transMap.put(field.getName(),propertyTransMap);
            }
        }
        Collections.sort(exportCellOrders);

        HSSFWorkbook workbook = create(response, sheetName, exportCellOrders, data, clazz,transMap);
        try {
            buildExcelFile(fileName, workbook);
            buildExcelDocument(fileName,workbook,response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static<T> HSSFWorkbook create(HttpServletResponse response,
                                          String sheetName,
                                          List<ExportCellOrder> cells,
                                          List<T> data,
                                          Class<?> clazz,
                                          Map<String, Map<String,String>> transMap) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet(sheetName);
        HSSFRow row = sheet.createRow(0);
        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("yyyy-MM-dd HH:mm:ss"));
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFont(font);

        HSSFCell cell;

        for (int i = 0; i < cells.size(); i++) {
            ExportCellOrder cellOrder = cells.get(i);
//            余两个字符使居中列头不挤压
            sheet.setColumnWidth(i, (cellOrder.getCellName().length() + 2) * 512);
            cell = row.createCell(i);
            cell.setCellValue(cellOrder.getCellName());
            cell.setCellStyle(style);
        }

        for (int i = 0; i < data.size(); i++) {
            HSSFRow dataRow = sheet.createRow(i + 1);
            T t = data.get(i);
            for (int j = 0; j < cells.size(); j++) {
                ExportCellOrder c = cells.get(j);
                Field field = null;
                try {
                    String property = c.getProperty();
                    field = clazz.getDeclaredField(property);
                    Class<?> type = field.getType();
                    field.setAccessible(true);
                    Object o = field.get(t);
                    field.setAccessible(false);
                    HSSFCell rowCell = dataRow.createCell(j);
                    if (!type.equals(Date.class)) {
                        Map<String, String> valueTransMap = transMap.get(field.getName());
                        if (valueTransMap != null && valueTransMap.get(o) != null)
                            o = valueTransMap.get(o);
                        rowCell.setCellValue(o == null ? null : o.toString());
                    } else {
                        rowCell.setCellValue(o == null ? null : new SimpleDateFormat().format(((Date) o)));
                    }
                } catch (Exception e) {
                    if (field != null)
                        field.setAccessible(false);
                    e.printStackTrace();
                }
            }
        }
        return workbook;
    }


    //生成excel文件
    private static void buildExcelFile(String filename, HSSFWorkbook workbook) throws Exception {
        FileOutputStream fos = new FileOutputStream(filename);
        workbook.write(fos);
        fos.flush();
        fos.close();
    }

    //浏览器下载excel
    private static void buildExcelDocument(String filename, HSSFWorkbook workbook, HttpServletResponse response) throws Exception {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }

    private static class ExportCellOrder implements Comparable<ExportCellOrder>{
        private int order;
        private String property;
        private String cellName;

        public ExportCellOrder() {
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }

        public String getCellName() {
            return cellName;
        }

        public void setCellName(String cellName) {
            this.cellName = cellName;
        }

        @Override
        public int compareTo(ExportCellOrder o) {
            int cop = order - o.getOrder();
            if (cop != 0)
                return cop;
            else
                return property.compareTo(o.property);
        }

        @Override
        public String toString() {
            return "ExportCellOrder{" +
                    "order=" + order +
                    ", property='" + property + '\'' +
                    ", cellName='" + cellName + '\'' +
                    '}';
        }
    }
}

