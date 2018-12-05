package com.sugarcrm.test.grimoire;

import static org.junit.Assert.*;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ViewTests extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}
	
	/**
	 * Verify that clone control and remove control features work properly
	 * 
	 * @throws Exception 
	 */
	@Test
	public void verifyRemoveControl() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyRemoveControl()...");

		// Edit the case using the UI.
		sugar().cases.navToListView();
				
		// Clone an existing control
		sugar().cases.listView.addControl("createCloneButton", "a", "css", ".fld_create_button a");

		// Verify both original and cloned controls work properly
		sugar().cases.listView.getControl("createButton").assertExists(true);
		sugar().cases.listView.getControl("createCloneButton").assertExists(true);
		
		// Now Remove clone
		sugar().cases.listView.removeControl("createCloneButton");
		
		// Again verify original control
		sugar().cases.listView.getControl("createButton").assertExists(true);

		// Verify that removed cloned control is not present 
		String errMsg = "";
		try {
			sugar().cases.listView.getControl("createCloneButton").assertExists(true);
		} catch(Exception e) {
			errMsg = e.getMessage();
			VoodooUtils.voodoo.log.info("Clone/Remove Control working properly - " + errMsg);
		}
		assertTrue("Clone/Remove Control not working properly >>"+errMsg, !errMsg.isEmpty());

		VoodooUtils.voodoo.log.info("verifyRemoveControl() complete.");
	}
	
	public void cleanup() throws Exception {}
}