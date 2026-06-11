package com.example.mcs.converter;

public interface FileConverter {
    byte[] convert(byte[] input);
    String getFormat();
}
