/* The following code was generated by JFlex 1.4.1 on 27-10-08 11:51 */

// overture scanner implementation for jflex
// use JFLEX version 1.4.1 or later - see http://www.jflex.de

// define the scope of the (generated) Java source file
package org.overturetool.scanner.imp;

// *****************************
// *** LOCAL REQUIRED INPUTS ***
// *****************************

// import generic java definitions
import java.util.*;

// import the VDM toolbox definitions
import jp.co.csk.vdm.toolbox.VDM.*;

// import the parser token definitions
import org.overturetool.parser.imp.OvertureParserTokens;

// import lexem definition and implementation
import org.overturetool.ast.itf.IOmlLexem;
import org.overturetool.ast.imp.OmlLexem;


/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.4.1
 * on 27-10-08 11:51 from the specification file
 * <tt>scanner.l</tt>
 */
public class OvertureScanner implements OvertureParserTokens {

  /** This character denotes the end of file */
  private static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  private static final int YYINITIAL = 0;

  /** 
   * Translates characters to character classes
   */
  private static final String ZZ_CMAP_PACKED = 
    "\11\21\1\40\1\23\2\21\1\35\22\21\1\40\1\42\1\25\1\10"+
    "\1\52\1\21\1\60\1\30\1\21\1\21\1\37\1\17\1\41\1\20"+
    "\1\14\1\36\4\31\4\32\2\3\1\55\1\21\1\33\1\53\1\34"+
    "\2\21\4\2\1\15\1\2\16\7\1\6\5\7\1\56\1\4\1\57"+
    "\1\61\1\13\1\11\2\26\1\22\1\2\1\16\1\26\2\7\1\46"+
    "\1\7\1\51\1\7\1\50\1\43\1\44\2\7\1\27\1\47\1\45"+
    "\1\5\1\27\1\7\1\24\2\7\1\21\1\54\1\21\1\12\1\21"+
    "\200\21\ufef1\1\17\0";

