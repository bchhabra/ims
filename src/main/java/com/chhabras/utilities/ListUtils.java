package com.chhabras.utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ListUtils {

    public static List<String> convert2ListOfString(String string) {
        return Arrays.stream(string.split("\\r?\\n")).collect(Collectors.toList());
    }

    public static List<String> filterList(List<String> mainList) {
        int end = endPointer(mainList);
        List<String> operationalList = mainList;
        if(end > 0){
            operationalList = mainList.subList(1, end);
        }
        operationalList = operationalList.stream().filter(ListUtils::excludeBasedOnString).collect(Collectors.toList());
        operationalList = operationalList.stream().filter(ListUtils::excludeBasedONRegex).collect(Collectors.toList());
        return operationalList;
    }

    private static int endPointer(List<String> mainList) {
        int i = 0;
        for (i = 0; i < mainList.size(); i++) {
            if (mainList.get(i).contains("zu zahlen") | mainList.get(i).contains("MwStd")) {
                break;
            }
        }
        return i;
    }
    public static boolean excludeBasedONRegex(String s) {
        String regex = "\\d{1,2}(,|.)\\d{1,2}|\\d x";
        return !s.matches(regex);
    }

    public static boolean excludeBasedOnString(String text) {
        List<String> excludeList = new ArrayList<>();
        excludeList.add("EUR/kg");
        excludeList.add("Tel: 089");
        excludeList.add("EUR");
        excludeList.add("Kirchheim");
        excludeList.add("85551");
        excludeList.add("Frauenhoferstr");
        excludeList.add("LIDL");
        excludeList.add("Lebensmittelspezialisten");
        excludeList.add("Lebensmitelspeciailstent");
        excludeList.add("Fraunhoferstraße");
        excludeList.add("90129219");
        excludeList.add("edeka");
        excludeList.add("EDEKA");
        excludeList.add("kg");
        excludeList.add("Posten");
        for (String str : excludeList) {
            if (text.contains(str)) {
                return false;
            }
        }
        return true;
    }
}
