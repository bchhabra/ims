package com.chhabras.utilities;

import com.chhabras.entities.Item;

import java.util.List;

public class PriceUtils {

    public static Item findFirstItemWithoutName(List<Item> items) {
        for (Item i : items) {
            if (i.getName() == null) {
                return i;
            }
        }
        return null;
    }

    public static void setPrice(Item item, String price) {
        item.setPrice(price);
    }

    public static String refinePriceBeforeSaving(String price) {
        if (price.endsWith(" A") | price.endsWith(" B") | price.endsWith(" BW") | price.endsWith(" A,")) {
            price = price.split("\\s")[0];
        }
        if (price.endsWith("*A") | price.endsWith("*B") | price.endsWith("*BW") | price.endsWith("*A,")) {
            price = price.split("\\*")[0];
        }
        return price;
    }

    public static Item findFirstItemWithoutPrice(List<Item> items) {
        for (Item i : items) {
            if (i.getPrice() == null) {
                return i;
            }
        }
        return null;
    }

    public static void setName(Item item, String name) {
        item.setName(name);
    }

}
