package com.maticz.BirthdaysDWH.service;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class PdfTextInsertionService {


        private static final float POINTS_PER_INCH = 72f;

        public void insertTextIntoPdf(String inputFilePath, String outputFilePath, String age,
                                      String dateFrom, String time, String location, String address,
                                      String phone) throws IOException {
            try (PDDocument document = Loader.loadPDF(new File(inputFilePath))) {
                PDPage page = document.getPage(0);
                PDRectangle pageSize = page.getMediaBox();
                float pageHeightInPoints = pageSize.getHeight();

                try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
                    contentStream.setNonStrokingColor(1f, 1f, 1f); // Set text color to white

                    insertText(contentStream, age, 1.25f, 5.3f, pageHeightInPoints, 110);

                    insertText(contentStream, dateFrom, 4.5f, 3.75f, pageHeightInPoints, 30);

                    insertText(contentStream, time, 4.5f, 4.65f, pageHeightInPoints, 30);

                    insertText(contentStream, location, 4.5f, 5.6f, pageHeightInPoints, 30);

                    insertText(contentStream, address, 4.5f, 6.4f, pageHeightInPoints, 28);

                    insertText(contentStream, phone , 2.8f, 8.42f, pageHeightInPoints,30);
                }

                document.save(outputFilePath);
            }
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

}

