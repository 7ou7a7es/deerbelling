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

package com.github.gujou.sonar_deerbelling_plugin.service;

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

import com.github.gujou.sonar_deerbelling_plugin.plugin.ReportsKeys;

/**
 * Creates a simple PDF/A document.
 */
public final class PdfApplicationGenerator {
	private PdfApplicationGenerator() {
	}

	private final static int DEFAULT_marginHeight = 50;

	private final static int marginWidth = 50;

	private final static int spaceWidth = 10;

	private final static int spaceHeight = 10;

	private final static int titleMarginWidth = 50;

	private final static int logoMarginWidth = 65;

	private final static int titleSpaceHeight = 15;

	private final static Color DARK_RED_COLOR = new Color(134, 0, 0);

	private static int marginHeight = DEFAULT_marginHeight;

	public static File generateFile(Project sonarProject, FileSystem sonarFileSystem, String sonarUrl,
			String sonarLogin, String sonarPassword) {
		// if (args.length != 3) {
		// System.err.println("usage: " +
		// PdfApplicationGenerator.class.getName() + " <output-file> <Message>
		// <ttf-file>");
		// System.exit(1);
		// }
		//
		// for (String arg : args) {
		// System.err.println("args: " + arg);
		// }

		String filePath = sonarFileSystem.workDir().getAbsolutePath() + File.separator + "application_report_"
				+ sonarProject.getEffectiveKey().replace(':', '-') + "."
				+ ReportsKeys.APPLICATION_REPORT_TYPE_PDF_EXTENSION;

		File file = new File(filePath);
		String message = "deerbelling";
		String fontfile = "/home/gujou/.gimp-2.8/fonts/28_Days_Later.ttf";

		PDDocument doc = new PDDocument();
		try {
			PDPage page = initNewPage(doc);

			// load the font as this needs to be embedded
			PDFont font = PDType0Font.load(doc, new File(fontfile));

			PDImageXObject image = PDImageXObject.createFromFile("/home/gujou/Pictures/belling/BELLING3_logo.png", doc);

			centerText(message, font, 100, page, doc);
			// centerImage(image, page, doc, 400, 400);
			centerImage(image, page, doc);
			centerText("REPORT", font, 160, page, doc);
			centerText("Who frequents the kitchen smells of smoke", font, 25, page, doc);

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

			title("Global Structure", font, 35, page, doc);

			attribute(icon_lines, 30, 30, "7360", " lines of code", font, 20, page, doc);
			attribute(icon_packages, 30, 30, "3", " packages", font, 20, page, doc);
			attribute(icon_classes, 30, 30, "114", " classes", font, 20, page, doc);
			attribute(icon_methods, 30, 30, "579", " methods", font, 20, page, doc);

			title("Global Analysis", font, 35, page, doc);

			attribute(icon_bugs, 30, 30, "533", " issues", font, 20, page, doc);
			attribute(icon_complexity, 30, 30, "3", " complexity", font, 20, page, doc);
			attribute(icon_cut_files, 30, 30, "41", " files dependencies to cut", font, 20, page, doc);
			attribute(icon_cut_directories, 30, 30, "10", " directories dependencies to cut", font, 20, page, doc);
			attribute(icon_duplicate, 30, 30, "82", " duplicate blocks", font, 20, page, doc, "4.3");

			page = initNewPage(doc);

			title("Global Quality", font, 35, page, doc);

			attribute(icon_debt, 30, 30, "7d 6h", " of technical debt", font, 20, page, doc);
			attribute(icon_comments, 30, 30, "933", " comment lines", font, 20, page, doc, "11.3");
			attribute(icon_javadoc, 30, 30, "397", " public undocumented API", font, 20, page, doc, "89.9");

			title("Global Test", font, 35, page, doc);

			attribute(icon_tests_success, 30, 30, "185", " success", font, 20, page, doc, "100");
			attribute(icon_tests_fail, 30, 30, "0", " failures and errors", font, 20, page, doc, "0");
			attribute(icon_tests_cover, 30, 30, "11721", " covered lines", font, 20, page, doc, "26");

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
				// won't happen here, as the provided value is valid
				throw new IllegalArgumentException(e);
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

		marginHeight += (titleSpaceHeight * 2);

		PDPageContentStream stream = new PDPageContentStream(doc, page, AppendMode.APPEND, false);

		float textWidth = font.getStringWidth(text) / 1000 * fontSize;
		float textHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;

		stream.beginText();
		stream.setFont(font, fontSize);

		stream.newLineAtOffset(titleMarginWidth, page.getMediaBox().getHeight() - marginHeight - textHeight);

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
		stream.moveTo(titleMarginWidth, page.getMediaBox().getHeight() - marginHeight - textHeight - 0.5f);
		stream.lineTo(titleMarginWidth + textWidth, page.getMediaBox().getHeight() - marginHeight - textHeight - 0.5f);
		stream.stroke();
		stream.close();

		marginHeight += titleSpaceHeight + textHeight;

	}

	private static void centerText(String text, PDFont font, int fontSize, PDPage page, PDDocument doc)
			throws IOException {

		PDPageContentStream stream = new PDPageContentStream(doc, page, AppendMode.APPEND, false);

		float textWidth = font.getStringWidth(text) / 1000 * fontSize;
		float textHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;

		stream.beginText();
		stream.setFont(font, fontSize);
		stream.newLineAtOffset((page.getMediaBox().getWidth() - textWidth) / 2,
				page.getMediaBox().getHeight() - marginHeight - textHeight);
		stream.showText(text);
		stream.endText();
		stream.close();

		marginHeight += textHeight;

	}

	private static void centerImage(PDImageXObject image, PDPage page, PDDocument doc) throws IOException {

		centerImage(image, page, doc, image.getHeight(), image.getWidth());

	}

	private static void centerImage(PDImageXObject image, PDPage page, PDDocument doc, int height, int width)
			throws IOException {

		PDPageContentStream stream = new PDPageContentStream(doc, page, AppendMode.APPEND, false);
		stream.drawImage(image, (page.getMediaBox().getWidth() - width) / 2,
				page.getMediaBox().getHeight() - marginHeight - height, width, height);
		stream.close();

		marginHeight += height;

	}

	private static void attribute(PDImageXObject logo, int logoHeight, int logoWidth, String data, String label,
			PDFont font, int fontSize, PDPage page, PDDocument doc, String percent) throws IOException {

		PDPageContentStream stream = new PDPageContentStream(doc, page, AppendMode.APPEND, false);

		int dataFontSize = (int) (fontSize * 1.5f);
		int labelFontSize = fontSize;

		float logoYCoordinate = page.getMediaBox().getHeight() - marginHeight - logoHeight;
		float textWidth = (font.getStringWidth(data) / 1000 * dataFontSize)
				+ (font.getStringWidth(label) / 1000 * labelFontSize);
		float textHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * dataFontSize;
		float textYCoordinate = page.getMediaBox().getHeight() - marginHeight - (logoHeight / 2) - (textHeight / 2);
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

		marginHeight += spaceHeight + ((logoHeight < textHeight) ? textHeight : logoHeight);
	}

	private static void attribute(PDImageXObject logo, int logoHeight, int logoWidth, String data, String label,
			PDFont font, int fontSize, PDPage page, PDDocument doc) throws IOException {
		attribute(logo, logoHeight, logoWidth, data, label, font, fontSize, page, doc, null);
	}

	private static PDPage initNewPage(PDDocument doc) {
		marginHeight = DEFAULT_marginHeight;
		PDPage page = new PDPage();
		doc.addPage(page);
		return page;
	}
}
