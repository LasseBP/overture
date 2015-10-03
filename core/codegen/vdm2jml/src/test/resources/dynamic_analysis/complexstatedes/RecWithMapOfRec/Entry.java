package project;

import org.overture.codegen.runtime.*;

import java.util.*;


//@ nullable_by_default
@SuppressWarnings("all")
final public class Entry {
    /*@ public ghost static boolean invChecksOn = true; @*/
    private Entry() {
    }

    public static Object Run() {
        IO.println("Before useOk");

        {
            Number ignorePattern_1 = useOk();

            //@ assert ignorePattern_1 != null;

            /* skip */
        }

        IO.println("After useOk");
        IO.println("Before useNotOk");

        {
            Number ignorePattern_2 = useNotOk();

            //@ assert ignorePattern_2 != null;

            /* skip */
        }

        IO.println("After useNotOk");

        return 0L;
    }

    public static Number useOk() {
        project.Entrytypes.A a = new project.Entrytypes.A(MapUtil.map());
        //@ assert a != null;
        a.set_m(MapUtil.munion(Utils.copy(a.get_m()),
                MapUtil.map(new Maplet(1L, new project.Entrytypes.B(2L)))));

        Number ret_1 = 0L;

        return ret_1;
    }

    public static Number useNotOk() {
        project.Entrytypes.A a = new project.Entrytypes.A(MapUtil.map());
        //@ assert a != null;
        a.set_m(MapUtil.munion(Utils.copy(a.get_m()),
                MapUtil.map(new Maplet(1L, new project.Entrytypes.B(1L)))));

        Number ret_2 = 0L;

        return ret_2;
    }

    public String toString() {
        return "Entry{}";
    }
}
