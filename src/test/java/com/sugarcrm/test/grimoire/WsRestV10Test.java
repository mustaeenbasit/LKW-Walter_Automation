package com.sugarcrm.test.grimoire;

import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.WsRestV10;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class WsRestV10Test extends SugarTest {
	Configuration grimoireConfig;
	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void testResetSugar() throws Exception {
		grimoireConfig = VoodooUtils.getGrimoireConfig();
		// Don't run this test unless universal_cleanup is enabled
		if (! "true".equals(grimoireConfig.getValue("universal_cleanup", "false"))) {
			VoodooUtils.voodoo.log.warning("Universal Cleanup is not enabled, skipping");
			return;
		}
		/*
		1. Create an account by api
		2. Verify it exists
		3. Log out
		4. Run restoreSugar
		5. Log in
		6. Verify the account no longer exists
		 */
		VoodooUtils.voodoo.log.info("Running testResetSugar...");
		sugar().accounts.api.create();
		sugar().accounts.navToListView();
		// Assert there is only one account
		assertEquals(1, sugar().accounts.listView.countRows());
		sugar().logout();

		// Make the api call to reset sugar
		new WsRestV10().restoreSugar();
		sugar().login();
		sugar().accounts.navToListView();
		// Assert there are no accounts
		assertEquals(0, sugar().accounts.listView.countRows());

		VoodooUtils.voodoo.log.info("testResetSugar complete.");
	}

	public void cleanup() throws Exception {
		// Empty cleanup function to make the test class non-abstract
	}
}
