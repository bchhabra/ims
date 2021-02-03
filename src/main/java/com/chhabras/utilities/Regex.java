package com.chhabras.utilities;

import java.util.regex.Pattern;

public final class Regex {

    public static final String postalcode = "^\\d{5}.*";
    public static final String phone1 = "Tel(\\.|:|\\s)?\\s?089.*";
    public static final String phone2 = "\\d{5}/\\d{7}"; //08121/2235917
    public static final String multiplier = "\\d{1,2} x";
    public static final String date = "\\d{2}\\.d{2}\\.d{4}";

    public static final String x = "\\s*x\\s*"; // x
    public static final String price = "-?\\s?\\d{1,2}(,|.)\\s*\\d{2}+"; //3, 99
    public static final String price_only = "\\d{1,2}\\s*(,|\\.)\\s*\\d{2}";
    public static final String eurperkg = "\\s*EUR\\s*/\\s*kg\\s*";
    public static final String quantity1 = "\\d{1,2}(\\s)Stk"; //2 Stk
    public static final String weight = "(\\d{1,3}\\s*(,|\\.)?\\s*(\\d{2,3})?\\s*(kg|KG|Kg|g))"; //100g 1kg 1,45 kg

    public static final String description = "^(([a-zA-ZäöüÄÖÜß0-9&\\.]*(\\d,\\d{2})?(\\.|\\s)*)+ )";
    public static final String price_x_quantity = "("+ price_only+x+"\\d{1,2} )";

    public static final String refine_price0 = "(-?" + price + ")" + x + "\\d+\\s*" + "(" + price + ")";
    public static final String refine_price1 = "(-?" + price + ")\\s*(B|BW|A|AW|A,|1|2)*";
    public static final String refine_price2 = "^" + refine_price1;
    public static final String refine_price3 = "(-)?(\\.)?" + price + "(\\*)(B|BW|A|AW|A,)";


    public static final Pattern pattern_d = Pattern.compile("^([a-zA-ZäöüÄÖÜß&,-\\.\\s]*)\\s?\\d");
    public static final Pattern pattern_p = Pattern.compile("(\\d+((\\.|,)\\d+))(\\s+\\w*)*(A|B|AB|AW)");
    public static final Pattern pattern_pz = Pattern.compile("(\\d+((\\.|,)\\d+))");

}
