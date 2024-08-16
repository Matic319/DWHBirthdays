package com.maticz.BirthdaysDWH.service.impl;

import com.maticz.BirthdaysDWH.service.PDFTextInsertionService;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Service
public class PdfTextInsertionServiceImpl implements PDFTextInsertionService {

    private static final float POINTS_PER_INCH = 72f;

    @Override
    public String locationName(Integer idLocation) {
        return switch (idLocation) {
            case 1 -> "WOOP! Fun park";
            case 2 -> "WOOP! Karting & Glow golf";
            case 3 -> "WOOP! Arena";
            case 5 -> "WOOP! Izzivi";
            case 6 -> "WOOP! Maribor";
            case 100 -> "WOOP! Test";
            default -> throw new IllegalStateException("wrong idLocation: " + idLocation);
        };
    }

    @Override
    public String locationAddress(Integer idLocation) {
        return switch (idLocation) {
            case 1 -> "Leskoškova cesta 3";
            case 2, 3 -> "Moskovska ulica 10";
            case 5 -> "rudnik";
            case 6 -> "MB";
            case 100 -> "Testni naslov 5";
            default -> throw new IllegalStateException("wrong idLocation: " + idLocation);
        };
    }

    private void insertText(PDPageContentStream contentStream, String text, float xInches, float yInches,
                            float pageHeightInPoints, float fontSize, PDDocument document) throws IOException {
        contentStream.beginText();
        PDType0Font font = PDType0Font.load(document, PDType0Font.class.getResourceAsStream("/montserrat.ttf"), true);

        contentStream.setFont(font, fontSize);
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
    public void insertTextIntoPdfInvite(String outputFilePath, String age, String dateFrom, String time, String phone, String childName, Integer idLocation) throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream("/vabilo_template.pdf")) {
            PDDocument document = Loader.loadPDF(inputStream.readAllBytes());

            PDPage page = document.getPage(0);
            PDRectangle pageSize = page.getMediaBox();
            float pageHeightInPoints = pageSize.getHeight();

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
                contentStream.setNonStrokingColor(1f, 1f, 1f);

                insertText(contentStream, age, 1.5f, 5.3f, pageHeightInPoints, 110,document);
                insertText(contentStream, dateFrom, 4.5f, 3.75f, pageHeightInPoints, 30,document);
                insertText(contentStream, time, 4.5f, 4.65f, pageHeightInPoints, 30,document);
                insertText(contentStream, locationName(idLocation), 4.5f, 5.6f, pageHeightInPoints, 30,document);
                insertText(contentStream, locationAddress(idLocation), 4.5f, 6.4f, pageHeightInPoints, 28,document);
                insertText(contentStream, convertPhoneNumber(phone), 2.8f, 8.42f, pageHeightInPoints, 30,document);

                if (childName.contains(" in ") || childName.contains("&")) {
                    insertText(contentStream, childName.split(" ")[0], 5.35f, 10.7f, pageHeightInPoints, 30,document);
                    insertText(contentStream, childName.split(" ")[1], 5.7f, 11.2f, pageHeightInPoints, 30,document);
                    insertText(contentStream, childName.split(" ")[2], 5.35f, 11.65f, pageHeightInPoints, 30,document);
                } else if (childName.length() > 14) {
                    insertText(contentStream, childName.split(" ")[0], 5.35f, 10.7f, pageHeightInPoints, 30,document);
                    insertText(contentStream, childName.split(" ")[1], 5.6f, 11.2f, pageHeightInPoints, 30,document);
                    insertText(contentStream, childName.split(" ")[2], 5.35f, 11.65f, pageHeightInPoints, 30,document);
                } else if (childName.length() < 8) {
                    insertText(contentStream, childName, 5.25f, 11.3f, pageHeightInPoints, 50,document);
                } else {
                    insertText(contentStream, childName, 5.1f, 11.3f, pageHeightInPoints, 30,document);
                }
            }

