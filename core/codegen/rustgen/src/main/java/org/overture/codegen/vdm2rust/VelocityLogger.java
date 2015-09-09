package org.overture.codegen.vdm2rust;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.LogChute;

public class VelocityLogger implements LogChute {

	List<String> messages = new ArrayList<>();
	
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
	
	@Override
	public void init(RuntimeServices rs) throws Exception {
		messages.clear();
	}

	@Override
	public void log(int level, String message) {
		messages.add(getLevelName(level) + ": " + message);
	}

	@Override
	public void log(int level, String message, Throwable t) {
		messages.add(getLevelName(level) + ": " + message + " EXCEPTION: " + t.toString());
	}

	@Override
	public boolean isLevelEnabled(int level) {		
		return true;
	}
	
	public List<String> getMessages()
	{
		return messages;
	}

}
