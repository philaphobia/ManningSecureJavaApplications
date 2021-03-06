package com.johnsonautoparts.servlet;

import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

import com.johnsonautoparts.logger.AppLogger;

/*
 * NO CHANGES NEEDED ON THIS CLASS FOR THE liveProject
 * 
 * Request Listener for the initialization and destroying requests. This class
 * registers into Tomcat via the web.xml and provides logs to help in debugging
 * the webapp.
 * 
 */
public class RequestListener implements ServletRequestListener {

	public void requestInitialized(ServletRequestEvent servletRequestEvent) {
		ServletRequest servletRequest = servletRequestEvent.getServletRequest();
		AppLogger.log("ServletRequest initialized. Remote IP: "
				+ servletRequest.getRemoteAddr());
	}

	public void requestDestroyed(ServletRequestEvent servletRequestEvent) {
		ServletRequest servletRequest = servletRequestEvent.getServletRequest();
		AppLogger.log("ServletRequest destroyed. Remote IP: "
				+ servletRequest.getRemoteAddr());
	}

}
