package com.example.library.mapper;

public abstract class CommonMapper {

    //this logic added for testing purpose
    static String modifyStringForTests(String s) {
        return String.format("%s - %d", s, s.length());
    }
}
