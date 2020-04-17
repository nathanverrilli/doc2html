package com.mythosseed.doc2html;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

// exists to transform IOExceptions into SAXExceptions
// added peephole-optimization of an inefficient sort

class proxyWriter {
    private static final String currentClassName = "proxyWriter";

    private final List<Pair<String, String>> repList = replacementList();
    private final StringBuilder sb = new StringBuilder(20 * 1024);
    private final Writer out;
    private final Logger logger = doc2html.mainLogger;

    proxyWriter(Writer output) {
        out = output;
    }

    private static List<Pair<String, String>> replacementList() {
        /* These changes will be made in order, this property should be preserved */
        List<Pair<String, String>> newList = new ArrayList<>(10);
        /* keyhole optimizations */
        newList.add(new ImmutablePair<>("\n\n", "\n"));
        newList.add(new ImmutablePair<>("<section></section>", ""));
        newList.add(new ImmutablePair<>("<div></div>", ""));
        newList.add(new ImmutablePair<>("<b></b>", ""));
        newList.add(new ImmutablePair<>("<i></i>", ""));
        newList.add(new ImmutablePair<>("<tt></tt>", ""));
        newList.add(new ImmutablePair<>("<p></p>", "<p>&#160;</p>"));

        /* apply global style changes only after the keyhole optimizations */
        if (doc2html.cmd.hasOption(mythosStrings.OPTION_MONOSPACE_SHORT)) {
            newList.add(new ImmutablePair<>("<tt>",
                                   mythosStrings.MakeStyleTag("tt", mythosStrings.StyleFontMono)));
        }
        if (doc2html.cmd.hasOption(mythosStrings.OPTION_LEFTALIGN_SHORT)) {
            newList.add(new ImmutablePair<>("<p>",
                                   mythosStrings.MakeStyleTag("p", mythosStrings.StyleAlignLeft)) );
            newList.add(new ImmutablePair<>("<div>",
                                   mythosStrings.MakeStyleTag("p", mythosStrings.StyleAlignLeft)) );
        }
        return newList;
    }

    void flush() throws SAXException {
        final String currentMethodName = "flush";
        try {
            for (Pair<String, String> rep : repList) {
                int ix = 0;
                while (0 <= (ix = sb.indexOf(rep.getLeft(), ix) ) ) {
                    sb.replace(ix, ix + rep.getLeft().length(), rep.getRight());
                }
            }
            out.write(sb.toString());
        } catch (IOException | IndexOutOfBoundsException e) {
            String msg = currentClassName
                    + ":" + currentMethodName
                    + " critical error ";
            logger.severe(msg + " " + e.toString());
            throw new SAXException(msg, e);
        }
    }

    void writeStuff(String... args) {
        for (String s : args) {
            sb.append(s);
        }
    }

    void writeStuff(char[] ch, int start, int len) {
        sb.append(ch, start, len);
    }

}


