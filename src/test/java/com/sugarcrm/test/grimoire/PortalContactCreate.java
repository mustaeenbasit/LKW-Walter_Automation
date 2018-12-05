package com.sugarcrm.test.grimoire;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class PortalContactCreate extends SugarTest {
	FieldSet portalUserData = new FieldSet();

	public void setup() throws Exception {
		// portal set up data
		portalUserData.put("firstName", "Portal");
		portalUserData.put("lastName", "User");
		portalUserData.put("password", "portalUser");
		portalUserData.put("confirmPassword", "portalUser");
		portalUserData.put("checkPortalActive", "true");
		portalUserData.put("portalName", "portalUser");

		sugar().login();

		// Enable portal
		sugar().admin.portalSetup.enablePortal();
	}

	@Test
	public void apiCreate() throws Exception {
		VoodooUtils.voodoo.log.info("Running apiCreate()...");

		ContactRecord portalUser = (ContactRecord)sugar().contacts.api.create(portalUserData);
		portalUser.verify(portalUserData);

		VoodooUtils.voodoo.log.info("apiCreate() complete.");
	}

	@Test
	public void create() throws Exception {
		VoodooUtils.voodoo.log.info("Running create()...");

		ContactRecord portalUser= (ContactRecord)sugar().contacts.create(portalUserData);
		portalUser.verify(portalUserData);

		VoodooUtils.voodoo.log.info("create() complete.");
	}

	public void cleanup() throws Exception {}
}