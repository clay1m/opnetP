package com.morgan.opnet.service;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.morgan.opnet.util.CLibrary;
import com.morgan.opnet.util.ProcessUtils;
import com.morgan.opnet.util.SystemCommand;
import com.morgan.opnet.util.TempDirUtils;
import com.sun.jna.Native;
import com.morgan.opnet.swig.activitynet.*;

@Service("runGPRService")
@Transactional
public class RunGPRServiceImpl implements RunGPRService {
	
	@Autowired
    ServletContext context; 
	
	private static CLibrary libc = (CLibrary) Native.loadLibrary("c", CLibrary.class);
	
	static {
	    try {
	        System.loadLibrary("activitynetswig");
	    } catch (UnsatisfiedLinkError e) {
	      System.err.println("Native code library failed to load. See the chapter on Dynamic Linking Problems in the SWIG Java documentation for help.\n" + e);
	      System.exit(1);
	    }
	  }
	
	private static final String GPR_EXE_WIN = "GPR2.exe";
	private static final String TEMP_DIR = File.separator + "tmp" + File.separator;

	@Override
	public File run(String inFileName, String outFileName) {
		//TODO: need to alter to run from shared lib on windows so exe need not be distributed with war
		String executionDir = context.getRealPath("/WEB-INF/executables");
		executionDir = executionDir + File.separator;

		String osName = System.getProperty("os.name");
		File f1 = new File(executionDir);

		if (osName.contains("Win")) {
			ProcessBuilder builderWin = new ProcessBuilder(executionDir + GPR_EXE_WIN, executionDir + inFileName, executionDir + outFileName);
			builderWin.directory(f1);
			try {
				Process process = builderWin.start();

			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		if (osName.contains("Linux")) {
			
			//set root permissions for temp dir
			libc.chmod(TEMP_DIR, 0777);
			
			ActivityNetwork AN = new ActivityNetwork();
		    AN.runGPR_TCTO_Flow(TEMP_DIR + inFileName, TEMP_DIR + outFileName);
		}
		
		return new File(TEMP_DIR + outFileName);

	}
	
	

}
