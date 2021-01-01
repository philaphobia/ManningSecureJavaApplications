/**
 * 
 */
package com.johnsonautoparts.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.johnsonautoparts.logger.AppLogger;

/**
 *
 */
public class SecurityFilter implements Filter {

	/**
	 * Called by the web container to indicate to a filter that it is being
	 * placed into service. The servlet container calls the init method exactly
	 * once after instantiating the filter. The init method must complete
	 * successfully before the filter is asked to do any filtering work.
	 * 
	 * @param filterConfig
	 *            configuration object
	 */
	public void init(FilterConfig filterConfig) {

	}

	/**
	 * The doFilter method of the Filter is called by the container each time a
	 * request/response pair is passed through the chain due to a client request
	 * for a resource at the end of the chain. The FilterChain passed in to this
	 * method allows the Filter to pass on the request and response to the next
	 * entity in the chain.
	 * 
	 * @param req
	 *            Request object to be processed
	 * @param resp
	 *            Response object
	 * @param chain
	 *            current FilterChain
	 * @exception IOException
	 *                if any occurs
	 */
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		
		try {			
            // add all the require security headers
			ServletUtilities.addSecurityHeaders(response);
			
			
			// throw an exception if a method other than GET or POST is sent
			if(request.getMethod().equalsIgnoreCase("GET")) {
				//do something with valid GET request
	    	}

	    	else if(request.getMethod().equalsIgnoreCase("POST")) {
	    		//do something with valid POST request
	    	}
			
			/**
			 * unknown method
			 * Send request to /accessdenied.jsp
			 */
	    	else {
	    		AppLogger.log("Unknown request verb used: " + request.getMethod());
	    		RequestDispatcher dispatcher = request.getRequestDispatcher("/accessdenied.jsp");
                dispatcher.forward(request, response);
                
                // return without forwarding to the next filter to stop
                return;
	    	}
			
			// forward this request on to the web application
			chain.doFilter(request, response);
            
		} 
		catch (Exception e) {
			AppLogger.log("Exception in security filter: " + e.getMessage());

			// return without forwarding to the next filter to stop
            return;
		}
	}

	/**
	 * Called by the web container to indicate to a filter that it is being
	 * taken out of service. This method is only called once all threads within
	 * the filter's doFilter method have exited or after a timeout period has
	 * passed. After the web container calls this method, it will not call the
	 * doFilter method again on this instance of the filter.
	 */
	public void destroy() {
		// finalize
	}

	
	/*
	 * Method to check if the referer is valid and contains the servlet path
	 * context.
	 */
	private boolean isValidReferer(HttpServletRequest request) {
		String referer = request.getHeader("referer");
		
		String servletPath = request.getServletPath();
		
	    return referer.contains(servletPath);
	}
}
