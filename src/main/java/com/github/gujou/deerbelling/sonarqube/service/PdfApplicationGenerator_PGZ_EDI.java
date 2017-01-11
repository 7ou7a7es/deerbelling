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
import org.apache.pdfbox.pdmodel.font.PDSimpleFont;
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
public final class PdfApplicationGenerator_PGZ_EDI {
	private PdfApplicationGenerator_PGZ_EDI() {
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
	
	private final static Color SMILE_ORANGE_COLOR = new Color(231,92,27);
	
	private final static Color SMILE_BLUE_COLOR = new Color(84,110,123);
	
	private final static Color DARK_BLUE_COLOR = new Color(60,90,100);
	
	private final static Color DEFAULT_TEXT_COLOR = DARK_BLUE_COLOR;
	
	private static final String REPORT_PROVERB = "Who frequents the kitchen smells of smoke";
	
	private static final int FONT_MAX_SIZE = 224;

	private static int positionHeight = DEFAULT_MARGIN_HEIGHT;
	
	private static int positionTitleWidth = DEFAULT_TITLE_MARGIN_WIDTH;
	
	private static int positionLogoWidth = DEFAULT_ICON_MARGIN_WIDTH;
	
	private static PDPage page;

	public static File generateFile(Project sonarProject, FileSystem sonarFileSystem, String sonarUrl,
			String sonarLogin, String sonarPassword) {

		String projectName = sonarProject.getName().replaceAll("[^A-Za-z0-9 ]", " ").trim().replaceAll(" +", " ");

		String filePath = sonarFileSystem.workDir().getAbsolutePath() + File.separator + "application_report_"
				+ projectName + "." + ReportsKeys.APPLICATION_REPORT_TYPE_PDF_EXTENSION;

		File file = new File(filePath);
		
		String fontfile = "/home/gujou/.gimp-2.8/fonts/Open_Sans/OpenSans-Regular.ttf";

		PDDocument doc = new PDDocument();
		try {

			initNewPage(doc);

			PDSimpleFont font = PDType1Font.TIMES_BOLD;
			PDSimpleFont fontItalic = PDType1Font.TIMES_BOLD_ITALIC;

			PDImageXObject smileLogo = PDImageXObject.createFromFile("/home/gujou/Desktop/Desktop/ImagesDoc/Logo_Smile.jpg", doc);
			PDImageXObject primagazLogo = PDImageXObject.createFromFile("/home/gujou/Desktop/Desktop/ImagesDoc/primagaz.png", doc);

			leftImage(smileLogo, page, doc, 80, 166);
			
			positionHeight -= 75;
			
			rightImage(primagazLogo, page, doc, 70, 242);
			
			positionHeight = (int) (page.getMediaBox().getHeight() / 2) - 65;
			
			centerText("Indicateurs du projet", font, 45, page, doc);
			
			centerText(projectName, font, 60, page, doc);
			
			positionHeight = (int) (page.getMediaBox().getHeight()) - 210;
			
			positionLogoWidth = (int) (page.getMediaBox().getWidth() / 2) - 100;
			positionTitleWidth = (int) (page.getMediaBox().getWidth() / 2) - 100; 
			
			PDImageXObject icon_lines = PDImageXObject
					.createFromFile("/home/gujou/Pictures/icon_flat/Lines-50.png", doc);
			
			PDImageXObject icon_author = PDImageXObject
					.createFromFile("/home/gujou/Pictures/icon_flat/Typewriter With Paper-50.png", doc);
			
			PDImageXObject icon_version = PDImageXObject
					.createFromFile("/home/gujou/Pictures/icon_flat/Versions-50.png", doc);
			
			PDImageXObject icon_date = PDImageXObject
					.createFromFile("/home/gujou/Pictures/icon_flat/Date To-50.png", doc);
			
			PDImageXObject icon_ncloc = PDImageXObject
					.createFromFile("/home/gujou/Pictures/icon_flat/CodeLines-52.png", doc);
			
			PDImageXObject icon_folders = PDImageXObject.createFromFile("/home/gujou/Pictures/icon_flat/Folder-50.png",
					doc);
			
			PDImageXObject icon_packages = PDImageXObject.createFromFile("/home/gujou/Pictures/icon_flat/Box-52.png",
					doc);

			PDImageXObject icon_classes = PDImageXObject.createFromFile("/home/gujou/Pictures/icon_flat/CodeFile-50.png",
					doc);
			
			PDImageXObject icon_files = PDImageXObject.createFromFile("/home/gujou/Pictures/icon_flat/File-50.png",
					doc);

			PDImageXObject icon_methods = PDImageXObject
					.createFromFile("/home/gujou/Pictures/icon_flat/Settings 3-50.png", doc);
			
			PDImageXObject icon_accessors = PDImageXObject
					.createFromFile("/home/gujou/Pictures/icon_flat/Accessors-50.png", doc);
			
			PDImageXObject icon_api = PDImageXObject
					.createFromFile("/home/gujou/Pictures/icon_flat/API Settings-50.png", doc);
			
			PDImageXObject icon_keyring = PDImageXObject
					.createFromFile("/home/gujou/Pictures/icon_flat/Keys.png", doc);

			PDImageXObject icon_bug = PDImageXObject.createFromFile("/home/gujou/Pictures/icon_flat/Bug-50.png", doc);
			
			PDImageXObject icon_balance = PDImageXObject.createFromFile("/home/gujou/Pictures/icon_flat/Scales-50.png", doc);
			
			PDImageXObject icon_wightBugs = PDImageXObject.createFromFile("/home/gujou/Pictures/icon_flat/Weight-Bug-50.png", doc);
			
			PDImageXObject icon_poison = PDImageXObject.createFromFile("/home/gujou/Pictures/icon_flat/Poison-50.png", doc);
			
			PDImageXObject icon_fire = PDImageXObject.createFromFile("/home/gujou/Pictures/icon_flat/Campfire-50.png", doc);
			
			PDImageXObject icon_major = PDImageXObject.createFromFile("/home/gujou/Pictures/icon_flat/Error-50.png", doc);
			
			PDImageXObject icon_minor = PDImageXObject.createFromFile("/home/gujou/Pictures/icon_flat/Attention-51.png", doc);
			
			PDImageXObject icon_info = PDImageXObject.createFromFile("/home/gujou/Pictures/icon_flat/Info-50.png", doc);
			
			PDImageXObject icon_ok = PDImageXObject.createFromFile("/home/gujou/Pictures/icon_flat/Ok-50.png", doc);
			
			PDImageXObject icon_open = PDImageXObject.createFromFile("/home/gujou/Pictures/icon_flat/Open Sign-50.png", doc);
			
			PDImageXObject icon_confirmed = PDImageXObject.createFromFile("/home/gujou/Pictures/icon_flat/Law-50.png", doc);
			
			PDImageXObject icon_debt = PDImageXObject.createFromFile("/home/gujou/Pictures/icon_flat/Banknotes-52.png",
					doc);

			PDImageXObject icon_codeGenerated = PDImageXObject.createFromFile("/home/gujou/Pictures/icon_flat/CodeFactory-50.png",
			doc);
			
			PDImageXObject icon_linesGenerated = PDImageXObject.createFromFile("/home/gujou/Pictures/icon_flat/LineFactory-50.png",
					doc);
			
			PDImageXObject icon_complexity = PDImageXObject
					.createFromFile("/home/gujou/Pictures/icon_flat/Frankensteins Monster-48.png", doc);
			
			PDImageXObject icon_complexityClass = PDImageXObject
					.createFromFile("/home/gujou/Pictures/icon_flat/ComplexCodeFile-50.png", doc);
			
			PDImageXObject icon_complexityMethod = PDImageXObject
					.createFromFile("/home/gujou/Pictures/icon_flat/WolfSettings-50.png", doc);
			
			PDImageXObject icon_complexityFile = PDImageXObject
					.createFromFile("/home/gujou/Pictures/icon_flat/FreddyFile-50.png", doc);

			PDImageXObject icon_comments = PDImageXObject.createFromFile("/home/gujou/Pictures/icon_flat/Quote-50.png",
					doc);

			PDImageXObject icon_javadoc = PDImageXObject
					.createFromFile("/home/gujou/Pictures/icon_flat/Comments-API.png", doc);

			PDImageXObject icon_tests_fail = PDImageXObject
					.createFromFile("/home/gujou/Pictures/icon_flat/Dizzy Person Filled-50.png", doc);
			
			PDImageXObject icon_tests_skip= PDImageXObject
					.createFromFile("/home/gujou/Pictures/icon_flat/Fast Forward-50.png", doc);
			
			PDImageXObject icon_tests_error = PDImageXObject
					.createFromFile("/home/gujou/Pictures/icon_flat/Explosion-50.png", doc);
			
			PDImageXObject icon_tests = PDImageXObject
					.createFromFile("/home/gujou/Pictures/icon_flat/Search-52.png", doc);

			PDImageXObject icon_tests_success = PDImageXObject
					.createFromFile("/home/gujou/Pictures/icon_flat/Goal-50.png", doc);

			PDImageXObject icon_conditions_cover = PDImageXObject
					.createFromFile("/home/gujou/Pictures/icon_flat/Waning Gibbous-52.png", doc);
			
			PDImageXObject icon_tests_cover = PDImageXObject
					.createFromFile("/home/gujou/Pictures/icon_flat/Checklist-50.png", doc);

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
			
			PDImageXObject icon_filecycle = PDImageXObject.createFromFile("/home/gujou/Pictures/icon_flat/FileCycle.png",
					doc);

			PDImageXObject icon_packagecycle = PDImageXObject.createFromFile("/home/gujou/Pictures/icon_flat/PackageCycle.png",
					doc);
			
			PDImageXObject icon_cut_files = PDImageXObject.createFromFile("/home/gujou/Pictures/icon_flat/Cut-50.png",
					doc);
			
			PDImageXObject icon_chain = PDImageXObject.createFromFile("/home/gujou/Pictures/icon_flat/Link-52.png",
					doc);

			PDImageXObject icon_cut_directories = PDImageXObject
					.createFromFile("/home/gujou/Pictures/icon_flat/Chainsaw-52.png", doc);

			PDImageXObject icon_duplicate = PDImageXObject
					.createFromFile("/home/gujou/Pictures/icon_flat/Feed Paper-50.png", doc);
			
			PDImageXObject icon_duplicate_lines = PDImageXObject
					.createFromFile("/home/gujou/Pictures/icon_flat/Line-Spacing-icon.png", doc);
			
			PDImageXObject icon_duplicate_packages = PDImageXObject
					.createFromFile("/home/gujou/Pictures/icon_flat/DuplicateBlocks2.png", doc);
			


			PDImageXObject icon_dev_count = PDImageXObject
					.createFromFile("/home/gujou/Pictures/icon_flat/Workers Male-50.png", doc);
			PDImageXObject icon_dev_best = PDImageXObject
					.createFromFile("/home/gujou/Pictures/icon_flat/Weightlifting Filled-50.png", doc);
			PDImageXObject icon_dev_issues = PDImageXObject
					.createFromFile("/home/gujou/Pictures/icon_flat/Full of Shit-50.png", doc);

			

			// attribute(icon_lines, "7360", " lines of code", font, 15, page,
			// doc);
			//
			// attribute(icon_comments, "11 dot 3", " percent comments", font,
			// 20, page, doc);
			
			//http://localhost:9000/api/resources?resource=com.virbac.petfood:petfood-parent
			
//			title("", font, 26, doc);
			attribute(icon_author, 22, 22, " Guillaume Jourdan", fontItalic, 15, doc, DEFAULT_TEXT_COLOR); 
			attribute(icon_version, 22, 22, " Version 1.0", fontItalic, 15, doc, DEFAULT_TEXT_COLOR); //  TODO switch field & value : resource => version
			attribute(icon_date, 22, 22, " 12 Septembre 2016", fontItalic, 15, doc, DEFAULT_TEXT_COLOR); //  TODO switch field & value : resource => date
			

			initNewPage(doc);

			
			title("Project Sizing", font, 26, doc);
			
			attribute(icon_ncloc, 22, 22, "1068", " lines of code", font, 15, doc); // ncloc
			attribute(icon_lines, 22, 22, "1259", " lines", font, 15, doc); // lines => TODO
			attribute(icon_classes, 22, 22, "21", " classes", font, 15, doc); // classes
			attribute(icon_files, 22, 22, "19", " files", font, 15, doc); // files => TODO
			attribute(icon_folders, 22, 22, "3", " directories", font, 15, doc); // directories
			attribute(icon_packages, 22, 22, "1", " modules", font, 15, doc); // projects
			attribute(icon_methods, 22, 22, "51", " methods", font, 15, doc); //functions
			attribute(icon_accessors, 22, 22, "28", " getters and setters", font, 15, doc); //accessors
			attribute(icon_api, 22, 22, "35", "  public API", font, 15, doc); //public_api
			attribute(icon_keyring, 22, 22, "365", " statements", font, 15, doc); //statements
			attribute(icon_codeGenerated, 22, 22, "0", " generated code lines", font, 15, doc); // generated_ncloc
			attribute(icon_linesGenerated, 22, 22, "0", " generated lines", font, 15, doc); // generated_lines
			
			
			title("Design", font, 26, doc);
			attribute(icon_packagecycle, 22, 22, "0", " package cycles detected", font, 15, doc); //package_cycles
			attribute(icon_cut_files, 22, 22, "0", " file dep. to cut cycles ", font, 15, doc, "0", true); //package_tangles + TODO package_tangle_index X
			attribute(icon_cut_directories, 22, 22, "0", " package dep. to cut cycles", font, 15, doc); //package_feedback_edges
			attribute(icon_chain, 22, 22, "0", " file dep. betw. packages", font, 15, doc); //package_edges_weight X

			
			title("Complexity", font, 26, doc);
			attribute(icon_complexity, 22, 22, "138", " complexity index", font, 15, doc); // complexity
			attribute(icon_complexityClass, 22, 22, "6.6", " complexity index by class", font, 15, doc); // class_complexity
			attribute(icon_complexityFile, 22, 22, "7.3", " complexity index by file", font, 15, doc); // file_complexity
			attribute(icon_complexityMethod, 22, 22, "2.7", " complexity index by method", font, 15, doc); // function_complexity
			
			title("Duplications", font, 26, doc);
			attribute(icon_duplicate_lines, 22, 22, "0", " duplicated lines", font, 15, doc, "0", true); // duplicated_lines + duplicated_lines_density
			attribute(icon_duplicate, 22, 22, "0", " involved files", font, 15, doc); // duplicated_files
			attribute(icon_duplicate_packages, 22, 22, "0", " duplicated blocks", font, 15, doc); // duplicated_blocks
			
			title("Sonarqube Issues", font, 26, doc);

			attribute(icon_bug, 22, 22, "66", " issues", font, 15, doc); // violations + new method for new_violations
			attribute(icon_poison, 22, 22, "1", " blocker issues", font, 15, doc); // blocker_violations + new method for new_blocker_violations
			attribute(icon_fire, 22, 22, "2", " critical issues", font, 15, doc); // critical_violations + new method for new_critical_violations
			attribute(icon_major, 22, 22, "22", " major issues", font, 15, doc); // major_violations + new method for new_major_violations
			attribute(icon_minor, 22, 22, "41", " minor issues", font, 15, doc); // minor_violations + new method for new_minor_violations
//			attribute(icon_info, 22, 22, "533", " info issues", font, 15, doc); // info_violations + new method for new_info_violations
//			attribute(icon_ok, 22, 22, "533", " false positive issues", font, 15, doc); // false_positive_issues
//			attribute(icon_open, 22, 22, "533", " open issues", font, 15, doc); // open_issues
//			attribute(icon_confirmed, 22, 22, "533", " confirmed issues", font, 15, doc); // confirmed_issues
//			attribute(icon_open, 22, 22, "533", " reopened issues", font, 15, doc); // reopened_issues
//			attribute(icon_wightBugs, 22, 22, "533", " weighted issues", font, 15, doc); // weighted_violations
//			attribute(icon_balance, 22, 22, "533", " rules compliance index", font, 15, doc); // violations_density
			attribute(icon_debt, 22, 22, "1 day", " of technical debt", font, 15, doc); // sqale_index

			title("Documentation", font, 26, doc);

			attribute(icon_comments, 22, 22, "9", " comment lines", font, 15, doc, "0.8", true); // comment_lines + comment_lines_density
			attribute(icon_javadoc, 22, 22, "35", " public undoc. API", font, 15, doc, "100", true); // public_undocumented_api + (1 - public_documented_api_density %)

			title("OWASP plugin", font, 26, doc);

			attribute(icon_vulnerability_high, 22, 22, "2", " high dep. vulnerabilities", font, 15, doc);
			attribute(icon_vulnerability_medium, 22, 22, "13", " medium dep. vulnerabilities", font, 15, doc);
			attribute(icon_vulnerability_low, 22, 22, "0", " low dep. vulnerabilities", font, 15, doc);
			
			writeToRight();
			
			title("Unit Test", font, 26, doc);

			attribute(icon_tests, 22, 22, "0", " unit tests", font, 15, doc); //tests + TODO test_execution_time
//			attribute(icon_tests_success, 22, 22, "0", " tests in success", font, 15, doc, "0", true); // calcul success + test_success_density
//			attribute(icon_tests_fail, 22, 22, "0", " tests in failure", font, 15, doc); // test_failures
//			attribute(icon_tests_error, 22, 22, "0", " tests in error", font, 15, doc); // test_errors + calcul error density
//			attribute(icon_tests_skip, 22, 22, "0", " tests skipped", font, 15, doc); // skipped_tests + calcul fail density
			attribute(icon_tests_cover, 22, 22, "0", " covered lines", font, 15, doc, "0", true); // line_coverage => lines_to_cover
//			attribute(icon_conditions_cover, 22, 22, "4450", " uncovered conditions", font, 15, doc, "85", true); // uncovered_conditions + (100 - branch_coverage)
			
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
			if (positionTitleWidth >  DEFAULT_TITLE_MARGIN_WIDTH){
				initNewPage(doc);
			} else {
				writeToRight();
			}
		}
		
		// Jump line if not the first page title.
		if (positionHeight != DEFAULT_MARGIN_HEIGHT){
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

	private static void centerText(String text, PDFont font, int fontSize, PDPage page, PDDocument doc)
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
		stream.newLineAtOffset( DEFAULT_LOGO_LEFT_MARGIN_WIDTH,
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
		
		stream.setNonStrokingColor(DEFAULT_TEXT_COLOR);
		
		int fontSize = maxSizeFont(text, font, page);
		centerText(text, font, fontSize, page, doc);
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
		
		stream.drawImage(image, (page.getMediaBox().getWidth() - width)-DEFAULT_LOGO_RIGHT_MARGIN_WIDTH,
				page.getMediaBox().getHeight() - positionHeight - height, width, height);
		stream.close();

		positionHeight += height;

	}
	
	private static void attribute(PDImageXObject logo, int logoHeight, int logoWidth, String value, String label,
			PDFont font, int fontSize, PDDocument doc, String index, boolean isPercent, Color color) throws IOException {

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
			if (positionLogoWidth >  DEFAULT_ICON_MARGIN_WIDTH){
				initNewPage(doc);
				attribute(logo, logoHeight, logoWidth, value, label, font, labelFontSize, doc, index, isPercent, color);
			} else {
				writeToRight();
				attribute(logo, logoHeight, logoWidth, value, label, font, labelFontSize, doc, index, isPercent, color);
			}
		}
		
		else {


			PDPageContentStream stream = new PDPageContentStream(doc, page, AppendMode.APPEND, false);
			try{
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
				if (isPercent){
					stream.showText("%");
				}
				stream.setNonStrokingColor(DEFAULT_TEXT_COLOR);
				stream.showText(")");
			}
			} finally{
			stream.endText();
			stream.close();
			}

		}
	}

	private static void attribute(PDImageXObject logo, int logoHeight, int logoWidth, String value, String label,
			PDFont font, int fontSize, PDDocument doc) throws IOException {
		attribute(logo, logoHeight, logoWidth, value, label, font, fontSize, doc, null, false);
	}
	
	private static void attribute(PDImageXObject logo, int logoHeight, int logoWidth, String label,
			PDFont font, int fontSize, PDDocument doc, Color color) throws IOException {
		attribute(logo, logoHeight, logoWidth, label, "", font, fontSize, doc, null, false, color);
	}
	
	private static void attribute(PDImageXObject logo, int logoHeight, int logoWidth, String value, String label,
			PDFont font, int fontSize, PDDocument doc, String attribute, boolean isPercent) throws IOException {
		attribute(logo, logoHeight, logoWidth, value, label, font, fontSize, doc, attribute, isPercent, SMILE_ORANGE_COLOR);
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
}
