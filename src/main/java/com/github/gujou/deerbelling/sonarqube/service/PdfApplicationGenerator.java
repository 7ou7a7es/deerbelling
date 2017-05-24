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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.xml.transform.TransformerException;

import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDSimpleFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.xmpbox.XMPMetadata;
import org.apache.xmpbox.schema.DublinCoreSchema;
import org.apache.xmpbox.schema.PDFAIdentificationSchema;
import org.apache.xmpbox.type.BadFieldValueException;
import org.apache.xmpbox.xml.XmpSerializer;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.resources.Project;

import com.github.gujou.deerbelling.sonarqube.gateway.ResourceGateway;
import com.github.gujou.deerbelling.sonarqube.model.metric.Measure;
import com.github.gujou.deerbelling.sonarqube.model.metric.Resource;
import com.github.gujou.deerbelling.sonarqube.model.metric.Resources;
import com.github.gujou.deerbelling.sonarqube.plugin.ReportsKeys;

/**
 * Creates a simple PDF/A document.
 */
public final class PdfApplicationGenerator {
	private PdfApplicationGenerator() {
	}

	private final static int DEFAULT_LOGO_LEFT_MARGIN_WIDTH = 70;

	private final static int DEFAULT_LOGO_RIGHT_MARGIN_WIDTH = 50;

	private final static int DEFAULT_MARGIN_HEIGHT = 50;

	private final static int DEFAULT_ICON_MARGIN_WIDTH = 65;

	private final static int DEFAULT_TITLE_MARGIN_WIDTH = 50;

	private final static int MARGIN_WIDTH = 30;

	private final static int DEFAULT_SPACE_WIDTH = 10;

	private final static int spaceHeight = 10;

	private final static int titleSpaceHeight = 15;

	private final static Color DARK_RED_COLOR = new Color(134, 0, 0);

	private final static Color SMILE_ORANGE_COLOR = new Color(231, 92, 27);

	private final static Color SMILE_BLUE_COLOR = new Color(84, 110, 123);

	private final static Color DARK_BLUE_COLOR = new Color(60, 90, 100);

	private final static Color DEFAULT_TEXT_COLOR = DARK_BLUE_COLOR;

	private static final String REPORT_PROVERB = "Who frequents the kitchen smells of smoke";

	private static final int FONT_MAX_SIZE = 224;

	private static int positionHeight = DEFAULT_MARGIN_HEIGHT;

	private static int positionTitleWidth = DEFAULT_TITLE_MARGIN_WIDTH;

	private static int positionLogoWidth = DEFAULT_ICON_MARGIN_WIDTH;

	private static final DecimalFormat decimalFormat = new DecimalFormat("#0.0");

	private static PDPage page;

