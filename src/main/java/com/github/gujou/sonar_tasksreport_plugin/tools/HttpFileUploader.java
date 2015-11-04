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
package com.github.gujou.sonar_tasksreport_plugin.tools;

import java.io.File;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;

public class HttpFileUploader {

	public static void uploadFile(File file, String username, String password, String sonarBaseUrl) {
		PostMethod filePost = new PostMethod(sonarBaseUrl + "/tasks_report/postReport");

		try {

			Part[] parts = { new FilePart("upload", file) };
			filePost.setRequestEntity(new MultipartRequestEntity(parts, filePost.getParams()));
			HttpClient client = new HttpClient();

			if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
				client.getParams().setAuthenticationPreemptive(true);
				Credentials credentials = new UsernamePasswordCredentials(username, password);
				client.getState().setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT), credentials);
			}
			
			client.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
			int status = client.executeMethod(filePost);
			if (status == HttpStatus.SC_OK) {
				
				// TODO change sysout => log4j.
				System.out.println("Excel uploaded.");
			} else {
				
				// TODO change sysout => log4j.
				System.out.println("Something went wrong storing the Excel at server side. Status: " + status);
			}
		} catch (Exception ex) {

			// TODO change sysout => log4j.
			System.out.println("Something went wrong storing the Excel at server side");
			
			// TODO manage error.
			ex.printStackTrace();
		} finally {
			filePost.releaseConnection();
		}

	}

}
