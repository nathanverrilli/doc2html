package com.mythosseed.doc2html;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.TikaMetadataKeys;
import org.apache.tika.parser.ParseContext;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;


public class doc2html {
    final static Logger mainLogger = Logger.getLogger(doc2html.class.getName());
    protected static CommandLine cmd;
    private static Options opts;
    private final static String currentClassName = "doc2html";

    public static void main(String[] args) {

        setupOptions(args);
        setUpLogger();

        // print help and exit
        if (cmd.hasOption("h")) {
            printOptionHelp();
            mainLogger.log(Level.INFO, "Print out help and exit");
            System.exit(0);
        }

        String inputFile = cmd.getOptionValue("f");
        String outputFile = cmd.getOptionValue("o");

        mainLogger.fine("outputfile: [" + outputFile +
                                "] inputfile: [" + inputFile + "]");

        java.io.File inF = new java.io.File(inputFile);
        java.io.File outF = new java.io.File(outputFile);

        if (!inF.exists()) {
            mainLogger.log(Level.SEVERE, "file " + inputFile + " does not exist");
            System.exit(-2);
        }

        if (outF.exists()) {
            //noinspection ResultOfMethodCallIgnored
            outF.delete();
        }

        /* *************************
         *  open files, detect content type, create parser
         ************************* */

        java.io.BufferedInputStream bin = null;
        java.io.BufferedWriter bout = null;
        org.apache.tika.metadata.Metadata meta = null;

        try {
            bin = new java.io.BufferedInputStream(
                    new java.io.FileInputStream(inF) );

            bout = new java.io.BufferedWriter(
                    new java.io.FileWriter(outF) );

            org.apache.tika.detect.Detector detector = new org.apache.tika.detect.DefaultDetector();
            meta = new org.apache.tika.metadata.Metadata();
            meta.add(TikaMetadataKeys.RESOURCE_NAME_KEY, inF.getName());
            org.apache.tika.mime.MediaType mt = detector.detect(bin, meta);
            mainLogger.info("meta content type: [ " + mt.toString() + " ]");
            meta.add(meta.CONTENT_TYPE, mt.toString());

        } catch (java.lang.Exception e) {
            mainLogger.log(Level.SEVERE, e.toString());
            System.exit(-3);
        }

        /* *************************
         * parse
         **************************/

        org.apache.tika.parser.Parser parse =
                new org.apache.tika.parser.AutoDetectParser();

        try {
            parse.parse(bin, new StripSearch(bout), meta, new ParseContext());
        } catch (IOException | SAXException | TikaException e) {
            try {
                bin.close();
                bout.flush();
                bout.close();
            } catch (IOException e2) {
                mainLogger.severe(e2.toString());
            }
            mainLogger.severe(e.toString());
            System.exit(-1);
        }

        /* *************************
         * program cleanup
         ***************************/

        try {
            bin.close();
            bout.flush();
            bout.close();
        } catch (java.io.IOException e) {
            mainLogger.severe(e.toString());
            System.exit(-1);
        }

        System.out.println("\n");
    }

    private static void printOptionHelp() {
        HelpFormatter hf = new HelpFormatter();
        hf.printHelp("rtf2html", opts);
    }

    private static void setupOptions(String[] args) {
        opts = new Options();
        opts.addOption(Option.builder(mythosStrings.OPTION_LEFTALIGN_SHORT)
                               .desc("force paragraphs to have ragged right edge (left-aligned text)")
                               .longOpt(mythosStrings.OPTION_LEFTALIGN_LONG)
                               .hasArg(false)
                               .required(false)
                               .build());

        opts.addOption(Option.builder(mythosStrings.OPTION_MONOSPACE_SHORT)
                               .desc("use prebuilt font list for monospace fonts")
                               .longOpt(mythosStrings.OPTION_MONOSPACE_LONG)
                               .hasArg(false)
                               .required(false)
                               .build());

        opts.addOption(Option.builder(mythosStrings.OPTION_DEBUG_SHORT)
                               .required(false)
                               .hasArg(false)
                               .longOpt(mythosStrings.OPTION_DEBUG_LONG)
                               .desc("enable debug & logging")
                               .build());

        opts.addOption(Option.builder(mythosStrings.OPTION_FILENAME_SHORT)
                               .required(true)
                               .longOpt(mythosStrings.OPTION_FILENAME_LONG)
                               .hasArg(true)
                               .numberOfArgs(1)
                               .argName("inputfile")
                               .type(String.class)
                               .desc("Input filename to convert")
                               .build());

        opts.addOption(Option.builder(mythosStrings.OPTION_OUTPUTFILE_SHORT)
                               .required(true)
                               .longOpt("output")
                               .hasArg(true)
                               .numberOfArgs(1)
                               .argName(mythosStrings.OPTION_OUTPUTFILE_LONG)
                               .type(String.class)
                               .desc("Output file name")
                               .build());

        opts.addOption(Option.builder(mythosStrings.OPTION_HELP_SHORT)
                               .required(false)
                               .longOpt(mythosStrings.OPTION_HELP_LONG)
                               .hasArg(false)
                               .desc("print help & usage message")
                               .build());

        try {
            org.apache.commons.cli.DefaultParser dp =
                    new org.apache.commons.cli.DefaultParser();
            cmd = dp.parse(opts, args);
        } catch (org.apache.commons.cli.ParseException pe) {
            printOptionHelp();
            System.exit(-1);
        }
    }

    private static void setUpLogger()
    {
        boolean debug = cmd.hasOption("d") ? true : false;
        mainLogger.setLevel( debug ? Level.FINE : Level.SEVERE);
        String logFilename = currentClassName + ".log";

        try {
            if (debug) {
                FileHandler fh =
                    new FileHandler(logFilename,
                0, 1, false);
                fh.setEncoding(StandardCharsets.UTF_8.name());
                fh.setFormatter(new java.util.logging.SimpleFormatter());
                mainLogger.addHandler(fh);
                System.out.println("debug logging to " + logFilename);
            }
            ConsoleHandler ch = new ConsoleHandler();
            ch.setFormatter(new java.util.logging.SimpleFormatter());
            mainLogger.addHandler(ch);
        } catch (java.lang.Exception e) {
            System.out.println(" Logger failed! ["
                   + e.getMessage()
                   + "] "
                   + e.toString());
            System.exit(-1);
        }
    }

}
