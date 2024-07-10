package com.maticz.BirthdaysDWH.service.impl;

import com.maticz.BirthdaysDWH.service.PDFTextInsertionService;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Service
public class PdfTextInsertionServiceImpl implements PDFTextInsertionService {

    private static final float POINTS_PER_INCH = 72f;


    private String locationName(Integer idLocation){
        return switch(idLocation) {
            case 1 -> "Woop! Fun park";
            case 2 -> "Woop! Karting & Glow golf";
            case 3 -> "Woop! Arena";
            case 5 -> "Woop! Izzivi";
            case 6 -> "Woop! Maribor";
            default -> throw new IllegalStateException("wrong idLocation: " + idLocation);
        };
    }

    private String locationAddress(Integer idLocation){
        return switch(idLocation) {
            case 1 -> "Leskoškova cesta 2";
            case 2, 3 -> "Moskovska ulica 10";
            case 5 -> "rudnik";
            case 6 -> "MB";
            default -> throw new IllegalStateException("wrong idLocation: " + idLocation);
        };
    }

    private void insertText(PDPageContentStream contentStream, String text, float xInches, float yInches,
                            float pageHeightInPoints, float fontSize) throws IOException {
        contentStream.beginText();
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), fontSize);
        float x = inchesToPoints(xInches);
        float y = pageHeightInPoints - inchesToPoints(yInches);
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(text);
        contentStream.endText();
    }

    private float inchesToPoints(float inches) {
        return inches * POINTS_PER_INCH;
    }

    public float[] getPageSizeInInches(String filePath) throws IOException {
        try (PDDocument document = Loader.loadPDF(new File(filePath))) {
            PDPage page = document.getPage(0);
            PDRectangle pageSize = page.getMediaBox();
            float widthInInches = pageSize.getWidth() / POINTS_PER_INCH;
            float heightInInches = pageSize.getHeight() / POINTS_PER_INCH;
            return new float[]{widthInInches, heightInInches};
        }
    }

    @Override
    public void insertTextIntoPdf(String inputFilePath, String outputFilePath, String age, String dateFrom, String time, String phone, String childName, Integer idLocation) throws IOException {

            try (PDDocument document = Loader.loadPDF(new File(inputFilePath))) {
                PDPage page = document.getPage(0);
                PDRectangle pageSize = page.getMediaBox();
                float pageHeightInPoints = pageSize.getHeight();

                try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
                    contentStream.setNonStrokingColor(1f, 1f, 1f);

                    insertText(contentStream, age, 1.25f, 5.3f, pageHeightInPoints, 110);
                    insertText(contentStream, dateFrom, 4.5f, 3.75f, pageHeightInPoints, 30);
                    insertText(contentStream, time, 4.5f, 4.65f, pageHeightInPoints, 30);
                    insertText(contentStream, locationName(idLocation), 4.5f, 5.6f, pageHeightInPoints, 30);
                    insertText(contentStream, locationAddress(idLocation), 4.5f, 6.4f, pageHeightInPoints, 28);
                    insertText(contentStream, phone , 2.8f, 8.42f, pageHeightInPoints,30);

                    if(childName.contains(" in")){

                        insertText(contentStream, childName.split(" ")[0], 5.35f,10.7f, pageHeightInPoints,30);
                        insertText(contentStream, childName.split(" ")[1], 5.7f,11.2f, pageHeightInPoints,30);
                        insertText(contentStream, childName.split(" ")[2], 5.35f,11.65f, pageHeightInPoints,30);

                    }if(childName.length() > 14){
                        insertText(contentStream, childName.split(" ")[0], 5.35f,10.7f, pageHeightInPoints,30);
                        insertText(contentStream, childName.split(" ")[1], 5.6f,11.2f, pageHeightInPoints,30);
                        insertText(contentStream, childName.split(" ")[2], 5.35f,11.65f, pageHeightInPoints,30);

                    }if(childName.length() < 8) {
                        insertText(contentStream, childName, 5.25f, 11.3f, pageHeightInPoints, 50);
                    } else {
                        insertText(contentStream, childName,5.1f,11.3f, pageHeightInPoints, 30);
                    }
                }

                document.save(outputFilePath);
            }
        }


    @Override
    public void convertPdfToJpg(String inputFilePath, String outputFilePrefix, int dpi) throws IOException {
        try (PDDocument document = Loader.loadPDF(new File(inputFilePath))) {
            PDFRenderer renderer = new PDFRenderer(document);

            for (int page = 0; page < document.getNumberOfPages(); ++page) {
                BufferedImage image = renderer.renderImageWithDPI(page, dpi);
                String outputFilePath = String.format("%s.jpg", outputFilePrefix);
                ImageIO.write(image, "jpg", new File(outputFilePath));
            }
        }
    }
}


