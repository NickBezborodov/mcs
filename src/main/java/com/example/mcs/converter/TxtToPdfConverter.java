package com.example.mcs.converter;

import com.example.mcs.exception.ConversionException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

@Component
public class TxtToPdfConverter implements FileConverter {

    private static final float LEADING = 14f; // межстрочный интервал
    private static final float MARGIN_X = 50f;
    private static final float START_Y = 750f;

    @Override
    public byte[] convert(byte[] input) {
        String text = new String(input, StandardCharsets.UTF_8);
        // убираем \r полностью, дальше работаем только с \n как разделителем строк
        String[] lines = text.replace("\r", "").split("\n");

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.beginText();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                contentStream.newLineAtOffset(MARGIN_X, START_Y);

                for (String line : lines) {
                    contentStream.showText(line);
                    contentStream.newLineAtOffset(0, -LEADING);
                }

                contentStream.endText();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new ConversionException("Failed to convert TXT to PDF", e);
        }
    }

    @Override
    public String getFormat() {
        return "txt";
    }
}