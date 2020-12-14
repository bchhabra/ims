package com.chhabras.parser;

import com.chhabras.entities.Item;

import java.util.List;

public interface Parser {
        List<Item> parse(String input);
        int endPointer(List<String> mainList);
        default boolean excludeBasedOnRegex(String text){
                return false;
        }
        default boolean excludeBasedOnString(String text){
                return false;
        }
        boolean validate(List<Item> items);
        boolean isPrice(String text);
        boolean hasPrice(String text);
        String[] segregate(String text);
        String getWeight(String text);
        String getQuantity(String text);
        String removeExtras(String text);
        String refinePrice(String text);
}
