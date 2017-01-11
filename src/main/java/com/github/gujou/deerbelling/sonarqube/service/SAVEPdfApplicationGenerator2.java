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

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.transform.TransformerException;

import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.xmpbox.XMPMetadata;
import org.apache.xmpbox.schema.DublinCoreSchema;
import org.apache.xmpbox.schema.PDFAIdentificationSchema;
import org.apache.xmpbox.type.BadFieldValueException;
import org.apache.xmpbox.xml.XmpSerializer;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.resources.Project;

import com.github.gujou.deerbelling.sonarqube.plugin.ReportsKeys;

/**
 * Creates a simple PDF/A document.
 */
public final class SAVEPdfApplicationGenerator2 {
	private SAVEPdfApplicationGenerator2() {
	}

	private final static int DEFAULT_marginHeight = 50;

	private final static int marginWidth = 50;

	private final static int spaceWidth = 10;

	private final static int spaceHeight = 10;

	private final static int titleMarginWidth = 50;

	private final static int logoMarginWidth = 65;

	private final static int titleSpaceHeight = 15;

	private final static Color DARK_RED_COLOR = new Color(134, 0, 0);

	private static final String REPORT_PROVERB = "Who frequents the kitchen smells of smoke";
	
	private static final int FONT_MAX_SIZE = 224;

	private static int positionHeight = DEFAULT_marginHeight;