	public static File generateFile(Project sonarProject, FileSystem sonarFileSystem, String sonarUrl,
			String sonarLogin, String sonarPassword, Map<String, Measure> sonarMeasures) {

		Resources sonarResources = ResourceGateway.getOpenIssues(sonarProject.getKey(), sonarUrl, sonarLogin,
				sonarPassword);

		// Only one resource => call with sonarProject.getKey()
		Resource projectResource = sonarResources.getResource().get(0);

		for (Measure measure : projectResource.getMsr()) {

			System.out.println(measure.getKey() + " " + measure.getVal());

			sonarMeasures.put(measure.getKey(), measure);
		}

		String projectName = sonarProject.getName().replaceAll("[^A-Za-z0-9 ]", " ").trim().replaceAll(" +", " ");

		String filePath = sonarFileSystem.workDir().getAbsolutePath() + File.separator + "application_report_"
				+ sonarProject.getEffectiveKey().replace(':', '-') + "."
				+ ReportsKeys.APPLICATION_REPORT_TYPE_PDF_EXTENSION;

		File file = new File(filePath);

		String fontfile = "font/OpenSans-Regular.ttf";

		PDDocument doc = new PDDocument();
		try {

			initNewPage(doc);

			PDSimpleFont font = PDType1Font.TIMES_BOLD;
			PDSimpleFont fontItalic = PDType1Font.TIMES_BOLD_ITALIC;

			PDImageXObject smileLogo = createFromFile("/images/Logo_Smile.png", doc);

			leftImage(smileLogo, page, doc, 80, 166);

			positionHeight = (int) (page.getMediaBox().getHeight() / 2) - 65;

			centerText("Indicateurs du projet", font, 45, page, doc);

			int heightProjectName = maximizeText(projectName, font, page, doc);

			positionHeight = (int) (page.getMediaBox().getHeight()) - 280 + heightProjectName;

			positionLogoWidth = (int) (page.getMediaBox().getWidth() / 2) - 100;
			positionTitleWidth = (int) (page.getMediaBox().getWidth() / 2) - 100;

			PDImageXObject icon_lines = createFromFile("/images/Lines-50.png", doc);

			PDImageXObject icon_author = createFromFile("/images/Typewriter_With_Paper-50.png", doc);

			PDImageXObject icon_version = createFromFile("/images/Versions-50.png", doc);

			PDImageXObject icon_date = createFromFile("/images/Date_To-50.png", doc);

			PDImageXObject icon_ncloc = createFromFile("/images/CodeLines-52.png", doc);

			PDImageXObject icon_folders = createFromFile("/images/Folder-50.png", doc);

			PDImageXObject icon_packages = createFromFile("/images/Box-52.png", doc);

			PDImageXObject icon_classes = createFromFile("/images/CodeFile-50.png", doc);

			PDImageXObject icon_files = createFromFile("/images/File-50.png", doc);

			PDImageXObject icon_methods = createFromFile("/images/Settings_3-50.png", doc);

			PDImageXObject icon_accessors = createFromFile("/images/Accessors-50.png", doc);

			PDImageXObject icon_api = createFromFile("/images/API_Settings-50.png", doc);

			PDImageXObject icon_keyring = createFromFile("/images/Keys.png", doc);

			PDImageXObject icon_bug = createFromFile("/images/Bug-50.png", doc);

			PDImageXObject icon_balance = createFromFile("/images/Scales-50.png", doc);

			PDImageXObject icon_wightBugs = createFromFile("/images/Weight-Bug-50.png", doc);

			PDImageXObject icon_poison = createFromFile("/images/Poison-50.png", doc);

			PDImageXObject icon_fire = createFromFile("/images/Campfire-50.png", doc);

			PDImageXObject icon_major = createFromFile("/images/Error-50.png", doc);

			PDImageXObject icon_minor = createFromFile("/images/Attention-51.png", doc);

			PDImageXObject icon_info = createFromFile("/images/Info-50.png", doc);

			PDImageXObject icon_ok = createFromFile("/images/Ok-50.png", doc);

			PDImageXObject icon_open = createFromFile("/images/Open_Sign-50.png", doc);

			PDImageXObject icon_confirmed = createFromFile("/images/Law-50.png", doc);

			PDImageXObject icon_debt = createFromFile("/images/Banknotes-52.png", doc);

			PDImageXObject icon_codeGenerated = createFromFile("/images/CodeFactory-50.png", doc);

			PDImageXObject icon_linesGenerated = createFromFile("/images/LineFactory-50.png", doc);

			PDImageXObject icon_screen = createFromFile("/images/Screen_TV-52.png", doc);

			PDImageXObject icon_screenSimple = createFromFile("/images/Screen_Pion-52.png", doc);

			PDImageXObject icon_screenMedium = createFromFile("/images/Screen_Cheval-52.png", doc);

			PDImageXObject icon_screenComplex = createFromFile("/images/Screen_Queen-52.png", doc);

			PDImageXObject icon_xmlTotal = createFromFile("/images/Conf_File-50.png", doc);

			PDImageXObject icon_xmlSimple = createFromFile("/images/Conf_File_simple-50.png", doc);

			PDImageXObject icon_xmlMedium = createFromFile("/images/Conf_File_medium-50.png", doc);

			PDImageXObject icon_xmlComplex = createFromFile("/images/Conf_File_complex-50.png", doc);

			PDImageXObject icon_mulesoftOut = createFromFile("/images/icon-mulesoftm-out.png", doc);
			PDImageXObject icon_mulesoftIn = createFromFile("/images/icon-mulesoftm-in.png", doc);

			PDImageXObject icon_mulesoftFlow = createFromFile("/images/icon-mulesoftm-flow.png", doc);

			PDImageXObject icon_complexity = createFromFile("/images/Frankensteins_Monster-48.png", doc);

			PDImageXObject icon_complexityClass = createFromFile("/images/ComplexCodeFile-50.png", doc);

			PDImageXObject icon_complexityMethod = createFromFile("/images/WolfSettings-50.png", doc);

			PDImageXObject icon_complexityFile = createFromFile("/images/FreddyFile-50.png", doc);

			PDImageXObject icon_comments = createFromFile("/images/Quote-50.png", doc);

			PDImageXObject icon_javadoc = createFromFile("/images/Comments-API.png", doc);

			PDImageXObject icon_tests_fail = createFromFile("/images/Dizzy_Person_Filled-50.png", doc);

			PDImageXObject icon_tests_skip = createFromFile("/images/Fast_Forward-50.png", doc);

			PDImageXObject icon_tests_error = createFromFile("/images/Explosion-50.png", doc);

			PDImageXObject icon_tests = createFromFile("/images/Search-52.png", doc);

			PDImageXObject icon_tests_success = createFromFile("/images/Goal-50.png", doc);

			PDImageXObject icon_conditions_cover = createFromFile("/images/Waning_Gibbous-52.png", doc);

			PDImageXObject icon_tests_cover = createFromFile("/images/Checklist-50.png", doc);

			PDImageXObject icon_vulnerability_high = createFromFile("/images/Shark-52.png", doc);

			PDImageXObject icon_vulnerability_medium = createFromFile("/images/Bee-50.png", doc);

			PDImageXObject icon_vulnerability_low = createFromFile("/images/Black_Cat-50.png", doc);

			PDImageXObject icon_declared = createFromFile("/images/Sugar_Cubes-64.png", doc);

			PDImageXObject icon_unused = createFromFile("/images/Litter_Disposal-50.png", doc);

			PDImageXObject icon_undeclared = createFromFile("/images/Move_by_Trolley-50.png", doc);

			PDImageXObject icon_filecycle = createFromFile("/images/FileCycle.png", doc);

			PDImageXObject icon_packagecycle = createFromFile("/images/PackageCycle.png", doc);

			PDImageXObject icon_cut_files = createFromFile("/images/Cut-50.png", doc);

			PDImageXObject icon_chain = createFromFile("/images/Link-52.png", doc);

			PDImageXObject icon_cut_directories = createFromFile("/images/Chainsaw-52.png", doc);

			PDImageXObject icon_duplicate = createFromFile("/images/Feed_Paper-50.png", doc);

			PDImageXObject icon_duplicate_lines = createFromFile("/images/Line-Spacing-icon.png", doc);

			PDImageXObject icon_duplicate_packages = createFromFile("/images/DuplicateBlocks2.png", doc);

			PDImageXObject icon_dev_count = createFromFile("/images/Workers_Male-50.png", doc);
			PDImageXObject icon_dev_best = createFromFile("/images/Weightlifting_Filled-50.png", doc);
			PDImageXObject icon_dev_issues = createFromFile("/images/Full_of_Shit-50.png", doc);

			attribute(icon_author, 22, 22, " Guillaume Jourdan", fontItalic, 15, doc, DEFAULT_TEXT_COLOR);
			attribute(icon_version, 22, 22, " Version 1.0", fontItalic, 15, doc, DEFAULT_TEXT_COLOR); // TODO
																										// switch
																										// field
																										// &
																										// value
																										// :
																										// resource
																										// =>
																										// version

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.FRENCH);
			attribute(icon_date, 22, 22, simpleDateFormat.format(new Date()), fontItalic, 15, doc, DEFAULT_TEXT_COLOR); // TODO
																														// switch
																														// field
																														// &
																														// value
																														// :
																														// resource
																														// =>
																														// date

			initNewPage(doc);

			title("Project Sizing", font, 26, doc);

			attribute(icon_ncloc, 22, 22, sonarMeasures.get("ncloc"), true, " lines of code", font, 15, doc); // ncloc
			attribute(icon_lines, 22, 22, sonarMeasures.get("lines"), true, " lines", font, 15, doc); // lines
																										// =>
																										// TODO
			attribute(icon_classes, 22, 22, sonarMeasures.get("classes"), true, " classes", font, 15, doc); // classes
			attribute(icon_files, 22, 22, sonarMeasures.get("files"), true, " files", font, 15, doc); // files
																										// =>
																										// TODO
			attribute(icon_folders, 22, 22, sonarMeasures.get("directories"), true, " directories", font, 15, doc); // directories
			attribute(icon_packages, 22, 22, sonarMeasures.get("projects"), true, " modules", font, 15, doc); // projects
			attribute(icon_methods, 22, 22, sonarMeasures.get("functions"), true, " methods", font, 15, doc); // functions
			attribute(icon_accessors, 22, 22, sonarMeasures.get("accessors"), true, " getters and setters", font, 15,
					doc); // accessors
			attribute(icon_api, 22, 22, sonarMeasures.get("public_api"), true, "  public API", font, 15, doc); // public_api
			attribute(icon_keyring, 22, 22, sonarMeasures.get("statements"), true, " statements", font, 15, doc); // statements
			attribute(icon_codeGenerated, 22, 22, sonarMeasures.get("generated_ncloc"), true, " generated code lines",
					font, 15, doc); // generated_ncloc
			attribute(icon_linesGenerated, 22, 22, sonarMeasures.get("generated_lines"), true, " generated lines", font,
					15, doc); // generated_lines

			Measure totalPages = sonarMeasures.get("total_pages");
			Measure simplePages = sonarMeasures.get("simple_pages");
			Measure mediumPages = sonarMeasures.get("medium_pages");
			Measure complexPages = sonarMeasures.get("complex_pages");

			if (totalPages != null) {
				attribute(icon_screen, 22, 22, totalPages, true, totalPages.getLabel(), font, 15, doc);
			}
			if (simplePages != null) {
				attribute(icon_screenSimple, 22, 22, simplePages, true, simplePages.getLabel(), font, 15, doc);
			}
			if (mediumPages != null) {
				attribute(icon_screenMedium, 22, 22, mediumPages, true, mediumPages.getLabel(), font, 15, doc);
			}
			if (complexPages != null) {
				attribute(icon_screenComplex, 22, 22, complexPages, true, complexPages.getLabel(), font, 15, doc);
			}

			Measure xmlNbTotal = sonarMeasures.get("xmlNbTotal");
			Measure xmlSimpleNbTotal = sonarMeasures.get("xmlSimpleNbTotal");
			Measure xmlMediumNbTotal = sonarMeasures.get("xmlMediumNbTotal");
			Measure xmlComplexNbTotal = sonarMeasures.get("xmlComplexNbTotal");
			Measure muleOutputField = sonarMeasures.get("muleOutputField");
			Measure muleNbRequestField = sonarMeasures.get("muleNbRequestField");
			Measure muleNbFlow = sonarMeasures.get("muleNbFlow");
			Measure muleNbSubFlow = sonarMeasures.get("muleNbSubFlow");

			if (xmlNbTotal != null) {
				attribute(icon_xmlTotal, 22, 22, xmlNbTotal, true, xmlNbTotal.getLabel(), font, 15, doc);
			}
			if (xmlSimpleNbTotal != null) {
				attribute(icon_xmlSimple, 22, 22, xmlSimpleNbTotal, true, xmlSimpleNbTotal.getLabel(), font, 15, doc);
			}
			if (xmlMediumNbTotal != null) {
				attribute(icon_xmlMedium, 22, 22, xmlMediumNbTotal, true, xmlMediumNbTotal.getLabel(), font, 15, doc);
			}
			if (xmlComplexNbTotal != null) {
				attribute(icon_xmlComplex, 22, 22, xmlComplexNbTotal, true, xmlComplexNbTotal.getLabel(), font, 15,
						doc);
			}
			if (muleOutputField != null) {
				attribute(icon_mulesoftOut, 22, 22, muleOutputField, true, muleOutputField.getLabel(), font, 15, doc);
			}
			if (muleNbRequestField != null) {
				attribute(icon_mulesoftIn, 22, 22, muleNbRequestField, true, muleNbRequestField.getLabel(), font, 15,
						doc);
			}
			if (muleNbFlow != null) {
				attribute(icon_mulesoftFlow, 22, 22, muleNbFlow, true, muleNbFlow.getLabel(), font, 15, doc);
			}
			if (muleNbSubFlow != null) {
				attribute(icon_mulesoftFlow, 22, 22, muleNbSubFlow, true, muleNbSubFlow.getLabel(), font, 15, doc);
			}

			title("Design", font, 26, doc);
			attribute(icon_packagecycle, 22, 22, sonarMeasures.get("package_cycles"), true, " package cycles detected",
					font, 15, doc); // package_cycles
			attribute(icon_cut_files, 22, 22, sonarMeasures.get("package_tangles"), true, " file dep. to cut cycles ",
					font, 15, doc, sonarMeasures.get("package_tangle_index"), true); // package_tangles
																						// +
																						// TODO
																						// package_tangle_index
																						// X
			attribute(icon_cut_directories, 22, 22, sonarMeasures.get("package_feedback_edges"), true,
					" package dep. to cut cycles", font, 15, doc); // package_feedback_edges
			attribute(icon_chain, 22, 22, sonarMeasures.get("package_edges_weight"), true, " file dep. betw. packages",
					font, 15, doc); // package_edges_weight X

			title("Complexity", font, 26, doc);
			attribute(icon_complexity, 22, 22, sonarMeasures.get("complexity"), true, " complexity index", font, 15,
					doc); // complexity
			attribute(icon_complexityClass, 22, 22, sonarMeasures.get("class_complexity"), true,
					" complexity index by class", font, 15, doc); // class_complexity
			attribute(icon_complexityFile, 22, 22, sonarMeasures.get("file_complexity"), true,
					" complexity index by file", font, 15, doc); // file_complexity
			attribute(icon_complexityMethod, 22, 22, sonarMeasures.get("function_complexity"), true,
					" complexity index by method", font, 15, doc); // function_complexity

			title("Duplications", font, 26, doc);
			attribute(icon_duplicate_lines, 22, 22, sonarMeasures.get("duplicated_lines"), true, " duplicated lines",
					font, 15, doc, sonarMeasures.get("duplicated_lines_density"), true); // duplicated_lines
																							// +
																							// duplicated_lines_density
			attribute(icon_duplicate, 22, 22, sonarMeasures.get("duplicated_files"), true, " involved files", font, 15,
					doc); // duplicated_files
			attribute(icon_duplicate_packages, 22, 22, sonarMeasures.get("duplicated_blocks"), true,
					" duplicated blocks", font, 15, doc); // duplicated_blocks

			title("Sonarqube Issues", font, 26, doc);

			attribute(icon_bug, 22, 22, sonarMeasures.get("violations"), true, " issues", font, 15, doc); // violations
																											// +
																											// new
																											// method
																											// for
																											// new_violations
			attribute(icon_poison, 22, 22, sonarMeasures.get("blocker_violations"), true, " blocker issues", font, 15,
					doc); // blocker_violations + new method for
							// new_blocker_violations
			attribute(icon_fire, 22, 22, sonarMeasures.get("critical_violations"), true, " critical issues", font, 15,
					doc); // critical_violations + new method for
							// new_critical_violations
			attribute(icon_major, 22, 22, sonarMeasures.get("major_violations"), true, " major issues", font, 15, doc); // major_violations
																														// +
																														// new
																														// method
																														// for
																														// new_major_violations
			attribute(icon_minor, 22, 22, sonarMeasures.get("minor_violations"), true, " minor issues", font, 15, doc); // minor_violations
																														// +
																														// new
																														// method
																														// for
																														// new_minor_violations
			// attribute(icon_info, 22, 22, "533", " info issues", font, 15,
			// doc); // info_violations + new method for new_info_violations
			// attribute(icon_ok, 22, 22, "533", " false positive issues", font,
			// 15, doc); // false_positive_issues
			// attribute(icon_open, 22, 22, "533", " open issues", font, 15,
			// doc); // open_issues
			// attribute(icon_confirmed, 22, 22, "533", " confirmed issues",
			// font, 15, doc); // confirmed_issues
			// attribute(icon_open, 22, 22, "533", " reopened issues", font, 15,
			// doc); // reopened_issues
			// attribute(icon_wightBugs, 22, 22, "533", " weighted issues",
			// font, 15, doc); // weighted_violations
			// attribute(icon_balance, 22, 22, "533", " rules compliance index",
			// font, 15, doc); // violations_density

			Measure squaleIndexMeasure = sonarMeasures.get("sqale_index");
			if (squaleIndexMeasure != null) {
				attribute(icon_debt, 22, 22, squaleIndexMeasure.getFrmt_val(), " Sqale technical debt", font, 15, doc,
						null, false, SMILE_ORANGE_COLOR); // sqale_index
			}

			title("Documentation", font, 26, doc);

			attribute(icon_comments, 22, 22, sonarMeasures.get("comment_lines"), true, " comment lines", font, 15, doc,
					sonarMeasures.get("comment_lines_density"), true); // comment_lines
																		// +
																		// comment_lines_density

			Measure publicApiUndocMeasure = sonarMeasures.get("public_undocumented_api");

			if (publicApiUndocMeasure != null) {
				String publicApiUndocDensity = "100";
				Measure publicApiDocDensityMeasure = sonarMeasures.get("public_documented_api_density");
				if (publicApiDocDensityMeasure != null) {
					publicApiUndocDensity = decimalFormat.format(
							100.0 - Float.parseFloat(sonarMeasures.get("public_documented_api_density").getVal()));
				}

				attribute(icon_javadoc, 22, 22, publicApiUndocMeasure, true, " public undoc. API", font, 15, doc,
						new Measure(publicApiUndocDensity), true); // public_undocumented_api
				// + (1
				// -
				// public_documented_api_density
				// %)
			}
			title("OWASP plugin", font, 26, doc);

			attribute(icon_vulnerability_high, 22, 22, sonarMeasures.get("high_severity_vulns"), true,
					" high dep. vulnerabilities", font, 15, doc);
			attribute(icon_vulnerability_medium, 22, 22, sonarMeasures.get("medium_severity_vulns"), true,
					" medium dep. vulnerabilities", font, 15, doc);
			attribute(icon_vulnerability_low, 22, 22, sonarMeasures.get("low_severity_vulns"), true,
					" low dep. vulnerabilities", font, 15, doc);

			title("Unit Test", font, 26, doc);

			Measure testsMeasure = sonarMeasures.get("tests");

			if (testsMeasure == null) {
				attribute(icon_tests, 22, 22, "0", " unit tests", font, 15, doc, null, false, SMILE_ORANGE_COLOR);
				attribute(icon_tests_cover, 22, 22, "0", " covered lines", font, 15, doc, "0", true,
						SMILE_ORANGE_COLOR);
			} else {
				attribute(icon_tests, 22, 22, testsMeasure, true, " unit tests", font, 15, doc);
				try {
					int nbTests = (int) Double.parseDouble(testsMeasure.getVal());
					Measure failureTestsMeasure = sonarMeasures.get("test_failures");
					Measure errorTestsMeasure = sonarMeasures.get("test_errors");
					int failureTests = failureTestsMeasure != null
							? (int) Double.parseDouble(failureTestsMeasure.getVal()) : 0;
					int errorTests = errorTestsMeasure != null ? (int) Double.parseDouble(errorTestsMeasure.getVal())
							: 0;
					int successTests = nbTests - failureTests - errorTests;
					float errorPercent = (errorTests * 100f) / nbTests;
					float failurePercent = (failureTests * 100f) / nbTests;

					Measure successPercentMeasure = sonarMeasures.get("test_success_density");
					String successPercent = successPercentMeasure != null ? successPercentMeasure.getVal()
							: decimalFormat.format((successTests * 100) / nbTests);

					attribute(icon_tests_success, 22, 22, Integer.valueOf(successTests).toString(), " tests in success",
							font, 15, doc, successPercent, true, SMILE_ORANGE_COLOR);
					attribute(icon_tests_fail, 22, 22, Integer.valueOf(failureTests).toString(), " tests in failure",
							font, 15, doc, decimalFormat.format(failurePercent), true, SMILE_ORANGE_COLOR);
					attribute(icon_tests_error, 22, 22, Integer.valueOf(errorTests).toString(), " tests in error", font,
							15, doc, decimalFormat.format(errorPercent), true, SMILE_ORANGE_COLOR);

					Measure coveragePercentMeasure = sonarMeasures.get("line_coverage");
					Measure uncoverMeasure = sonarMeasures.get("uncovered_lines");
					Measure totalLineToCoverMeasure = sonarMeasures.get("lines_to_cover");
					int uncover = uncoverMeasure != null ? (int) Double.parseDouble(uncoverMeasure.getVal()) : 0;
					int totalLineToCover = totalLineToCoverMeasure != null
							? (int) Double.parseDouble(totalLineToCoverMeasure.getVal()) : 0;
					if (coveragePercentMeasure != null) {
						attribute(icon_tests_cover, 22, 22, Integer.valueOf(totalLineToCover - uncover).toString(),
								" covered lines", font, 15, doc, coveragePercentMeasure.getVal(), true,
								SMILE_ORANGE_COLOR);
					} else {
						attribute(icon_tests_cover, 22, 22, "0", " covered lines", font, 15, doc, "0", true,
								SMILE_ORANGE_COLOR);
					}

				} catch (NumberFormatException nfe) {
					System.err.println(nfe);
					nfe.printStackTrace();
				}
			}

			// attribute(icon_tests, 22, 22, sonarMeasures.get("tests"), " unit
			// tests", font, 15, doc); //tests + TODO test_execution_time
			// attribute(icon_tests_success, 22, 22, "0", " tests in success",
			// font, 15, doc, sonarMeasures.get("test_success_density"), true);
			// // calcul success + test_success_density
			// attribute(icon_tests_fail, 22, 22, "0", " tests in failure",
			// font, 15, doc); // test_failures + calcul fail density
			// attribute(icon_tests_error, 22, 22, "0", " tests in error", font,
			// 15, doc); // test_errors + calcul error density
			// attribute(icon_tests_skip, 22, 22, "0", " tests skipped", font,
			// 15, doc); // skipped_tests
			// attribute(icon_tests_cover, 22, 22,
			// sonarMeasures.get("line_coverage"), " covered lines", font, 15,
			// doc, sonarMeasures.get("lines_to_cover"), true); // line_coverage
			// => lines_to_cover
			// attribute(icon_conditions_cover, 22, 22, "4450", " uncovered
			// conditions", font, 15, doc, "85", true); // uncovered_conditions
			// + (100 - branch_coverage)

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

	private static void title(String text, PDFont font, int fontSize, PDDocument doc) throws IOException {

		if (positionHeight + (page.getMediaBox().getHeight() / 6) >= page.getMediaBox().getHeight()) {
			if (positionTitleWidth > DEFAULT_TITLE_MARGIN_WIDTH) {
				initNewPage(doc);
			} else {
				writeToRight();
			}
		}

		// Jump line if not the first page title.
		if (positionHeight != DEFAULT_MARGIN_HEIGHT) {
			positionHeight += (titleSpaceHeight * 2);
		}

		PDPageContentStream stream = new PDPageContentStream(doc, page, AppendMode.APPEND, false);

		stream.setNonStrokingColor(DEFAULT_TEXT_COLOR);

		float textWidth = font.getStringWidth(text) / 1000 * fontSize;
		float textHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;

		stream.beginText();
		stream.setFont(font, fontSize);

		stream.newLineAtOffset(positionTitleWidth, page.getMediaBox().getHeight() - positionHeight - textHeight);

		stream.showText(text);
		stream.endText();
		stream.close();

		positionHeight += titleSpaceHeight + textHeight;

	}

	private static int centerText(String text, PDFont font, int fontSize, PDPage page, PDDocument doc)
			throws IOException {

		PDPageContentStream stream = new PDPageContentStream(doc, page, AppendMode.APPEND, false);

		stream.setNonStrokingColor(DEFAULT_TEXT_COLOR);

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

		return (int) textHeight;

	}

	private static void rightText(String text, PDFont font, int fontSize, PDPage page, PDDocument doc)
			throws IOException {

		PDPageContentStream stream = new PDPageContentStream(doc, page, AppendMode.APPEND, false);

		stream.setNonStrokingColor(DEFAULT_TEXT_COLOR);

		float textWidth = font.getStringWidth(text) / 1000 * fontSize;
		float textHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;

		stream.beginText();
		stream.setFont(font, fontSize);
		stream.newLineAtOffset(page.getMediaBox().getWidth() - textWidth - DEFAULT_LOGO_RIGHT_MARGIN_WIDTH,
				page.getMediaBox().getHeight() - positionHeight - textHeight);
		stream.showText(text);
		stream.endText();
		stream.close();

		positionHeight += textHeight;

	}

	private static void leftText(String text, PDFont font, int fontSize, PDPage page, PDDocument doc)
			throws IOException {

		PDPageContentStream stream = new PDPageContentStream(doc, page, AppendMode.APPEND, false);

		stream.setNonStrokingColor(DEFAULT_TEXT_COLOR);

		float textWidth = font.getStringWidth(text) / 1000 * fontSize;
		float textHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;

		stream.beginText();
		stream.setFont(font, fontSize);
		stream.newLineAtOffset(DEFAULT_LOGO_LEFT_MARGIN_WIDTH,
				page.getMediaBox().getHeight() - positionHeight - textHeight);
		stream.showText(text);
		stream.endText();
		stream.close();

		positionHeight += textHeight;

	}

	private static int maxSizeFont(String text, PDFont font, PDPage page) throws IOException {
		return (int) (page.getMediaBox().getWidth() / (font.getStringWidth(text) * 1.1 / 1000));
	}

	private static int maximizeText(String text, PDFont font, PDPage page, PDDocument doc) throws IOException {
		// PDPageContentStream stream = new PDPageContentStream(doc, page,
		// AppendMode.APPEND, false);

		// stream.setNonStrokingColor(DEFAULT_TEXT_COLOR);

		int fontSize = maxSizeFont(text, font, page);

		return centerText(text, font, fontSize, page, doc);
	}

	private static void centerImage(PDImageXObject image, PDPage page, PDDocument doc) throws IOException {

		centerImage(image, page, doc, image.getHeight(), image.getWidth());

	}

	private static void centerImage(PDImageXObject image, PDPage page, PDDocument doc, int height, int width)
			throws IOException {

		PDPageContentStream stream = new PDPageContentStream(doc, page, AppendMode.APPEND, false);

		stream.setNonStrokingColor(DEFAULT_TEXT_COLOR);

		stream.drawImage(image, (page.getMediaBox().getWidth() - width) / 2,
				page.getMediaBox().getHeight() - positionHeight - height, width, height);
		stream.close();

		positionHeight += height;

	}

	private static void leftImage(PDImageXObject image, PDPage page, PDDocument doc) throws IOException {
		leftImage(image, page, doc, image.getHeight(), image.getWidth());
	}

	private static void leftImage(PDImageXObject image, PDPage page, PDDocument doc, int height, int width)
			throws IOException {

		PDPageContentStream stream = new PDPageContentStream(doc, page, AppendMode.APPEND, false);

		stream.setNonStrokingColor(DEFAULT_TEXT_COLOR);

		stream.drawImage(image, DEFAULT_LOGO_LEFT_MARGIN_WIDTH,
				page.getMediaBox().getHeight() - positionHeight - height, width, height);
		stream.close();

		positionHeight += height;

	}

	private static void rightImage(PDImageXObject image, PDPage page, PDDocument doc, int height, int width)
			throws IOException {

		PDPageContentStream stream = new PDPageContentStream(doc, page, AppendMode.APPEND, false);

		stream.setNonStrokingColor(DEFAULT_TEXT_COLOR);

		stream.drawImage(image, (page.getMediaBox().getWidth() - width) - DEFAULT_LOGO_RIGHT_MARGIN_WIDTH,
				page.getMediaBox().getHeight() - positionHeight - height, width, height);
		stream.close();

		positionHeight += height;

	}

	private static void attribute(PDImageXObject logo, int logoHeight, int logoWidth, String value, String label,
			PDFont font, int fontSize, PDDocument doc, String index, boolean isPercent, Color color)
			throws IOException {

		int dataFontSize = (int) (fontSize * 1.5f);
		int labelFontSize = fontSize;

		float logoYCoordinate = page.getMediaBox().getHeight() - positionHeight - logoHeight;
		float textWidth = (font.getStringWidth(value) / 1000 * dataFontSize)
				+ (font.getStringWidth(label) / 1000 * labelFontSize);
		float textHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * dataFontSize;
		float textYCoordinate = page.getMediaBox().getHeight() - positionHeight - (logoHeight / 2) - (textHeight / 2);
		float dataXCoordinate = positionLogoWidth + logoHeight + DEFAULT_SPACE_WIDTH;

		positionHeight += spaceHeight + ((logoHeight < textHeight) ? textHeight : logoHeight);

		if (positionHeight >= page.getMediaBox().getHeight()) {
			if (positionLogoWidth > DEFAULT_ICON_MARGIN_WIDTH) {
				initNewPage(doc);
				attribute(logo, logoHeight, logoWidth, value, label, font, labelFontSize, doc, index, isPercent, color);
			} else {
				writeToRight();
				attribute(logo, logoHeight, logoWidth, value, label, font, labelFontSize, doc, index, isPercent, color);
			}
		}

		else {

			PDPageContentStream stream = new PDPageContentStream(doc, page, AppendMode.APPEND, false);
			try {
				stream.drawImage(logo, positionLogoWidth, logoYCoordinate, logoWidth, logoHeight);
				stream.beginText();
				stream.setFont(font, dataFontSize);
				stream.setNonStrokingColor(color);
				stream.newLineAtOffset(dataXCoordinate, textYCoordinate + 6);
				stream.showText(value);
				stream.setNonStrokingColor(DEFAULT_TEXT_COLOR);
				stream.setFont(font, labelFontSize);
				stream.showText(label);

				if (index != null) {
					stream.newLineAtOffset(textWidth, (int) (labelFontSize * 0.3));
					stream.setFont(PDType1Font.COURIER_BOLD, (int) (labelFontSize * 0.8));
					stream.showText("(");
					stream.setNonStrokingColor(color);
					stream.showText(index);
					if (isPercent) {
						stream.showText("%");
					}
					stream.setNonStrokingColor(DEFAULT_TEXT_COLOR);
					stream.showText(")");
				}
			} finally {
				stream.endText();
				stream.close();
			}

		}
	}

	private static void attribute(PDImageXObject logo, int logoHeight, int logoWidth, Measure measure, boolean isInt,
			String label, PDFont font, int fontSize, PDDocument doc) throws IOException {
		attribute(logo, logoHeight, logoWidth, measure, isInt, label, font, fontSize, doc, null, false);
	}

	private static void attribute(PDImageXObject logo, int logoHeight, int logoWidth, String label, PDFont font,
			int fontSize, PDDocument doc, Color color) throws IOException {
		attribute(logo, logoHeight, logoWidth, label, "", font, fontSize, doc, null, false, color);
	}

	private static void attribute(PDImageXObject logo, int logoHeight, int logoWidth, Measure measure, boolean isInt,
			String label, PDFont font, int fontSize, PDDocument doc, Measure attribute, boolean isPercent)
			throws IOException {

		if (measure != null) {

			String value = measure.getVal();
			if (isInt && value.endsWith(".0")) {
				value = value.substring(0, value.indexOf(".0"));
			}

			if (attribute != null) {
				attribute(logo, logoHeight, logoWidth, value, label, font, fontSize, doc, attribute.getVal(), isPercent,
						SMILE_ORANGE_COLOR);
			} else {
				attribute(logo, logoHeight, logoWidth, value, label, font, fontSize, doc, null, false,
						SMILE_ORANGE_COLOR);
			}
		}
	}

	private static void initNewPage(PDDocument doc) {
		positionHeight = DEFAULT_MARGIN_HEIGHT;
		positionLogoWidth = DEFAULT_ICON_MARGIN_WIDTH;
		positionTitleWidth = DEFAULT_TITLE_MARGIN_WIDTH;
		page = new PDPage();
		doc.addPage(page);
	}

	private static void writeToRight() {
		positionHeight = DEFAULT_MARGIN_HEIGHT;
		positionTitleWidth = MARGIN_WIDTH + (int) (page.getMediaBox().getWidth() / 2);
		positionLogoWidth = positionTitleWidth;
	}

	private static PDImageXObject createFromFile(String path, PDDocument doc) throws IOException {
		System.out.println("path : " + PdfApplicationGenerator.class.getResource(path).getPath());
		return LosslessFactory.createFromImage(doc,
				ImageIO.read(PdfApplicationGenerator.class.getResourceAsStream(path)));
	}
}
