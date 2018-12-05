package com.sugarcrm.test.grimoire;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class SugarTestTest extends SugarTest {
	/**
	 * All threeof the below scenarios need to work correctly. When baseCleanup() executes...
	 * <ol>
	 *     <ol>
	 *         <li>there are no special circumstances.</li>
	 *         <li>a Modal JS dialog is already on-screen.</li>
	 *         <li>a Modal JS dialog will be triggered by closing window.</li>
	 *         <li>there is no window open</li>
	 *     </ol>
	 *</ol>
	 */

	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void baseCleanupStandard() throws Exception{
		// Empty test method for no special circumstances
	}

	@Test
	public void baseCleanupAlert() throws Exception{
		VoodooUtils.executeJS("alert('Unexpected Alert');");
	}

	@Test
	public void baseCleanupTriggerAlert() throws Exception{
		sugar().accounts.navToListView();
		sugar().accounts.listView.create();
		sugar().accounts.createDrawer.getEditField("name").set("Test Name");
	}

	@Test
	public void baseCleanupNoWindow() throws Exception{
		VoodooUtils.closeWindow();
	}

	public void cleanup() throws Exception {
		// Empty cleanup function to make the test class non-abstract
	}
}
