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

// ***** VDMTOOLS END Name=imports



public class UmlParameterDirectionKind extends IUmlParameterDirectionKind {

// ***** VDMTOOLS START Name=vdmComp KEEP=NO
  static UTIL.VDMCompare vdmComp = new UTIL.VDMCompare();
// ***** VDMTOOLS END Name=vdmComp

// ***** VDMTOOLS START Name=val KEEP=NO
  private Long val = null;
// ***** VDMTOOLS END Name=val


// ***** VDMTOOLS START Name=vdm_init_UmlParameterDirectionKind KEEP=NO
  private void vdm_init_UmlParameterDirectionKind () throws CGException {
    try {
      val = null;
    }
    catch (Exception e){

      e.printStackTrace(System.out);
      System.out.println(e.getMessage());
    }
  }
// ***** VDMTOOLS END Name=vdm_init_UmlParameterDirectionKind


// ***** VDMTOOLS START Name=UmlParameterDirectionKind KEEP=NO
  public UmlParameterDirectionKind () throws CGException {
    vdm_init_UmlParameterDirectionKind();
  }
// ***** VDMTOOLS END Name=UmlParameterDirectionKind


// ***** VDMTOOLS START Name=identity KEEP=NO
  public String identity () throws CGException {
    return new String("ParameterDirectionKind");
  }
// ***** VDMTOOLS END Name=identity


// ***** VDMTOOLS START Name=accept#1|IUmlVisitor KEEP=NO
  public void accept (final IUmlVisitor pVisitor) throws CGException {
    pVisitor.visitParameterDirectionKind((IUmlParameterDirectionKind) this);
  }
// ***** VDMTOOLS END Name=accept#1|IUmlVisitor


// ***** VDMTOOLS START Name=UmlParameterDirectionKind#1|Long KEEP=NO
  public UmlParameterDirectionKind (final Long pv) throws CGException {

    vdm_init_UmlParameterDirectionKind();
    setValue(pv);
  }
// ***** VDMTOOLS END Name=UmlParameterDirectionKind#1|Long


// ***** VDMTOOLS START Name=UmlParameterDirectionKind#3|Long|Long|Long KEEP=NO
  public UmlParameterDirectionKind (final Long pv, final Long pline, final Long pcolumn) throws CGException {

    vdm_init_UmlParameterDirectionKind();
    {

      setValue(pv);
      setPosition(pline, pcolumn);
    }
  }
// ***** VDMTOOLS END Name=UmlParameterDirectionKind#3|Long|Long|Long


// ***** VDMTOOLS START Name=setValue#1|Long KEEP=NO
  public void setValue (final Long pval) throws CGException {
    val = UTIL.NumberToLong(UTIL.clone(pval));
  }
// ***** VDMTOOLS END Name=setValue#1|Long


// ***** VDMTOOLS START Name=getValue KEEP=NO
  public Long getValue () throws CGException {
    return val;
  }
// ***** VDMTOOLS END Name=getValue


// ***** VDMTOOLS START Name=getStringValue KEEP=NO
  public String getStringValue () throws CGException {

    String rexpr_1 = null;
    rexpr_1 = UmlParameterDirectionKindQuotes.getQuoteName(val);
    return rexpr_1;
  }
// ***** VDMTOOLS END Name=getStringValue

}
;
