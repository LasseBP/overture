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



public class UmlClass extends IUmlClass {

// ***** VDMTOOLS START Name=vdmComp KEEP=NO
  static UTIL.VDMCompare vdmComp = new UTIL.VDMCompare();
// ***** VDMTOOLS END Name=vdmComp

// ***** VDMTOOLS START Name=ivName KEEP=NO
  private String ivName = null;
// ***** VDMTOOLS END Name=ivName

// ***** VDMTOOLS START Name=ivClassBody KEEP=NO
  private HashSet ivClassBody = new HashSet();
// ***** VDMTOOLS END Name=ivClassBody

// ***** VDMTOOLS START Name=ivIsAbstract KEEP=NO
  private Boolean ivIsAbstract = null;
// ***** VDMTOOLS END Name=ivIsAbstract

// ***** VDMTOOLS START Name=ivSuperClass KEEP=NO
  private Vector ivSuperClass = null;
// ***** VDMTOOLS END Name=ivSuperClass

// ***** VDMTOOLS START Name=ivVisibility KEEP=NO
  private IUmlVisibilityKind ivVisibility = null;
// ***** VDMTOOLS END Name=ivVisibility

// ***** VDMTOOLS START Name=ivIsStatic KEEP=NO
  private Boolean ivIsStatic = null;
// ***** VDMTOOLS END Name=ivIsStatic

// ***** VDMTOOLS START Name=ivIsActive KEEP=NO
  private Boolean ivIsActive = null;
// ***** VDMTOOLS END Name=ivIsActive

// ***** VDMTOOLS START Name=ivTemplatesignature KEEP=NO
  private IUmlTemplateSignature ivTemplatesignature = null;
// ***** VDMTOOLS END Name=ivTemplatesignature


// ***** VDMTOOLS START Name=vdm_init_UmlClass KEEP=NO
  private void vdm_init_UmlClass () throws CGException {
    try {

      ivName = UTIL.ConvertToString(new String());
      ivClassBody = new HashSet();
      ivIsAbstract = null;
      ivSuperClass = new Vector();
      ivVisibility = null;
      ivIsStatic = null;
      ivIsActive = null;
      ivTemplatesignature = null;
    }
    catch (Exception e){

      e.printStackTrace(System.out);
      System.out.println(e.getMessage());
    }
  }
// ***** VDMTOOLS END Name=vdm_init_UmlClass


// ***** VDMTOOLS START Name=UmlClass KEEP=NO
  public UmlClass () throws CGException {
    vdm_init_UmlClass();
  }
// ***** VDMTOOLS END Name=UmlClass


// ***** VDMTOOLS START Name=identity KEEP=NO
  public String identity () throws CGException {
    return new String("Class");
  }
// ***** VDMTOOLS END Name=identity


// ***** VDMTOOLS START Name=accept#1|IUmlVisitor KEEP=NO
  public void accept (final IUmlVisitor pVisitor) throws CGException {
    pVisitor.visitClass((IUmlClass) this);
  }
// ***** VDMTOOLS END Name=accept#1|IUmlVisitor


// ***** VDMTOOLS START Name=UmlClass#8|String|HashSet|Boolean|Vector|IUmlVisibilityKind|Boolean|Boolean|IUmlTemplateSignature KEEP=NO
  public UmlClass (final String p1, final HashSet p2, final Boolean p3, final Vector p4, final IUmlVisibilityKind p5, final Boolean p6, final Boolean p7, final IUmlTemplateSignature p8) throws CGException {

    vdm_init_UmlClass();
    {

      setName(p1);
      setClassBody(p2);
      setIsAbstract(p3);
      setSuperClass(p4);
      setVisibility((IUmlVisibilityKind) p5);
      setIsStatic(p6);
      setIsActive(p7);
      setTemplatesignature((IUmlTemplateSignature) p8);
    }
  }
// ***** VDMTOOLS END Name=UmlClass#8|String|HashSet|Boolean|Vector|IUmlVisibilityKind|Boolean|Boolean|IUmlTemplateSignature


// ***** VDMTOOLS START Name=UmlClass#10|String|HashSet|Boolean|Vector|IUmlVisibilityKind|Boolean|Boolean|IUmlTemplateSignature|Long|Long KEEP=NO
  public UmlClass (final String p1, final HashSet p2, final Boolean p3, final Vector p4, final IUmlVisibilityKind p5, final Boolean p6, final Boolean p7, final IUmlTemplateSignature p8, final Long line, final Long column) throws CGException {

    vdm_init_UmlClass();
    {

      setName(p1);
      setClassBody(p2);
      setIsAbstract(p3);
      setSuperClass(p4);
      setVisibility((IUmlVisibilityKind) p5);
      setIsStatic(p6);
      setIsActive(p7);
      setTemplatesignature((IUmlTemplateSignature) p8);
      setPosition(line, column);
    }
  }
// ***** VDMTOOLS END Name=UmlClass#10|String|HashSet|Boolean|Vector|IUmlVisibilityKind|Boolean|Boolean|IUmlTemplateSignature|Long|Long


// ***** VDMTOOLS START Name=init#1|HashMap KEEP=NO
  public void init (final HashMap data) throws CGException {

    {

      String tmpVal_3 = null;
      tmpVal_3 = new String("name");
      String fname = null;
      fname = tmpVal_3;
      Boolean cond_4 = null;
      cond_4 = new Boolean(data.containsKey(fname));
      if (cond_4.booleanValue()) 
        setName(UTIL.ConvertToString(data.get(fname)));
    }
    {

      String tmpVal_12 = null;
      tmpVal_12 = new String("classBody");
      String fname = null;
      fname = tmpVal_12;
      Boolean cond_13 = null;
      cond_13 = new Boolean(data.containsKey(fname));
      if (cond_13.booleanValue()) 
        setClassBody((HashSet) data.get(fname));
    }
    {

      String tmpVal_21 = null;
      tmpVal_21 = new String("isAbstract");
      String fname = null;
      fname = tmpVal_21;
      Boolean cond_22 = null;
      cond_22 = new Boolean(data.containsKey(fname));
      if (cond_22.booleanValue()) 
        setIsAbstract((Boolean) data.get(fname));
    }
    {

      String tmpVal_30 = null;
      tmpVal_30 = new String("superClass");
      String fname = null;
      fname = tmpVal_30;
      Boolean cond_31 = null;
      cond_31 = new Boolean(data.containsKey(fname));
      if (cond_31.booleanValue()) 
        setSuperClass((Vector) data.get(fname));
    }
    {

      String tmpVal_39 = null;
      tmpVal_39 = new String("visibility");
      String fname = null;
      fname = tmpVal_39;
      Boolean cond_40 = null;
      cond_40 = new Boolean(data.containsKey(fname));
      if (cond_40.booleanValue()) 
        setVisibility((IUmlVisibilityKind) data.get(fname));
    }
    {

      String tmpVal_48 = null;
      tmpVal_48 = new String("isStatic");
      String fname = null;
      fname = tmpVal_48;
      Boolean cond_49 = null;
      cond_49 = new Boolean(data.containsKey(fname));
      if (cond_49.booleanValue()) 
        setIsStatic((Boolean) data.get(fname));
    }
    {

      String tmpVal_57 = null;
      tmpVal_57 = new String("isActive");
      String fname = null;
      fname = tmpVal_57;
      Boolean cond_58 = null;
      cond_58 = new Boolean(data.containsKey(fname));
      if (cond_58.booleanValue()) 
        setIsActive((Boolean) data.get(fname));
    }
    {

      String tmpVal_66 = null;
      tmpVal_66 = new String("templatesignature");
      String fname = null;
      fname = tmpVal_66;
      Boolean cond_67 = null;
      cond_67 = new Boolean(data.containsKey(fname));
      if (cond_67.booleanValue()) 
        setTemplatesignature((IUmlTemplateSignature) data.get(fname));
    }
  }
// ***** VDMTOOLS END Name=init#1|HashMap


// ***** VDMTOOLS START Name=getName KEEP=NO
  public String getName () throws CGException {
    return ivName;
  }
// ***** VDMTOOLS END Name=getName


// ***** VDMTOOLS START Name=setName#1|String KEEP=NO
  public void setName (final String parg) throws CGException {
    ivName = UTIL.ConvertToString(UTIL.clone(parg));
  }
// ***** VDMTOOLS END Name=setName#1|String


// ***** VDMTOOLS START Name=getClassBody KEEP=NO
  public HashSet getClassBody () throws CGException {
    return ivClassBody;
  }
// ***** VDMTOOLS END Name=getClassBody


// ***** VDMTOOLS START Name=setClassBody#1|HashSet KEEP=NO
  public void setClassBody (final HashSet parg) throws CGException {
    ivClassBody = (HashSet) UTIL.clone(parg);
  }
// ***** VDMTOOLS END Name=setClassBody#1|HashSet


// ***** VDMTOOLS START Name=addClassBody#1|IUmlNode KEEP=NO
  public void addClassBody (final IUmlNode parg) throws CGException {
    ivClassBody.add(parg);
  }
// ***** VDMTOOLS END Name=addClassBody#1|IUmlNode


// ***** VDMTOOLS START Name=getIsAbstract KEEP=NO
  public Boolean getIsAbstract () throws CGException {
    return ivIsAbstract;
  }
// ***** VDMTOOLS END Name=getIsAbstract


// ***** VDMTOOLS START Name=setIsAbstract#1|Boolean KEEP=NO
  public void setIsAbstract (final Boolean parg) throws CGException {
    ivIsAbstract = (Boolean) UTIL.clone(parg);
  }
// ***** VDMTOOLS END Name=setIsAbstract#1|Boolean


// ***** VDMTOOLS START Name=getSuperClass KEEP=NO
  public Vector getSuperClass () throws CGException {
    return ivSuperClass;
  }
// ***** VDMTOOLS END Name=getSuperClass


// ***** VDMTOOLS START Name=setSuperClass#1|Vector KEEP=NO
  public void setSuperClass (final Vector parg) throws CGException {
    ivSuperClass = (Vector) UTIL.ConvertToList(UTIL.clone(parg));
  }
// ***** VDMTOOLS END Name=setSuperClass#1|Vector


// ***** VDMTOOLS START Name=addSuperClass#1|IUmlNode KEEP=NO
  public void addSuperClass (final IUmlNode parg) throws CGException {
    ivSuperClass.add(parg);
  }
// ***** VDMTOOLS END Name=addSuperClass#1|IUmlNode


// ***** VDMTOOLS START Name=getVisibility KEEP=NO
  public IUmlVisibilityKind getVisibility () throws CGException {
    return (IUmlVisibilityKind) ivVisibility;
  }
// ***** VDMTOOLS END Name=getVisibility


// ***** VDMTOOLS START Name=setVisibility#1|IUmlVisibilityKind KEEP=NO
  public void setVisibility (final IUmlVisibilityKind parg) throws CGException {
    ivVisibility = (IUmlVisibilityKind) UTIL.clone(parg);
  }
// ***** VDMTOOLS END Name=setVisibility#1|IUmlVisibilityKind


// ***** VDMTOOLS START Name=getIsStatic KEEP=NO
  public Boolean getIsStatic () throws CGException {
    return ivIsStatic;
  }
// ***** VDMTOOLS END Name=getIsStatic


// ***** VDMTOOLS START Name=setIsStatic#1|Boolean KEEP=NO
  public void setIsStatic (final Boolean parg) throws CGException {
    ivIsStatic = (Boolean) UTIL.clone(parg);
  }
// ***** VDMTOOLS END Name=setIsStatic#1|Boolean


// ***** VDMTOOLS START Name=getIsActive KEEP=NO
  public Boolean getIsActive () throws CGException {
    return ivIsActive;
  }
// ***** VDMTOOLS END Name=getIsActive


// ***** VDMTOOLS START Name=setIsActive#1|Boolean KEEP=NO
  public void setIsActive (final Boolean parg) throws CGException {
    ivIsActive = (Boolean) UTIL.clone(parg);
  }
// ***** VDMTOOLS END Name=setIsActive#1|Boolean


// ***** VDMTOOLS START Name=getTemplatesignature KEEP=NO
  public IUmlTemplateSignature getTemplatesignature () throws CGException {
    return (IUmlTemplateSignature) ivTemplatesignature;
  }
// ***** VDMTOOLS END Name=getTemplatesignature


// ***** VDMTOOLS START Name=hasTemplatesignature KEEP=NO
  public Boolean hasTemplatesignature () throws CGException {
    return new Boolean(!UTIL.equals(ivTemplatesignature, null));
  }
// ***** VDMTOOLS END Name=hasTemplatesignature


// ***** VDMTOOLS START Name=setTemplatesignature#1|IUmlTemplateSignature KEEP=NO
  public void setTemplatesignature (final IUmlTemplateSignature parg) throws CGException {
    ivTemplatesignature = (IUmlTemplateSignature) UTIL.clone(parg);
  }
// ***** VDMTOOLS END Name=setTemplatesignature#1|IUmlTemplateSignature

}
;
