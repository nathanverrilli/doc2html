package com.mythosseed.doc2html;

import java.util.logging.Logger;

class mythosStrings {

    static final Logger logger = doc2html.mainLogger;

    static final String OPTION_MONOSPACE_SHORT = "m";
    static final String OPTION_MONOSPACE_LONG = "monospace";
    static final String OPTION_LEFTALIGN_SHORT = "l";
    static final String OPTION_LEFTALIGN_LONG = "leftalign";
    static final String OPTION_DEBUG_SHORT = "d";
    static final String OPTION_DEBUG_LONG = "debug";
    static final String OPTION_FILENAME_SHORT = "f";
    static final String OPTION_FILENAME_LONG = "filename";
    static final String OPTION_OUTPUTFILE_SHORT = "o";
    static final String OPTION_OUTPUTFILE_LONG = "outputfile";
    static final String OPTION_HELP_SHORT = "h";
    static final String OPTION_HELP_LONG = "help";


    static final String SPACE = " ";
    static final String LEFTBRACKET = "<";
    static final String RIGHTBRACKET = ">";
    static final String HTMLCLOSE = "/";
    static final String NEWLINE = "\n";
    static final String startHTMLDocument = "<!doctype html>";
    static final String STYLE = " style=\"";
    static final String ENDSTYLE = "\"";

    static final String StyleAlignLeft = "text-align: left;";
    static final String StyleFontMono = "font-family: 'IBM Plex Mono', "
            + "'Cascadia Code', 'Hack', 'Consolas', 'Source Code Pro', "
            + "'Ubuntu Mono', 'Oxygen Mono', "
            + "'Roboto Mono', 'Share Tech Mono', monospace;";

    protected static String MakeStartTag(String tag) {
        return LEFTBRACKET + tag + RIGHTBRACKET;
    }

    protected static String MakeEndTag(String tag) {
        return LEFTBRACKET + HTMLCLOSE + tag + RIGHTBRACKET;
    }

    public static String MakeStartEndTag(String tag) {
        // some parsers need a space in a self-closing tag
        return LEFTBRACKET + tag + SPACE + HTMLCLOSE + RIGHTBRACKET;
    }

    protected static String MakeStyleTag(String tag, String... styles) {
        StringBuilder sb = new StringBuilder(128);
        sb.append(LEFTBRACKET)
                .append(tag)
                .append(STYLE);
        for (String style : styles) {
            sb.append(style);
        }
        sb.append(ENDSTYLE)
                .append(RIGHTBRACKET);

        return sb.toString();
    }
    
}


