package com.chhabras.utilities;

import com.chhabras.entities.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Parser {
    public static List<Item> parse(String input) {
        List<String> mainList = convert2ListOfString(input);
        List<String> operationalList = filterList(mainList);

        List<Item> items = new ArrayList<>();

        for (String str : operationalList) {
            Item it;
            if (isPrice(str)) {
                it = findFirstItemWithoutPrice(items);
                String pr = refinePriceBeforeSaving(str);
                if (it != null) {
                    setPrice(it, pr);
                } else {
                    it = new Item(null, pr);
                    items.add(it);
                }
            } else {
                str = removeExtras(str);
                if (hasPrice(str)) {
                    String[] ar = segregate(str);
                    items.add(new Item(ar[0], ar[1]));
                } else {
                    it = findFirstItemWithoutName(items);
                    if (it != null) {
                        setName(it, str);
                    } else {
                        it = new Item(str, null);
                        items.add(it);
                    }
                }

            }
        }

        return items;
    }

    private static List<String> filterList(List<String> mainList) {
        List<String> operationalList = mainList.subList(1, endPointer(mainList));
        operationalList = operationalList.stream().filter(Parser::excludeBasedOnString).collect(Collectors.toList());
        operationalList = operationalList.stream().filter(Parser::excludeBasedONRegex).collect(Collectors.toList());
        return operationalList;
    }

    private static boolean excludeBasedONRegex(String s) {
        String regex = "\\d{1,2},\\d{1,2}";
        return !s.matches(regex);
    }

    public static boolean excludeBasedOnString(String text) {
        List<String> excludeList = new ArrayList<>();
        excludeList.add("EUR/kg");
        excludeList.add("EUR");
        excludeList.add("Kirchheim");
        excludeList.add("85551");
        excludeList.add("Frauenhoferstr");
        excludeList.add("LIDL");
        excludeList.add("Lebensmittelspezialisten");
        excludeList.add("Fraunhoferstraße");
        excludeList.add("90129219");
        excludeList.add("edeka");
        excludeList.add("EDEKA");
        excludeList.add("4 x");
        excludeList.add("2 x");
        excludeList.add("kg");
        excludeList.add("Posten");
        for (String str : excludeList) {
            if (text.contains(str)) {
                return false;
            }
        }
        return true;
    }

    private static int endPointer(List<String> mainList) {
        int i = 0;
        for (i = 0; i < mainList.size(); i++) {
            if (mainList.get(i).contains("zu zahlen") | mainList.get(i).contains("SUMME")) {
                break;
            }
        }
        return i;
    }

    private static List<String> convert2ListOfString(String string) {
        return Arrays.stream(string.split("\\r?\\n")).collect(Collectors.toList());
    }


    public static String removeExtras(String str) {
        String weight = getWeight(str);
        if (weight != null) {
            str = str.replaceAll(weight, "");
        }
        String quantity = getQuantity(str);
        if (quantity != null) {
            str = str.replaceAll(quantity, "");
        }
        return str;
    }

    public static void setName(Item item, String name) {
        item.setName(name);
    }

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


    public static boolean isPrice(String text) {
        boolean flag = false;
        if (text.startsWith("-")) {
            flag = text.matches("^(-)(.)?\\d{0,3}(\\,\\s?\\d{1,2})?\\s?\\*?(A|B|BW|A,)?\\s?");
        } else {
            flag = text.matches("^(-)?(.)?\\d{0,3}(\\,\\s?\\d{1,2})?\\s?\\*?(A|B|BW|A,)\\s?");
        }
        return flag;
    }

    public static boolean hasPrice(String text) {
        //return text.matches("(\\w{1,}\\s{1,}){1,}(-)?\\d{0,3}(\\,\\d{1,2})?\\s?(A|B)?");
        return text.matches("(.*?)(\\d{0,3},\\s?\\d{0,2})(.*?)\\s?\\*?(A|B|BW|A,)\\s?");
    }

    public static String getWeight(String text) {
        Pattern pattern = Pattern.compile("(.*?)(\\d{0,3}\\s?(KG|Kg|kg|g))(.*?)");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            return matcher.group(2).trim();
        }
        return null;
    }

    public static String getQuantity(String text) {
        Pattern pattern = Pattern.compile("(.*?)(\\d{0,3},\\d{0,2}\\s?x\\s?\\d{0,2})(.*?)");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            return matcher.group(2).trim();
        }
        return null;
    }

    public static String[] segregate(String text) {
        String[] arr = new String[2];
        Pattern pattern = Pattern.compile("(.*?)(\\d+,\\d+)(\\s?(A|B|BW))");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            arr[0] = matcher.group(1).trim();
            arr[1] = matcher.group(2).trim();
        }
        return arr;
    }
}
