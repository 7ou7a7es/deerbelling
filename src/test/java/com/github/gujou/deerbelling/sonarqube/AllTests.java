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
package com.github.gujou.deerbelling.sonarqube;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.github.gujou.deerbelling.sonarqube.gateway.ResourceGatewayTest;
import com.github.gujou.deerbelling.sonarqube.service.PdfApplicationGeneratorTest2;
import com.github.gujou.deerbelling.sonarqube.service.PdfApplicationGeneratorTest3;

@RunWith(Suite.class)
@SuiteClasses({
    PdfApplicationGeneratorTest2.class,
    PdfApplicationGeneratorTest3.class,
    ResourceGatewayTest.class})
public class AllTests {

}
