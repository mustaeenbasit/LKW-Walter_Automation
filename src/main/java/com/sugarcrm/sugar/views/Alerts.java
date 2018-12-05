package com.sugarcrm.sugar.views;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;

/**
 * Alerts container to act on individual object of Alert type.
 * 
 * @author Mazen Louis <mlouis@sugarcrm.com>
 * 
 */
public class Alerts extends View {
	protected static Alerts alerts;

	public static Alerts getInstance() throws Exception {
		if (alerts == null)
			alerts = new Alerts();
		return alerts;
	}

	public Alerts() throws Exception {
		super("div", "css", "#alerts");
	}

	/**
	 * Get the first alert dialog box of the process type.
	 * <p>
	 * (Gray messages) e.g. Loading... or Saving...
	 * 
	 * @return Alert container representing the first process type alert.
	 * @throws Exception
	 */
	public Alert getProcess() throws Exception {
		return getProcess(1);
	}

	/**
	 * Get the specified alert dialog box of the process type by index.
	 * <p>
	 * (Gray messages) e.g. Loading... or Saving...
	 * 
	 * @param index
	 *            int index of the nth alert dialog box desired.
	 * @return Alert container representing the specified process type alert.
	 * @throws Exception
	 */
	public Alert getProcess(int index) throws Exception {
		return new Alert("div", "css", "#alerts .alert.alert-process:nth-of-type(" + index + ")");
	}

	/**
	 * Get the first alert dialog box of the info type.
	 * <p>
	 * (Blue messages).<br>
	 * Only contains "x" close button.
	 * 
	 * @return Alert container representing the first Info type alert.
	 * @throws Exception
	 */
	public Alert getInfo() throws Exception {
		return getInfo(1);
	}

	/**
	 * Get the specified alert dialog box of the info type by index.
	 * <p>
	 * (Blue messages).<br>
	 * Only contains "x" close button.
	 * 
	 * @param index
	 *            int index of the nth alert dialog box desired.
	 * @return Alert container representing the specified Info type alert.
	 * @throws Exception
	 */
	public Alert getInfo(int index) throws Exception {
		return new Alert("div", "css", getHookString() + " .alert.alert-info.alert-block:nth-of-type(" + index + ")");
	}

	/**
	 * Get the first alert dialog box of the success type.
	 * <p>
	 * (Green messages).<br>
	 * Can contain a link. <br>
	 * Contains "x" close button.
	 * 
	 * @return Alert container representing the first Success type alert.
	 * @throws Exception
	 */
	public Alert getSuccess() throws Exception {
		return getSuccess(1);
	}

	/**
	 * Get the specified alert dialog box of the success type by index.
	 * <p>
	 * (Green messages).<br>
	 * Can contain a link. <br>
	 * Contains "x" close button.
	 * 
	 * @param index
	 *            int index of the nth alert dialog box desired.
	 * @return Alert container representing the specified Success type alert.
	 * @throws Exception
	 */
	public Alert getSuccess(int index) throws Exception {
		return new Alert("div", "css", getHookString() + " .alert.alert-success.alert-block:nth-of-type(" + index + ")");
	}

	/**
	 * Get the first alert dialog box of the warning type.
	 * <p>
	 * (Yellow messages).<br>
	 * Can contain confirm and cancel buttons.
	 * 
	 * @return Alert container representing the first Warning type alert.
	 * @throws Exception
	 */
	public Alert getWarning() throws Exception {
		return getWarning(1);
	}

	/**
	 * Get the specified alert dialog box of the warning type by index.
	 * <p>
	 * (Yellow messages).<br>
	 * Can contain confirm and cancel buttons.
	 * 
	 * @param index
	 *            int index of the nth alert dialog box desired.
	 * @return Alert container representing the specified Warning type alert.
	 * @throws Exception
	 */
	public Alert getWarning(int index) throws Exception {
		return new Alert("div", "css", getHookString() + " .alert.alert-warning.alert-block:nth-of-type(" + index + ")");
	}

	/**
	 * Get the first alert dialog box of the error type.
	 * <p>
	 * (Red Messages)
	 * 
	 * @return Alert container representing the first Error type alert.
	 * @throws Exception
	 */
	public Alert getError() throws Exception {
		return getError(1);
	}

	/**
	 * Get the specified alert dialog box of the error type by index.
	 * <p>
	 * (Red Messages)
	 * 
	 * @param index
	 *            int index of the nth alert dialog box desired.
	 * @return Alert container representing the specified Error type alert.
	 * @throws Exception
	 */
	public Alert getError(int index) throws Exception {
		return new Alert("div", "css", getHookString() + " .alert.alert-danger.alert-block:nth-of-type(" + index + ")");
	}

