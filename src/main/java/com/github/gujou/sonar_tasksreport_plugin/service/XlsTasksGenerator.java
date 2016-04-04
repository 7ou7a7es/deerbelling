/*
 * sonar_tasksreport_plugin
 * Copyright (C) 2015 guillaume jourdan
 * guillaume.jourdan.pro@gmail.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package com.github.gujou.sonar_tasksreport_plugin.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDataValidationHelper;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.resources.Project;

import com.github.gujou.sonar_tasksreport_plugin.gateway.IssueGateway;
import com.github.gujou.sonar_tasksreport_plugin.model.issue.Issue;
import com.github.gujou.sonar_tasksreport_plugin.model.issue.Issues;
import com.github.gujou.sonar_tasksreport_plugin.plugin.TasksReportKeys;

public class XlsTasksGenerator {

	private static final int STATUS_COLUMN_INDEX = 0;
	private static final int SEVERITY_COLUMN_INDEX = 1;
	private static final int COMPONENT_COLUMN_INDEX = 2;
	private static final int LINE_COLUMN_INDEX = 3;
	private static final int MESSAGE_COLUMN_INDEX = 4;
	private static final int AUTHOR_COLUMN_INDEX = 5;
	private static final int ASSIGNED_COLUMN_INDEX = 6;
	private static final int CREATION_DATE_COLUMN_INDEX = 7;
	private static final int UPDATE_DATE_COLUMN_INDEX = 8;
	private static final int COMPONENT_PATH_COLUMN_INDEX = 9;

	public static File generateFile(Project sonarProject, FileSystem sonarFileSystem, String sonarUrl, String sonarLogin,
			String sonarPassword) {

		short formatIndex;
		HSSFDataFormat dataFormat = null;
		FileOutputStream out = null;
		HSSFWorkbook workbook = null;

		String filePath = sonarFileSystem.workDir().getAbsolutePath() + File.separator + "tasks_report_"
				+ sonarProject.getEffectiveKey().replace(':', '-') + "."
				+ TasksReportKeys.TASKS_REPORT_TYPE_XLS_EXTENSION;

		File resultFile = new File(filePath);

		try {
			out = new FileOutputStream(resultFile);

			workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("Tasks list");

			// Date format.
			dataFormat = workbook.createDataFormat();
			formatIndex = dataFormat.getFormat("yyyy-MM-ddTHH:mm:ss");
			HSSFCellStyle dateStyle = workbook.createCellStyle();
			dateStyle.setDataFormat(formatIndex);

			Issues rootIssue = IssueGateway.getOpenIssues(sonarProject.getEffectiveKey(), sonarUrl, sonarLogin,
					sonarPassword);

			if (rootIssue == null) {
				return null;
			}

			DataValidationHelper validationHelper = new HSSFDataValidationHelper(sheet);
			DataValidationConstraint constraint = validationHelper.createExplicitListConstraint(
					new String[] { "OPENED", "CONFIRMED", "REOPENED", "RESOLVED", "CLOSE" });
			CellRangeAddressList addressList = new CellRangeAddressList(1, rootIssue.getIssues().size() + 1,
					STATUS_COLUMN_INDEX, STATUS_COLUMN_INDEX);
			DataValidation dataValidation = validationHelper.createValidation(constraint, addressList);
			dataValidation.setSuppressDropDownArrow(false);
			sheet.addValidationData(dataValidation);

			int rownum = 0;

			Row row = sheet.createRow(rownum++);
			row.createCell(STATUS_COLUMN_INDEX).setCellValue("Status");
			row.createCell(SEVERITY_COLUMN_INDEX).setCellValue("Severity");
			row.createCell(COMPONENT_COLUMN_INDEX).setCellValue("Component");
			row.createCell(LINE_COLUMN_INDEX).setCellValue("Line");
			row.createCell(MESSAGE_COLUMN_INDEX).setCellValue("Message");
			row.createCell(AUTHOR_COLUMN_INDEX).setCellValue("Author");
			row.createCell(ASSIGNED_COLUMN_INDEX).setCellValue("Assigned");
			row.createCell(CREATION_DATE_COLUMN_INDEX).setCellValue("CreationDate");
			row.createCell(UPDATE_DATE_COLUMN_INDEX).setCellValue("UpdateDate");
			row.createCell(COMPONENT_PATH_COLUMN_INDEX).setCellValue("Path");

			for (Issue issue : rootIssue.getIssues()) {
				row = sheet.createRow(rownum++);
				int componentIndex = issue.getComponent().lastIndexOf('/');
				String component;
				String path;
				if (componentIndex > 0) {
					component = issue.getComponent().substring(componentIndex + 1);
					path = issue.getComponent().substring(0, componentIndex);
				} else {
					component = issue.getComponent();
					path = "";
				}

				// Set values.
				row.createCell(STATUS_COLUMN_INDEX).setCellValue(issue.getStatus());
				row.createCell(SEVERITY_COLUMN_INDEX).setCellValue(issue.getSeverity());
				row.createCell(COMPONENT_COLUMN_INDEX).setCellValue(component);
				row.createCell(LINE_COLUMN_INDEX).setCellValue(issue.getLine());
				row.createCell(MESSAGE_COLUMN_INDEX).setCellValue(issue.getMessage());
				row.createCell(AUTHOR_COLUMN_INDEX).setCellValue(issue.getAuthor());
				row.createCell(ASSIGNED_COLUMN_INDEX).setCellValue(issue.getAssignee());
				row.createCell(CREATION_DATE_COLUMN_INDEX).setCellValue(issue.getCreationDate());
				row.createCell(UPDATE_DATE_COLUMN_INDEX).setCellValue(issue.getUpdateDate());
				row.createCell(COMPONENT_PATH_COLUMN_INDEX).setCellValue(path);

				// Set date style to date column.
				row.getCell(CREATION_DATE_COLUMN_INDEX).setCellStyle(dateStyle);
				row.getCell(UPDATE_DATE_COLUMN_INDEX).setCellStyle(dateStyle);
			}

			// Auto-size sheet columns.
			sheet.autoSizeColumn(STATUS_COLUMN_INDEX);
			sheet.autoSizeColumn(STATUS_COLUMN_INDEX);
			sheet.autoSizeColumn(COMPONENT_COLUMN_INDEX);
			sheet.autoSizeColumn(LINE_COLUMN_INDEX);
			sheet.autoSizeColumn(MESSAGE_COLUMN_INDEX);
			sheet.autoSizeColumn(AUTHOR_COLUMN_INDEX);
			sheet.autoSizeColumn(ASSIGNED_COLUMN_INDEX);
			sheet.autoSizeColumn(CREATION_DATE_COLUMN_INDEX);
			sheet.autoSizeColumn(UPDATE_DATE_COLUMN_INDEX);
			sheet.autoSizeColumn(COMPONENT_PATH_COLUMN_INDEX);

			workbook.write(out);

		} catch (FileNotFoundException e) {
			
			// TODO manage error.
			e.printStackTrace();
		} catch (IOException e) {
			
			// TODO manage error.
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(workbook);
			IOUtils.closeQuietly(out);
		}

		return resultFile;
	}

}
