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
package com.github.gujou.deerbelling.sonarqube.plugin;

import java.io.File;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.CheckProject;
import org.sonar.api.batch.PostJob;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;

import com.github.gujou.deerbelling.sonarqube.model.metric.Measure;
import com.github.gujou.deerbelling.sonarqube.service.CsvTasksGenerator;
import com.github.gujou.deerbelling.sonarqube.service.PdfApplicationGenerator2;
import com.github.gujou.deerbelling.sonarqube.service.XlsTasksGenerator;
import com.github.gujou.deerbelling.sonarqube.tools.HttpFileUploader;

public class TasksReportPostJob implements PostJob, CheckProject {

	private static final Logger logger = LoggerFactory.getLogger(TasksReportPostJob.class);

	private final FileSystem sonarFileSystem;
	private final boolean tasksreportSkip;
	private final String sonarUrl;
	private final String sonarLogin;
	private final String sonarPassword;
	private final String csvSepartor;

	public TasksReportPostJob(Settings sonarSettings, FileSystem sonarFs) {
		sonarFileSystem = sonarFs;
		tasksreportSkip = sonarSettings.getBoolean(ReportsKeys.TASKS_REPORT_SKIP_KEY);
		sonarUrl = sonarSettings.hasKey(ReportsKeys.TASKS_REPORT_SONAR_URL_KEY)
				? sonarSettings.getString(ReportsKeys.TASKS_REPORT_SONAR_URL_KEY)
				: ReportsKeys.TASKS_REPORT_SONAR_URL_DEFAULT;
		sonarLogin = sonarSettings.getString(ReportsKeys.TASKS_REPORT_SONAR_LOGIN_KEY);
		sonarPassword = sonarSettings.getString(ReportsKeys.TASKS_REPORT_SONAR_PWD_KEY);
		csvSepartor = sonarSettings.getString(ReportsKeys.TASKS_REPORT_TYPE_CSV_SEPARATOR_KEY);

	}

	@Override
	public boolean shouldExecuteOnProject(Project project) {
		return !tasksreportSkip;
	}

	@Override
	public void executeOn(Project sonarProject, SensorContext ctx) {
		logger.info("Launching tasks report...");

		File generateFile = XlsTasksGenerator.generateFile(sonarProject, sonarFileSystem, sonarUrl, sonarLogin,
				sonarPassword);

		if (generateFile != null) {

			// TODO replace localhost.
			HttpFileUploader.uploadFile(generateFile, sonarLogin, sonarPassword, "http://localhost:9000/tasks_report");
		} else {

			// TODO change sysout => log4j.
			System.out.println("Error : xls file not generated.");
		}

		generateFile = CsvTasksGenerator.generateFile(sonarProject, sonarFileSystem, sonarUrl, sonarLogin,
				sonarPassword, csvSepartor);

		if (generateFile != null) {

			// TODO replace localhost.
			HttpFileUploader.uploadFile(generateFile, sonarLogin, sonarPassword, "http://localhost:9000/tasks_report");
		} else {

			// TODO change sysout => log4j.
			System.out.println("Error : csv file not generated.");
		}

//		generateFile = PdfApplicationGenerator.generateFile(sonarProject, sonarFileSystem, sonarUrl, sonarLogin,
//				sonarPassword);
		
		generateFile = PdfApplicationGenerator2.generateFile(sonarProject, sonarFileSystem, sonarUrl, sonarLogin,
				sonarPassword, new HashMap<String, Measure>());

		if (generateFile != null) {

			// TODO replace localhost.
			HttpFileUploader.uploadFile(generateFile, sonarLogin, sonarPassword, "http://localhost:9000/application_report");
		} else {

			// TODO change sysout => log4j.
			System.out.println("Error : pdf file not generated.");
		}

	}

}
