package edu.neu.javachip.aedfinalproject.ui;

import java.util.regex.Pattern;

public interface Validation {
    String PATTERN_USERNAME = "[\\w]+";
    String PATTERN_PASSWORD = "[\\w]+";
    String PATTERN_NAME = "^([a-zA-Z]+[\\'\\,\\.\\-]?[a-zA-Z ]*)+[ ]([a-zA-Z]+[\\'\\,\\.\\-]?[a-zA-Z ]+)+$";
    Pattern PATTERN_EMAIL = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    String PATTERN_MOBILENUMBER = "[0-9]{10}";
    String PATTERN_NUMBER = "[0-9]+";
    String PATTERN_SIMPLENAME = "^[a-zA-Z]*$";
    String PATTERN_ALPHASPACES = "^[A-Za-z0-9- ]+$";
    String ERROR_CSS_CLASS = "error";
    String PATTERN_ADDRESS = "^[#.0-9a-zA-Z\\s,-]+$";

}