            document.save(outputFilePath);
            document.close();
        }
    }

    @Override
    public void convertPdfToJpgAndSave(String inputFilePath, String outputFilePrefix, int dpi) throws IOException {
        try (PDDocument document = Loader.loadPDF(new File(inputFilePath))) {
            PDFRenderer renderer = new PDFRenderer(document);

            for (int page = 0; page < document.getNumberOfPages(); ++page) {
                BufferedImage image = renderer.renderImageWithDPI(page, dpi);
                String outputFilePath = String.format("%s.jpg", outputFilePrefix);
                ImageIO.write(image, "jpg", new File(outputFilePath));
            }
        }
    }

    @Override
    public byte[] createAndConvertPdfToJpg(String age, String dateFrom, String time, String phone, String childName, Integer idLocation) throws IOException {
        byte[] pdfBytes = createPdfInMemory(age, dateFrom, time, phone, childName, idLocation);
        return convertPdfToJpg(pdfBytes, 600);
    }

    @Override
    public byte[] createPdfInMemory(String age, String dateFrom, String time, String phone, String childName, Integer idLocation) throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream("/vabilo_template.pdf");
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            PDDocument document = Loader.loadPDF(inputStream.readAllBytes());
            PDPage page = document.getPage(0);
            PDRectangle pageSize = page.getMediaBox();
            float pageHeightInPoints = pageSize.getHeight();

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
                contentStream.setNonStrokingColor(1f, 1f, 1f);

                insertText(contentStream, age, 1.5f, 5.3f, pageHeightInPoints, 110,document);
                insertText(contentStream, dateFrom, 4.5f, 3.75f, pageHeightInPoints, 30,document);
                insertText(contentStream, time, 4.5f, 4.65f, pageHeightInPoints, 30,document);
                insertText(contentStream, locationName(idLocation), 4.5f, 5.6f, pageHeightInPoints, 30,document);
                insertText(contentStream, locationAddress(idLocation), 4.5f, 6.4f, pageHeightInPoints, 28,document);
                insertText(contentStream, convertPhoneNumber(phone), 2.8f, 8.42f, pageHeightInPoints, 30,document);

                if (childName.contains(" in ") || childName.contains("&")) {
                    insertText(contentStream, childName.split(" ")[0], 5.35f, 10.7f, pageHeightInPoints, 30,document);
                    insertText(contentStream, childName.split(" ")[1], 5.7f, 11.2f, pageHeightInPoints, 30,document);
                    insertText(contentStream, childName.split(" ")[2], 5.35f, 11.65f, pageHeightInPoints, 30,document);
                } else if (childName.length() > 14) {
                    insertText(contentStream, childName.split(" ")[0], 5.35f, 10.7f, pageHeightInPoints, 30,document);
                    insertText(contentStream, childName.split(" ")[1], 5.6f, 11.2f, pageHeightInPoints, 30,document);
                    insertText(contentStream, childName.split(" ")[2], 5.35f, 11.65f, pageHeightInPoints, 30,document);
                } else if (childName.length() < 8) {
                    insertText(contentStream, childName, 5.25f, 11.3f, pageHeightInPoints, 50,document);
                } else {
                    insertText(contentStream, childName, 5.1f, 11.3f, pageHeightInPoints, 30,document);
                }
            }

            document.save(outputStream);
            document.close();

            return outputStream.toByteArray();
        }
    }

    public byte[] convertPdfToJpg(byte[] pdfBytes, int dpi) throws IOException {
        try (PDDocument document = Loader.loadPDF(pdfBytes);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            PDFRenderer renderer = new PDFRenderer(document);
            BufferedImage image = renderer.renderImageWithDPI(0, dpi);
            ImageIO.write(image, "jpg", outputStream);

            return outputStream.toByteArray();
        }
    }

    @Override
    public String convertPhoneNumber(String phone) {

        String phoneNumber = phone.trim().replaceAll("\\s", "");
        if (phoneNumber.startsWith("+386") || phoneNumber.startsWith("00386")) {
            phoneNumber = "0" + phoneNumber.substring(phoneNumber.length() - 8);
            phoneNumber = phoneNumber.substring(0, 3) + " " + phoneNumber.substring(3, 6) + " " + phoneNumber.substring(6);
        }
        if (!phoneNumber.startsWith("+") && !phoneNumber.startsWith("+386") && !phoneNumber.startsWith("00") && phoneNumber.length() != 9) {
            phoneNumber = "0" + phoneNumber.substring(1, 3) + " " + phoneNumber.substring(3, 6) + " " + phoneNumber.substring(6);
        } else {
            phoneNumber = phoneNumber.substring(0, 3) + " " + phoneNumber.substring(3, 6) + " " + phoneNumber.substring(6);
        }
        return phoneNumber;
    }



        @Override
        public void insertTextIntoPdfFormForBDay(String outputFilePath, String date, String time, String partyType, String childName,
                                                 String childSurname, String participantCount, String age, String phone, String partyPlace) throws IOException {
            try (InputStream inputStream = getClass().getResourceAsStream("/obrazec.pdf")) {
                PDDocument document = Loader.loadPDF(inputStream.readAllBytes());

                PDPage page = document.getPage(0);
                PDRectangle pageSize = page.getMediaBox();
                float pageHeightInPoints = pageSize.getHeight();

                try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
                   // contentStream.setNonStrokingColor(1f, 1f, 1f);

                   /* insertTextIntoBDayForm(date,startTime,endTime,partyType,
                            childName,childSurname,participantCount,contentStream, pageHeightInPoints);*/
                }

                document.save(outputFilePath);
                document.close();
            }
        }


    @Override
    public byte[] createPdfInMemoryBDayForm(String date, String starTime,String endTime,  String partyProgram, String childName, String childSurname,
                                            String participantCount, String age, String phone, String partyPlace, String minAge, String maxAge, String parentName,
                                            String comments, String animator, String partySubProgram) throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream("/obrazec.pdf");
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            PDDocument document = Loader.loadPDF(inputStream.readAllBytes());
            PDPage page = document.getPage(0);
            PDRectangle pageSize = page.getMediaBox();
            float pageHeightInPoints = pageSize.getHeight();

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {

                insertTextIntoBDayForm(date,starTime,endTime,partyProgram,childName,childSurname,participantCount,
                        age,phone,partyPlace,contentStream,pageHeightInPoints,
                        parentName,comments,minAge,maxAge,animator,partySubProgram,document);
            }

            document.save(outputStream);
            document.close();

            return outputStream.toByteArray();
        }
    }

    private void insertTextIntoBDayForm(String date, String startTime,String endTime, String partyProgram, String childName, String childSurname,
                                        String participantCount, String age, String phone, String partyPlace,
                                        PDPageContentStream contentStream, float pageHeightInPoints, String parentName,
                                        String comments, String minAge, String maxAge, String animator,String partySubProgram, PDDocument document) throws IOException {



        insertText(contentStream, date, 1.6f, 1.8f, pageHeightInPoints, 18,document);
        insertText(contentStream, startTime + " - " + endTime, 2.0f, 2.15f, pageHeightInPoints, 18,document);
        insertText(contentStream, partyProgram, 2.4f, 2.52f, pageHeightInPoints, 18,document);
        insertText(contentStream, partySubProgram, 2.4f, 2.88f, pageHeightInPoints, 18,document);
        insertText(contentStream, partyPlace, 1.95f, 3.25f, pageHeightInPoints, 18,document);
        insertText(contentStream, childName, 2.2f, 3.95f, pageHeightInPoints, 18,document);
        insertText(contentStream, childSurname, 2.5f, 4.35f, pageHeightInPoints, 18,document);

        String convertedPhoneNumber = phone != null ? convertPhoneNumber(phone) : " ";
        insertText(contentStream, convertedPhoneNumber + "  " + parentName, 2.1f, 4.7f, pageHeightInPoints, 18,document);

        insertText(contentStream, participantCount, 1.75f, 5.45f, pageHeightInPoints, 18,document);
        insertText(contentStream, age, 2.45f, 5.82f, pageHeightInPoints, 18,document);
        insertText(contentStream, minAge, 4.55f, 5.82f, pageHeightInPoints, 18,document);
        insertText(contentStream, maxAge, 7.05f, 5.82f, pageHeightInPoints, 18,document);
        insertText(contentStream, animator, 5.4f, 3.25f, pageHeightInPoints, 18,document);

        if (comments.length() > 72) {
            StringBuilder sb = new StringBuilder();
            String[] words = comments.split(" ");
            int currentLength = 0;

            for (String word : words) {
                if (currentLength + word.length() <= 72) {
                    sb.append(word).append(" ");
                    currentLength += word.length() + 1;
                } else {
                    break;
                }
            }

            insertText(contentStream, sb.toString(), 1.95f, 6.55f, pageHeightInPoints, 11, document);

            if (currentLength < comments.length()) {
                String remainingComments = comments.substring(sb.length()).trim();
                insertText(contentStream, remainingComments, 0.2f, 6.9f, pageHeightInPoints, 11, document);
            }
        } else {
            insertText(contentStream, comments, 1.95f, 6.5f, pageHeightInPoints, 18, document);
        }
    }
}



