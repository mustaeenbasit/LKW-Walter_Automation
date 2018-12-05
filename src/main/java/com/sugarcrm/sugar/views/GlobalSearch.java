package com.sugarcrm.sugar.views;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;

/**
 * Model of the Global Search View and its possible methods.
 *
 * @author Mazen Louis <mlouis@sugarcrm.com>
 *
 */
public class GlobalSearch extends View {
	protected static GlobalSearch globalSearch;

	public static GlobalSearch getInstance() throws Exception {
		if (globalSearch == null)
			globalSearch = new GlobalSearch();
		return globalSearch;
	}

	public GlobalSearch() throws Exception {
		super("div", "css", "#content div .layout_default");

		String parentCSS = getHookString();
		addControl("toggleSidebar", "button", "css", parentCSS + " .search-headerpane.fld_sidebar_toggle button");
		addControl("headerpaneTitle", "div", "css", parentCSS + " .headerpane [data-fieldname=title] div");
	}

	/**
	 * Click Preview Eye Icon on a given row.
	 * <p>
	 * Must be on the global search view to use.<br>
	 * Must have search results returned in Global Search to use.<br>
	 * When used, the record information will be displayed on the right hand side view.<br>
	 *
	 * @param row int Index of desired row to preview.
	 * @throws Exception
	 */
	public void preview(int row) throws Exception {
		new VoodooControl("a", "css", getHookString() + " .search-result:nth-of-type(" + row + ") .search-list a").click();
		VoodooUtils.waitForReady();
	}

	/**
	 * Click Preview Eye Icon on a given row.
	 * <p>
	 * Must be on the global search view to use.<br>
	 * Must have search results returned in Global Search to use.<br>
	 * When used, the record information will be displayed on the right hand side view.<br>
	 *
	 * @param record Record to preview
	 * @throws Exception
	 */
	public void preview(Record record) throws Exception {
		new VoodooControl("a", "css", getHookString() + " [data-id='" + record.getGuid() + "'] a[data-event='list:preview:fire']").click();
		VoodooUtils.waitForReady();
	}

	/**
	 * Click the record name on the given row.
	 * <p>
	 * Must be on the global search view to use.<br>
	 * Must have search results returned in Global Search to use.<br>
	 * When used, you will be taken to the recordView of the desired record.<br>
	 * NOTE: Global Search View is not ordered and use of Index may not return the actual desired record row
	 *
	 * @param row int Index of desired row to click.
	 * @throws Exception
	 */
	public void clickRecord(int row) throws Exception {
		new VoodooControl("a", "css", getHookString() + " .search-result:nth-of-type(" + row + ") h3 a").click();
		VoodooUtils.waitForReady();
	}

	/**
	 * Click the link to the given record.
	 * <p>
	 * Must be on the global search view to use.<br>
	 * Must have search results returned in Global Search to use.<br>
	 * When used, you will be taken to the recordView of the desired record.<br>
	 *
	 * @param record Record to click on
	 * @throws Exception
	 */
	public void clickRecord(Record record) throws Exception {
		VoodooUtils.voodoo.log.info("Record: " + record.getRecordIdentifier());
		new VoodooControl("a", "css", getHookString() + " a[href*='" + record.getGuid() + "']").click();
		VoodooUtils.waitForReady();
	}

	/**
	 * Toggle the Sidebar/Dashboard/Right Hand Side view.
	 * <p>
	 * Must be on the global search view to use.<br>
	 * When used, the Sidebar/Dashboard/Right Hand Side view will be toggled to displayed or not displayed.
	 * @throws Exception
	 */
	public void toggleSidebar() throws Exception {
		getControl("toggleSidebar").click();
		VoodooUtils.waitForReady();
	}

	/**
	 * Expand the Sidebar/Dashboard/Right Hand Side view.
	 * <p>
	 * Must be on the global search view to use.<br>
	 * When used, the Sidebar/Dashboard/Right Hand Side view will be toggled to displayed if it is not already.
	 * @throws Exception
	 */
	public void expandSidebar() throws Exception {
		if(!(new VoodooControl("div", "css", ".search-dashboard-headerpane").queryVisible())) {
			toggleSidebar();
		}
	}

	/**
	 * Collapse the Sidebar/Dashboard/Right Hand Side view.
	 * <p>
	 * Must be on the global search view to use.<br>
	 * When used, the Sidebar/Dashboard/Right Hand Side view will be toggled to not displayed.
	 * @throws Exception
	 */
	public void collapseSidebar() throws Exception {
		if(new VoodooControl("div", "css", ".search-dashboard-headerpane").queryVisible()) {
			toggleSidebar();
		}
	}

	/**
	 * Get a row in this Global Search View.
	 * <p>
	 * Must be on the Global Search View to use.<br>
	 * NOTE: Global Search View is not ordered and use of Index may not return the actual desired row
	 *
	 * @param index Int index of the row desired
	 * @return VoodooControl representing the row desired
	 * @throws Exception
	 */
	public VoodooControl getRow(int index) throws Exception {
		return new VoodooControl("li", "css", "ul.nav.search-results li.search-result:nth-of-type(" + index + ")");
	}

	/**
	 * Get a row in this Global Search View.
	 * <p>
	 * Must be on the Global Search View to use.
	 *
	 * @param record Record of the row desired
	 * @return VoodooControl representing the record row desired
	 * @throws Exception
	 */
	public VoodooControl getRow(Record record) throws Exception {
		return new VoodooControl("li", "css", "[data-id='" + record.getGuid() + "']");
	}
}