	/**
	 * Get the first alert on the screen.
	 * 
	 * @return Alert object
	 * @throws Exception
	 */
	public Alert getAlert() throws Exception {
		return getAlert(1);
	}

	/**
	 * Get the nth alert on the screen.
	 * 
	 * @param index
	 *            int index of the nth alert
	 * @return Alert object
	 * @throws Exception
	 */
	public Alert getAlert(int index) throws Exception {
		return new Alert("div", "css", getHookString() + " .alert.alert-block:nth-of-type(" + index + ")");
	}

	/**
	 * Query if any alerts are visible on the screen.
	 * 
	 * @return true if alert dialog box is visible, false if not visible.
	 * @throws Exception
	 */
	public boolean queryVisible() throws Exception {
		return getControl("alertsDIV").queryVisible();
	}

	/**
	 * Waits up to 15s for "Loading..." info dialog box to not be visible.
	 *
	 * @throws Exception
	 *             if no "Loading..." info Dialog exists or ms seconds have
	 *             elapsed and the Alert Dialog still exists.
	 */
	public void waitForLoadingExpiration() throws Exception {
		//TODO: This should be reduced to 15000, see VOOD-1378
		waitForLoadingExpiration(120000);
	}

	/**
	 * Waits for the "Loading..." info dialog box to not be visible.
	 * @param ms    Timeout in milliseconds.
	 * @throws Exception
	 */
	public void waitForLoadingExpiration(int ms) throws Exception {
		VoodooUtils.waitForReady(ms);
	}

	/**
	 * Close all alerts on the screen that can be closed.
	 * 
	 * @throws Exception
	 */
	public void closeAllAlerts() throws Exception {
		while (new VoodooControl("a", "css", getHookString() + " .close").queryVisible()) {
			new VoodooControl("a", "css", getHookString() + " .close").click();
		}
	}

	/**
	 * Close all success type alerts on the screen.
	 * 
	 * @throws Exception
	 */
	public void closeAllSuccess() throws Exception {
		while (new VoodooControl("a", "css", getHookString() + " .alert-success .close").queryVisible()) {
			new VoodooControl("a", "css", getHookString() + " .alert-success .close").click();
		}
	}

	/**
	 * Close all warning type alerts on the screen.
	 * 
	 * @throws Exception
	 */
	public void closeAllWarning() throws Exception {
		while (new VoodooControl("a", "css", getHookString() + " .alert-warning .close").queryVisible()) {
			new VoodooControl("a", "css", getHookString() + " .alert-warning .close").click();
		}
	}

	/**
	 * Close all error type alerts on the screen.
	 * 
	 * @throws Exception
	 */
	public void closeAllError() throws Exception {
		while (new VoodooControl("a", "css", getHookString() + " .alert-danger .close").queryVisible()) {
			new VoodooControl("a", "css", getHookString() + " .alert-danger .close").click();
		}
	}

	/**
	 * Cancel all alerts on the screen.
	 * 
	 * @throws Exception
	 */
	public void cancelAllAlerts() throws Exception {
		while (new VoodooControl("a", "css", getHookString() + " a[data-action='cancel']").queryVisible()) {
			new VoodooControl("a", "css", getHookString() + " a[data-action='cancel']").click();
		}
	}

	/**
	 * Cancel all warning alerts on the screen.
	 * 
	 * @throws Exception
	 */
	public void cancelAllWarning() throws Exception {
		while (new VoodooControl("a", "css", getHookString() + " .alert-warning a[data-action='cancel']").queryVisible()) {
			new VoodooControl("a", "css", getHookString() + " .alert-warning a[data-action='cancel']").click();
		}
	}

	/**
	 * Confirm all warning alerts on the screen.
	 * 
	 * @throws Exception
	 */
	public void confirmAllWarning() throws Exception {
		while (new VoodooControl("a", "css", getHookString() + " .alert-warning a[data-action='confirm']").queryVisible()) {
			new VoodooControl("a", "css", getHookString() + " .alert-warning a[data-action='confirm']").click();
		}
	}
	
	/**
	 * Confirm all alerts that can be confirmed on screen.
	 * 
	 * @throws Exception
	 */
	public void confirmAllAlerts() throws Exception {
		while(new VoodooControl("a", "css", getHookString() + " a[data-action='confirm']").queryVisible()) {
			new VoodooControl("a", "css", getHookString() + " a[data-action='confirm']").click();
		}
	}
}
