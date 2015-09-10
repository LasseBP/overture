package org.overture.codegen.merging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.LogChute;
import org.overture.codegen.logging.ILogger;
import org.overture.codegen.logging.Logger;

public class VelocityLogger implements LogChute {

	private int minLogLevel;

	private ILogger logger;
	
	private static final Map<Integer, String> logLevels;
    
	static
    {
        logLevels = new HashMap<Integer, String>();
        logLevels.put(Integer.valueOf(LogChute.ERROR_ID), LogChute.ERROR_PREFIX);
        logLevels.put(Integer.valueOf(LogChute.WARN_ID), LogChute.WARN_PREFIX);
        logLevels.put(Integer.valueOf(LogChute.INFO_ID), LogChute.INFO_PREFIX);
        logLevels.put(Integer.valueOf(LogChute.DEBUG_ID), LogChute.DEBUG_PREFIX);
        logLevels.put(Integer.valueOf(LogChute.TRACE_ID), LogChute.TRACE_PREFIX);
    }
    
    private static String getLevelName(int lvl)
    {
    	return logLevels.get(Integer.valueOf(lvl));
    }
    
    public VelocityLogger() {
		this(LogChute.TRACE_ID, Logger.getLog());
	}
    
    public VelocityLogger(int minLogLevel, ILogger logger) {
		this.minLogLevel = minLogLevel;
		this.logger = logger;
	}
	
	@Override
	public void init(RuntimeServices rs) throws Exception {
	}

	@Override
	public void log(int level, String message) {
		String msg = "VELOCITY: " + getLevelName(level) + ": " + message;
		
		if(level >= LogChute.WARN_ID)
		{
			logger.printErrorln(msg);
		}
		else
		{
			logger.println(msg);
		}
	}

	@Override
	public void log(int level, String message, Throwable t) {
		String msg = message + " EXCEPTION: " + t.toString();
		log(level, msg);
	}

	@Override
	public boolean isLevelEnabled(int level) {		
		return level >= minLogLevel;
	}
}
