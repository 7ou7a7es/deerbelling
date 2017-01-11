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
package com.github.gujou.deerbelling.sonarqube.model.metric;

public class Measure {

	private String key;
	private String val;
	private String frmt_val;
	private String label = "";
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getVal() {
		return val;
	}
	public void setVal(String val) {
		this.val = val;
	}
	public String getFrmt_val() {
		return frmt_val;
	}
	public void setFrmt_val(String frmt_val) {
		this.frmt_val = frmt_val;
	}
	
	public Measure(String val){
		this.val = val;
	}
	
	public Measure(String key, String val){
		this.key = key;
		this.val = val;
	}
	
	public Measure(String key, String label, String val){
		this.key = key;
		this.label = label;
		this.val = val;
	}
	
	public Measure(){}
	
	@Override
	public String toString() {
		return "Measure [key=" + key + ", val=" + val + ", frmt_val=" + frmt_val + "]";
	}
}
