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

public class MulesoftAnalyser {

	public static Map<String, Measure> analyse(File folder){
		Map<String, Measure> measures = new HashMap<>();
		
		int xmlNbTotal = 0, xmlSimpleNbTotal = 0, xmlMediumNbTotal = 0, xmlComplexNbTotal = 0;
		int muleNbOutputField = 0, muleNbRequestField = 0, muleNbFlow = 0, muleNbSubFlow = 0;

		List<File> xmlFileList = FileUtil.listFileByExtension(folder, ".xml");

		for (File file : xmlFileList) {
			if (file != null && file.exists()){
				xmlNbTotal++;
				System.out.println(file.getPath());
				int nbTag = XmlUtil.countTag(file);
				if (nbTag <= 20) {
					xmlSimpleNbTotal++;
				} else if (nbTag <= 50) {
					xmlMediumNbTotal++;
				} else {
					xmlComplexNbTotal++;
				}
				
				muleNbOutputField += XmlUtil.countTag(file, "result");
				muleNbRequestField +=  XmlUtil.countTag(file, "select") +  XmlUtil.countTag(file, "update") +  XmlUtil.countTag(file, "delete") + XmlUtil.countTag(file, "insert");
				muleNbFlow += XmlUtil.countTag(file, "flow");
				muleNbSubFlow  += XmlUtil.countTag(file, "sub-flow");
			}
		}
		
		if (xmlNbTotal > 0){
			measures.put("xmlSimpleNbTotal", new Measure("xmlSimpleNbTotal", " xml light files", String.valueOf(xmlSimpleNbTotal)));
			measures.put("xmlMediumNbTotal", new Measure("xmlMediumNbTotal", " xml medium files", String.valueOf(xmlMediumNbTotal)));
			measures.put("xmlComplexNbTotal", new Measure("xmlComplexNbTotal", " xml large files", String.valueOf(xmlComplexNbTotal)));
			measures.put("xmlNbTotal", new Measure("xmlNbTotal"," xml files", String.valueOf(xmlNbTotal)));
			
			if (muleNbOutputField > 0){
				measures.put("muleOutputField", new Measure("muleOutputField"," mulesoft output fields", String.valueOf(muleNbOutputField)));
			}
			if (muleNbRequestField > 0){
				measures.put("muleNbRequestField",  new Measure("muleNbRequestField"," mulesoft database request", String.valueOf(muleNbRequestField)));
			}
			if (muleNbFlow > 0){
				measures.put("muleNbFlow",  new Measure("muleNbFlow"," mulesoft flow configured", String.valueOf(muleNbFlow)));
			}
			if (muleNbSubFlow > 0){
				measures.put("muleNbSubFlow",  new Measure("muleNbSubFlow"," mulesoft subflow config", String.valueOf(muleNbSubFlow)));
			}
		}
		
		return measures;
	}
	
}