  /** 
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\1\0\1\1\1\2\1\3\1\4\1\1\1\5\1\6"+
    "\3\1\1\7\2\1\1\10\1\11\3\1\3\2\1\1"+
    "\1\12\3\1\1\13\1\14\6\0\1\15\1\16\1\17"+
    "\1\20\1\21\2\0\1\22\6\0\1\23\1\24\1\25"+
    "\1\26\1\27\1\0\1\30\2\0\4\2\1\31\2\0"+
    "\1\32\1\33\1\34\1\0\1\35\1\36\1\37\1\40"+
    "\1\41\1\42\2\43\7\0\1\44\5\0\1\45\1\0"+
    "\1\46\1\47\3\0\1\2\1\0\2\2\1\0\1\50"+
    "\1\51\1\52\11\0\1\53\3\0\1\54\2\0\1\55"+
    "\11\0\1\56\5\0\1\57\5\0\1\60\13\0\1\61";

  private static int [] zzUnpackAction() {
    int [] result = new int[156];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\62\0\144\0\226\0\310\0\372\0\62\0\62"+
    "\0\u012c\0\u015e\0\u0190\0\62\0\u01c2\0\u01f4\0\u0226\0\u0258"+
    "\0\u028a\0\u02bc\0\u02ee\0\u0320\0\u0352\0\u0384\0\u03b6\0\u03e8"+
    "\0\u041a\0\u044c\0\u047e\0\62\0\62\0\310\0\u04b0\0\u04e2"+
    "\0\u0514\0\u0546\0\u0578\0\62\0\62\0\62\0\u05aa\0\62"+
    "\0\u01c2\0\u05dc\0\62\0\u060e\0\u0640\0\u0672\0\u06a4\0\u06d6"+
    "\0\u0708\0\62\0\u073a\0\62\0\62\0\62\0\u076c\0\62"+
    "\0\u079e\0\u02ee\0\u07d0\0\u0802\0\u0834\0\u0866\0\u0898\0\u08ca"+
    "\0\u08fc\0\62\0\62\0\u092e\0\u0960\0\62\0\u0992\0\62"+
    "\0\62\0\62\0\62\0\u09c4\0\u09f6\0\u09f6\0\u0a28\0\u0a5a"+
    "\0\u0a8c\0\u0abe\0\u0af0\0\u0b22\0\62\0\u0b54\0\u0b86\0\u0bb8"+
    "\0\u0bea\0\u0c1c\0\62\0\u0c4e\0\62\0\62\0\u0c80\0\u0cb2"+
    "\0\u0ce4\0\u0d16\0\u0d48\0\u0d7a\0\u0dac\0\u0dde\0\62\0\62"+
    "\0\62\0\u0e10\0\u0e42\0\u0e74\0\u0ea6\0\u0ed8\0\u0f0a\0\u0f3c"+
    "\0\u0f6e\0\u0fa0\0\62\0\u0fd2\0\u1004\0\u1036\0\u1068\0\u109a"+
    "\0\u10cc\0\u10fe\0\u1130\0\u1162\0\u1194\0\u11c6\0\u11f8\0\u122a"+
    "\0\u125c\0\u128e\0\u12c0\0\62\0\u12f2\0\u1324\0\u1356\0\u1388"+
    "\0\u13ba\0\62\0\u13ec\0\u141e\0\u1450\0\u1482\0\u14b4\0\62"+
    "\0\u14e6\0\u1518\0\u154a\0\u157c\0\u15ae\0\u15e0\0\u1612\0\u1644"+
    "\0\u1676\0\u16a8\0\u16da\0\62";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[156];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\2\2\3\1\4\1\5\3\3\1\6\1\7\1\10"+
    "\1\2\1\11\2\3\1\12\1\13\1\2\1\3\1\14"+
    "\1\3\1\15\2\3\1\16\2\4\1\17\1\20\1\14"+
    "\1\21\1\22\1\14\1\23\1\2\1\24\2\3\1\25"+
    "\1\3\1\26\1\3\1\27\1\30\1\31\1\32\1\2"+
    "\1\33\1\34\1\35\63\0\3\3\1\36\3\3\1\37"+
    "\2\0\1\3\1\0\2\3\3\0\1\3\1\0\1\3"+
    "\1\0\5\3\10\0\7\3\13\0\1\4\10\0\1\40"+
    "\2\41\12\0\2\4\34\0\2\42\55\0\1\3\2\0"+
    "\3\3\1\43\4\0\2\3\3\0\1\3\1\0\1\3"+
    "\1\0\2\3\13\0\7\3\20\0\1\44\70\0\1\45"+
    "\14\0\1\46\45\0\1\47\13\0\1\50\26\0\3\51"+
    "\1\52\20\51\1\53\34\51\1\0\3\54\1\55\55\54"+
    "\1\0\2\56\1\0\1\57\3\56\1\60\4\0\2\56"+
    "\1\0\1\61\1\0\1\56\1\0\1\56\1\0\2\56"+
    "\4\0\1\62\6\0\7\56\1\0\1\63\1\0\1\64"+
    "\1\65\56\0\1\66\44\0\1\47\1\67\61\0\1\70"+
    "\36\0\1\71\6\0\1\72\11\0\1\72\2\0\1\72"+
    "\22\0\3\3\1\36\3\3\1\37\2\0\1\3\1\0"+
    "\2\3\3\0\1\3\1\0\1\3\1\0\5\3\10\0"+
    "\1\3\1\73\5\3\11\0\3\3\1\36\3\3\1\37"+
    "\2\0\1\3\1\0\2\3\3\0\1\3\1\0\1\3"+
    "\1\0\5\3\10\0\1\74\3\3\1\75\2\3\11\0"+
    "\3\3\1\36\3\3\1\37\2\0\1\3\1\0\2\3"+
    "\3\0\1\3\1\0\1\3\1\0\5\3\10\0\6\3"+
    "\1\76\11\0\2\77\1\0\1\100\3\77\1\101\4\0"+
    "\2\77\3\0\1\77\1\0\1\77\1\0\2\77\13\0"+
    "\7\77\1\102\43\0\1\103\16\0\1\104\26\0\1\105"+
    "\33\0\1\106\25\0\1\107\13\0\1\110\16\0\1\111"+
    "\1\0\1\112\40\0\1\113\27\0\1\3\2\0\3\3"+
    "\5\0\2\3\3\0\1\3\1\0\1\3\1\0\2\3"+
    "\13\0\7\3\13\0\1\114\25\0\2\114\32\0\1\115"+
    "\13\0\2\116\10\0\2\115\31\0\2\117\11\0\2\117"+
    "\3\0\1\117\3\0\1\117\2\0\2\117\71\0\1\120"+
    "\17\0\23\47\1\0\11\47\1\0\24\47\4\0\1\51"+
    "\2\121\7\0\1\51\3\0\1\122\1\0\1\123\4\51"+
    "\1\124\11\0\1\51\1\0\1\51\44\0\1\125\35\0"+
    "\1\54\1\126\1\127\7\0\1\54\3\0\1\130\1\0"+
    "\1\131\4\54\1\132\11\0\1\54\1\0\1\54\15\0"+
    "\3\56\1\57\3\56\1\60\2\0\1\56\1\0\2\56"+
    "\3\0\1\56\1\0\1\56\1\0\5\56\1\0\1\133"+
    "\6\0\7\56\15\0\2\134\55\0\1\56\2\0\3\56"+
    "\5\0\2\56\3\0\1\56\1\0\1\56\1\0\2\56"+
    "\13\0\7\56\65\0\1\135\40\0\1\136\25\0\37\137"+
    "\1\140\22\137\14\0\1\141\46\0\3\3\1\36\3\3"+
    "\1\37\2\0\1\3\1\0\2\3\3\0\1\3\1\0"+
    "\1\3\1\0\5\3\10\0\2\3\1\142\4\3\11\0"+
    "\3\3\1\36\3\3\1\37\2\0\1\3\1\0\2\3"+
    "\3\0\1\3\1\143\1\3\1\0\5\3\2\0\1\143"+
    "\2\0\1\143\2\0\7\3\11\0\3\3\1\36\3\3"+
    "\1\37\2\0\1\144\1\0\2\3\3\0\1\3\1\0"+
    "\1\3\1\0\5\3\10\0\7\3\11\0\3\3\1\36"+
    "\3\3\1\37\2\0\1\145\1\0\2\3\3\0\1\3"+
    "\1\0\1\3\1\0\5\3\10\0\7\3\11\0\3\77"+
    "\1\100\3\77\1\101\2\0\1\77\1\0\2\77\3\0"+
    "\1\77\1\0\1\77\1\0\5\77\10\0\7\77\15\0"+
    "\2\146\55\0\1\77\2\0\3\77\5\0\2\77\3\0"+
    "\1\77\1\0\1\77\1\0\2\77\13\0\7\77\44\0"+
    "\1\147\61\0\1\150\61\0\1\151\30\0\1\114\11\0"+
    "\2\41\12\0\2\114\32\0\1\115\25\0\2\115\31\0"+
    "\2\152\11\0\2\152\3\0\1\152\3\0\1\152\2\0"+
    "\2\152\71\0\1\153\21\0\2\154\11\0\2\154\3\0"+
    "\1\154\3\0\1\154\2\0\2\154\27\0\23\51\1\0"+
    "\36\51\23\122\1\0\36\122\31\0\2\155\31\0\2\156"+
    "\11\0\2\156\3\0\1\156\3\0\1\156\2\0\2\156"+
    "\31\0\2\157\11\0\2\157\3\0\1\157\3\0\1\157"+
    "\2\0\2\157\27\0\23\54\1\0\36\54\23\130\1\0"+
    "\36\130\31\0\2\160\31\0\2\161\11\0\2\161\3\0"+
    "\1\161\3\0\1\161\2\0\2\161\27\0\37\137\1\162"+
    "\22\137\36\0\1\163\1\140\36\0\1\164\46\0\3\3"+
    "\1\36\3\3\1\37\2\0\1\3\1\0\2\3\3\0"+
    "\1\3\1\165\1\3\1\0\5\3\2\0\1\165\2\0"+
    "\1\165\2\0\7\3\33\0\1\143\11\0\1\143\2\0"+
    "\1\143\6\0\1\166\13\0\2\167\1\3\1\170\3\167"+
    "\1\171\2\0\1\3\1\0\2\167\3\0\1\167\1\0"+
    "\1\167\1\0\2\167\3\3\10\0\7\167\11\0\2\172"+
    "\1\3\1\173\3\172\1\174\2\0\1\3\1\0\2\172"+
    "\3\0\1\172\1\0\1\172\1\0\2\172\3\3\10\0"+
    "\7\172\12\0\2\175\11\0\2\175\3\0\1\175\3\0"+
    "\1\175\2\0\2\175\31\0\2\176\11\0\2\176\3\0"+
    "\1\176\3\0\1\176\2\0\2\176\37\0\1\177\53\0"+
    "\2\200\11\0\2\200\3\0\1\200\3\0\1\200\2\0"+
    "\2\200\60\0\2\51\31\0\2\201\11\0\2\201\3\0"+
    "\1\201\3\0\1\201\2\0\2\201\31\0\2\202\11\0"+
    "\2\202\3\0\1\202\3\0\1\202\2\0\2\202\60\0"+
    "\2\54\31\0\2\203\11\0\2\203\3\0\1\203\3\0"+
    "\1\203\2\0\2\203\27\0\36\137\1\163\1\162\22\137"+
    "\23\0\1\164\11\0\1\164\2\0\1\164\1\204\43\0"+
    "\1\165\11\0\1\165\2\0\1\165\5\0\1\205\31\0"+
    "\1\206\44\0\3\167\1\170\3\167\1\171\2\0\1\167"+
    "\1\0\2\167\3\0\1\167\1\0\1\167\1\0\5\167"+
    "\10\0\7\167\15\0\2\207\55\0\1\167\2\0\3\167"+
    "\5\0\2\167\3\0\1\167\1\0\1\167\1\0\2\167"+
    "\13\0\7\167\11\0\3\172\1\173\3\172\1\174\2\0"+
    "\1\172\1\0\2\172\3\0\1\172\1\0\1\172\1\0"+
    "\5\172\10\0\7\172\15\0\2\210\55\0\1\172\2\0"+
    "\3\172\5\0\2\172\3\0\1\172\1\0\1\172\1\0"+
    "\2\172\13\0\7\172\12\0\2\211\11\0\2\211\3\0"+
    "\1\211\3\0\1\211\2\0\2\211\31\0\2\3\11\0"+
    "\2\3\3\0\1\3\3\0\1\3\2\0\2\3\71\0"+
    "\1\212\21\0\2\213\11\0\2\213\3\0\1\213\3\0"+
    "\1\213\2\0\2\213\31\0\2\214\11\0\2\214\3\0"+
    "\1\214\3\0\1\214\2\0\2\214\31\0\2\215\11\0"+
    "\2\215\3\0\1\215\3\0\1\215\2\0\2\215\31\0"+
    "\2\216\11\0\2\216\3\0\1\216\3\0\1\216\2\0"+
    "\2\216\72\0\1\217\63\0\1\220\16\0\2\221\11\0"+
    "\2\221\3\0\1\221\3\0\1\221\2\0\2\221\31\0"+
    "\2\222\11\0\2\222\3\0\1\222\3\0\1\222\2\0"+
    "\2\222\31\0\2\223\11\0\2\223\3\0\1\223\3\0"+
    "\1\223\2\0\2\223\31\0\2\51\11\0\2\51\3\0"+
    "\1\51\3\0\1\51\2\0\2\51\31\0\2\224\11\0"+
    "\2\224\3\0\1\224\3\0\1\224\2\0\2\224\31\0"+
    "\2\54\11\0\2\54\3\0\1\54\3\0\1\54\2\0"+
    "\2\54\31\0\2\56\11\0\2\56\3\0\1\56\3\0"+
    "\1\56\2\0\2\56\52\0\1\225\11\0\1\225\2\0"+
    "\1\225\23\0\2\226\11\0\2\226\3\0\1\226\3\0"+
    "\1\226\2\0\2\226\31\0\2\227\11\0\2\227\3\0"+
    "\1\227\3\0\1\227\2\0\2\227\31\0\2\77\11\0"+
    "\2\77\3\0\1\77\3\0\1\77\2\0\2\77\31\0"+
    "\1\54\2\0\3\54\5\0\2\54\3\0\1\54\1\0"+
    "\1\54\1\0\2\54\1\125\12\0\7\54\33\0\1\225"+
    "\11\0\1\225\2\0\1\225\6\0\1\230\14\0\2\231"+
    "\11\0\2\231\3\0\1\231\3\0\1\231\2\0\2\231"+
    "\31\0\2\232\11\0\2\232\3\0\1\232\3\0\1\232"+
    "\2\0\2\232\45\0\1\233\45\0\2\167\11\0\2\167"+
    "\3\0\1\167\3\0\1\167\2\0\2\167\31\0\2\172"+
    "\11\0\2\172\3\0\1\172\3\0\1\172\2\0\2\172"+
    "\74\0\1\234\14\0";

  private static int [] zzUnpackTrans() {
    int [] result = new int[5900];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String ZZ_ERROR_MSG[] = {
    "Unkown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\1\0\1\11\4\1\2\11\3\1\1\11\17\1\2\11"+
    "\6\0\3\11\1\1\1\11\2\0\1\11\6\0\1\11"+
    "\1\1\3\11\1\0\1\11\2\0\5\1\2\0\2\11"+
    "\1\1\1\0\1\11\1\1\4\11\2\1\7\0\1\11"+
    "\5\0\1\11\1\0\2\11\3\0\1\1\1\0\2\3"+
    "\1\0\3\11\11\0\1\11\3\0\1\5\2\0\1\5"+
    "\11\0\1\11\5\0\1\11\5\0\1\11\13\0\1\11";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[156];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private char zzBuffer[] = new char[ZZ_BUFFERSIZE];

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the textposition at the last state to be included in yytext */
  private int zzPushbackPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /** number of newlines encountered up to the start of the matched text */
  private int yyline;

