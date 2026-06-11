package com.example.mcs.converter;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
public class JpgToPdfConverter implements FileConverter {
    @Override
    public byte[] convert(byte[] input){
    try(PDDocument document = new PDDocument()){

        PDImageXObject pdImage = PDImageXObject.createFromByteArray(document, input, "");
        PDRectangle pageSize = new PDRectangle(pdImage.getWidth(), pdImage.getHeight());
        PDPage page = new PDPage(pageSize);
        document.addPage(page);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            contentStream.drawImage(pdImage, 0, 0, pdImage.getWidth(), pdImage.getHeight());
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        document.save(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    } catch(IOException e){
        throw  new RuntimeException("Failed to convert", e);
    }
}

    @Override
    public String getFormat() {
        return "jpg";
    }
}
