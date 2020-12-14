package com.chhabras.parser.impl;

import com.chhabras.entities.Item;
import com.chhabras.parser.AbstractParser;
import com.chhabras.parser.Parser;

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
    public boolean validate(List<Item> items) {
        return false;
    }

    @Override
    public boolean isPrice(String text) {
        return false;
    }

    @Override
    public boolean hasPrice(String text) {
        return false;
    }

    @Override
    public String[] segregate(String text) {
        return new String[0];
    }

    @Override
    public String getWeight(String text) {
        return null;
    }

    @Override
    public String getQuantity(String text) {
        return null;
    }

    @Override
    public String removeExtras(String text) {
        return null;
    }

    @Override
    public String refinePrice(String text) {
        return null;
    }
}
