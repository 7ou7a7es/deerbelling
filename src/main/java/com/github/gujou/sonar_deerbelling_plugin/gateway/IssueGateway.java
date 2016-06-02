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
package com.github.gujou.sonar_deerbelling_plugin.gateway;

import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.github.gujou.sonar_deerbelling_plugin.model.issue.Issues;

public class IssueGateway {

	public static Issues getOpenIssues(String projecName, String sonarUrl, String sonarLogin, String sonarPassword) {

		JacksonJsonProvider jacksonJsonProvider = new JacksonJaxbJsonProvider()
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

		ObjectMapper objMapper = jacksonJsonProvider.locateMapper(Object.class, MediaType.APPLICATION_JSON_TYPE);

		Client client = ClientBuilder.newClient();

		WebTarget target = client
				.target(sonarUrl + "/api/issues/search?pageSize=-1&statuses=OPEN,REOPENED,CONFIRMED&componentKeys="
						+ projecName + "&onComponentOnly=false&assignees");

		if (StringUtils.isNotBlank(sonarLogin) && StringUtils.isNotBlank(sonarPassword)) {
			target = target.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, sonarLogin)
					.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, sonarPassword);
		}

		Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();
		String responseStr = response.readEntity(String.class);

		try {
			return objMapper.readValue(responseStr, Issues.class);
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
