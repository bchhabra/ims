package com.chhabras.parser;

import com.chhabras.entities.Item;

import java.util.HashMap;
import java.util.List;

public interface Parser {
    List<Item> parse(List<String> mainList);

    int startPointer(List<String> mainList);

    int endPointer(List<String> mainList);

    boolean excludeBasedOnRegex(String text);

    boolean excludeBasedOnString(String text);

    boolean validate(List<Item> items);

    HashMap<String, String> segregate(String text);
}
