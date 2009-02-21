import junit.framework.TestCase;
import org.jinterop.dcom.core.JISession;
import org.jinterop.dcom.common.JIException;
import org.jvnet.hudson.wmi.SWbemServices;
import org.jvnet.hudson.wmi.WMI;
import org.jvnet.hudson.wmi.Win32Service;
import static org.jvnet.hudson.wmi.Win32Service.Win32OwnProcess;

/**
 * @author Kohsuke Kawaguchi
 */
public class WmiTest extends TestCase {
    private String host = System.getProperty("wmi.host","192.168.2.9");
    private String domain = System.getProperty("wmi.domain","");
    private String user = System.getProperty("wmi.user","Administrator");
    private String password = System.getProperty("wmi.password","");

    public void testService() throws Exception {
        JISession session = JISession.createSession(domain, user, password);
        session.setGlobalSocketTimeout(30000);
        SWbemServices services = WMI.connect(session, host);

        assertFalse(services.Exists("Win32_Service.Name=\"no_such_service\""));

        Win32Service svc = services.Get("Win32_Service").cast(Win32Service.class);
        int r = svc.Create("test", "test service", "notepad.exe",
                Win32OwnProcess, 0, "Manual", true);
        if(r==0 || r==23/*already exists*/)
            ;
        else
            assertEquals(0,r); // let it fail and see the value of r

        Win32Service inst = services.Get("Win32_Service.Name=\"test\"").cast(Win32Service.class);

        System.out.println(inst.Status());
        System.out.println(inst.StopService());

        r = inst.Delete();
        assertEquals(0,r);
    }
}
