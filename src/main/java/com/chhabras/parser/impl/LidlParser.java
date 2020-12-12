package com.chhabras.parser.impl;

import com.chhabras.entities.Item;
import com.chhabras.parser.AbstractParser;
import com.chhabras.utilities.ListUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LidlParser extends AbstractParser {

    @Override
    public boolean validate(List<Item> items) {
        return super.validate(items);
    }

    @Override
    public boolean isPrice(String text) {
        boolean flag = false;
        if (text.startsWith("-")) {
            flag = text.matches("^(-)(.)?\\d{0,3}(\\,\\s?\\d{1,2})?\\s?\\*?(A|B|BW|A,)?\\s?");
        } else {
            flag = text.matches("^(-)?(.)?\\d{0,3}(\\,\\s?\\d{1,2})?\\s?\\*?(A|B|BW|A,)\\s?");
        }
        return flag;
    }

    @Override
    public boolean hasPrice(String text) {
        return text.matches("(.*?)(\\d{0,3},\\s?\\d{0,2})(.*?)\\s?\\*?(A|B|BW|A,)\\s?");
    }

    @Override
    public String[] segregate(String text) {
        String[] arr = new String[2];
        Pattern pattern = Pattern.compile("(.*?)(\\d+,\\d+)(\\s?(A|B|BW))");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            arr[0] = matcher.group(1).trim();
            arr[1] = matcher.group(2).trim();
        }
        return arr;
    }

    @Override
    public String getWeight(String text) {
        Pattern pattern = Pattern.compile("(.*?)(\\d{0,3}\\s?(KG|Kg|kg|g))(.*?)");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            return matcher.group(2).trim();
        }
        return null;
    }

    @Override
    public String getQuantity(String text) {
        Pattern pattern = Pattern.compile("(.*?)(\\d{0,3},\\d{0,2}\\s?x\\s?\\d{0,2})(.*?)");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            return matcher.group(2).trim();
        }
        return null;
    }

    @Override
    public String removeExtras(String text) {
        String weight = getWeight(text);
        if (weight != null) {
            text = text.replaceAll(weight, "");
        }
        String quantity = getQuantity(text);
        if (quantity != null) {
            text = text.replaceAll(quantity, "");
        }
        return text;
    }

    @Override
    public String refinePrice(String text) {
        if (text.endsWith(" A") | text.endsWith(" B") | text.endsWith(" BW") | text.endsWith(" A,")) {
            text = text.split("\\s")[0];
        }
        if (text.endsWith("*A") | text.endsWith("*B") | text.endsWith("*BW") | text.endsWith("*A,")) {
            text = text.split("\\*")[0];
        }
        return text;
    }
}