  /** the number of characters up to the start of the matched text */
  private int yychar;

  /**
   * the number of characters from the last newline up to the start of the 
   * matched text
   */
  private int yycolumn;

  /** 
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /** denotes if the user-EOF-code has already been executed */
  private boolean zzEOFDone;

  /* user code: */

// placeholder for the list of scanned lexems
private Vector theLexems = null;

// placeholder for the reserved word (keyword) table
static private HashMap<String,Short> keywords = null;

// initialize the reserved word table as a static constructor
static {
	keywords = new HashMap<String,Short>();
	keywords.put("#act", LEX_ACT);
	keywords.put("#active", LEX_ACTIVE);
	keywords.put("#fin", LEX_FIN);
	keywords.put("#req", LEX_REQ);
	keywords.put("#waiting", LEX_WAITING);
	keywords.put("abs", LEX_ABS);
	keywords.put("all", LEX_ALL);
	keywords.put("always", LEX_ALWAYS);
	keywords.put("and", LEX_AND);
	keywords.put("atomic", LEX_ATOMIC);
	keywords.put("async", LEX_ASYNC);
	keywords.put("be", LEX_BE);
	keywords.put("bool", LEX_BOOL);
	keywords.put("by", LEX_BY);
	keywords.put("card", LEX_CARD);
	keywords.put("cases", LEX_CASES);
	keywords.put("char", LEX_CHAR);
	keywords.put("class", LEX_CLASS);
	keywords.put("comp", LEX_COMP);
	keywords.put("compose", LEX_COMPOSE);
	keywords.put("conc", LEX_CONC);
	keywords.put("cycles", LEX_CYCLES);
	keywords.put("dcl", LEX_DCL);
	keywords.put("def", LEX_DEF);
	keywords.put("dinter", LEX_DINTER);
	keywords.put("div", LEX_ARITHMETIC_INTEGER_DIVISION);
	keywords.put("do", LEX_DO);
	keywords.put("dom", LEX_DOM);
	keywords.put("dunion", LEX_DUNION);
	keywords.put("duration", LEX_DURATION);
	keywords.put("elems", LEX_ELEMS);
	keywords.put("else", LEX_ELSE);
	keywords.put("elseif", LEX_ELSEIF);
	keywords.put("end", LEX_END);
	keywords.put("error", LEX_ERROR);
	keywords.put("errs", LEX_ERRS);
	keywords.put("exists", LEX_EXISTS);
	keywords.put("exists1", LEX_EXISTS1);
	keywords.put("exit", LEX_EXIT);
	keywords.put("ext", LEX_EXT);
	keywords.put("false", LEX_bool_false);
	keywords.put("floor", LEX_FLOOR);
	keywords.put("for", LEX_FOR);
	keywords.put("forall", LEX_FORALL);
	keywords.put("from", LEX_FROM);
	keywords.put("functions", LEX_FUNCTIONS);
	keywords.put("hd", LEX_HD);
	keywords.put("if", LEX_IF);
	keywords.put("in", LEX_IN);
	keywords.put("inds", LEX_INDS);
	keywords.put("inmap", LEX_INMAP);
	keywords.put("instance", LEX_INSTANCE);
	keywords.put("int", LEX_INT);
	keywords.put("inter", LEX_SET_INTERSECTION);
	keywords.put("inv", LEX_INV);
	keywords.put("inverse", LEX_INVERSE);
	keywords.put("iota", LEX_IOTA);
	keywords.put("is", LEX_IS);
	keywords.put("is_", LEX_IS_);
	keywords.put("isofbaseclass", LEX_ISOFBASECLASS);
	keywords.put("isofclass", LEX_ISOFCLASS);
	keywords.put("lambda", LEX_LAMBDA);
	keywords.put("len", LEX_LEN);
	keywords.put("let", LEX_LET);
	keywords.put("map", LEX_MAP);
	keywords.put("measure", LEX_MEASURE);
	keywords.put("merge", LEX_DMERGE);
	keywords.put("mk_", LEX_MK_);
	keywords.put("mod", LEX_MOD);
	keywords.put("mu", LEX_MU);
	keywords.put("munion", LEX_MAP_MERGE);
	keywords.put("mutex", LEX_MUTEX);
	keywords.put("nat", LEX_NAT);
	keywords.put("nat1", LEX_NATONE);
	keywords.put("new", LEX_NEW);
	keywords.put("nil", LEX_NIL);
	keywords.put("not", LEX_NOT);
	keywords.put("of", LEX_OF);
	keywords.put("operations", LEX_OPERATIONS);
	keywords.put("or", LEX_OR);
	keywords.put("others", LEX_OTHERS);
	keywords.put("per", LEX_PER);
	keywords.put("periodic", LEX_PERIODIC);
	keywords.put("post", LEX_POST);
	keywords.put("power", LEX_POWER);
	keywords.put("pre", LEX_PRE);
	keywords.put("pre_", LEX_PRECONDAPPLY);
	keywords.put("private", LEX_PRIVATE);
	keywords.put("protected", LEX_PROTECTED);
	keywords.put("psubset", LEX_PROPER_SUBSET);
	keywords.put("public", LEX_PUBLIC);
	keywords.put("rat", LEX_RAT);
	keywords.put("rd", LEX_RD);
	keywords.put("real", LEX_REAL);
	keywords.put("rem", LEX_REM);
	keywords.put("responsibility", LEX_RESPONSIBILITY);
	keywords.put("return", LEX_RETURN);
	keywords.put("reverse", LEX_REVERSE);
	keywords.put("rng", LEX_RNG);
	keywords.put("samebaseclass", LEX_SAMEBASECLASS);
	keywords.put("sameclass", LEX_SAMECLASS);
	keywords.put("self", LEX_SELF);
	keywords.put("seq", LEX_SEQ);
	keywords.put("seq1", LEX_SEQ1);
	keywords.put("set", LEX_SET);
	keywords.put("skip", LEX_SKIP);
	keywords.put("specified", LEX_SPECIFIED);
	keywords.put("sporadic", LEX_SPORADIC);		/* added for Marcel Verhoef */
	keywords.put("st", LEX_ST);
	keywords.put("start", LEX_START);
	keywords.put("startlist", LEX_STARTLIST);
	keywords.put("static", LEX_STATIC);
	keywords.put("subclass", LEX_SUBCLASS);
	keywords.put("subset", LEX_SUBSET);
	keywords.put("sync", LEX_SYNC);
	keywords.put("system", LEX_SYSTEM);
	keywords.put("then", LEX_THEN);
	keywords.put("thread", LEX_THREAD);
	keywords.put("threadid", LEX_THREADID);
	keywords.put("time", LEX_TIME);
	keywords.put("tixe", LEX_TIXE);
	keywords.put("tl", LEX_TL);
	keywords.put("to", LEX_TO);
	keywords.put("token", LEX_TOKEN);
	keywords.put("traces", LEX_TRACES);			/* added for Adriana Sucena */
	keywords.put("trap", LEX_TRAP);
	keywords.put("true", LEX_bool_true);
	keywords.put("types", LEX_TYPES);
	keywords.put("undefined", LEX_UNDEFINED);
	keywords.put("union", LEX_SET_UNION);
	keywords.put("values", LEX_VALUES);
	keywords.put("variables", LEX_VARIABLES);
	keywords.put("while", LEX_WHILE);
	keywords.put("with", LEX_WITH);
	keywords.put("wr", LEX_WR);
	keywords.put("yet", LEX_YET);
}

// *************************
// *** PUBLIC OPERATIONS ***
// *************************

// additional constructor - read from input buffer
public OvertureScanner (String inbuf)
{
	this(new java.io.StringReader(inbuf));
}

// the parser will call 'getNextToken'.
public OmlLexem getNextToken() throws java.io.IOException
{
	OmlLexem theLexem = yylex();
	if ( theLexem != null) theLexems.add(theLexem);
	return theLexem;
}

// the parser will initialise the sequence of lexems (from OmlDocument)
public void setLexems (Vector vec)
{
	assert (vec != null);
	theLexems = vec;
}

public int getLine()
{
	return yyline + 1;
}

public int getColumn()
{
	return yycolumn + 1;
}

// ************************************
// *** AUXILIARY PRIVATE OPERATIONS ***
// ************************************

// helper function for checking reserved words and identifiers
private OmlLexem checkIdentifier(String id) {
    Long line = new Long(yyline+1);
    Long column = new Long(yycolumn+1);
    try {
		if (keywords.containsKey(id)) {
			return new OmlLexem(line, column, new Long(keywords.get(id)), id, IOmlLexem.ILEXEMKEYWORD);
		} else {
		    //DEBUG String theText = yytext();
			//DEBUG System.out.print(theText + " = ");
			//DEBUG for (int idx=0; idx< theText.length(); idx++) System.out.format("%04x ", (int) theText.charAt(idx));
			//DEBUG System.out.println();
			return new OmlLexem(line, column, new Long(LEX_identifier), id, IOmlLexem.ILEXEMIDENTIFIER);
		}
	}
	catch (CGException cge) {
		cge.printStackTrace();
		return null;
	}
}

// helper function for default token creation
private OmlLexem defaultToken()
{
	return createToken((short) yytext().charAt(0));
}

// helper function to create a new token
private OmlLexem createToken(short lex)
{
    return createToken(lex, IOmlLexem.ILEXEMUNKNOWN);
}

private OmlLexem createToken(short lex, Long tp)
{
    Long line = new Long(yyline+1);
    Long column = new Long(yycolumn+1);
    Long lexem = new Long(lex);
    try {
		return new OmlLexem(line, column, lexem, yytext(), tp);
	}
	catch (CGException cge) {
		cge.printStackTrace();
		return null;
	}
}



