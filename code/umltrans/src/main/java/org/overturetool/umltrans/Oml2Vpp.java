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

// ***** VDMTOOLS START Name=imports KEEP=YES

import jp.co.csk.vdm.toolbox.VDM.*;



import org.overturetool.ast.itf.*;
// ***** VDMTOOLS END Name=imports



public class Oml2Vpp {

// ***** VDMTOOLS START Name=vdmComp KEEP=NO
  static UTIL.VDMCompare vdmComp = new UTIL.VDMCompare();
// ***** VDMTOOLS END Name=vdmComp


// ***** VDMTOOLS START Name=vdm_init_Oml2Vpp KEEP=NO
  private void vdm_init_Oml2Vpp () throws CGException {}
// ***** VDMTOOLS END Name=vdm_init_Oml2Vpp


// ***** VDMTOOLS START Name=Oml2Vpp KEEP=NO
  public Oml2Vpp () throws CGException {
    vdm_init_Oml2Vpp();
  }
// ***** VDMTOOLS END Name=Oml2Vpp


// ***** VDMTOOLS START Name=Save#2|String|IOmlDocument KEEP=NO
  public void Save (final String fileName, final IOmlDocument doc) throws CGException {

    Oml2VppVisitor visitor = new Oml2VppVisitor();
    visitor.visitDocument((IOmlDocument) doc);
    Util.SetFileName(fileName);
    String tmpArg_v_8 = null;
    tmpArg_v_8 = visitor.result;
    Util.PrintL(tmpArg_v_8);
  }
// ***** VDMTOOLS END Name=Save#2|String|IOmlDocument

}
;
