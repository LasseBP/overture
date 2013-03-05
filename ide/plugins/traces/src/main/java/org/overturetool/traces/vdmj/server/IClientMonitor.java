package org.overturetool.traces.vdmj.server;

public interface IClientMonitor
{

	void progress(String traceName, Integer progress);

	void initialize(String module);

	void completed();

	void traceStart(String traceName);
	
	void traceError(String message);

}
