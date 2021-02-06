package com.chhabras.parser.impl;

import com.chhabras.entities.Item;
import com.chhabras.parser.AbstractParser;
import com.chhabras.utilities.PriceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;

import static com.chhabras.utilities.Regex.pattern_p;

public class BonusParser extends AbstractParser {

    @Override
    public List<Item> parse(List<String> mainList) {
        printList(mainList);
        List<Item> items = new ArrayList<>();
        System.out.println("########### Items ###########");

        //TODO get rid of map returning from segregate
        Iterator<String> lineIterator = mainList.iterator();
        while (lineIterator.hasNext()) {
            Item it;
            String description = "";
            String price;
            HashMap<String, String> map;

            map = segregate(lineIterator.next());
            if (map != null) {
                if (map.containsKey("description") && map.containsKey("price")) {
                    description = map.get("description");
                    price = map.get("price");
                    addToItem(items, description, price);
                } else if (map.containsKey("description") && !map.containsKey("price")) {
                    description = map.get("description");
                    price = "";
                    while (price.isEmpty()) {
                        price = getPrice(lineIterator.next());
                    }
                    addToItem(items, description, price);
                }
                if(description.toLowerCase().contains("summe") || description.toLowerCase().contains("zahlen")){
                    break;
                }
            }
        }
        for (Item i : items) {
            System.out.println(i.getName() + " : " + i.getPrice());
        }
        return items;
    }

    private void addToItem(List<Item> items, String description, String price) {
        Item it;
        if (price != null && !price.isEmpty()) {
            price = price.replaceAll(" ", "");
            it = PriceUtils.findFirstItemWithoutPrice(items);
            if (it != null) {
                it.setPrice(price);
            } else {
                it = new Item(null, price);
                items.add(it);
            }
        }
        if (description != null && !description.isEmpty()) {
            it = PriceUtils.findFirstItemWithoutName(items);
            if (it != null) {
                it.setName(description);
            } else {
                it = new Item(description, null);
                items.add(it);
            }
        }
    }

    private String getPrice(String text) {
        Matcher matcher_p = pattern_p.matcher(text);
        if (matcher_p.find() && matcher_p.group(1) != null) {
             return matcher_p.group(1);
        }
        return "";
    }

}
