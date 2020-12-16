package com.chhabras.parser;

import com.chhabras.entities.Item;

import java.util.List;

public interface Parser {
        List<Item> parse(String input);
        int endPointer(List<String> mainList);
        boolean excludeBasedOnRegex(String text);
        boolean excludeBasedOnString(String text);
        boolean validate(List<Item> items);
        /*
        hasPrice and segregate should work together.
        hasPrice will be used to check if string contains product and price both and later segregate them
         */
        boolean hasPrice(String text);
        String[] segregate(String text);
        String getWeight(String text);
        String getQuantity(String text);
        String removeWeightandQuantity(String text);
        String refinePrice(String text);
}
