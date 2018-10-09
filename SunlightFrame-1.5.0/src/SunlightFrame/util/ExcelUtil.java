/**
 * SunlightFrame
 *    
 * (C) Copyright 光链科技
 * 本内容仅限于光链科技授权使用，未经授权不得用于商业用途。谢谢合作！
 */
package SunlightFrame.util;

import java.io.*;
import java.util.*;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * Excel工具
 */
public class ExcelUtil {
	/**
	 * excel文件读取
	 * 
	 * @param filePath
	 *            excel文件路径
	 * @return excel文件的内容
	 * @throws Exception
	 */
	public static Map<String, List<List<String>>> readExcel(String filePath) throws Exception {
		Map<String, List<List<String>>> datas = new HashMap<String, List<List<String>>>();
		InputStream is = null;

		try {
			is = new FileInputStream(filePath);
			Workbook rwb = Workbook.getWorkbook(is);
			// Sheet st = rwb.getSheet(0);
			// Sheet st = rwb.getSheet("Book1");
			Sheet st[] = rwb.getSheets();

			for (int a = 0; a < st.length; a++) {
				String sheetName = st[a].getName().trim();
				List<List<String>> sheetDatas = new ArrayList<List<String>>();

				for (int i = 0; i < st[a].getRows(); i++) {
					Vector<String> rowDatas = new Vector<String>();
					for (int j = 0; j < st[a].getColumns(); j++) {
						Cell c = st[a].getCell(j, i);
						String content = c.getContents().trim();
						rowDatas.add(content);
					}
					sheetDatas.add(rowDatas);
				}

				datas.put(sheetName, sheetDatas);
			}
			rwb.close();
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (Exception e) {
			}
		}
		return datas;
	}

	/**
	 * excel文件输出
	 * 
	 * @param filePath
	 *            输出文件路径
	 * @param sheetName
	 *            sheet名
	 * @param titles
	 *            标题
	 * @param fieldNames
	 *            数据字段名
	 * @param datas
	 *            数据
	 * @throws Exception
	 */
	public static void writeExcel(String filePath, String sheetName, String[] titles, String[] fieldNames,
			List<Map<String, String>> datas) throws Exception {
		OutputStream os = null;
		try {
			os = new FileOutputStream(filePath);
			WritableWorkbook wwb = Workbook.createWorkbook(os);
			WritableSheet ws = wwb.createSheet(sheetName, 0);

			for (int i = 0; i < titles.length; i++) {
				Label labelCF = new Label(i, 0, titles[i]);
				ws.addCell(labelCF);
			}

			for (int i = 0; i < datas.size(); i++) {
				Map<String, String> rowData = datas.get(i);
				for (int j = 0; j < fieldNames.length; j++) {
					Label labelCF = new Label(j, i + 1, rowData.get(fieldNames[j]));
					ws.addCell(labelCF);
				}
			}

			wwb.write();
			wwb.close();
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (os != null) {
					os.close();
				}
			} catch (Exception e) {
			}
		}

	}

}