//
// THIS FILE IS AUTOMATICALLY GENERATED!!
//
// Generated at 2009-02-27 by the VDM++ to JAVA Code Generator
// (v8.2b - Wed 18-Feb-2009 16:15:35)
//
// Supported compilers: jdk 1.4/1.5/1.6
//

// ***** VDMTOOLS START Name=HeaderComment KEEP=NO
// ***** VDMTOOLS END Name=HeaderComment

// ***** VDMTOOLS START Name=package KEEP=NO
package org.overturetool.umltrans;

// ***** VDMTOOLS END Name=package

//***** VDMTOOLS START Name=imports KEEP=YES

import jp.co.csk.vdm.toolbox.VDM.*;
import java.util.*;
@SuppressWarnings("unchecked")
// ***** VDMTOOLS END Name=imports



public class UmlLiteralInteger extends IUmlLiteralInteger {

// ***** VDMTOOLS START Name=vdmComp KEEP=NO
  static UTIL.VDMCompare vdmComp = new UTIL.VDMCompare();
// ***** VDMTOOLS END Name=vdmComp

// ***** VDMTOOLS START Name=ivValue KEEP=NO
  private Long ivValue = null;
// ***** VDMTOOLS END Name=ivValue


// ***** VDMTOOLS START Name=vdm_init_UmlLiteralInteger KEEP=NO
  private void vdm_init_UmlLiteralInteger () throws CGException {
    try {
      ivValue = null;
    }
    catch (Exception e){

      e.printStackTrace(System.out);
      System.out.println(e.getMessage());
    }
  }
// ***** VDMTOOLS END Name=vdm_init_UmlLiteralInteger


// ***** VDMTOOLS START Name=UmlLiteralInteger KEEP=NO
  public UmlLiteralInteger () throws CGException {
    vdm_init_UmlLiteralInteger();
  }
// ***** VDMTOOLS END Name=UmlLiteralInteger


// ***** VDMTOOLS START Name=identity KEEP=NO
  public String identity () throws CGException {
    return new String("LiteralInteger");
  }
// ***** VDMTOOLS END Name=identity


// ***** VDMTOOLS START Name=accept#1|IUmlVisitor KEEP=NO
  public void accept (final IUmlVisitor pVisitor) throws CGException {
    pVisitor.visitLiteralInteger((IUmlLiteralInteger) this);
  }
// ***** VDMTOOLS END Name=accept#1|IUmlVisitor


// ***** VDMTOOLS START Name=UmlLiteralInteger#1|Long KEEP=NO
  public UmlLiteralInteger (final Long p1) throws CGException {

    vdm_init_UmlLiteralInteger();
    setValue(p1);
  }
// ***** VDMTOOLS END Name=UmlLiteralInteger#1|Long


// ***** VDMTOOLS START Name=UmlLiteralInteger#3|Long|Long|Long KEEP=NO
  public UmlLiteralInteger (final Long p1, final Long line, final Long column) throws CGException {

    vdm_init_UmlLiteralInteger();
    {

      setValue(p1);
      setPosition(line, column);
    }
  }
// ***** VDMTOOLS END Name=UmlLiteralInteger#3|Long|Long|Long


// ***** VDMTOOLS START Name=init#1|HashMap KEEP=NO
  public void init (final HashMap data) throws CGException {

    String tmpVal_3 = null;
    tmpVal_3 = new String("value");
    String fname = null;
    fname = tmpVal_3;
    Boolean cond_4 = null;
    cond_4 = new Boolean(data.containsKey(fname));
    if (cond_4.booleanValue()) 
      setValue(UTIL.NumberToLong(data.get(fname)));
  }
// ***** VDMTOOLS END Name=init#1|HashMap


// ***** VDMTOOLS START Name=getValue KEEP=NO
  public Long getValue () throws CGException {
    return ivValue;
  }
// ***** VDMTOOLS END Name=getValue


// ***** VDMTOOLS START Name=setValue#1|Long KEEP=NO
  public void setValue (final Long parg) throws CGException {
    ivValue = UTIL.NumberToLong(UTIL.clone(parg));
  }
// ***** VDMTOOLS END Name=setValue#1|Long

}
;
