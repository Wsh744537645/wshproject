package com.mpool.common.util;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

public class CSVUtil {
	public static void createCSVFile(List<Object> head, List<List<Object>> dataList, OutputStream outputStream)
			throws IOException {
		BufferedWriter csvWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
		if (head != null) {
			writeRow(head, csvWriter);
		}
		for (List<Object> row : dataList) {
			writeRow(row, csvWriter);
		}
		csvWriter.close();
	}

	/**
	 * 写一行数据方法
	 * 
	 * @param row
	 * @param csvWriter
	 * @throws IOException
	 */
	private static void writeRow(List<Object> row, BufferedWriter csvWriter) throws IOException {
		// 写入文件头部
		int size = row.size();
		for(int i = 0; i < size; i++){
			StringBuffer sb = new StringBuffer();
			sb = sb.append(row.get(i));
			if(i < size - 1){
				sb = sb.append(",");
			}
			String rowStr = sb.toString();
			csvWriter.write(rowStr);
		}
		csvWriter.newLine();
	}
}
