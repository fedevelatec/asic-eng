package com.fedevela.engine;

/**
 * Hello world!
 *
 */

import com.fedevela.asic.util.TypeCast;
import net.codicentro.core.CDCException;
import java.lang.reflect.InvocationTargetException;

public class App 
{
    public static void main(String[] args) throws CDCException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
//         String[] p =  "{A} {B} a lo mejor {par{a den}tro de un}os añ}os".split("\\s(?=\\{)|(?<=\\})\\s");
//      //  String[] p = "{A} {B} [C] [D] (E) (F)".split("(?<=\\)),\\s*");
//
//        for (String s : p) {
//            System.out.println(s);
//        }

        TypeCast cast = new TypeCast();

        System.out.println(TypeCast.class.getMethod("toLong", Object.class).invoke(cast, 10L).getClass());


        //System.out.println(TypeCast.invoke(TypeCast.class, "toString",new Class[]{ Object.class}, new Object[]{90L}));

    }
}
