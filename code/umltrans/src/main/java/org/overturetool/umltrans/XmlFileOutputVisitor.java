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
import java.util.*;
@SuppressWarnings("unused")
// ***** VDMTOOLS END Name=imports



public class XmlFileOutputVisitor extends XmlVisitor {

// ***** VDMTOOLS START Name=vdmComp KEEP=NO
  static UTIL.VDMCompare vdmComp = new UTIL.VDMCompare();
// ***** VDMTOOLS END Name=vdmComp

// ***** VDMTOOLS START Name=encoding KEEP=NO
  private String encoding = null;
// ***** VDMTOOLS END Name=encoding


// ***** VDMTOOLS START Name=vdm_init_XmlFileOutputVisitor KEEP=NO
  private void vdm_init_XmlFileOutputVisitor () throws CGException {
    try {
      encoding = new String("");
    }
    catch (Exception e){

      e.printStackTrace(System.out);
      System.out.println(e.getMessage());
    }
  }
// ***** VDMTOOLS END Name=vdm_init_XmlFileOutputVisitor


// ***** VDMTOOLS START Name=XmlFileOutputVisitor KEEP=NO
  public XmlFileOutputVisitor () throws CGException {
    vdm_init_XmlFileOutputVisitor();
  }
// ***** VDMTOOLS END Name=XmlFileOutputVisitor


// ***** VDMTOOLS START Name=setEncoding#1|String KEEP=NO
  public void setEncoding (final String encodingType) throws CGException {
    encoding = UTIL.ConvertToString(UTIL.clone(encodingType));
  }
// ***** VDMTOOLS END Name=setEncoding#1|String


// ***** VDMTOOLS START Name=print#1|String KEEP=NO
  private void print (final String text) throws CGException {
    Util.Put(text);
  }
// ***** VDMTOOLS END Name=print#1|String


// ***** VDMTOOLS START Name=printData#1|String KEEP=NO
  private void printData (final String pval) throws CGException {
    print(pval);
  }
// ***** VDMTOOLS END Name=printData#1|String


// ***** VDMTOOLS START Name=printAttribute#1|String KEEP=NO
  private void printAttribute (final String pval) throws CGException {

    String tmpArg_v_3 = null;
    String var1_4 = null;
    var1_4 = new String("\"").concat(pval);
    tmpArg_v_3 = var1_4.concat(new String("\""));
    print(tmpArg_v_3);
  }
// ***** VDMTOOLS END Name=printAttribute#1|String


// ***** VDMTOOLS START Name=XmlFileOutputVisitor#1|String KEEP=NO
  public XmlFileOutputVisitor (final String var_1_1) throws CGException {
    vdm_init_XmlFileOutputVisitor();
  }
// ***** VDMTOOLS END Name=XmlFileOutputVisitor#1|String


// ***** VDMTOOLS START Name=VisitXmlDocument#1|XmlDocument KEEP=NO
  public void VisitXmlDocument (final XmlDocument pxmld) throws CGException {

    Boolean cont = null;
    XmlEntityList obj_2 = null;
    obj_2 = pxmld.entities;
    cont = obj_2.First();
    if (new Boolean(new Long(encoding.length()).intValue() == new Long(0).intValue()).booleanValue()) 
      print(new String("<?xml version=\"1.0\"?>\n"));
    else {

      String tmpArg_v_9 = null;
      String var1_10 = null;
      var1_10 = new String("<?xml version=\"1.0\" encoding=\"").concat(encoding);
      tmpArg_v_9 = var1_10.concat(new String("\"?>\n"));
      print(tmpArg_v_9);
    }
    while ( cont.booleanValue()){

      XmlEntity ent = null;
      XmlEntityList obj_17 = null;
      obj_17 = pxmld.entities;
      ent = (XmlEntity) obj_17.getEntity();
      ent.accept((XmlVisitor) this);
      Boolean rhs_21 = null;
      XmlEntityList obj_22 = null;
      obj_22 = pxmld.entities;
      rhs_21 = obj_22.Next();
      cont = (Boolean) UTIL.clone(rhs_21);
    }
  }
// ***** VDMTOOLS END Name=VisitXmlDocument#1|XmlDocument


// ***** VDMTOOLS START Name=VisitXmlEntity#1|XmlEntity KEEP=NO
  public void VisitXmlEntity (final XmlEntity pxmle) throws CGException {

    Boolean cont = null;
    XmlAttributeList obj_2 = null;
    obj_2 = pxmle.attributes;
    cont = obj_2.First();
    String tmpArg_v_5 = null;
    String var2_7 = null;
    var2_7 = pxmle.name;
    tmpArg_v_5 = new String("<").concat(var2_7);
    print(tmpArg_v_5);
    while ( cont.booleanValue()){

      XmlAttribute attr = null;
      XmlAttributeList obj_10 = null;
      obj_10 = pxmle.attributes;
      attr = (XmlAttribute) obj_10.getAttribute();
      attr.accept((XmlVisitor) this);
      Boolean rhs_14 = null;
      XmlAttributeList obj_15 = null;
      obj_15 = pxmle.attributes;
      rhs_14 = obj_15.Next();
      cont = (Boolean) UTIL.clone(rhs_14);
    }
    Boolean cond_17 = null;
    Long var1_18 = null;
    XmlEntityList obj_19 = null;
    obj_19 = pxmle.entities;
    var1_18 = obj_19.Length();
    cond_17 = new Boolean((var1_18.intValue()) > (new Long(0).intValue()));
    if (cond_17.booleanValue()) {

      print(new String(">\n"));
      Boolean rhs_43 = null;
      XmlEntityList obj_44 = null;
      obj_44 = pxmle.entities;
      rhs_43 = obj_44.First();
      cont = (Boolean) UTIL.clone(rhs_43);
      while ( cont.booleanValue()){

        XmlEntity ent = null;
        XmlEntityList obj_47 = null;
        obj_47 = pxmle.entities;
        ent = (XmlEntity) obj_47.getEntity();
        ent.accept((XmlVisitor) this);
        Boolean rhs_51 = null;
        XmlEntityList obj_52 = null;
        obj_52 = pxmle.entities;
        rhs_51 = obj_52.Next();
        cont = (Boolean) UTIL.clone(rhs_51);
      }
      String tmpArg_v_55 = null;
      String var1_56 = null;
      String var2_58 = null;
      var2_58 = pxmle.name;
      var1_56 = new String("</").concat(var2_58);
      tmpArg_v_55 = var1_56.concat(new String(">\n"));
      print(tmpArg_v_55);
    }
    else {

      Boolean cond_35 = null;
      XmlData var1_36 = null;
      var1_36 = pxmle.data;
      cond_35 = new Boolean(UTIL.equals(var1_36, null));
      if (cond_35.booleanValue()) 
        print(new String("/>\n"));
      else {

        print(new String(">"));
        XmlData obj_25 = null;
        obj_25 = pxmle.data;
        obj_25.accept((XmlVisitor) this);
        String tmpArg_v_29 = null;
        String var1_30 = null;
        String var2_32 = null;
        var2_32 = pxmle.name;
        var1_30 = new String("</").concat(var2_32);
        tmpArg_v_29 = var1_30.concat(new String(">\n"));
        print(tmpArg_v_29);
      }
    }
  }
// ***** VDMTOOLS END Name=VisitXmlEntity#1|XmlEntity


// ***** VDMTOOLS START Name=VisitXmlAttribute#1|XmlAttribute KEEP=NO
  public void VisitXmlAttribute (final XmlAttribute pattr) throws CGException {

    String tmpArg_v_3 = null;
    String var1_4 = null;
    String var2_6 = null;
    var2_6 = pattr.name;
    var1_4 = new String(" ").concat(var2_6);
    tmpArg_v_3 = var1_4.concat(new String("="));
    print(tmpArg_v_3);
    String tmpArg_v_10 = null;
    tmpArg_v_10 = pattr.val;
    printAttribute(tmpArg_v_10);
  }
// ***** VDMTOOLS END Name=VisitXmlAttribute#1|XmlAttribute


// ***** VDMTOOLS START Name=VisitXmlData#1|XmlData KEEP=NO
  public void VisitXmlData (final XmlData pdata) throws CGException {

    String tmpArg_v_3 = null;
    tmpArg_v_3 = pdata.data;
    printData(tmpArg_v_3);
  }
// ***** VDMTOOLS END Name=VisitXmlData#1|XmlData

}
;
