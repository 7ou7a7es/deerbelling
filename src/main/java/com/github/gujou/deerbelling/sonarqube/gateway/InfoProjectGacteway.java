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
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.github.gujou.deerbelling.sonarqube.model.issue.Issues;

public class InfoProjectGacteway {

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
