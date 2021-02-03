package com.chhabras.parser;

import com.chhabras.entities.Item;

import java.util.HashMap;
import java.util.List;

public interface Parser {
    List<Item> parse(List<String> mainList);

    boolean validate(List<Item> items);

    HashMap<String, String> segregate(String text);
}
