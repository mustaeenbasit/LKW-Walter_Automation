package com.sugarcrm.sugar.views;

public class StudioModuleMobileLayout extends View {
	protected static StudioModuleMobileLayout view;
	
	private StudioModuleMobileLayout() throws Exception {}
	
	public static StudioModuleMobileLayout getInstance() throws Exception {
		if (view == null)
			view = new StudioModuleMobileLayout();
		return view;
	}
}