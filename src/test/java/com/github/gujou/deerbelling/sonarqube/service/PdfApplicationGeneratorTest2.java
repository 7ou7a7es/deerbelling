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
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.resources.Project;

import com.github.gujou.deerbelling.sonarqube.model.metric.Measure;

public class PdfApplicationGeneratorTest2 {
	
	@Test
	public void testGenerateFile(){
		
		File baseDir = new File("/tmp/tmp");
		
		Project sonarProject = new Project("pouette.pouette");
		
		sonarProject.setName("e-materiel");
		
		sonarProject.setKey("com.primagaz:primodm");
		
		File srcFolder = new File("/home/gujou/Workspaces/java/Primagaz_workspace/emateriel/src");
		
		DefaultFileSystem sonarFileSystem = new DefaultFileSystem(baseDir);
		
		sonarFileSystem.setWorkDir(baseDir);
		
		Map<String, Measure> sonarMeasures = new HashMap<>();
		
		sonarMeasures.put("high_vuln_owasp", new Measure("high_vuln_owasp", "4"));
		sonarMeasures.put("medium_vuln_owasp", new Measure("medium_vuln_owasp", "7"));
		sonarMeasures.put("low_vuln_owasp", new Measure("low_vuln_owasp", "0"));
		
		sonarMeasures.putAll(XhtmlAnalyser.analyse(srcFolder));
		sonarMeasures.putAll(MulesoftAnalyser.analyse(srcFolder));
				
		PdfApplicationGenerator2.generateFile(sonarProject, sonarFileSystem, "http://localhost:9000", "", "", sonarMeasures);
	}

	
}
