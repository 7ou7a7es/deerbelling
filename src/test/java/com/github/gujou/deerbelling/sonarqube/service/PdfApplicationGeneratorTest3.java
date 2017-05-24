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

public class PdfApplicationGeneratorTest3 {
	
	@Test
	public void testGenerateFile(){
		
		
		
		File baseDir = new File("/tmp/tmp");
		
		Project sonarProject = new Project("pouette.pouette");
		
		sonarProject.setName("e-materiel : alfresco");
		
		sonarProject.setKey("pgz-edi:alfresco");
		
		File srcFolder = new File("/home/gujou/Workspaces/java/Primagaz_workspace/alfresco/src");
		
		DefaultFileSystem sonarFileSystem = new DefaultFileSystem(baseDir);
		
		sonarFileSystem.setWorkDir(baseDir);
		
		Map<String, Measure> sonarMeasures = new HashMap<>();
		
		sonarMeasures.put("high_vuln_owasp", new Measure("high_vuln_owasp", "2"));
		sonarMeasures.put("medium_vuln_owasp", new Measure("medium_vuln_owasp", "7"));
		sonarMeasures.put("low_vuln_owasp", new Measure("low_vuln_owasp", "0"));
		
		sonarMeasures.putAll(XhtmlAnalyser.analyse(srcFolder));
		sonarMeasures.putAll(MulesoftAnalyser.analyse(srcFolder));
				
		PdfApplicationGenerator.generateFile(sonarProject, sonarFileSystem, "http://localhost:9000", "", "", sonarMeasures);
		
		/****************************************************************************************************************************/
		/* **************************************************************************************************************************/
		
		sonarProject = new Project("pouette.pouette");
		
		sonarProject.setName("e-materiel : pgz-edi");
		
		sonarProject.setKey("pgz-edi:pgz-edi");
		
		srcFolder = new File("/home/gujou/Workspaces/java/Primagaz_workspace/pgz-edi/src");
		
		sonarFileSystem = new DefaultFileSystem(baseDir);
		
		sonarFileSystem.setWorkDir(baseDir);
		
		sonarMeasures = new HashMap<>();
		
		sonarMeasures.put("high_vuln_owasp", new Measure("high_vuln_owasp", "2"));
		sonarMeasures.put("medium_vuln_owasp", new Measure("medium_vuln_owasp", "13"));
		sonarMeasures.put("low_vuln_owasp", new Measure("low_vuln_owasp", "0"));
		
		sonarMeasures.putAll(XhtmlAnalyser.analyse(srcFolder));
		sonarMeasures.putAll(MulesoftAnalyser.analyse(srcFolder));
				
		PdfApplicationGenerator.generateFile(sonarProject, sonarFileSystem, "http://localhost:9000", "", "", sonarMeasures);
		
		/****************************************************************************************************************************/
		/* **************************************************************************************************************************/
		
		sonarProject = new Project("pouette.pouette");
		
		sonarProject.setName("e-materiel");
		
		sonarProject.setKey("com.primagaz:primodm");
		
		srcFolder = new File("/home/gujou/Workspaces/java/Primagaz_workspace/emateriel/src");
		
		sonarFileSystem = new DefaultFileSystem(baseDir);
		
		sonarFileSystem.setWorkDir(baseDir);
		
		sonarMeasures = new HashMap<>();
		
		sonarMeasures.put("high_vuln_owasp", new Measure("high_vuln_owasp", "4"));
		sonarMeasures.put("medium_vuln_owasp", new Measure("medium_vuln_owasp", "26"));
		sonarMeasures.put("low_vuln_owasp", new Measure("low_vuln_owasp", "2"));
		
		sonarMeasures.putAll(XhtmlAnalyser.analyse(srcFolder));
		sonarMeasures.putAll(MulesoftAnalyser.analyse(srcFolder));
				
		PdfApplicationGenerator.generateFile(sonarProject, sonarFileSystem, "http://localhost:9000", "", "", sonarMeasures);
		
		/****************************************************************************************************************************/
		/* **************************************************************************************************************************/
		
		sonarProject = new Project("pouette.pouette");
		
		sonarProject.setName("e-securite");
		
		sonarProject.setKey("com.primagaz:primeve");
		
		srcFolder = new File("/home/gujou/Workspaces/java/Primagaz_workspace/e_securite/primeve/src");
		
		sonarFileSystem = new DefaultFileSystem(baseDir);
		
		sonarFileSystem.setWorkDir(baseDir);
		
		sonarMeasures = new HashMap<>();
		
		sonarMeasures.put("high_vuln_owasp", new Measure("high_vuln_owasp", "11"));
		sonarMeasures.put("medium_vuln_owasp", new Measure("medium_vuln_owasp", "109"));
		sonarMeasures.put("low_vuln_owasp", new Measure("low_vuln_owasp", "20"));
		
		sonarMeasures.putAll(XhtmlAnalyser.analyse(srcFolder));
		sonarMeasures.putAll(MulesoftAnalyser.analyse(srcFolder));
				
		PdfApplicationGenerator.generateFile(sonarProject, sonarFileSystem, "http://localhost:9000", "", "", sonarMeasures);
		
		/****************************************************************************************************************************/
		/* **************************************************************************************************************************/
		
		sonarProject = new Project("pouette.pouette");
		
		sonarProject.setName("telemetrie");
		
		sonarProject.setKey("pgz-gaslog:pgz-gaslog");
		
		srcFolder = new File("/home/gujou/Workspaces/java/Primagaz_workspace/télémétrie/pgz-gaslog/src");
		
		sonarFileSystem = new DefaultFileSystem(baseDir);
		
		sonarFileSystem.setWorkDir(baseDir);
		
		sonarMeasures = new HashMap<>();
		
		sonarMeasures.put("high_vuln_owasp", new Measure("high_vuln_owasp", "13"));
		sonarMeasures.put("medium_vuln_owasp", new Measure("medium_vuln_owasp", "123"));
		sonarMeasures.put("low_vuln_owasp", new Measure("low_vuln_owasp", "12"));
		
		sonarMeasures.putAll(XhtmlAnalyser.analyse(srcFolder));
		sonarMeasures.putAll(MulesoftAnalyser.analyse(srcFolder));
				
		PdfApplicationGenerator.generateFile(sonarProject, sonarFileSystem, "http://localhost:9000", "", "", sonarMeasures);
		
		/****************************************************************************************************************************/
		/* **************************************************************************************************************************/
		
		sonarProject = new Project("pouette.pouette");
		
		sonarProject.setName("souscription en ligne");
		
		sonarProject.setKey("com.primagaz:esign");
		
		srcFolder = new File("/home/gujou/Workspaces/java/Primagaz_workspace/Souscription en ligne/esign/esign/src");
		
		sonarFileSystem = new DefaultFileSystem(baseDir);
		
		sonarFileSystem.setWorkDir(baseDir);
		
		sonarMeasures = new HashMap<>();
		
		sonarMeasures.put("high_vuln_owasp", new Measure("high_vuln_owasp", "4"));
		sonarMeasures.put("medium_vuln_owasp", new Measure("medium_vuln_owasp", "10"));
		sonarMeasures.put("low_vuln_owasp", new Measure("low_vuln_owasp", "1"));
		
		sonarMeasures.putAll(XhtmlAnalyser.analyse(srcFolder));
		sonarMeasures.putAll(MulesoftAnalyser.analyse(srcFolder));
				
		PdfApplicationGenerator.generateFile(sonarProject, sonarFileSystem, "http://localhost:9000", "", "", sonarMeasures);
		
		/****************************************************************************************************************************/
		/* **************************************************************************************************************************/
		
		sonarProject = new Project("pouette.pouette");
		
		sonarProject.setName("GED Client");
		
		sonarProject.setKey("pgz-ged-client:pgz-ged-client");
		
		srcFolder = new File("/home/gujou/Workspaces/java/Primagaz_workspace/GED Client/pgz-ged-client/src");
		
		sonarFileSystem = new DefaultFileSystem(baseDir);
		
		sonarFileSystem.setWorkDir(baseDir);
		
		sonarMeasures = new HashMap<>();
		
		sonarMeasures.put("high_vuln_owasp", new Measure("high_vuln_owasp", "2"));
		sonarMeasures.put("medium_vuln_owasp", new Measure("medium_vuln_owasp", "17"));
		sonarMeasures.put("low_vuln_owasp", new Measure("low_vuln_owasp", "0"));
		
		sonarMeasures.putAll(XhtmlAnalyser.analyse(srcFolder));
		sonarMeasures.putAll(MulesoftAnalyser.analyse(srcFolder));
				
		PdfApplicationGenerator.generateFile(sonarProject, sonarFileSystem, "http://localhost:9000", "", "", sonarMeasures);
		
		/****************************************************************************************************************************/
		/* **************************************************************************************************************************/
		
		sonarProject = new Project("pouette.pouette");
		
		sonarProject.setName("GED Client : Alfresco");
		
		sonarProject.setKey("pgz-ged-client:alfresco");
		
		srcFolder = new File("/home/gujou/Workspaces/java/Primagaz_workspace/GED Client/alfresco/src");
		
		sonarFileSystem = new DefaultFileSystem(baseDir);
		
		sonarFileSystem.setWorkDir(baseDir);
		
		sonarMeasures = new HashMap<>();
		
		sonarMeasures.put("high_vuln_owasp", new Measure("high_vuln_owasp", "2"));
		sonarMeasures.put("medium_vuln_owasp", new Measure("medium_vuln_owasp", "7"));
		sonarMeasures.put("low_vuln_owasp", new Measure("low_vuln_owasp", "0"));
		
		sonarMeasures.putAll(XhtmlAnalyser.analyse(srcFolder));
		sonarMeasures.putAll(MulesoftAnalyser.analyse(srcFolder));
				
		PdfApplicationGenerator.generateFile(sonarProject, sonarFileSystem, "http://localhost:9000", "", "", sonarMeasures);
		
		/****************************************************************************************************************************/
		/* **************************************************************************************************************************/
		
		sonarProject = new Project("pouette.pouette");
		
		sonarProject.setName("Editique");
		
		sonarProject.setKey("com.mycompany:pz_primodele");
		
		srcFolder = new File("/home/gujou/Workspaces/java/Primagaz_workspace/Editique/pz_primodele/src");
		
		sonarFileSystem = new DefaultFileSystem(baseDir);
		
		sonarFileSystem.setWorkDir(baseDir);
		
		sonarMeasures = new HashMap<>();
		
		sonarMeasures.put("high_vuln_owasp", new Measure("high_vuln_owasp", "13"));
		sonarMeasures.put("medium_vuln_owasp", new Measure("medium_vuln_owasp", "123"));
		sonarMeasures.put("low_vuln_owasp", new Measure("low_vuln_owasp", "12"));
		
		sonarMeasures.putAll(XhtmlAnalyser.analyse(srcFolder));
		sonarMeasures.putAll(MulesoftAnalyser.analyse(srcFolder));
				
		PdfApplicationGenerator.generateFile(sonarProject, sonarFileSystem, "http://localhost:9000", "", "", sonarMeasures);
		
		/****************************************************************************************************************************/
		/* **************************************************************************************************************************/
		
		sonarProject = new Project("pouette.pouette");
		
		sonarProject.setName("Primagaz GED");
		
		sonarProject.setKey("com.primagaz.ged:primaged");
		
		srcFolder = new File("/home/gujou/Workspaces/java/Primagaz_workspace/GED Client/primaged/src");
		
		sonarFileSystem = new DefaultFileSystem(baseDir);
		
		sonarFileSystem.setWorkDir(baseDir);
		
		sonarMeasures = new HashMap<>();
		
		sonarMeasures.put("high_vuln_owasp", new Measure("high_vuln_owasp", "4"));
		sonarMeasures.put("medium_vuln_owasp", new Measure("medium_vuln_owasp", "17"));
		sonarMeasures.put("low_vuln_owasp", new Measure("low_vuln_owasp", "1"));
		
		sonarMeasures.putAll(XhtmlAnalyser.analyse(srcFolder));
		sonarMeasures.putAll(MulesoftAnalyser.analyse(srcFolder));
				
		PdfApplicationGenerator.generateFile(sonarProject, sonarFileSystem, "http://localhost:9000", "", "", sonarMeasures);
		
	}

	
}
