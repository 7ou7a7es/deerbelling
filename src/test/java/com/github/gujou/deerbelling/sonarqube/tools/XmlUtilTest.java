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
package com.github.gujou.deerbelling.sonarqube.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.github.gujou.deerbelling.sonarqube.tools.XmlUtil;

public class XmlUtilTest {

	@Test
	public void testCountTag() {

		int xmlNbTotal = 0, xmlSimpleNbTotal = 0, xmlMediumNbTotal = 0, xmlComplexNbTotal = 0;
		int xhtmlNbTotal = 0, xhtmlSimpleNbTotal = 0, xhtmlMediumNbTotal = 0, xhtmlComplexNbTotal = 0;

		int nbOutputField = 0, nbRequestField = 0;

		List<File> xmlFileList = listFileByExtension(
				new File("/home/gujou/Workspaces/java/Primagaz_workspace/emateriel/src"), ".xml");
		List<File> xhtmlFileList = listFileByExtension(
				new File("/home/gujou/Workspaces/java/Primagaz_workspace/emateriel/src"), ".xhtml");

		for (File file : xmlFileList) {
			xmlNbTotal++;
			int nbTag = XmlUtil.countTag(file);
			if (nbTag <= 20) {
				xmlSimpleNbTotal++;
			} else if (nbTag <= 50) {
				xmlMediumNbTotal++;
			} else {
				xmlComplexNbTotal++;
			}
			
			nbOutputField += XmlUtil.countTag(file, "result");
			nbRequestField +=  XmlUtil.countTag(file, "select") +  XmlUtil.countTag(file, "update") +  XmlUtil.countTag(file, "delete") + XmlUtil.countTag(file, "insert");
		}
		
		for (File file : xhtmlFileList) {
			xhtmlNbTotal++;
			int nbTag = XmlUtil.countTag(file);
			if (nbTag <= 20) {
				xhtmlSimpleNbTotal++;
			} else if (nbTag <= 50) {
				xhtmlMediumNbTotal++;
			} else {
				xhtmlComplexNbTotal++;
			}
		}
		System.out.println("xmlSimpleNbTotal : "+xmlSimpleNbTotal);
		System.out.println("xmlMediumNbTotal : "+xmlMediumNbTotal);
		System.out.println("xmlComplexNbTotal : "+xmlComplexNbTotal);
		System.out.println("xmlNbTotal : "+xmlNbTotal);
		System.out.println("nbOutputField : "+nbOutputField);
		System.out.println("nbRequestField : "+nbRequestField);
		
		System.out.println("xhtmlSimpleNbTotal : "+xhtmlSimpleNbTotal);
		System.out.println("xhtmlMediumNbTotal : "+xhtmlMediumNbTotal);
		System.out.println("xhtmlComplexNbTotal : "+xhtmlComplexNbTotal);
		System.out.println("xhtmlNbTotal : "+xhtmlNbTotal);

	}

	// private static void fileListByExtension(String extension) {
	// for (String path : listXmlFile(new
	// File("/home/gujou/Workspaces/java/Primagaz_workspace/emateriel/src"),extension)){
	// System.err.println(path);
	// }
	// }

	private static List<File> listFileByExtension(File folder, String extension) {
		List<File> result = new ArrayList<>();
		for (File file : folder.listFiles()) {
			if (file != null && file.exists()) {
				if (file.isDirectory()) {
					result.addAll(listFileByExtension(file, extension));
				} else if (file.getName().toLowerCase().endsWith(extension)) {
					result.add(file);
				}
			}
		}
		return result;
	}
}
