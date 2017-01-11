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
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlUtil {

	private static DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	private static DocumentBuilder dBuilder;

	static {
		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static int countTag(File file, String tagName) {
		try {
			Document doc = dBuilder.parse(file);
			return countChildTag(doc.getChildNodes(), tagName);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
		}
		return -1;
	}

	public static int countTag(File file) {
		try {
			Document doc = dBuilder.parse(file);
			return countChildTag(doc.getChildNodes(), null);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
		}
		return 0;
	}

	private static int countChildTag(NodeList nodes, String tagName) {
		int count = 0;
		for (int index = 0; index < nodes.getLength(); index++) {
			Node child = nodes.item(index);
			if (child != null && child.getNodeType() == Node.ELEMENT_NODE) {
				if (tagName == null || child.getNodeName() == tagName){
					count++;
				}
				// FIXME log to debug : System.out.println(child.getNodeName());
				if (child.getChildNodes() != null && child.getChildNodes().getLength() > 0) {
					count += countChildTag(child.getChildNodes(), tagName);
				}
			}

		}
		return count;
	}

}