	public static File generateFile(Project sonarProject, FileSystem sonarFileSystem, String sonarUrl,
			String sonarLogin, String sonarPassword) {

		String projectName = sonarProject.getName().replaceAll("[^A-Za-z0-9 ]", " ").trim().replaceAll(" +", " ");

		String filePath = sonarFileSystem.workDir().getAbsolutePath() + File.separator + "application_report_"
				+ projectName + "." + ReportsKeys.APPLICATION_REPORT_TYPE_PDF_EXTENSION;

		File file = new File(filePath);
		String fontfile = "/home/gujou/.gimp-2.8/fonts/28_Days_Later.ttf";

		PDDocument doc = new PDDocument();
		try {

			PDPage page = initNewPage(doc);

			// load the font as this needs to be embedded
			PDFont font = PDType0Font.load(doc, new File(fontfile));

			PDImageXObject image = PDImageXObject.createFromFile("/home/gujou/Pictures/belling/BELLING3_logo.png", doc);

			centerText("deerbelling report", font, 70, page, doc);
			// centerImage(image, page, doc, 400, 400);
			centerImage(image, page, doc);

			int projectNameFontSize = maxSizeFont(projectName, font, page);
			
			projectNameFontSize = (projectNameFontSize > FONT_MAX_SIZE) ? FONT_MAX_SIZE : projectNameFontSize;

			float projectNameHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000
					* projectNameFontSize;
			float reportProverbHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * 25;

			positionHeight += (page.getMediaBox().getHeight() - positionHeight - projectNameHeight
					- reportProverbHeight - spaceHeight) / 2;

			centerText(projectName, font, projectNameFontSize, page, doc);
			centerText(REPORT_PROVERB, font, 25, page, doc);

			PDImageXObject icon_lines = PDImageXObject
					.createFromFile("/home/gujou/Pictures/icon_flat/Saving Book-50.png", doc);

			PDImageXObject icon_packages = PDImageXObject.createFromFile("/home/gujou/Pictures/icon_flat/Box-52.png",
					doc);

			PDImageXObject icon_classes = PDImageXObject.createFromFile("/home/gujou/Pictures/icon_flat/File-50.png",
					doc);

			PDImageXObject icon_methods = PDImageXObject
					.createFromFile("/home/gujou/Pictures/icon_flat/Settings 3-50.png", doc);

			PDImageXObject icon_bugs = PDImageXObject.createFromFile("/home/gujou/Pictures/icon_flat/Bug-50.png", doc);

			PDImageXObject icon_debt = PDImageXObject.createFromFile("/home/gujou/Pictures/icon_flat/Banknotes-52.png",
					doc);

			PDImageXObject icon_complexity = PDImageXObject
					.createFromFile("/home/gujou/Pictures/icon_flat/Frankensteins Monster-48.png", doc);

			PDImageXObject icon_comments = PDImageXObject.createFromFile("/home/gujou/Pictures/icon_flat/Quote-50.png",
					doc);

			PDImageXObject icon_javadoc = PDImageXObject
					.createFromFile("/home/gujou/Pictures/icon_flat/Comments-API.png", doc);

			PDImageXObject icon_tests_fail = PDImageXObject
					.createFromFile("/home/gujou/Pictures/icon_flat/Dizzy Person Filled-50.png", doc);

			PDImageXObject icon_tests_success = PDImageXObject
					.createFromFile("/home/gujou/Pictures/icon_flat/Goal-50.png", doc);

			PDImageXObject icon_tests_cover = PDImageXObject
					.createFromFile("/home/gujou/Pictures/icon_flat/Waning Gibbous-52.png", doc);

			PDImageXObject icon_vulnerability_high = PDImageXObject
					.createFromFile("/home/gujou/Pictures/icon_flat/Shark-52.png", doc);

			PDImageXObject icon_vulnerability_medium = PDImageXObject
					.createFromFile("/home/gujou/Pictures/icon_flat/Bee-50.png", doc);

			PDImageXObject icon_vulnerability_low = PDImageXObject
					.createFromFile("/home/gujou/Pictures/icon_flat/Black Cat-50.png", doc);

			PDImageXObject icon_declared = PDImageXObject
					.createFromFile("/home/gujou/Pictures/icon_flat/Sugar Cubes-64.png", doc);

			PDImageXObject icon_unused = PDImageXObject
					.createFromFile("/home/gujou/Pictures/icon_flat/Litter Disposal-50.png", doc);

			PDImageXObject icon_undeclared = PDImageXObject
					.createFromFile("/home/gujou/Pictures/icon_flat/Move by Trolley-50.png", doc);

			PDImageXObject icon_cut_files = PDImageXObject.createFromFile("/home/gujou/Pictures/icon_flat/Cut-50.png",
					doc);

			PDImageXObject icon_cut_directories = PDImageXObject
					.createFromFile("/home/gujou/Pictures/icon_flat/Chainsaw-52.png", doc);

			PDImageXObject icon_duplicate = PDImageXObject
					.createFromFile("/home/gujou/Pictures/icon_flat/Feed Paper-50.png", doc);

			PDImageXObject icon_dev_count = PDImageXObject
					.createFromFile("/home/gujou/Pictures/icon_flat/Workers Male-50.png", doc);
			PDImageXObject icon_dev_best = PDImageXObject
					.createFromFile("/home/gujou/Pictures/icon_flat/Weightlifting Filled-50.png", doc);
			PDImageXObject icon_dev_issues = PDImageXObject
					.createFromFile("/home/gujou/Pictures/icon_flat/Full of Shit-50.png", doc);

			page = initNewPage(doc);

			// attribute(icon_lines, "7360", " lines of code", font, 20, page,
			// doc);
			//
			// attribute(icon_comments, "11 dot 3", " percent comments", font,
			// 20, page, doc);
			
			//http://localhost:9000/api/resources?resource=com.virbac.petfood:petfood-parent
			
			title("Global Information", font, 35, page, doc);
			attribute(icon_lines, 30, 30, "7360", " project name", font, 20, page, doc); // TODO switch field & value : resource => name
			attribute(icon_lines, 30, 30, "7360", " version", font, 20, page, doc); //  TODO switch field & value : resource => version
			attribute(icon_lines, 30, 30, "7360", " project analysis date", font, 20, page, doc); //  TODO switch field & value : resource => date
			attribute(icon_lines, 30, 30, "7360", " first analysis date", font, 20, page, doc); //  TODO switch field & value : resource => creationDate
			

			title("Global Size", font, 35, page, doc);
			
			attribute(icon_lines, 30, 30, "7360", " lines of code", font, 20, page, doc); // ncloc
			attribute(icon_lines, 30, 30, "25840", " lines", font, 20, page, doc); // lines => TODO
			attribute(icon_classes, 30, 30, "114", " classes", font, 20, page, doc); // classes
			attribute(icon_classes, 30, 30, "250", " files", font, 20, page, doc); // files => TODO
			attribute(icon_packages, 30, 30, "3", " directories", font, 20, page, doc); // directories
			attribute(icon_packages, 30, 30, "3", " projects", font, 20, page, doc); // projects
			attribute(icon_methods, 30, 30, "579", " methods", font, 20, page, doc); //functions
			attribute(icon_methods, 30, 30, "579", " getters and setters", font, 20, page, doc); //accessors
			attribute(icon_methods, 30, 30, "579", "  public API", font, 20, page, doc); //public_api
			attribute(icon_methods, 30, 30, "579", " statements", font, 20, page, doc); //statements
			attribute(icon_debt, 30, 30, "7d 6h", " of technical debt", font, 20, page, doc); // sqale_index
			attribute(icon_lines, 30, 30, "25840", " generated code lines", font, 20, page, doc); // generated_ncloc
			attribute(icon_lines, 30, 30, "25840", " generated lines", font, 20, page, doc); // generated_lines

			title("Global Issues", font, 35, page, doc);

			attribute(icon_bugs, 30, 30, "533", " issues", font, 20, page, doc); // violations + new method for new_violations
			attribute(icon_bugs, 30, 30, "533", " blocker issues", font, 20, page, doc); // blocker_violations + new method for new_blocker_violations
			attribute(icon_bugs, 30, 30, "533", " critical issues", font, 20, page, doc); // critical_violations + new method for new_critical_violations
			attribute(icon_bugs, 30, 30, "533", " major issues", font, 20, page, doc); // major_violations + new method for new_major_violations
			attribute(icon_bugs, 30, 30, "533", " minor issues", font, 20, page, doc); // minor_violations + new method for new_minor_violations
			attribute(icon_bugs, 30, 30, "533", " info issues", font, 20, page, doc); // info_violations + new method for new_info_violations
			attribute(icon_bugs, 30, 30, "533", " false positive issues", font, 20, page, doc); // false_positive_issues
			attribute(icon_bugs, 30, 30, "533", " open issues", font, 20, page, doc); // open_issues
			attribute(icon_bugs, 30, 30, "533", " confirmed issues", font, 20, page, doc); // confirmed_issues
			attribute(icon_bugs, 30, 30, "533", " reopened issues", font, 20, page, doc); // reopened_issues
			attribute(icon_bugs, 30, 30, "533", " weighted issues", font, 20, page, doc); // weighted_violations
			attribute(icon_bugs, 30, 30, "533", " rules compliance", font, 20, page, doc); // violations_density
			
			title("Global Complexity", font, 35, page, doc);
			attribute(icon_complexity, 30, 30, "3", " complexity", font, 20, page, doc); // complexity
			attribute(icon_complexity, 30, 30, "3", " complexity by class", font, 20, page, doc); // class_complexity
			attribute(icon_complexity, 30, 30, "3", " complexity by file", font, 20, page, doc); // file_complexity
			attribute(icon_complexity, 30, 30, "3", " complexity by method", font, 20, page, doc); // function_complexity
			
			title("Global Design", font, 35, page, doc);
			attribute(icon_cut_files, 30, 30, "41", " file cycles detected inside a directory", font, 20, page, doc); //file_cycles
			attribute(icon_cut_files, 30, 30, "41", " file dependencies inside a directory", font, 20, page, doc); //file_edges_weight
			attribute(icon_cut_files, 30, 30, "41", " file dependencies to cut ", font, 20, page, doc); //package_tangles
			attribute(icon_cut_files, 30, 30, "41", " suspect file dependencies ", font, 20, page, doc); //file_tangles (This metric is available at directory level.) + TODO file_tangle_index (File tangle index = 2 * (File tangle / File edges weight) * 100)
			attribute(icon_cut_files, 30, 30, "41", " directory cycles detected", font, 20, page, doc); //package_cycles
			attribute(icon_cut_directories, 30, 30, "10", " directory dependencies to cut", font, 20, page, doc); //package_feedback_edges + TODO package_tangle_index
			attribute(icon_cut_files, 30, 30, "41", " file dependencies between directories", font, 20, page, doc); //package_edges_weight
			attribute(icon_cut_files, 30, 30, "41", " file dependencies to cut in order to remove cycles between files inside a directorys", font, 20, page, doc); //file_feedback_edges
			
			title("Global Duplications", font, 35, page, doc);
			attribute(icon_duplicate, 30, 30, "82", " duplicated lines", font, 20, page, doc, "4.3"); // duplicated_lines + duplicated_lines_density
			attribute(icon_duplicate, 30, 30, "82", " duplicated blocks", font, 20, page, doc); // duplicated_blocks
			attribute(icon_duplicate, 30, 30, "82", " duplicated files", font, 20, page, doc); // duplicated_files

			page = initNewPage(doc);

			title("Global Documentation", font, 35, page, doc);

			attribute(icon_comments, 30, 30, "933", " comment lines", font, 20, page, doc, "11.3"); // comment_lines + comment_lines_density
			attribute(icon_javadoc, 30, 30, "397", " public undocumented API", font, 20, page, doc, "89.9"); // public_undocumented_api + (1 - public_documented_api_density %)

			title("Global Test", font, 35, page, doc);

			attribute(icon_tests_success, 30, 30, "185", " unit tests", font, 20, page, doc, "100"); //tests + TODO test_execution_time
			attribute(icon_tests_success, 30, 30, "185", " unit tests in failure", font, 20, page, doc, "100"); // test_failures
			attribute(icon_tests_fail, 30, 30, "0", " unit tests in error", font, 20, page, doc, "0"); // test_errors + calcul error density
			attribute(icon_tests_fail, 30, 30, "0", " unit tests skipped", font, 20, page, doc, "0"); // skipped_tests + calcul fail density
			attribute(icon_tests_fail, 30, 30, "0", " unit tests in success", font, 20, page, doc, "0"); // calcul success + test_success_density
			attribute(icon_tests_fail, 30, 30, "0", " PERCENT unit tests coverage", font, 20, page, doc, "0"); // coverage + new_coverage
			attribute(icon_tests_cover, 30, 30, "11721", " covered lines", font, 20, page, doc, "26"); // line_coverage + new_line_coverage
			attribute(icon_tests_cover, 30, 30, "9", " PERCENT condition covered", font, 20, page, doc, "26"); // branch_coverage + new_branch_coverage
			attribute(icon_tests_cover, 30, 30, "9", " lines not covered by unit tests", font, 20, page, doc, "26"); // uncovered_lines + new_uncovered_lines
			attribute(icon_tests_cover, 30, 30, "9", " branches not covered by unit tests", font, 20, page, doc, "26"); // uncovered_conditions + new_uncovered_conditions
			//Branch coverage hits	branch_coverage_hits_data	List of covered branches.
			

			title("Global libraries", font, 35, page, doc);

			attribute(icon_declared, 30, 30, "56", " declared dependencies", font, 20, page, doc);
			attribute(icon_unused, 30, 30, "5", " unused dependencies", font, 20, page, doc);
			attribute(icon_undeclared, 30, 30, "12", " undeclared dependencies", font, 20, page, doc);

			page = initNewPage(doc);

			title("Global Team", font, 35, page, doc);

			attribute(icon_dev_count, 30, 30, "12", " developers commit on project", font, 20, page, doc);
			attribute(icon_dev_best, 30, 30, "gujou", " is the top code line producer", font, 20, page, doc, "26.4");
			attribute(icon_dev_issues, 30, 30, "toto", " has created the most issues by line", font, 20, page, doc,
					"12.3");

			title("Global vulnerabilities", font, 35, page, doc);

			attribute(icon_vulnerability_low, 30, 30, "46", " low dependency vulnerabilities", font, 20, page, doc);
			attribute(icon_vulnerability_medium, 30, 30, "125", " medium dependency vulnerabilities", font, 20, page,
					doc);
			attribute(icon_vulnerability_high, 30, 30, "3", " high dependency vulnerabilities", font, 20, page, doc);
			
			page = initNewPage(doc);
			attribute(icon_vulnerability_high, 30, 30, String.valueOf(positionHeight), " TEST PAGE", font, 20, page, doc);
			attribute(icon_vulnerability_high, 30, 30, String.valueOf(positionHeight), " TEST PAGE", font, 20, page, doc);
			attribute(icon_vulnerability_high, 30, 30, String.valueOf(positionHeight), " TEST PAGE", font, 20, page, doc);
			attribute(icon_vulnerability_high, 30, 30, String.valueOf(positionHeight), " TEST PAGE", font, 20, page, doc);
			attribute(icon_vulnerability_high, 30, 30, String.valueOf(positionHeight), " TEST PAGE", font, 20, page, doc);
			attribute(icon_vulnerability_high, 30, 30, String.valueOf(positionHeight), " TEST PAGE", font, 20, page, doc);
			attribute(icon_vulnerability_high, 30, 30, String.valueOf(positionHeight), " TEST PAGE", font, 20, page, doc);
			attribute(icon_vulnerability_high, 30, 30, String.valueOf(positionHeight), " TEST PAGE", font, 20, page, doc);
			attribute(icon_vulnerability_high, 30, 30, String.valueOf(positionHeight), " TEST PAGE", font, 20, page, doc);
			attribute(icon_vulnerability_high, 30, 30, String.valueOf(positionHeight), " TEST PAGE", font, 20, page, doc);
			attribute(icon_vulnerability_high, 30, 30, String.valueOf(positionHeight), " TEST PAGE", font, 20, page, doc);
			attribute(icon_vulnerability_high, 30, 30, String.valueOf(positionHeight), " TEST PAGE", font, 20, page, doc);
			attribute(icon_vulnerability_high, 30, 30, String.valueOf(positionHeight), " TEST PAGE", font, 20, page, doc);
			attribute(icon_vulnerability_high, 30, 30, String.valueOf(positionHeight), " TEST PAGE", font, 20, page, doc);
			attribute(icon_vulnerability_high, 30, 30, String.valueOf(positionHeight), " TEST PAGE", font, 20, page, doc);
			attribute(icon_vulnerability_high, 30, 30, String.valueOf(positionHeight), " TEST PAGE", font, 20, page, doc);
			attribute(icon_vulnerability_high, 30, 30, String.valueOf(positionHeight), " TEST PAGE", font, 20, page, doc);
			attribute(icon_vulnerability_high, 30, 30, String.valueOf(positionHeight), " TEST PAGE", font, 20, page, doc);
			attribute(icon_vulnerability_high, 30, 30, String.valueOf(positionHeight), " TEST PAGE", font, 20, page, doc);
			attribute(icon_vulnerability_high, 30, 30, String.valueOf(positionHeight), " TEST PAGE", font, 20, page, doc);
			attribute(icon_vulnerability_high, 30, 30, String.valueOf(positionHeight), " TEST PAGE", font, 20, page, doc);
			attribute(icon_vulnerability_high, 30, 30, String.valueOf(positionHeight), " TEST PAGE", font, 20, page, doc);
			attribute(icon_vulnerability_high, 30, 30, String.valueOf(positionHeight), " TEST PAGE", font, 20, page, doc);
			attribute(icon_vulnerability_high, 30, 30, String.valueOf(positionHeight), " TEST PAGE", font, 20, page, doc);
			attribute(icon_vulnerability_high, 30, 30, String.valueOf(positionHeight), " TEST PAGE", font, 20, page, doc);
			attribute(icon_vulnerability_high, 30, 30, String.valueOf(positionHeight), " TEST PAGE", font, 20, page, doc);
			attribute(icon_vulnerability_high, 30, 30, String.valueOf(positionHeight), " TEST PAGE", font, 20, page, doc);
			attribute(icon_vulnerability_high, 30, 30, String.valueOf(positionHeight), " TEST PAGE", font, 20, page, doc);
			attribute(icon_vulnerability_high, 30, 30, String.valueOf(positionHeight), " TEST PAGE", font, 20, page, doc);
			attribute(icon_vulnerability_high, 30, 30, String.valueOf(positionHeight), " TEST PAGE", font, 20, page, doc);
			attribute(icon_vulnerability_high, 30, 30, String.valueOf(positionHeight), " TEST PAGE", font, 20, page, doc);
			attribute(icon_vulnerability_high, 30, 30, String.valueOf(positionHeight), " TEST PAGE", font, 20, page, doc);
			attribute(icon_vulnerability_high, 30, 30, String.valueOf(positionHeight), " TEST PAGE", font, 20, page, doc);
			attribute(icon_vulnerability_high, 30, 30, String.valueOf(positionHeight), " TEST PAGE", font, 20, page, doc);
			attribute(icon_vulnerability_high, 30, 30, String.valueOf(positionHeight), " TEST PAGE", font, 20, page, doc);
			attribute(icon_vulnerability_high, 30, 30, String.valueOf(positionHeight), " TEST PAGE", font, 20, page, doc);
			attribute(icon_vulnerability_high, 30, 30, String.valueOf(positionHeight), " TEST PAGE", font, 20, page, doc);
			attribute(icon_vulnerability_high, 30, 30, String.valueOf(positionHeight), " TEST PAGE", font, 20, page, doc);
			attribute(icon_vulnerability_high, 30, 30, String.valueOf(positionHeight), " TEST PAGE", font, 20, page, doc);
			attribute(icon_vulnerability_high, 30, 30, String.valueOf(positionHeight), " TEST PAGE", font, 20, page, doc);
			attribute(icon_vulnerability_high, 30, 30, String.valueOf(positionHeight), " TEST PAGE", font, 20, page, doc);
			attribute(icon_vulnerability_high, 30, 30, String.valueOf(positionHeight), " TEST PAGE", font, 20, page, doc);
			attribute(icon_vulnerability_high, 30, 30, String.valueOf(positionHeight), " TEST PAGE", font, 20, page, doc);
			attribute(icon_vulnerability_high, 30, 30, String.valueOf(positionHeight), " TEST PAGE", font, 20, page, doc);
			
			page = initNewPage(doc);
			title(String.valueOf(positionHeight), font, 35, page, doc);
			title(String.valueOf(positionHeight), font, 35, page, doc);
			title(String.valueOf(positionHeight), font, 35, page, doc);
			title(String.valueOf(positionHeight), font, 35, page, doc);
			title(String.valueOf(positionHeight), font, 35, page, doc);
			title(String.valueOf(positionHeight), font, 35, page, doc);
			title(String.valueOf(positionHeight), font, 35, page, doc);
			title(String.valueOf(positionHeight), font, 35, page, doc);
			title(String.valueOf(positionHeight), font, 35, page, doc);
			title(String.valueOf(positionHeight), font, 35, page, doc);
			title(String.valueOf(positionHeight), font, 35, page, doc);
			title(String.valueOf(positionHeight), font, 35, page, doc);
			title(String.valueOf(positionHeight), font, 35, page, doc);
			title(String.valueOf(positionHeight), font, 35, page, doc);
			title(String.valueOf(positionHeight), font, 35, page, doc);
			title(String.valueOf(positionHeight), font, 35, page, doc);
			title(String.valueOf(positionHeight), font, 35, page, doc);
			title(String.valueOf(positionHeight), font, 35, page, doc);
			title(String.valueOf(positionHeight), font, 35, page, doc);
			title(String.valueOf(positionHeight), font, 35, page, doc);
			title(String.valueOf(positionHeight), font, 35, page, doc);
			title(String.valueOf(positionHeight), font, 35, page, doc);
			title(String.valueOf(positionHeight), font, 35, page, doc);
			title(String.valueOf(positionHeight), font, 35, page, doc);
			title(String.valueOf(positionHeight), font, 35, page, doc);
			title(String.valueOf(positionHeight), font, 35, page, doc);
			title(String.valueOf(positionHeight), font, 35, page, doc);
			title(String.valueOf(positionHeight), font, 35, page, doc);
			
			page = initNewPage(doc);
			attribute(icon_vulnerability_high, 30, 30, String.valueOf(positionHeight), " TEST PAGE", font, 20, page, doc,String.valueOf(page.getMediaBox().getHeight()));
			

			// add XMP metadata
			XMPMetadata xmp = XMPMetadata.createXMPMetadata();

			try {
				DublinCoreSchema dc = xmp.createAndAddDublinCoreSchema();
				dc.setTitle(filePath);

				PDFAIdentificationSchema id = xmp.createAndAddPFAIdentificationSchema();
				id.setPart(1);
				id.setConformance("B");

				XmpSerializer serializer = new XmpSerializer();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				serializer.serialize(xmp, baos, true);

				PDMetadata metadata = new PDMetadata(doc);
				metadata.importXMPMetadata(baos.toByteArray());
				doc.getDocumentCatalog().setMetadata(metadata);
			} catch (BadFieldValueException e) {

				e.printStackTrace();
				// // won't happen here, as the provided value is valid
				// throw new IllegalArgumentException(e);
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// sRGB output intent
			// InputStream colorProfile =
			// CreatePDFA.class.getResourceAsStream("/usr/share/color/icc/colord/BestRGB.icc");
			// // /usr/share/color/icc/colord/sRGB.icc

			FileInputStream iccFile = new FileInputStream(new File("/usr/share/color/icc/colord/BestRGB.icc"));
			// PDOutputIntent intent = new PDOutputIntent(doc, colorProfile);
			PDOutputIntent intent = new PDOutputIntent(doc, iccFile);
			intent.setInfo("sRGB IEC61966-2.1");
			intent.setOutputCondition("sRGB IEC61966-2.1");
			intent.setOutputConditionIdentifier("sRGB IEC61966-2.1");
			intent.setRegistryName("http://www.color.org");
			doc.getDocumentCatalog().addOutputIntent(intent);

			doc.save(file);
		} catch (IOException e1) {

			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {

			IOUtils.closeQuietly(doc);
		}
		return file;
	}

	private static void title(String text, PDFont font, int fontSize, PDPage page, PDDocument doc) throws IOException {

		positionHeight += (titleSpaceHeight * 2);

		PDPageContentStream stream = new PDPageContentStream(doc, page, AppendMode.APPEND, false);

		float textWidth = font.getStringWidth(text) / 1000 * fontSize;
		float textHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;

		stream.beginText();
		stream.setFont(font, fontSize);

		stream.newLineAtOffset(titleMarginWidth, page.getMediaBox().getHeight() - positionHeight - textHeight);

		for (String word : text.split(" ")) {
			if (word != null && word.length() > 0) {
				stream.setNonStrokingColor(DARK_RED_COLOR);
				stream.showText(word.substring(0, 1));
				stream.setNonStrokingColor(Color.BLACK);
				stream.showText(word.substring(1));
				stream.showText(" ");
			}
		}

		stream.endText();
		// stream.drawLine(xOne, yOne - .5, xTwo, yYwo - .5);
		stream.moveTo(titleMarginWidth, page.getMediaBox().getHeight() - positionHeight - textHeight - 0.5f);
		stream.lineTo(titleMarginWidth + textWidth,
				page.getMediaBox().getHeight() - positionHeight - textHeight - 0.5f);
		stream.stroke();
		stream.close();

		positionHeight += titleSpaceHeight + textHeight;

	}

	private static void centerText(String text, PDFont font, int fontSize, PDPage page, PDDocument doc)
			throws IOException {

		PDPageContentStream stream = new PDPageContentStream(doc, page, AppendMode.APPEND, false);

		float textWidth = font.getStringWidth(text) / 1000 * fontSize;
		float textHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;

		stream.beginText();
		stream.setFont(font, fontSize);
		stream.newLineAtOffset((page.getMediaBox().getWidth() - textWidth) / 2,
				page.getMediaBox().getHeight() - positionHeight - textHeight);
		stream.showText(text);
		stream.endText();
		stream.close();

		positionHeight += textHeight;

	}

	private static int maxSizeFont(String text, PDFont font, PDPage page) throws IOException {
		return (int) (page.getMediaBox().getWidth() / (font.getStringWidth(text) * 1.1 / 1000));
	}

	private static void maximizeText(String text, PDFont font, PDPage page, PDDocument doc) throws IOException {
		PDPageContentStream stream = new PDPageContentStream(doc, page, AppendMode.APPEND, false);
		int fontSize = maxSizeFont(text, font, page);
		centerText(text, font, fontSize, page, doc);
	}

	private static void centerImage(PDImageXObject image, PDPage page, PDDocument doc) throws IOException {

		centerImage(image, page, doc, image.getHeight(), image.getWidth());

	}

	private static void centerImage(PDImageXObject image, PDPage page, PDDocument doc, int height, int width)
			throws IOException {

		PDPageContentStream stream = new PDPageContentStream(doc, page, AppendMode.APPEND, false);
		stream.drawImage(image, (page.getMediaBox().getWidth() - width) / 2,
				page.getMediaBox().getHeight() - positionHeight - height, width, height);
		stream.close();

		positionHeight += height;

	}

	private static void attribute(PDImageXObject logo, int logoHeight, int logoWidth, String data, String label,
			PDFont font, int fontSize, PDPage page, PDDocument doc, String percent) throws IOException {

		PDPageContentStream stream = new PDPageContentStream(doc, page, AppendMode.APPEND, false);

		int dataFontSize = (int) (fontSize * 1.5f);
		int labelFontSize = fontSize;
		int position = positionHeight;

		float logoYCoordinate = page.getMediaBox().getHeight() - position - logoHeight;
		float textWidth = (font.getStringWidth(data) / 1000 * dataFontSize)
				+ (font.getStringWidth(label) / 1000 * labelFontSize);
		float textHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * dataFontSize;
		float textYCoordinate = page.getMediaBox().getHeight() - position - (logoHeight / 2) - (textHeight / 2);
		float dataXCoordinate = logoMarginWidth + logoHeight + spaceWidth;

		stream.drawImage(logo, logoMarginWidth, logoYCoordinate, logoWidth, logoHeight);
		stream.beginText();
		stream.setFont(font, dataFontSize);
		stream.setNonStrokingColor(DARK_RED_COLOR);
		stream.newLineAtOffset(dataXCoordinate, textYCoordinate);
		stream.showText(data);
		stream.setNonStrokingColor(Color.BLACK);
		stream.setFont(font, labelFontSize);
		stream.showText(label);

		if (percent != null) {
			stream.newLineAtOffset(textWidth, 4);
			stream.setFont(PDType1Font.COURIER_BOLD, labelFontSize - 6);
			stream.showText("(");
			stream.setNonStrokingColor(DARK_RED_COLOR);
			stream.showText(percent);
			stream.showText("%");
			stream.setNonStrokingColor(Color.BLACK);
			stream.showText(")");
		}

		stream.endText();
		stream.close();

		positionHeight += spaceHeight + ((logoHeight < textHeight) ? textHeight : logoHeight);
	}

	private static void attribute(PDImageXObject logo, int logoHeight, int logoWidth, String data, String label,
			PDFont font, int fontSize, PDPage page, PDDocument doc) throws IOException {
		attribute(logo, logoHeight, logoWidth, data, label, font, fontSize, page, doc, null);
	}

	private static PDPage initNewPage(PDDocument doc) {
		positionHeight = DEFAULT_marginHeight;
		PDPage page = new PDPage();
		doc.addPage(page);
		return page;
	}
}
