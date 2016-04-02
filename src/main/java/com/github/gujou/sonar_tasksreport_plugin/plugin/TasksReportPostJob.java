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
package com.github.gujou.sonar_tasksreport_plugin.plugin;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.CheckProject;
import org.sonar.api.batch.PostJob;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;

import com.github.gujou.sonar_tasksreport_plugin.service.FileGenerator;
import com.github.gujou.sonar_tasksreport_plugin.service.impl.XlsTasksGenerator;
import com.github.gujou.sonar_tasksreport_plugin.tools.HttpFileUploader;

public class TasksReportPostJob implements PostJob, CheckProject {

	private static final Logger logger = LoggerFactory.getLogger(TasksReportPostJob.class);

	private final FileSystem sonarFileSystem;
	private final FileGenerator tasksFileGenerator;
	private final boolean tasksreportSkip;
	private final String sonarUrl;
	private final String sonarLogin;
	private final String sonarPassword;

	public TasksReportPostJob(Settings sonarSettings, FileSystem sonarFs) {
		sonarFileSystem = sonarFs;

		tasksreportSkip = sonarSettings.getBoolean(TasksReportKeys.TASKS_REPORT_SKIP_KEY);
		sonarUrl = sonarSettings.hasKey(TasksReportKeys.TASKS_REPORT_SONAR_URL_KEY)
				? sonarSettings.getString(TasksReportKeys.TASKS_REPORT_SONAR_URL_KEY)
				: TasksReportKeys.TASKS_REPORT_SONAR_URL_DEFAULT;
		sonarLogin = sonarSettings.getString(TasksReportKeys.TASKS_REPORT_SONAR_LOGIN_KEY);
		sonarPassword = sonarSettings.getString(TasksReportKeys.TASKS_REPORT_SONAR_PWD_KEY);

		switch (sonarSettings.getString(TasksReportKeys.TASKS_REPORT_TYPE_KEY)) {
		case TasksReportKeys.TASKS_REPORT_TYPE_XLS_EXTENSION:
			tasksFileGenerator = new XlsTasksGenerator();
			break;
		default:
			tasksFileGenerator = new XlsTasksGenerator();
			break;
		}
	}

	@Override
	public boolean shouldExecuteOnProject(Project project) {
		return !tasksreportSkip;
	}

	@Override
	public void executeOn(Project sonarProject, SensorContext ctx) {
		logger.info("Launching tasks report...");

		File generateFile = tasksFileGenerator.generateFile(sonarProject, sonarFileSystem, sonarUrl, sonarLogin,
				sonarPassword);

		if (generateFile != null) {
			HttpFileUploader.uploadFile(generateFile, sonarLogin, sonarPassword, "http://localhost:9000");
		} else {
			
			// TODO change sysout => log4j.
			System.out.println("Error : file not generated.");
		}

	}

}
