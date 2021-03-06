package com.johnsonautoparts.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.johnsonautoparts.logger.AppLogger;

/*
 * NO CHANGES NEEDED ON THIS CLASS FOR THE liveProject
 * 
 * Context Listener for deployment and destroying of servlet. This class
 * registers into Tomcat via the web.xml and provides logs to help in debugging
 * the webapp.
 * 
 */
public class ContextListener implements ServletContextListener {

	public void contextInitialized(ServletContextEvent servletContextEvent) {
		ServletContext context = servletContextEvent.getServletContext();
		AppLogger.log("Context initialized " + context.getContextPath());

	}

	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		ServletContext context = servletContextEvent.getServletContext();
		AppLogger.log("Context destroyed  " + context.getContextPath());
	}

}
