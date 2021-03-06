/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.laytonsmith.testing;

import com.laytonsmith.aliasengine.Constructs.*;
import com.laytonsmith.aliasengine.functions.exceptions.MarshalException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import com.laytonsmith.aliasengine.functions.Function;
import com.laytonsmith.aliasengine.functions.FunctionList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Layton
 */
public class RandomTests {
    /**
     * This function automatically tests all the boilerplate portions of all functions. Note that
     * this can be disabled in the StaticTest class, so that high quality test coverage can be measured.
     */
    @Test public void testAllBoilerplate(){
        for(Function f : FunctionList.getFunctionList()){
            StaticTest.TestBoilerplate(f, f.getName());
            Class upper = f.getClass().getEnclosingClass();
            if(upper == null){
                fail(f.getName() + " is not enclosed in an upper class.");
            }
            try {
                Method m = upper.getMethod("docs", new Class[]{});
                try{
                    String docs = m.invoke(null, new Object[]{}).toString();
                    StaticTest.TestClassDocs(docs, upper);
                } catch (NullPointerException ex){
                    fail(upper.getName() + "'s docs function should be static");
                }
            } catch (IllegalAccessException ex) {
                Logger.getLogger(RandomTests.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(RandomTests.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                fail(upper.getName() + " throws an exception!");
            } catch (NoSuchMethodException ex) {
                fail(upper.getName() + " does not include a class level documentation function.");
            } catch (SecurityException ex) {
                Logger.getLogger(RandomTests.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @Test public void testConstuctToString(){
        System.out.println("Construct.toString");
        assertEquals("hello", new CString("hello", 0, null).toString());
    }
    
    @Test public void testClone() throws CloneNotSupportedException{
        CArray c1 = C.Array(C.Void(), C.Void()).clone();
        CBoolean c2 = C.Boolean(true).clone();
        CClosure c3 = new CClosure("", null, 0, null).clone();
        CDouble c4 = C.Double(1).clone();
        CFunction c5 = new CFunction("", 0, null).clone();
        CInt c6 = C.Int(1).clone();
        CNull c7 = C.Null().clone();
        CString c8 = C.String("").clone();
        CVoid c9 = C.Void().clone();
        Command c10 = new Command("/c", 0, null).clone();
        IVariable c12 = new IVariable("@name", C.Null(), 0, null).clone();
        Variable c13 = new Variable("$name", "", false, false, 0, null);
    }
    
    @Test public void testJSONEscapeString() throws MarshalException{
        CArray ca = new CArray(0, null);
        ca.push(C.Int(1));
        ca.push(C.Double(2.2));
        ca.push(C.String("string"));
        ca.push(C.String("\"Quote\""));
        ca.push(C.Boolean(true));
        ca.push(C.Boolean(false));
        ca.push(C.Null());
        ca.push(C.Void());
        ca.push(new Command("/Command", 0, null));
        ca.push(new CArray(0, null, new CInt(1, 0, null)));
        assertEquals("[1, 2.2, \"string\", \"\\\"Quote\\\"\", true, false, null, \"\", \"\\/Command\", [1]]", Construct.json_encode(ca));
    }
    
    @Test public void testJSONDecodeString() throws MarshalException{
        CArray ca = new CArray(0, null);
        ca.push(C.Int(1));
        ca.push(C.Double(2.2));
        ca.push(C.String("string"));
        ca.push(C.String("\"Quote\""));
        ca.push(C.Boolean(true));
        ca.push(C.Boolean(false));
        ca.push(C.Null());
        ca.push(C.Void());
        ca.push(new Command("/Command", 0, null));
        ca.push(new CArray(0, null, new CInt(1, 0, null)));
        StaticTest.assertCEquals(ca, Construct.json_decode("[1, 2.2, \"string\", \"\\\"Quote\\\"\", true, false, null, \"\", \"\\/Command\", [1]]"));
    }
    
}
