package com.sugarcrm.sugar.views;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;

/**
 * Models some of Sugar Portal Editor views/controls/functionalities.
 * 
 * @author Mazen Louis <mlouis@sugarcrm.com>
 */
public class PortalEditor extends StudioView {
	public View configurePortal, themePortal, portalLayout;
	
	/**
	 * Initializes the portal editor view.
	 * 
	 * @throws Exception
	 */
	public PortalEditor() throws Exception {
		setHookString("#contentTable");
		
		// Sugar Portal Editor options
		addControl("configurePortal", "a", "css", getHookString() + " #SPSync .studiolink");
		addControl("themePortal", "a", "css", getHookString() + " #SPUploadCSS .studiolink");
		addControl("layoutPortal", "a", "css", getHookString() + " #Layouts .studiolink");
		
		configurePortal = new View("div", "css", "#mbtabs");
		themePortal = new View("div", "css", "[data-voodoo-name='Styleguide']");
		portalLayout = new View("div", "css", "#mbtabs");

	}
	
	public void setControls() throws Exception {
		// Configure Portal controls
		configurePortal.addControl("enablePortal", "input", "css", configurePortal.getHookString() + " #appStatus");
		configurePortal.addControl("logoURL", "input", "css", configurePortal.getHookString() + " #logoURL");
		configurePortal.addControl("maxQueryResult", "input", "css", configurePortal.getHookString() + " #maxQueryResult");
		configurePortal.addSelect("defaultUser", "a", "css", configurePortal.getHookString() + " .defaultUser a");
		configurePortal.addControl("save", "input", "css", configurePortal.getHookString() + " #gobutton");
		configurePortal.addControl("toConfigureTabs", "a", "css", configurePortal.getHookString() + " #configure_tabs");
		
		// Portal Theme controls
		themePortal.addControl("cancel", "a", "css", themePortal.getHookString() + " a[name='back_button']");
		themePortal.addControl("saveAndDeploy", "button", "css", themePortal.getHookString() + " button[name='save_button']");
		themePortal.addControl("borderColor", "input", "css", themePortal.getHookString() + " input[name='BorderColor']");
		themePortal.addControl("navigationBarColor", "input", "css", themePortal.getHookString() + " input[name='NavigationBar']");
		themePortal.addControl("primaryButtonColor", "input", "css", themePortal.getHookString() + " input[name='PrimaryButton']");
		themePortal.addControl("reset", "button", "css", themePortal.getHookString() + " [name='refresh_button']");
		themePortal.addControl("restoreDefault", "a", "css", themePortal.getHookString() + " [name='reset_button']");
	}
	
	/**
	 * Configure Portal Settings.
	 * <p>
	 * Limited to the Configure Portal tab in Sugar Portal Editor.<br>
	 * Can be called from any part of Sugar.<br>
	 * When used you will be left on the Configure Portal view with the settings saved.<br>
	 * 
	 * @param portalSettings
	 * @throws Exception
	 */
	public void configurePortal(FieldSet portalSettings) throws Exception {
		sugar().admin.navToPortalSettings();
		VoodooUtils.focusFrame("bwc-frame");
		getControl("configurePortal").click();
		for(String controlName : portalSettings.keySet()) {
			configurePortal.getControl(controlName).set(portalSettings.get(controlName));
		}
		configurePortal.getControl("save").click();
		waitForSave(true);
		VoodooUtils.focusDefault();
	}
	
	/**
	 * Enable Portal support in Sugar.
	 * <p>
	 * Can be called from any part of Sugar.<br>
	 * When used you will be left on the Configure Portal view with Portal Enabled.<br>
	 * 
	 * @throws Exception
	 */
	public void enablePortal() throws Exception {
		enablePortal(true);
	}
	
	/**
	 * Disable Portal support in Sugar.
	 * <p>
	 * Can be called from any part of Sugar.<br>
	 * When used you will be left on the Configure Portal view with Portal Disabled.<br>
	 * 
	 * @throws Exception
	 */
	public void disablePortal() throws Exception {
		enablePortal(false);
	}

	private void enablePortal(boolean shouldEnable) throws Exception {
		sugar().admin.navToPortalSettings();
		clickConfigurePortal();
		VoodooUtils.focusFrame("bwc-frame");
		configurePortal.getControl("enablePortal").set(Boolean.toString(shouldEnable));
		VoodooUtils.pause(100);
		configurePortal.getControl("save").click();
		waitForSave(shouldEnable);
		VoodooUtils.focusDefault();
	}

	private void clickConfigurePortal() throws Exception {
		VoodooUtils.focusFrame("bwc-frame");
		getControl("configurePortal").click();
		VoodooUtils.waitForReady();
		VoodooUtils.pause(250); // Necessary to address an intermittent issue with finding the next control
		configurePortal.getControl("enablePortal").waitForVisible(60000);
		VoodooUtils.focusDefault();
	}
	
	private void waitForSave(Boolean shouldEnable) throws Exception {
		VoodooUtils.waitForReady(120000);
		// If state is used to make sure portal is enabled or disabled by waiting on visibility of elements that should be present
		// after enabling or disabling portal.
		if(shouldEnable) {
			new VoodooControl("a", "css", ".tabform tr:nth-of-type(2) td:nth-of-type(2) a").waitForVisible(); // portal url link
		} else {
			new VoodooControl("a", "css", ".tabform tr:nth-of-type(2) td:nth-of-type(2) input").waitForVisible(); // portal logo URL input
		}
	}
	
	/**
	 * Configure the Portal Theme settings.
	 * <p>
	 * Can be called from any part of Sugar.<br>
	 * When used, you will be left on the Configure Portal Theme page with the settings saved and deployed.<br>
	 * 
	 * @param theme FieldSet of theme settings
	 * @throws Exception
	 */
	public void configurePortalTheme(FieldSet theme) throws Exception {
		sugar().admin.navToPortalSettings();
		VoodooUtils.focusFrame("bwc-frame");
		getControl("themePortal").click();
		VoodooUtils.focusDefault();
		themePortal.getControl("cancel").waitForVisible(60000);
		for(String controlName : theme.keySet()) {
			themePortal.getControl(controlName).set(theme.get(controlName));
		}
		themePortal.getControl("saveAndDeploy").click();
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady(150000);
	}
	
	/**
	 * Restore Portal Theme back to default.
	 * <p>
	 * Must be in Configure Portal Theme view to use.<br>
	 * When used, you will be left on the Portal Theme page with default settings saved.<br>
	 * 
	 * @throws Exception
	 */
	public void restorePortalTheme() throws Exception {
		themePortal.getControl("restoreDefault").click();
		sugar().alerts.getWarning().confirmAlert();
		sugar().alerts.waitForLoadingExpiration(30000);
	}
}