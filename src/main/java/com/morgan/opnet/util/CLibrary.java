package com.morgan.opnet.util;

import com.sun.jna.Library;

public interface CLibrary extends Library {
	 public int chmod(String path, int mode);
}
