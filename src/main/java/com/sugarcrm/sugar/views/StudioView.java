package com.sugarcrm.sugar.views;

import com.sugarcrm.sugar.AppModel;
import com.sugarcrm.sugar.UnexpiredAlertException;
import com.sugarcrm.sugar.VoodooUtils;

/**
 * Models some of Studio in SugarCRM.
 * 
 * @author Mazen Louis <mlouis@sugarcrm.com>
 */
public class StudioView extends View {
	/**
	 * Initializes the studio view.
	 * 
	 * @throws Exception
	 */
	public StudioView() throws Exception {
		super("table", "css", "#contentTable");

		// Old BWC AJAX message div
		addControl("ajaxStatusDiv", "div", "id", "ajaxStatusDiv");
		
		// Common Studio Controls
		addControl("toggleTreePane", "div", "css", getHookString() + " #collapse_tree");
		addControl("toggleHelpPane", "div", "css", getHookString() + " #collapse_help");
		addControl("homeButton", "input", "css", "#footerHTML input[onclick*='home']");
		addControl("studioButton", "input", "css", "#footerHTML input[onclick*='studio']");
		addControl("moduleBuilderButton", "input", "css", "#footerHTML input[onclick*='mb']");
		addControl("portalEditorButton", "input", "css", "#footerHTML input[onclick*='sugarportal']");
		addControl("dropdownEditorButton", "input", "css", "#footerHTML input[onclick*='dropdowns']");
		
		// Bread Crumbs
		addControl("homeCrumb", "a", "css", "#mbtabs a[onclick*='Home']");
		addControl("portalEditorCrumb", "a", "css", "#mbtabs a[onclick*='sugarportal'].crumbLink");
		addControl("studioCrumb", "a", "css", "#mbtabs a[onclick*='studio'].crumbLink");
		addControl("modulebuilderCrumb", "a", "css", "#mbtabs a[onclick*='ModuleBuilder'].crumbLink");
		addControl("dropdownCrumb", "a", "css", "#mbtabs a[onclick*='dropdowns'].crumbLink");
	}

	/**
	 * Wait for AJAX message in studio to not be visible.
	 * <p>
	 * Waits up to a default of 15seconds.<br>
	 * This wait places you focused on the Default Content when done!
	 * 
	 * @throws Exception
	 */
	public void waitForAJAX() throws Exception {
		waitForAJAX(15000);
	}

	/**
	 * Wait for AJAX message in studio to not be visible.
	 * <p>
	 * This wait keeps you focused on the "bwc-frame" when done!
	 * 
	 * @param ms
	 *            Desired time to wait, in milliseconds
	 * @throws Exception
	 */
	public void waitForAJAX(int ms) throws Exception {
		long totalTime = 0;
		long iterationStartTime, iterationDuration = 0;

		while (totalTime < ms) {
			iterationStartTime = System.currentTimeMillis();

			if (!queryLoading()) {
				return;
			}

			VoodooUtils.pause(250);
			iterationDuration = System.currentTimeMillis() - iterationStartTime;
			totalTime = totalTime + iterationDuration;
		}

		throw new UnexpiredAlertException();
	}

	/**
	 * Query visibility of AJAX message in studio.
	 * <p>
	 * Only works in studio!<br>
	 * 
	 * @return true if control is visible, false otherwise.
	 * @throws Exception
	 */
	private boolean queryLoading() throws Exception {
		boolean isVisible = false;

		// needed to make sure that when focusing into bwc-frame we aren't already focused to it.
		VoodooUtils.focusDefault(); 
		VoodooUtils.focusFrame("bwc-frame");
		if (getControl("ajaxStatusDiv").queryVisible()) {
			isVisible = true;
		}

		return isVisible;
	}
	
	/**
	 * Click the Home button in this view.
	 * <p>
	 * Must be in a Studio view to use.<br>
	 * When used you will be taken the the Home view of Developer Tools.<br>
	 * 
	 * @throws Exception
	 */
	public void clickHome() throws Exception {
		getControl("homeButton").click();
		getControl("ajaxStatusDiv").waitForVisible();
		waitForAJAX();
	}
	
	/**
	 * Click the Studio button in this view.
	 * <p>
	 * Must be in a Studio view to use.<br>
	 * When used you will be taken the the Studio view of Developer Tools.<br>
	 * 
	 * @throws Exception
	 */
	public void clickStudio() throws Exception {
		getControl("studioButton").click();
		getControl("ajaxStatusDiv").waitForVisible();
		waitForAJAX();
	}
	
	/**
	 * Click the Portal Editor button in this view.
	 * <p>
	 * Must be in a Studio view to use.<br>
	 * When used you will be taken the the Portal Editor view of Developer Tools.<br>
	 * 
	 * @throws Exception
	 */
	public void clickPortalEditor() throws Exception {
		getControl("portalEditorButton").click();
		getControl("ajaxStatusDiv").waitForVisible();
		waitForAJAX();
	}
	
	/**
	 * Click the Module Builder button in this view.
	 * <p>
	 * Must be in a Studio view to use.<br>
	 * When used you will be taken the the Module Builder view of Developer Tools.<br>
	 * 
	 * @throws Exception
	 */
	public void clickModuleBuilder() throws Exception {
		getControl("moduleBuilderButton").click();
		getControl("ajaxStatusDiv").waitForVisible();
		waitForAJAX();
	}
	
	/**
	 * Click the Dropdown Editor button in this view.
	 * <p>
	 * Must be in a Studio view to use.<br>
	 * When used you will be taken the the Dropdown Editor view of Developer Tools.<br>
	 * 
	 * @throws Exception
	 */
	public void clickDropdownEditor() throws Exception {
		getControl("dropdownEditorButton").click();
		getControl("ajaxStatusDiv").waitForVisible();
		waitForAJAX();
	}
	
	/**
	 * Click the arrow toggle icon to expose/hide this views Left hand side Tree pane.
	 * <p>
	 * Must be in a Studio view to use.<br>
	 * When used, the left hand side Tree pane will be either exposed or hidden.
	 * 
	 * @throws Exception
	 */
	public void toggleTreePane() throws Exception {
		getControl("toggleTreePane").click();
	}
	
	/**
	 * Click the arrow toggle icon to expose/hide this views Right hand side Help pane.
	 * <p>
	 * Must be in a Studio view to use.<br>
	 * When used, the right hand side Help pane will be either exposed or hidden.
	 * 
	 * @throws Exception
	 */
	public void toggleHelpPane() throws Exception {
		getControl("toggleHelpPane").click();
	}
}