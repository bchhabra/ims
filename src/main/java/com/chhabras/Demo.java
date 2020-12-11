package com.chhabras;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Demo {
    public static void main(String[] args) {
        String str1 = "Bio Rindergulasch 4,36 A";
        String str2 = "Bio Rindergulasch";
        String str3 = "4,36 A";
        String str4 = "-4,36A";
        String str5 = "indersuppenfleisch 3,97A";

        String regex1 = "^(-)?\\d{0,3}(\\,\\d{1,2})?\\s?(A|B)?";
        String regex2 = "(\\w{1,}\\s{1,}){1,}(-)?\\d{0,3}(\\,\\d{1,2})?\\s?(A|B)?";
        String regex3 = "(.*?)(\\d{0,3}\\,\\d{1,2})?\\s?(A|B)?";

        System.out.println(str1.matches(regex1));
        System.out.println(str2.matches(regex1));
        System.out.println(str3.matches(regex1));
        System.out.println(str4.matches(regex1));
        System.out.println(str5.matches(regex1));
        System.out.println();
        System.out.println();
        System.out.println(str1.matches(regex2));
        System.out.println(str2.matches(regex2));
        System.out.println(str3.matches(regex2));
        System.out.println(str4.matches(regex2));
        System.out.println(str5.matches(regex2));
        System.out.println();
        System.out.println();

        Pattern pattern = Pattern.compile(regex3);
        Matcher matcher = pattern.matcher(str1);

        while (matcher.find()) {
            if (matcher.group(2) != null && matcher.group(2) != "") {
                System.out.println("group 0: " + matcher.group(0));
                System.out.println("group 1: " + matcher.group(1));
                System.out.println("group 2: " + matcher.group(2));
                System.out.println("group 3: " + matcher.group(3));
            }
        }


        String line = "This order was placed for QT3000,89! OK?";


    }

    //arr[0] = String.valueOf(matcher.start());
    //  arr[1] = String.valueOf(matcher.end());
    //System.out.println(arr[0]);
}