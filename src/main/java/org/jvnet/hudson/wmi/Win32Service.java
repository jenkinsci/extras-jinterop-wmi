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
 * Represents a Windows service.
 *
 * See http://msdn.microsoft.com/en-us/library/aa394418(VS.85).aspx
 *
 * @author Kohsuke Kawaguchi
 */
public interface Win32Service extends JIProxy {
    /**
     * Current status of the object. Various operational and nonoperational statuses can
     * be defined. Operational statuses include: "OK", "Degraded", and "Pred Fail"
     * (an element, such as a SMART-enabled hard disk drive, may be functioning
     * properly but predicting a failure in the near future). Nonoperational
     * statuses include: "Error", "Starting", "Stopping", and "Service".
     * The latter, "Service", could apply during mirror-resilvering of a disk,
     * reload of a user permissions list, or other administrative work.
     * Not all such work is online, yet the managed element is neither "OK" nor
     * in one of the other states.
     *
     * <p>
     * The values are:
     * <ul>
     * <li>"OK"
     * <li>"Error"
     * <li>"Degraded"
     * <li>"Unknown"
     * <li>"Pred Fail"
     * <li>"Starting"
     * <li>"Stopping"
     * <li>"Service"
     * </ul>
     */
    @Property
    String Status() throws JIException;

    /**
     * Current state of the service.
     *
     * Possible values are:
     * <ul>
     * <li>"Stopped"
     * <li>"Start Pending"
     * <li>"Stop Pending"
     * <li>"Running"
     * <li>"Continue Pending"
     * <li>"Pause Pending"
     * <li>"Paused"
     * <li>"Unknown"
     * </ul>
     */
    @Property
    String State() throws JIException;

    /**
     * Service has been started.
     */
    @Property
    boolean Started() throws JIException;

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
