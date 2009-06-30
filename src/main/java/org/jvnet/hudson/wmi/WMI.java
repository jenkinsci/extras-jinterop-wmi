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

import org.jinterop.dcom.core.JIComServer;
import org.jinterop.dcom.core.JIClsid;
import org.jinterop.dcom.core.JISession;
import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.common.JISystem;
import org.kohsuke.jinterop.JInteropInvocationHandler;

import java.net.UnknownHostException;

/**
 * Connects to the remote WMI via DCOM. 
 * @author Kohsuke Kawaguchi
 */
public class WMI {
    public static SWbemServices connect(JISession session, String hostName) throws UnknownHostException, JIException {
        /*
            If this fails with 0x00000005 or 0x80070005,
            I found that the following makes a difference:

            "Control Panel" -> "Administrative Tools" -> "Local Security Policy"
             -> "Local Policies" -> "Security Options" -> "Network Access: Sharing security model for local accounts"

            Set this to classic, so that the specified credential of the local user is authenticaed accordingly,
            instead of as a guest, which won't have the access to the system.

            Also see more comprehensive trobule-shooting guide on this topic at pages like:
            http://www.pcreview.co.uk/forums/thread-2164135.php
            http://www.computerperformance.co.uk/Logon/code/code_80070005.htm
            http://social.answers.microsoft.com/Forums/en-US/vistawu/thread/8eee0f53-9d95-46c8-89b7-5f12538e9f88

            TODO: user account with empty password also doesn't seem to work.
            
         */
        JIComServer comStub = new JIComServer(
                JIClsid.valueOf("76A64158-CB41-11D1-8B02-00600806D9B6"),hostName, session);
        SWbemLocator loc = JInteropInvocationHandler.wrap(SWbemLocator.class,comStub.createInstance());
        return loc.ConnectServer("localhost", null, null, null, null, null, 0, null);
    }

    static {
        JISystem.setAutoRegisteration(true);
    }
}
