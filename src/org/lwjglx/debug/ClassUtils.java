/*
 * (C) Copyright 2017 Kai Burjack

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.

 */
package org.lwjglx.debug;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

class ClassUtils {

    private static final MethodHandle defineClass;

    static {
        try {
            Method defineClassMethod = ClassLoader.class.getDeclaredMethod("defineClass", new Class[] { String.class, byte[].class, int.class, int.class });
            defineClassMethod.setAccessible(true);
            defineClass = MethodHandles.lookup().unreflect(defineClassMethod);
        } catch (Exception e) {
            throw new AssertionError("Could not find method: ClassLoader.defineClass");
        }
    }

    static <T> Class<T> defineClass(ClassLoader cl, String name, byte[] definition) {
        String apiName = name.replace('/', '.');
        try {
            return (Class<T>) defineClass.invokeExact(cl, apiName, definition, 0, definition.length);
        } catch (Throwable e) {
            throw new AssertionError("Could not define class in JVM: " + name, e);
        }
    }

}
