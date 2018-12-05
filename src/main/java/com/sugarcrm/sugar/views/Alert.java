package com.sugarcrm.sugar.views;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;

/**
 * Model of an individual alert dialog and its possible methods.
 * 
 * @author Mazen Louis <mlouis@sugarcrm.com>
 *
 */
public class Alert extends View {
	String alertDiv;

	public Alert(String tagIn, String strategyNameIn, String hookStringIn) throws Exception {
		super(tagIn, strategyNameIn, hookStringIn);
		addControl("confirmAlert", "a", "css", hookStringIn + " a[data-action='confirm']");
		addControl("cancelAlert", "a", "css", hookStringIn + " a[data-action='cancel']");
		addControl("closeAlert", "a", "css", hookStringIn + " button[data-action='close']");
	}

	/**
	 * Clicks the Confirm link in this Alert Dialog.
	 * <p>
	 * An Alert dialog box must be on the screen to use this method.
	 * 
	 * When used, this alert dialog box will not be visible and the action
	 * that triggered the alert box to appear will continue.
	 * 
	 * @throws Exception if no Alert Dialog exists
	 */
	public void confirmAlert() throws Exception {
		getControl("confirmAlert").click();
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * Clicks the Cancel link in this Alert Dialog.
	 * <p>
	 * An Alert dialog box must be on the screen to use this method.
	 * The Alert dialog box must also have a cancel link/button.
	 * 
	 * When used, this alert dialog box will not be visible and the action
	 * that triggered the alert box to appear will not continue.
	 * 
	 * @throws Exception if no Alert Dialog exists
	 */
	public void cancelAlert() throws Exception {
		getControl("cancelAlert").click();
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * Clicks the Close "x" icon in this Alert Dialog.
	 * <p>
	 * An Alert dialog box must be on the screen to use this method.
	 * 
	 * When used, this alert dialog box will be forcibly closed.
	 * 
	 * @throws Exception if no Alert Dialog exists
	 */
	public void closeAlert() throws Exception {
		getControl("closeAlert").click();
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * Clicks on the desired link, by its index, in this Alert Dialog.
	 * <p>
	 * An Alert dialog box must be on the screen to use this method.
	 * There also must be at least 1 link in this Alert dialog box.
	 * 
	 * When used, this alert dialog box will not be visible and the action
	 * associated with the link will commence
	 * 
	 * @param index 1-based index of the link
	 * @throws Exception if no Alert Dialog exists
	 */
	public void clickLink(int index) throws Exception {
		// We use (index+1) because the close "x" link is always the first a link in the Alert Dialog
		// First check to see if the "x" icon is present and visible, if so
		if(getControl("closeAlert").queryVisible()) {
			index = index + 1;
		}			
		new VoodooControl("a","css", getHookString() + " a:nth-of-type(" + index + ")").click();
		VoodooUtils.pause(500);
	}
}
