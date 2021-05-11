package com.fulan.server.common.util;

import com.fulan.server.common.annotation.CellMapping;
import com.fulan.server.common.annotation.ExcelMapping;
import com.fulan.server.common.myexception.FNException;
import com.fulan.server.common.myexception.FNExceptionMessage;
import com.fulan.server.model.ParamentEntity;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExcelUtils {

    /**
     * Logger 日志记录
     */
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private Workbook workBook;

    private ExcelUtils(){}

    public static final ExcelUtils getInstance() {
        return SingletonHolder.instance;
    }

    //内部静态类单例模式
    private static class SingletonHolder {
        private static final ExcelUtils instance = new ExcelUtils();
    }

    public <T> List<T> parseExcel(InputStream excelFile, Class<T> entityClass) throws Exception {
        return parseExcel(excelFile, entityClass, 0);
    }

    public <T> List<T> parseExcel(InputStream excelFile, Class<T> entityClass, int index) throws FNException, Exception {
        ExcelMapping excelMapping = (ExcelMapping) entityClass.getAnnotation(ExcelMapping.class);

        if (excelMapping == null) {
            throw new IllegalArgumentException("无法解析EXCLE-请添加@ExcelMapping");
        }
        Workbook wb = null;
        try{
            wb = new XSSFWorkbook(excelFile);
        }catch(Exception e){
            logger.info("解析异常！"+e);
            throw new FNException("");
        }
        return extracted(entityClass, excelMapping, wb, index);

    }

    private Map<Integer, ParamentEntity> parseAnnotation(Class<?> entity, int index) throws IntrospectionException {
        Field[] fields = entity.getDeclaredFields();

        if (fields == null) {
            throw new IllegalArgumentException("this class " + entity.getName() + " is not field");
        }
        Map<Integer, ParamentEntity> map = new HashMap<Integer, ParamentEntity>();

        for (Field element : fields) {
            if (!(element.isAnnotationPresent(CellMapping.class))) {
                continue;
            }
            CellMapping cm = (CellMapping) element.getAnnotation(CellMapping.class);
            if(cm.tableHeader().length >= index+1 && cm.cellNum().length >= index+1 && cm.cellNum()[index] > 0){
                ParamentEntity pe = new ParamentEntity();

                pe.setType(element.getType());

                pe.setFieldDescriptor(new PropertyDescriptor(element.getName(), entity));

                pe.setTableHeader(cm.tableHeader()[index]);

                pe.setNull(cm.isNull());

                pe.setDateFormat(cm.dateFormat());

                map.put(Integer.valueOf(cm.cellNum()[index]), pe);
            }
        }

        if (map.size() < 1) {
            throw new IllegalArgumentException("No CellMapping Annotation in " + entity.getName() + " Field");
        }
        return map;
    }

    private <T> List<T> extracted(Class<T> entityClass, ExcelMapping excelMapping, Workbook wb, int index)throws Exception {
        return excelToEntity(entityClass, wb.getSheetAt(0), parseAnnotation(entityClass, index), excelMapping.beginRow());
    }

    private <T> List<T> excelToEntity(Class<T> entity, org.apache.poi.ss.usermodel.Sheet sheet, Map<Integer, ParamentEntity> map, int beginRow) throws FNException, Exception {
        List<T> list = new ArrayList<T>();
        for (int i = beginRow - 1; i < sheet.getPhysicalNumberOfRows(); ++i) {
            Row row = sheet.getRow(i);
            T instance;
            try {
                instance = entity.newInstance();
            } catch (Exception e) {
                logger.info("解析异常！"+e);
                throw new FNException("解析异常！请检查Excel文件");
            }

            if (row != null) {
                for (int j = 0; j < row.getLastCellNum(); ++j) {
                    if (!(map.containsKey(Integer.valueOf(j + 1)))) {
                        continue;
                    }
                    try {
                        Cell cell = row.getCell(j);

                        ParamentEntity pe = (ParamentEntity) map.get(Integer.valueOf(j + 1));

                        if (cell == null && !(pe.isNull()) ){
                            throw new IllegalArgumentException(FNExceptionMessage.CELL_CHECK_ISNULL.getErrorMessageFromProperites());
                        }

                        if (cell != null || !(pe.isNull()) ) {
                            setValue(instance, getCellValue(cell, pe.getType(), pe.getDateFormat()), pe);
                        }
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        logger.info("解析异常:"+e);
                        throw new FNException(FNExceptionMessage.TOEXCEL_SETVALUE_ERROR.getErrorMessageFromProperites(
                                new Object[] { Integer.valueOf(i + 1), Integer.valueOf(j + 1)}));
                    }
                }
            }
            list.add(instance);
        }
        return list;
    }


    public <T> OutputStream exportExcel(List<T> entitylist) throws Exception {
        return exportExcel(entitylist, 0);
    }

    public <T> OutputStream exportExcel(List<T> entitylist, int index) throws Exception {

        if ((entitylist == null) || (entitylist.size() < 1)) {
            throw new IllegalArgumentException("Export Excel Class[" + ExcelUtils.class.getName() + "] Argument is Null or no data");
        }
        Object annotation = entitylist.get(0);

        ExcelMapping excelMapping = (ExcelMapping) annotation.getClass().getAnnotation(ExcelMapping.class);

        if (excelMapping == null) {
            throw new IllegalArgumentException("No Have ExcelExportMapping Annotation In Class [" + annotation.getClass().getName() + "]");
        }
        int rownum = 0;

        Map<Integer, ParamentEntity> annotationMap = parseAnnotation(annotation.getClass(), index);

        workBook = new XSSFWorkbook();
        Sheet sheet = workBook.createSheet("Excel Export");

        CellStyle cellStyle = workBook.createCellStyle();
        Font font = workBook.createFont();
       // font.setBold(true);
        font.setFontHeightInPoints((short)10);
        cellStyle.setFont(font);
        //cellStyle.setAlignment(HorizontalAlignment.CENTER);

        Iterator<T> i$ = entitylist.iterator();

        while (i$.hasNext()) {
            T entity = i$.next();

            if ((rownum == 0) && (hasTableHeader(excelMapping))) {
                createTableHeader(sheet, rownum++, entity, annotationMap, cellStyle);
            }
            createTableRow(sheet, rownum++, entity, annotationMap);
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        workBook.write(os);

        return os;
    }

    private <T> boolean hasTableHeader(ExcelMapping excelMapping) {
        return excelMapping.hasTableHeader();
    }

    private <T> void createTableHeader(Sheet sheet, int rownum, T entity,
                                       Map<Integer, ParamentEntity> annotationMap, CellStyle cellStyle) throws Exception {
        Row row = sheet.createRow(rownum);
        row.setHeight((short) 600);
        Iterator<Integer> i$ = annotationMap.keySet().iterator();
        int i = 0;
        while ( i$.hasNext() ) {
            int key = ((Integer) i$.next()).intValue();

            Cell cell = row.createCell(key - 1);
            ParamentEntity pe = (ParamentEntity) annotationMap.get(Integer.valueOf(key));

            cell.setCellValue(String.valueOf(pe.getTableHeader()));
            cell.setCellStyle(cellStyle);
            sheet.setColumnWidth(i, 4000);
            i++;
        }
    }

    private <T> void createTableRow(Sheet sheet, int rownum, T entity, Map<Integer, ParamentEntity> annotationMap) throws Exception {

        Row row = sheet.createRow(rownum);

        Iterator<Integer> i$ = annotationMap.keySet().iterator();
        while ( i$.hasNext()) {
            int key = ((Integer) i$.next()).intValue();

            Cell cell = row.createCell(key - 1);

            ParamentEntity pe = (ParamentEntity) annotationMap.get(Integer.valueOf(key));

            SimpleDateFormat sdf = new SimpleDateFormat(pe.getDateFormat());

            Object obj = pe.getFieldDescriptor().getReadMethod().invoke(entity, new Object[0]);
            if(pe.isNull() && obj == null){
                cell.setCellValue("");
            }else{
                if(obj == null){
                    cell.setCellValue("");
                }else if (pe.getFieldDescriptor().getPropertyType().equals(Date.class)) {
                    cell.setCellValue(sdf.format((Date)obj));
                } else {
                    cell.setCellValue(String.valueOf(obj));
                }
            }

        }
    }

    private <T> T setValue(T instance, Object value, ParamentEntity pe) throws Exception {
        pe.getFieldDescriptor().getWriteMethod().invoke(instance, new Object[]{value});

        return instance;
    }

    private Object getCellValue(Cell cell, Type type, String dateFormat) throws ParseException{
        Object value = null;
        if(type.equals(int.class) || type.equals(Integer.class)){
            value = Integer.parseInt(cell.getRichStringCellValue().getString());
        }else if(type.equals(String.class)){
            value = cell.getRichStringCellValue().getString();
        }else if(type.equals(double.class) || type.equals(Double.class)){
            value = Double.parseDouble(cell.getRichStringCellValue().getString());
        }else if(type.equals(long.class) || type.equals(Long.class)){
            value = Long.parseLong(cell.getRichStringCellValue().getString());
        }else if(type.equals(boolean.class) || type.equals(Boolean.class)){
            value = cell.getBooleanCellValue();
        }else if(type.equals(short.class) || type.equals(Short.class)){
            value = Short.parseShort(cell.getRichStringCellValue().getString());
        }else if(type.equals(float.class) || type.equals(Float.class)){
            value = Float.parseFloat(cell.getRichStringCellValue().getString());
        }else if(type.equals(byte.class) || type.equals(Byte.class)){
            value = Byte.parseByte(cell.getRichStringCellValue().getString());
        }else if(type.equals(char.class) || type.equals(Character.class)){
            value = cell.getRichStringCellValue().getString().charAt(0);
        }else if(type.equals(Date.class)){
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            value = sdf.parse(cell.getRichStringCellValue().getString());
        }else if(type.equals(BigDecimal.class)){
            value = new BigDecimal(cell.getRichStringCellValue().getString());
        }else if(type.equals(BigInteger.class)){
            value = new BigInteger(cell.getRichStringCellValue().getString());
        }
        return value;
    }
}

