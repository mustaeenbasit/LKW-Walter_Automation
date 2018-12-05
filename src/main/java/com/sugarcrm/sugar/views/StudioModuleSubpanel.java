package com.sugarcrm.sugar.views;

public class StudioModuleSubpanel extends View {
	protected static StudioModuleSubpanel view;
	
	private StudioModuleSubpanel() throws Exception {}
	
	public static StudioModuleSubpanel getInstance() throws Exception {
		if (view == null)
			view = new StudioModuleSubpanel();
		return view;
	}
}