  /**
   * Creates a new scanner
   * There is also a java.io.InputStream version of this constructor.
   *
   * @param   in  the java.io.Reader to read input from.
   */
  public OvertureScanner(java.io.Reader in) {
    this.zzReader = in;
  }

  /**
   * Creates a new scanner.
   * There is also java.io.Reader version of this constructor.
   *
   * @param   in  the java.io.Inputstream to read input from.
   */
  public OvertureScanner(java.io.InputStream in) {
    this(new java.io.InputStreamReader(in));
  }

  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    char [] map = new char[0x10000];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < 146) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }


  /**
   * Refills the input buffer.
   *
   * @return      <code>false</code>, iff there was new input.
   * 
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {

    /* first: make room (if you can) */
    if (zzStartRead > 0) {
      System.arraycopy(zzBuffer, zzStartRead,
                       zzBuffer, 0,
                       zzEndRead-zzStartRead);

      /* translate stored positions */
      zzEndRead-= zzStartRead;
      zzCurrentPos-= zzStartRead;
      zzMarkedPos-= zzStartRead;
      zzPushbackPos-= zzStartRead;
      zzStartRead = 0;
    }

    /* is the buffer big enough? */
    if (zzCurrentPos >= zzBuffer.length) {
      /* if not: blow it up */
      char newBuffer[] = new char[zzCurrentPos*2];
      System.arraycopy(zzBuffer, 0, newBuffer, 0, zzBuffer.length);
      zzBuffer = newBuffer;
    }

    /* finally: fill the buffer with new input */
    int numRead = zzReader.read(zzBuffer, zzEndRead,
                                            zzBuffer.length-zzEndRead);

    if (numRead < 0) {
      return true;
    }
    else {
      zzEndRead+= numRead;
      return false;
    }
  }

    
  /**
   * Closes the input stream.
   */
  private final void yyclose() throws java.io.IOException {
    zzAtEOF = true;            /* indicate end of file */
    zzEndRead = zzStartRead;  /* invalidate buffer    */

    if (zzReader != null)
      zzReader.close();
  }


  /**
   * Resets the scanner to read from a new input stream.
   * Does not close the old reader.
   *
   * All internal variables are reset, the old input stream 
   * <b>cannot</b> be reused (internal buffer is discarded and lost).
   * Lexical state is set to <tt>ZZ_INITIAL</tt>.
   *
   * @param reader   the new input stream 
   */
  private final void yyreset(java.io.Reader reader) {
    zzReader = reader;
    zzAtBOL  = true;
    zzAtEOF  = false;
    zzEndRead = zzStartRead = 0;
    zzCurrentPos = zzMarkedPos = zzPushbackPos = 0;
    yyline = yychar = yycolumn = 0;
    zzLexicalState = YYINITIAL;
  }


  /**
   * Returns the current lexical state.
   */
  private final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  private final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  private final String yytext() {
    return new String( zzBuffer, zzStartRead, zzMarkedPos-zzStartRead );
  }


  /**
   * Returns the character at position <tt>pos</tt> from the 
   * matched text. 
   * 
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch. 
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  private final char yycharat(int pos) {
    return zzBuffer[zzStartRead+pos];
  }


  /**
   * Returns the length of the matched text region.
   */
  private final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of 
   * yypushback(int) and a match-all fallback rule) this method 
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  } 


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  private void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Contains user EOF-code, which will be executed exactly once,
   * when the end of file is reached
   */
  private void zzDoEOF() throws java.io.IOException {
    if (!zzEOFDone) {
      zzEOFDone = true;
      yyclose();
    }
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private OmlLexem yylex() throws java.io.IOException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    char [] zzBufferL = zzBuffer;
    char [] zzCMapL = ZZ_CMAP;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;
    int zzPushbackPosL = zzPushbackPos = -1;
    boolean zzWasPushback;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      boolean zzR = false;
      for (zzCurrentPosL = zzStartRead; zzCurrentPosL < zzMarkedPosL;
                                                             zzCurrentPosL++) {
        switch (zzBufferL[zzCurrentPosL]) {
        case '\u000B':
        case '\u000C':
        case '\u0085':
        case '\u2028':
        case '\u2029':
          yyline++;
          yycolumn = 0;
          zzR = false;
          break;
        case '\r':
          yyline++;
          yycolumn = 0;
          zzR = true;
          break;
        case '\n':
          if (zzR)
            zzR = false;
          else {
            yyline++;
            yycolumn = 0;
          }
          break;
        default:
          zzR = false;
          yycolumn++;
        }
      }

      if (zzR) {
        // peek one character ahead if it is \n (if we have counted one line too much)
        boolean zzPeek;
        if (zzMarkedPosL < zzEndReadL)
          zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        else if (zzAtEOF)
          zzPeek = false;
        else {
          boolean eof = zzRefill();
          zzEndReadL = zzEndRead;
          zzMarkedPosL = zzMarkedPos;
          zzBufferL = zzBuffer;
          if (eof) 
            zzPeek = false;
          else 
            zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        }
        if (zzPeek) yyline--;
      }
      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;
  
      zzState = zzLexicalState;

      zzWasPushback = false;

      zzForAction: {
        while (true) {
    
          if (zzCurrentPosL < zzEndReadL)
            zzInput = zzBufferL[zzCurrentPosL++];
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            zzPushbackPos = zzPushbackPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            zzPushbackPosL = zzPushbackPos;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = zzBufferL[zzCurrentPosL++];
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMapL[zzInput] ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          int zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 2) == 2 )
            zzPushbackPosL = zzCurrentPosL;

          if ( (zzAttributes & 1) == 1 ) {
            zzWasPushback = (zzAttributes & 4) == 4;
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;
      if (zzWasPushback)
        zzMarkedPos = zzPushbackPosL;

      switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
        case 18: 
          { return createToken(LEX_text_lit);
          }
        case 50: break;
        case 41: 
          { return createToken(LEX_BAR_ARROW);
          }
        case 51: break;
        case 3: 
          { return createToken(LEX_num_lit);
          }
        case 52: break;
        case 10: 
          { return createToken(LEX_EQUAL);
          }
        case 53: break;
        case 43: 
          { if (yytext().indexOf('\n') != -1) {
											return createToken(LEX_COMMENT, OmlLexem.ILEXEMBLOCKCOMMENT);
                                          } else {
											return createToken(LEX_COMMENT, OmlLexem.ILEXEMLINECOMMENT);
                                          }
          }
        case 54: break;
        case 13: 
          { return createToken(LEX_DOTHASH);
          }
        case 55: break;
        case 45: 
          { return createToken(LEX_MK_);
          }
        case 56: break;
        case 48: 
          { return createToken(LEX_IN_SET);
          }
        case 57: break;
        case 20: 
          { return createToken(LEX_LESS_THAN_OR_EQUAL);
          }
        case 58: break;
        case 39: 
          { return createToken(LEX_LOGICAL_EQUIVALENCE);
          }
        case 59: break;
        case 19: 
          { return createToken(LEX_NOT_EQUAL);
          }
        case 60: break;
        case 21: 
          { return createToken(LEX_MAP_DOMAIN_RESTRICT_TO);
          }
        case 61: break;
        case 24: 
          { return createToken(LEX_EXP_OR_ITERATE);
          }
        case 62: break;
        case 15: 
          { return createToken(LEX_TARROW);
          }
        case 63: break;
        case 31: 
          { return createToken(LEX_MAP_RANGE_RESTRICT_TO);
          }
        case 64: break;
        case 26: 
          { return createToken(LEX_LAST_RESULT);
          }
        case 65: break;
        case 37: 
          { return createToken(LEX_quote_lit);
          }
        case 66: break;
        case 27: 
          { return createToken(LEX_IMPLY);
          }
        case 67: break;
        case 46: 
          { return createToken(LEX_RANGE_OVER);
          }
        case 68: break;
        case 47: 
          { return createToken(LEX_TEXBREAK);
          }
        case 69: break;
        case 9: 
          { return createToken(LEX_GREATER_THAN);
          }
        case 70: break;
        case 1: 
          { return defaultToken();
          }
        case 71: break;
        case 44: 
          { return createToken(LEX_IS_);
          }
        case 72: break;
        case 11: 
          { return createToken(LEX_RAISED_DOT);
          }
        case 73: break;
        case 2: 
          { return checkIdentifier(yytext());
          }
        case 74: break;
        case 16: 
          { return createToken(LEX_COMMENT, OmlLexem.ILEXEMLINECOMMENT);
          }
        case 75: break;
        case 33: 
          { return createToken(LEX_DOUBLE_COLON);
          }
        case 76: break;
        case 4: 
          { return createToken(LEX_SET_MINUS);
          }
        case 77: break;
        case 14: 
          { return createToken(LEX_MODIFY_BY);
          }
        case 78: break;
        case 34: 
          { return createToken(LEX_GENERIC_RIGHT_BRACKET);
          }
        case 79: break;
        case 32: 
          { return createToken(LEX_ASSIGN);
          }
        case 80: break;
        case 5: 
          { return createToken(LEX_PRIME);
          }
        case 81: break;
        case 30: 
          { return createToken(LEX_DONTCARE);
          }
        case 82: break;
        case 6: 
          { return createToken(LEX_HOOK);
          }
        case 83: break;
        case 38: 
          { return createToken(LEX_MAP_DOMAIN_RESTRICT_BY);
          }
        case 84: break;
        case 25: 
          { return createToken(LEX_dollar_identifier);
          }
        case 85: break;
        case 12: 
          { return createToken(LEX_SEQUENCE_CONCATENATE);
          }
        case 86: break;
        case 42: 
          { return createToken(LEX_MAP_RANGE_RESTRICT_BY);
          }
        case 87: break;
        case 23: 
          { return createToken(LEX_GREATER_THAN_OR_EQUAL);
          }
        case 88: break;
        case 35: 
          { return createToken(LEX_real_lit);
          }
        case 89: break;
        case 22: 
          { return createToken(LEX_GENERIC_LEFT_BRACKET);
          }
        case 90: break;
        case 8: 
          { return createToken(LEX_LESS_THAN);
          }
        case 91: break;
        case 17: 
          { return createToken(LEX_ARROW);
          }
        case 92: break;
        case 29: 
          { return createToken(LEX_NONDET);
          }
        case 93: break;
        case 40: 
          { return createToken(LEX_OPERATION_ARROW);
          }
        case 94: break;
        case 28: 
          { return createToken(LEX_IS_DEFINED_AS);
          }
        case 95: break;
        case 7: 
          { /* IGNORE */
          }
        case 96: break;
        case 49: 
          { return createToken(LEX_NOT_IN_SET);
          }
        case 97: break;
        case 36: 
          { return createToken(LEX_char_lit);
          }
        case 98: break;
        default: 
          if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
            zzAtEOF = true;
            zzDoEOF();
              {
                return null;
              }
          } 
          else {
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}
