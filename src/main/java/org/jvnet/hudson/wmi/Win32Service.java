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

    /**
     * Gets the error message for the return code of the service manipulation method.
     */
    public String getErrorMessage(int r);

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

    /**
     * @deprecated
     *      This method doesn't throw a failure as an exception.
     *      Use {@link #start()} instead.
     */
    int StartService() throws JIException;

    /**
     * Starts a service.
     */
    void start() throws JIException;

    /**
     * @deprecated
     *      This method doesn't throw a failure as an exception.
     *      Use {@link #stop()} instead.
     */
    int StopService() throws JIException;

    /**
     * Stops a service.
     */
    void stop() throws JIException;

    public class Implementation {
        public static void start(Win32Service _this) throws JIException {
            int r = _this.StartService();
            if(r!=0)
                throw new JIException(E_FAIL,getErrorMessage(_this,r));
        }

        public static void stop(Win32Service _this) throws JIException {
            int r = _this.StopService();
            if(r!=0)
                throw new JIException(E_FAIL,getErrorMessage(_this,r));
        }

        public static String getErrorMessage(Win32Service _this, int r) {
            switch (r) {
            case 0: return "Success";
            case 1: return "Not Supported";
            case 2: return "Access Denied";
            case 3: return "Dependent Services Running";
            case 4: return "Invalid Service Control";
            case 5: return "Service Cannot Accept Control";
            case 6: return "Service Not Active";
            case 7: return "Service Request Timeout";
            case 8: return "Unknown Failure";
            case 9: return "Path Not Found";
            case 10: return "Service Already Running";
            case 11: return "Service Database Locked";
            case 12: return "Service Dependency Deleted";
            case 13: return "Service Dependency Failure";
            case 14: return "Service Disabled";
            case 15: return "Service Logon Failure";
            case 16: return "Service Marked For Deletion";
            case 17: return "Service No Thread";
            case 18: return "Status Circular Dependency";
            case 19: return "Status Duplicate Name";
            case 20: return "Status Invalid Name";
            case 21: return "Status Invalid Parameter";
            case 22: return "Status Invalid Service Account";
            case 23: return "Status Service Exists";
            case 24: return "Service Already Paused";
            default:
                return "Unknown error code "+r;
            }
        }
    }

    static final int E_FAIL = 0x80004005;
}
