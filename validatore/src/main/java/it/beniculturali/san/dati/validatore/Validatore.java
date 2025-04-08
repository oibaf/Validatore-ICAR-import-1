package it.beniculturali.san.dati.validatore;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.ext.DefaultHandler2;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 https://xerces.apache.org/xerces2-j/faq-xs.html
 */
public class Validatore {
  static void uso() {
    System.err.println("uso: Validatore [-v] [-out] [-1.0] <input xml> [<schema xsd> ...]");
    System.exit(-1);
  }

  static class veh extends DefaultHandler {
    final ArrayList<SAXParseException> val$errorList;

    veh(ArrayList<SAXParseException> el) {
      this.val$errorList = el;
    }

    public void error(SAXParseException e) throws SAXException {
      this.val$errorList.add(e);
    }

    public void fatalError(SAXParseException e) throws SAXException {
      this.val$errorList.add(e);
    }
    //public void warning(SAXParseException e) throws SAXException {System.err.println(e);}
  }

  static class sfe extends DefaultHandler {
    //public void error(SAXParseException e) throws SAXException {System.err.println("error " + e);}
    //public void fatalError(SAXParseException e) throws SAXException {System.err.println("fatal " + e);}
    public void warning(SAXParseException e) throws SAXException {
      System.err.println("warning: " + e);
    }
  }

  static void SPEM(SAXParseException e) { //System.err.println(e);
    System.err.println("line " + e.getLineNumber() + ", column " + e.getColumnNumber() + ": " + e.getMessage());
  }

  public static void main(String[] args) throws IOException, SAXException {
    boolean out = false, one = false, verbose = false;
    int pi = 0;
    //if (args[0].compareTo("-out")==0) { out = true; pi = 1; } if (args.length<2) uso();
    for (int j = 0; j < args.length; j++) {
      if (args[j].substring(0, 1).compareTo("-") != 0) {
        pi = j;
        break;
      }
      if (args[j].compareTo("-out") == 0) {
        out = true;
      } else if (args[j].compareTo("-1.0") == 0) {
        one = true;
      } else if (args[j].compareTo("-v") == 0) {
        verbose = true;
      } else
        uso();
    }
    if (args.length - pi < 1) uso();
    try {
      StreamSource instanceDocument = new StreamSource(args[pi])/* created by your application */;
      List<StreamSource> sd = new ArrayList<StreamSource>();
      for (int j = pi + 1; j < args.length; j++)
        sd.add(new StreamSource(args[j]));
      String schemaLanguage = "http://www.w3.org/XML/XMLSchema" + (one ? "" : "/v1.1");
      if (!out && one) System.out.println(schemaLanguage);
      //Validator v = SchemaFactory.newInstance(schemaLanguage).newSchema(sd.toArray(new StreamSource[0])).newValidator();
      SchemaFactory schemaFactory = SchemaFactory.newInstance(schemaLanguage);
      schemaFactory.setErrorHandler(new sfe());
      Validator v = schemaFactory.newSchema(sd.toArray(new StreamSource[0])).newValidator();

      ArrayList<SAXParseException> spel = new ArrayList<SAXParseException>();
      if (verbose) v.setErrorHandler(new veh(spel));

      System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8.name()));
      v.validate(instanceDocument, out ? new StreamResult(System.out) : null);//v.validate(instanceDocument, new StreamResult(System.out));
      //System.out.println("success");

      if (verbose) {
        if (spel.isEmpty()) {
          System.out.println("La validazione è stata completata con successo.");
        } else {
          System.err.println("Sono stati trovati i seguenti errori:");
          Iterator it = spel.iterator();
          while (it.hasNext()) {
            SPEM((SAXParseException) it.next());
          }
          System.exit(-1);
        }
      }
    } catch (SAXParseException e) {
      SPEM(e);
      System.exit(-1);
    } catch (Exception e) {
      System.err.println(e);
      System.exit(-1);
    }
  }
}