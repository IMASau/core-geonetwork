package org.fao.geonet.monitor.link;

import org.fao.geonet.test.TestCase;
import org.jdom.Element;

public class LinkInfoTest extends TestCase {
    public static boolean testResult = true;

    public class LinkCheckerMock implements LinkCheckerInterface {
        public LinkCheckerMock() {}

        @Override
        public void setOnlineResource(final Element onlineResource) {}

        @Override
        public boolean check() {
            return testResult;
        }

        @Override
        public String toString() {
            return "";
        }

        @Override
        public boolean canHandle(String linkType) {
            return true;
        }
    }

    public void testGetStatus() throws Exception {
        LinkMonitorService.maxChecks = 3;
        LinkMonitorService.percentWorkingThreshold = 50;

        LinkInfo linkInfo = new LinkInfo(new LinkCheckerMock());
        testResult = true;

        // No checks done - status should be UNKNOWN
        assertEquals(LinkMonitorService.Status.UNKNOWN, linkInfo.getStatus());

        // One check out of 3 done - status should be WORKING
        linkInfo.check();
        assertEquals(LinkMonitorService.Status.WORKING, linkInfo.getStatus());

        // 3 checks done, should be WORKING
        linkInfo.check();
        linkInfo.check();
        assertEquals(LinkMonitorService.Status.WORKING, linkInfo.getStatus());

        // 1 check is now failing (out of 3), but we need only 50%, so still working
        testResult = false;
        linkInfo.check();
        assertEquals(LinkMonitorService.Status.WORKING, linkInfo.getStatus());

        // 2 checks out of 3 are failing, so it should be failing
        linkInfo.check();
        assertEquals(LinkMonitorService.Status.FAILED, linkInfo.getStatus());
    }

    public void testGetStatusAllFailed() throws Exception {
        LinkMonitorService.maxChecks = 10;
        LinkMonitorService.percentWorkingThreshold = 10;

        LinkInfo linkInfo = new LinkInfo(new LinkCheckerMock());
        testResult = false;

        // Not enough checks done, but all have failed
        linkInfo.check();
        linkInfo.check();
        assertEquals(LinkMonitorService.Status.FAILED, linkInfo.getStatus());
    }


    public void testCheck() throws Exception {
        // Verify we save only 3 checks back
        LinkMonitorService.maxChecks = 3;
        LinkInfo linkInfo = new LinkInfo(new LinkCheckerMock());
        for (int i = 0; i < 10; i++) {
            linkInfo.check();
        }

        assertEquals(LinkMonitorService.maxChecks, linkInfo.getCheckCount());

    }
}