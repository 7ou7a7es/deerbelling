/*
 * sonar_deerbelling_plugin
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
package com.github.gujou.deerbelling.sonarqube.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.resources.Project;

import com.github.gujou.deerbelling.sonarqube.gateway.IssueGateway;
import com.github.gujou.deerbelling.sonarqube.model.issue.Issue;
import com.github.gujou.deerbelling.sonarqube.model.issue.Issues;
import com.github.gujou.deerbelling.sonarqube.plugin.ReportsKeys;

public class CsvTasksGenerator {

	private static final String lineSeparator = System.lineSeparator();

	private static String csvFileSeparator = ReportsKeys.TASKS_REPORT_TYPE_CSV_SEPARATOR_DEFAULT;

	private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

	public static File generateFile(Project sonarProject, FileSystem sonarFileSystem, String sonarUrl,
			String sonarLogin, String sonarPassword, String fileSeparator) {

		String filePath = sonarFileSystem.workDir().getAbsolutePath() + File.separator + "tasks_report_"
				+ sonarProject.getEffectiveKey().replace(':', '-') + "."
				+ ReportsKeys.TASKS_REPORT_TYPE_CSV_EXTENSION;

		if (StringUtils.isNotBlank(fileSeparator)) {
			csvFileSeparator = fileSeparator;
		}

		File resultFile = new File(filePath);

		Issues rootIssue = IssueGateway.getOpenIssues(sonarProject.getEffectiveKey(), sonarUrl, sonarLogin,
				sonarPassword);

		FileWriter writer = null;

		try {

			writer = new FileWriter(resultFile);

			append(writer, "Status", false);
			append(writer, "Severity", false);
			append(writer, "Component", false);
			append(writer, "Line", false);
			append(writer, "Message", false);
			append(writer, "Author", false);
			append(writer, "Assigned", false);
			append(writer, "CreationDate", false);
			append(writer, "UpdateDate", false);
			append(writer, "Path", true);

			for (Issue issue : rootIssue.getIssues()) {

				int componentIndex = 0;
				if (issue.getComponent() != null){
					componentIndex = issue.getComponent().lastIndexOf('/');
				}
				String component;
				String path;
				if (componentIndex > 0) {
					component = issue.getComponent().substring(componentIndex + 1);
					path = issue.getComponent().substring(0, componentIndex);
				} else {
					component = issue.getComponent();
					path = "";
				}

				append(writer, issue.getStatus(), false);
				append(writer, issue.getSeverity(), false);
				append(writer, component, false);
				append(writer, Integer.toString(issue.getLine()), false);
				append(writer, issue.getMessage(), false);
				append(writer, issue.getAuthor(), false);
				append(writer, issue.getAssignee(), false);
				append(writer, dateFormat.format(issue.getCreationDate()), false);
				append(writer, dateFormat.format(issue.getUpdateDate()), false);
				append(writer, path, true);
			}

			writer.flush();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return resultFile;
	}

	private static void append(FileWriter writer, String value, boolean lastValue) throws IOException {
		writer.append(StringUtils.isBlank(value) ? "" : value).append(lastValue ? lineSeparator : csvFileSeparator);
	}

}
