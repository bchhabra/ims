package com.chhabras.parser.impl;

import com.chhabras.entities.Item;
import com.chhabras.parser.AbstractParser;

import java.util.ArrayList;
import java.util.List;

public class DmParser extends AbstractParser {

    @Override
    public boolean validate(List<Item> items) {
        String regex = "(.*)Vorteil Satz(.*)";
        List<Boolean> results = new ArrayList<>();
        for (Item item : items) {
            if (item.getName().matches(regex)) {
                results.add(item.getPrice().startsWith("-"));
            }
            if(item.getPrice() == null){
                results.add(false);
            }
        }
        if (results.contains(false)) {
            return false;
        } else {
            return true;
        }
    }
}
