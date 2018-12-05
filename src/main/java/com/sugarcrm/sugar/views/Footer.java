package com.sugarcrm.sugar.views;

public class Footer extends View {
	protected static Footer Footerbar;
	
	public static Footer getInstance() throws Exception {
		if (Footerbar == null) Footerbar = new Footer();
		return Footerbar;
	}

	private Footer() throws Exception {
		addControl("downArrow", "div", "id", "arrow");
	}
	
	public void toggle() throws Exception {
		getControl("downArrow").click();
	}
}
