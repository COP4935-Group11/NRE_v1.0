package com.auxiliary;

import com.ucf.pcte.gold.*;

public class WebUiCommonHelper {
		public static void findWebElement(TestObject to,int wait)
		{
			WebUI.verifyElementPresent(to, wait);
		}
}

    
