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



public class UmlCharType extends IUmlCharType {

// ***** VDMTOOLS START Name=vdmComp KEEP=NO
  static UTIL.VDMCompare vdmComp = new UTIL.VDMCompare();
// ***** VDMTOOLS END Name=vdmComp


// ***** VDMTOOLS START Name=vdm_init_UmlCharType KEEP=NO
  private void vdm_init_UmlCharType () throws CGException {}
// ***** VDMTOOLS END Name=vdm_init_UmlCharType


// ***** VDMTOOLS START Name=identity KEEP=NO
  public String identity () throws CGException {
    return new String("CharType");
  }
// ***** VDMTOOLS END Name=identity


// ***** VDMTOOLS START Name=accept#1|IUmlVisitor KEEP=NO
  public void accept (final IUmlVisitor pVisitor) throws CGException {
    pVisitor.visitCharType((IUmlCharType) this);
  }
// ***** VDMTOOLS END Name=accept#1|IUmlVisitor


// ***** VDMTOOLS START Name=UmlCharType KEEP=NO
  public UmlCharType () throws CGException {
    vdm_init_UmlCharType();
  }
// ***** VDMTOOLS END Name=UmlCharType


// ***** VDMTOOLS START Name=UmlCharType#2|Long|Long KEEP=NO
  public UmlCharType (final Long line, final Long column) throws CGException {

    vdm_init_UmlCharType();
    setPosition(line, column);
  }
// ***** VDMTOOLS END Name=UmlCharType#2|Long|Long


// ***** VDMTOOLS START Name=init#1|HashMap KEEP=NO
  public void init (final HashMap var_1_1) throws CGException {}
// ***** VDMTOOLS END Name=init#1|HashMap

}
;
