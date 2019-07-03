package com.mpool.common.util;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.beanutils.locale.converters.BigDecimalLocaleConverter;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import com.mpool.common.RequestResolveUtil;

public class ExcelUtil {

	/**
	 * 导出Excel
	 * 
	 * @param sheetName sheet名称
	 * @param title     标题
	 * @param values    内容
	 * @param wb        HSSFWorkbook对象
	 * @return
	 */
	public static HSSFWorkbook getHSSFWorkbook(String sheetName, List<Object> title, List<List<Object>> values,
			HSSFWorkbook wb) {

		// 第一步，创建一个HSSFWorkbook，对应一个Excel文件
		if (wb == null) {
			wb = new HSSFWorkbook();
		}

		// 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet(sheetName);

		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
		HSSFRow row = sheet.createRow(0);

		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER); // 创建一个居中格式

		// 声明列对象
		HSSFCell cell = null;

		// 创建标题
		if (title != null) {
			for (int i = 0; i < title.size(); i++) {
				cell = row.createCell(i);
				cell.setCellValue(title.get(i).toString());
				cell.setCellStyle(style);
			}

		}
		TimeZone requestTimeZone = RequestResolveUtil.getRequestTimeZone();
		SimpleDateFormat format = new SimpleDateFormat("'(UTC)' yyyy-MM-dd HH:mm:ss");
		if (requestTimeZone != null) {
			format.setTimeZone(requestTimeZone);
		}

		// 创建内容
		for (int i = 0; i < values.size(); i++) {
			row = sheet.createRow(i + 1);
			List<Object> rows = values.get(i);
			for (int j = 0; j < rows.size(); j++) {
				// 将内容按顺序赋给对应的列对象
				Object object = rows.get(j);
				if (object == null) {
					row.createCell(j).setCellValue("");
				} else {

					if (object instanceof Date) {
						Date date = (Date) object;
						row.createCell(j).setCellValue(format.format(date));
					} else if (object instanceof java.sql.Date) {
						java.sql.Date date = (java.sql.Date) object;
						format.format(date);
					} else if (object instanceof BigDecimal) {
						BigDecimal bigDecimal = (BigDecimal) object;
						row.createCell(j).setCellValue(bigDecimal.toPlainString());
					} else {
						row.createCell(j).setCellValue(object.toString());
					}

				}
			}

		}
		return wb;
	}
}
