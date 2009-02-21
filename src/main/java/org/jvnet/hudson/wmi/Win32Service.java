/*
 * The MIT License
 *
 * Copyright (c) 2004-2009, Sun Microsystems, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jvnet.hudson.wmi;

import org.kohsuke.jinterop.JIProxy;
import org.kohsuke.jinterop.Property;
import org.jinterop.dcom.common.JIException;

/**
 * 
 * @author Kohsuke Kawaguchi
 */
public interface Win32Service extends JIProxy {
    @Property
    String Status() throws JIException;

    /**
     * Creates a service.
     *
     * See http://msdn.microsoft.com/en-us/library/aa389390(VS.85).aspx
     */
    int Create(String name, String displayName,
               String pathName, int serviceType, int errorControl,
               String startMode, boolean desktopInteract, String startName,
               String startPassword, String loadOrderGroup, String[] loadOrderGroupDependencies,
               String[] serviceDependencies) throws JIException;

    int Create(String name, String displayName,
               String pathName, int serviceType, int errorControl,
               String startMode, boolean desktopInteract) throws JIException;

    // serviceType constants
    // see http://msdn.microsoft.com/en-us/library/tfdtdw0e(VS.80).aspx
    // and http://msdn.microsoft.com/en-us/library/aa389390(VS.85).aspx
    final int Win32OwnProcess = 16;
    final int Win32ShareProcess = 32;
    final int InteractiveProcess = 256;

    /**
     * Deletes a service.
     */
    int Delete() throws JIException;

    int StartService() throws JIException;
    int StopService() throws JIException;
}
