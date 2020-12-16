package com.chhabras.parser.impl;

import com.chhabras.parser.AbstractParser;

import java.util.List;

public class ReweParser extends AbstractParser {
    @Override
    public int endPointer(List<String> mainList) {
        return 0;
    }

    @Override
    public boolean excludeBasedOnRegex(String text) {
        return false;
    }

    @Override
    public boolean excludeBasedOnString(String text) {
        return false;
    }

    @Override
    public String refinePrice(String text) {
        return null;
    }
}
