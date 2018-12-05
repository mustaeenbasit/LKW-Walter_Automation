package com.sugarcrm.test.targetlists;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class TargetLists_28452 extends SugarTest {
	public void setup() throws Exception {
		sugar.targetlists.api.create();
		sugar.login();
	}

	/**
	 * Verify that OOB Default fields that display for Target List Search Result
	 * 
	 * @throws Exception
	 */
	@Test
	public void TargetLists_28452_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Enter a query string that match the target list name
		// TODO: VOOD-668
		new VoodooControl("input", "css", ".search-query").set(sugar.targetlists.getDefaultData().get("targetlistName"));
		sugar.alerts.waitForLoadingExpiration();
		
		// Verify that target list module icon appear and the matched string is highlighted
		// TODO: VOOD-668
		new VoodooControl("span", "css", ".label-module-md.label-ProspectLists").hover();
		VoodooUtils.waitForReady();
		
		VoodooUtils.focusDefault();
		
		// TODO: VOOD-2199 - Need Lib Support to assert Tooltip text on Tooltip control on a Sidecar view
		new VoodooControl("div", "css", ".tooltip .tooltip-inner").assertContains("Target List", true);
		new VoodooControl("h3", "css", ".search-result a div.ellipsis_inline.primary h3").assertContains(sugar.targetlists.getDefaultData().get("targetlistName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}