package com.morgan.opnet.util;

import com.sun.jna.Library;

public interface AN extends Library {
	
	public void _ZN15ActivityNetwork16runGPR_TCTO_FlowESsSs(StringByReference inFile, StringByReference outFile);
}
