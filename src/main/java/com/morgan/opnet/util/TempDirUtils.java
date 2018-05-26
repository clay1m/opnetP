package com.morgan.opnet.util;

import java.io.File;

public class TempDirUtils {
	public static File getTempDirectory() {
		return new File(System.getProperty("java.io.tmpdir"));
	}

}
