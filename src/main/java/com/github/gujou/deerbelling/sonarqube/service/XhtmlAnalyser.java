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
import java.util.List;
import java.util.Map;

import com.github.gujou.deerbelling.sonarqube.model.metric.Measure;
import com.github.gujou.deerbelling.sonarqube.tools.FileUtil;
import com.github.gujou.deerbelling.sonarqube.tools.XmlUtil;

public class XhtmlAnalyser {
	
	public static Map<String, Measure> analyse(File folder){
		Map<String, Measure> measures = new HashMap<>();
		
		int xhtmlNbTotal = 0, xhtmlSimpleNbTotal = 0, xhtmlMediumNbTotal = 0, xhtmlComplexNbTotal = 0;

		List<File> xhtmlFileList = FileUtil.listFileByExtension(folder, ".xhtml");

		for (File file : xhtmlFileList) {
			if (file != null && file.exists()){
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
		}
		
		if (xhtmlNbTotal > 0){
			measures.put("simple_pages", new Measure("simple_pages",  " simple pages xhtml",String.valueOf(xhtmlSimpleNbTotal)));
			measures.put("medium_pages", new Measure("medium_pages",  " medium pages xhtml",String.valueOf(xhtmlMediumNbTotal)));
			measures.put("complex_pages", new Measure("complex_pages",  " complex pages xhtml",String.valueOf(xhtmlComplexNbTotal)));
			measures.put("total_pages", new Measure("total_pages",  " pages xhtml",String.valueOf(xhtmlNbTotal)));
		}
		
		return measures;
	}
}
