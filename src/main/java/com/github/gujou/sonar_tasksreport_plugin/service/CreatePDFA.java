/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.gujou.sonar_tasksreport_plugin.service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.transform.TransformerException;

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

/**
 * Creates a simple PDF/A document.
 */
public final class CreatePDFA {
	private CreatePDFA() {
	}

	private static int marginHeight = 50;

	private final static int marginWidth = 50;

	private final static int spaceWidth = 10;

	private final static int spaceHeight = 10;

	private final static int titleMarginWidth = 50;

	private final static int logoMarginWidth = 65;

	private final static int titleSpaceHeight = 15;

	private final static Color DARK_RED_COLOR = new Color(134, 0, 0);

	public static void main(String[] args) throws IOException, TransformerException {
		if (args.length != 3) {
			System.err.println("usage: " + CreatePDFA.class.getName() + " <output-file> <Message> <ttf-file>");
			System.exit(1);
		}

		for (String arg : args) {
			System.err.println("args: " + arg);
		}

		String file = args[0];
		String message = args[1];
		String fontfile = args[2];

		PDDocument doc = new PDDocument();
		try {
			PDPage page1 = new PDPage();

			doc.addPage(page1);

			// load the font as this needs to be embedded
			PDFont font = PDType0Font.load(doc, new File(fontfile));

			PDImageXObject image = PDImageXObject.createFromFile("/home/gujou/Pictures/belling/BELLING3_logo.png", doc);

			centerText(message, font, 100, page1, doc);
			// centerImage(image, page, doc, 400, 400);
			centerImage(image, page1, doc);
			centerText("REPORT", font, 160, page1, doc);
			centerText("Who frequents the kitchen smells of smoke", font, 25, page1, doc);

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
					.createFromFile("/home/gujou/Pictures/icon_flat/Poison-50.png", doc);
			
			PDImageXObject icon_tests_success = PDImageXObject
					.createFromFile("/home/gujou/Pictures/icon_flat/Goal-50.png", doc);
			
			PDImageXObject icon_tests_cover = PDImageXObject
					.createFromFile("/home/gujou/Pictures/icon_flat/Waning Gibbous-52.png", doc);

			PDPage page2 = new PDPage();
			PDPage page3 = new PDPage();

			doc.addPage(page2);
			doc.addPage(page3);

			// Reinit margin => todo with new page function
			marginHeight = 50;

			// attribute(icon_lines, "7360", " lines of code", font, 20, page2,
			// doc);
			//
			// attribute(icon_comments, "11 dot 3", " percent comments", font,
			// 20, page2, doc);

			title("Global Structure", font, 35, page2, doc);

			attribute(icon_lines, 30, 30, "7360", " lines of code", font, 20, page2, doc);
			attribute(icon_packages, 30, 30, "3", " packages", font, 20, page2, doc);
			attribute(icon_classes, 30, 30, "114", " classes", font, 20, page2, doc);
			attribute(icon_methods, 30, 30, "579", " methods", font, 20, page2, doc);

			title("Global Analysis", font, 35, page2, doc);

			attribute(icon_bugs, 30, 30, "533", " issues", font, 20, page2, doc);
			attribute(icon_complexity, 30, 30, "3", " complexity", font, 20, page2, doc);

			title("Global Quality", font, 35, page2, doc);

			attribute(icon_debt, 30, 30, "7d 6h", " of technical debt", font, 20, page2, doc);
			attribute(icon_comments, 30, 30, "933", " comment lines", font, 20, page2, doc, "11.3");
			attribute(icon_javadoc, 30, 30, "397", " public undocumented API", font, 20, page2, doc, "89.9");
			
			
			// Reinit margin => todo with new page function
						marginHeight = 50;
			
			title("Global Test", font, 35, page3, doc);
			
			attribute(icon_tests_success, 30, 30, "185", " success", font, 20, page3, doc, "100");
			attribute(icon_tests_fail, 30, 30, "0", " failures and errors", font, 20, page3, doc, "0");
			attribute(icon_tests_cover, 30, 30, "11721", " covered lines", font, 20, page3, doc, "26");
			

			// add XMP metadata
			XMPMetadata xmp = XMPMetadata.createXMPMetadata();

			try {
				DublinCoreSchema dc = xmp.createAndAddDublinCoreSchema();
				dc.setTitle(file);

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
			}

			// sRGB output intent
			InputStream colorProfile = CreatePDFA.class.getResourceAsStream("/usr/share/color/icc/colord/BestRGB.icc"); // /usr/share/color/icc/colord/sRGB.icc

			FileInputStream iccFile = new FileInputStream(new File("/usr/share/color/icc/colord/BestRGB.icc"));
			// PDOutputIntent intent = new PDOutputIntent(doc, colorProfile);
			PDOutputIntent intent = new PDOutputIntent(doc, iccFile);
			intent.setInfo("sRGB IEC61966-2.1");
			intent.setOutputCondition("sRGB IEC61966-2.1");
			intent.setOutputConditionIdentifier("sRGB IEC61966-2.1");
			intent.setRegistryName("http://www.color.org");
			doc.getDocumentCatalog().addOutputIntent(intent);

			doc.save(file);
		} finally {
			doc.close();
		}
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

		float logoYCoordinate = page.getMediaBox().getHeight() - marginHeight - logoHeight;
		float textWidth = font.getStringWidth(data + label) / 1000 * fontSize;
		float textHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;
		float textYCoordinate = page.getMediaBox().getHeight() - marginHeight - (logoHeight / 2) - (textHeight / 2);
		float dataXCoordinate = logoMarginWidth + logoHeight + spaceWidth;

		stream.drawImage(logo, logoMarginWidth, logoYCoordinate, logoWidth, logoHeight);
		stream.beginText();
		stream.setFont(font, fontSize);
		stream.setNonStrokingColor(DARK_RED_COLOR);
		stream.newLineAtOffset(dataXCoordinate, textYCoordinate);
		stream.showText(data);
		stream.setNonStrokingColor(Color.BLACK);
		stream.setFont(font, fontSize);
		stream.showText(label);

		if (percent != null) {
			stream.newLineAtOffset(textWidth, 4);
			stream.setFont(PDType1Font.COURIER_BOLD, fontSize - 6);
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
}
