package com.sugarcrm.test.grimoire;

import com.sugarcrm.sugar.UnfoundElementException;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class VoodooChildElementTests extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void waitForVisible() throws Exception {
		VoodooUtils.voodoo.log.info("Running waitForVisible()...");

		VoodooControl parent = new VoodooControl("div", "css", "[data-voodoo-name='profileactions'] .dropdown-menu.pull-right");
		VoodooControl child = parent.getChildElement("li", "css", ".profileactions-about");
		sugar().navbar.userAction.getControl("userActions").click();
		child.waitForVisible();
		VoodooUtils.voodoo.log.info(child.toString());
		child.click();
		VoodooUtils.waitForReady();
		new VoodooControl("span", "css" ,".about-headerpane.fld_title").assertContains("SugarCRM", true);

		VoodooUtils.voodoo.log.info("waitForVisible() test complete.");
	}

	@Test
	public void waitForVisibleMultipleMatches() throws Exception {
		VoodooUtils.voodoo.log.info("Running waitForVisibleMultipleMatches()...");

		VoodooControl parent = new VoodooControl("div", "css", ".dropdown[data-module='Contacts']");
		VoodooControl child = parent.getChildElement("li", "css", ".ellipsis_inline");

		sugar().navbar.clickModuleDropdown(sugar().contacts);
		child.waitForVisible();

		VoodooUtils.voodoo.log.info("waitForVisibleMultipleMatches() test complete.");
	}

	@Test(expected = UnfoundElementException.class)
	public void waitForNonVisibleElement() throws Exception {
		VoodooUtils.voodoo.log.info("Running waitForNonVisibleElement()...");

		VoodooControl parent = new VoodooControl("div", "css", "[data-voodoo-name='profileactions'] .dropdown-menu.pull-right");
		VoodooControl child = parent.getChildElement("li", "css", ".profileactions-about");
		child.waitForVisible();

		VoodooUtils.voodoo.log.info("waitForNonVisibleElement() test complete.");
	}

	@Test
	public void waitForElementMultipleMatches() throws Exception {
		VoodooUtils.voodoo.log.info("Running waitForElementMultipleMatches()...");

		VoodooControl parent = new VoodooControl("div", "css", ".dropdown[data-module='Contacts']");
		VoodooControl child = parent.getChildElement("li", "css", ".ellipsis_inline");

		child.waitForElement();

		VoodooUtils.voodoo.log.info("waitForElementMultipleMatches() test complete.");
	}

	@Test(expected = UnfoundElementException.class)
	public void waitForElementShortTimeout() throws Exception {
		VoodooUtils.voodoo.log.info("Running waitForElementShortTimeout()...");

		VoodooControl parent = new VoodooControl("div", "css", "[data-voodoo-name='profileactions'] .dropdown-menu.pull-right");
		VoodooControl child = parent.getChildElement("li", "css", ".profileactions-aboutX");
		VoodooUtils.voodoo.log.info(child.toString());
		child.waitForElement(1000);

		VoodooUtils.voodoo.log.info("waitForElementShortTimeout() test complete.");
	}

	@Test(expected = UnfoundElementException.class)
	public void waitForElementLongTimeout() throws Exception {
		VoodooUtils.voodoo.log.info("Running waitForElementLongTimeout()...");

		VoodooControl parent = new VoodooControl("div", "css", "[data-voodoo-name='profileactions'] .dropdown-menu.pull-right");
		VoodooControl child = parent.getChildElement("li", "css", ".profileactions-aboutX");
		VoodooUtils.voodoo.log.info(child.toString());
		child.waitForElement(20000);

		VoodooUtils.voodoo.log.info("waitForElementLongTimeout() test complete.");
	}

	public void cleanup() throws Exception {}
}