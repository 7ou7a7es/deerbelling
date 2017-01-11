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

public class FileUtil {
	
	public static List<File> listFileByExtension(File folder, String extension) {
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
