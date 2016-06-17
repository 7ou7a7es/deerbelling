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

import org.junit.Test;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.resources.Project;

public class PdfApplicationGeneratorTest {
	
	@Test
	public void testGenerateFile(){
		
		File baseDir = new File("/tmp");
		
		Project sonarProject = new Project("pouette.pouette");
		
		sonarProject.setName("Z");
		
		DefaultFileSystem sonarFileSystem = new DefaultFileSystem(baseDir);
		
		sonarFileSystem.setWorkDir(baseDir);
		
		PdfApplicationGenerator.generateFile(sonarProject, sonarFileSystem, "", "", "");
	}

}
