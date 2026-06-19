package com.example.mcs.converter;

import com.example.mcs.exception.ConversionException;

import java.io.ByteArrayInputStream;

import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Component;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Component
@RequiredArgsConstructor
public class ZipToPdfConverter {

    private final List<FileConverter> converters;


    public byte[] convert(byte[] input) {
        List<byte[]> pdfList = new ArrayList<>();

        try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(input), StandardCharsets.UTF_8)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.isDirectory()) continue;

                byte[] fileBytes = zis.readAllBytes();
                String extension = getExtension(entry.getName());

                FileConverter converter = converters.stream()
                        .filter(c -> c.getFormat().equalsIgnoreCase(extension))
                        .findFirst()
                        .orElseThrow(() -> new ConversionException("No converter for: " + extension, null));
                byte[] pdfBytes = converter.convert(fileBytes);
                pdfList.add(pdfBytes);
            }
        } catch (Exception e) {
            throw new ConversionException("Failed to convert ZIP", e);
        }

        return mergePdfs(pdfList);
    }

    private String getExtension(String fileName) {
        int dot = fileName.lastIndexOf('.');
        return dot == -1 ? "" : fileName.substring(dot + 1).toLowerCase();
    }

    private byte[] mergePdfs(List<byte[]> pdfList) {
        try {
            PDFMergerUtility merger = new PDFMergerUtility();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            merger.setDestinationStream(outputStream);

            for (byte[] pdfBytes : pdfList) {
                PDDocument doc = Loader.loadPDF(pdfBytes);
                merger.addSource(new ByteArrayInputStream(pdfBytes).toString());
                doc.close();
            }

            merger.mergeDocuments(null);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new ConversionException("Failed to merge PDFs", e);
        }
    }
}
