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
package com.github.gujou.deerbelling.sonarqube.gateway;

import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.github.gujou.deerbelling.sonarqube.model.metric.Resources;

public class ResourceGateway {

	public static Resources getOpenIssues(String projecName, String sonarUrl, String sonarLogin, String sonarPassword) {

		JacksonXmlModule module = new JacksonXmlModule();
		module.setDefaultUseWrapper(false);
		XmlMapper xmlMapper = new XmlMapper(module);
		
		// FIXME problem when "//"
		String url = sonarUrl + "/api/resources?resource=" + projecName
				+ "&metrics=ncloc,lines,classes,files,directories,projects,functions,accessors,public_api,statements,generated_ncloc,generated_lines,package_cycles,package_tangles,package_tangle_index,package_feedback_edges,package_edges_weight,complexity,class_complexity,file_complexity,function_complexity,duplicated_lines,duplicated_lines_density,duplicated_files,duplicated_blocks,violations,blocker_violations,critical_violations,major_violations,minor_violations,sqale_index,comment_lines,comment_lines_density,public_undocumented_api,public_documented_api_density,high_severity_vulns,medium_severity_vulns,low_severity_vulns,tests,test_success_density,test_failures,test_errors,skipped_tests,line_coverage,lines_to_cover,uncovered_lines";

		System.out.println(url);

		Client client = ClientBuilder.newClient();

		WebTarget target = client.target(url);
		
		if (StringUtils.isNotBlank(sonarLogin) && StringUtils.isNotBlank(sonarPassword)) {
			target = target.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, sonarLogin)
					.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, sonarPassword);
		}

		Response response = target.request(MediaType.APPLICATION_XML_TYPE).get();

		System.out.println(response);

		String responseStr = response.readEntity(String.class);
		
		System.out.println(responseStr);

		try {
			return xmlMapper.readValue(responseStr, Resources.class);
		} catch (JsonParseException e) {

			// TODO manage error.
			e.printStackTrace();
		} catch (JsonMappingException e) {

			// TODO manage error.
			e.printStackTrace();
		} catch (IOException e) {

			// TODO manage error.
			e.printStackTrace();
		}

		return null;
	}
}
