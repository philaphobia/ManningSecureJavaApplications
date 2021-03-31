package com.johnsonautoparts.servlet;

import java.io.*;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;

import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.johnsonautoparts.logger.AppLogger;

/*
 * NO CHANGES ARE NEEDED IN THIS CLASS FOR THE liveProject
 * 
 * Class for handling some common method needed throughout the webapp.
 * 
 */
public class ServletUtilities {

	/*
	 * Utility class so create private constructor to throw error if the class
	 * is instantiated
	 */
	private ServletUtilities() {
		throw new IllegalStateException("Utility class not for instantiation");
	}

	/*
	 * Add headers for protection based on information from OWASP.
	 *
	 * @param response the response header
	 */
	public static void addSecurityHeaders(HttpServletResponse response) {
		// click-jacking defense so content cannot be framed from a different
		// website
		response.addHeader("X-Frame-Options", "SAMEORIGIN");

		// forces client to only use content-type sent from server and not try
		// to
		// determine the content type by magic sniffing
		response.addHeader("X-Content-Type-Options", "nosniff");

		// stop caching
		response.addHeader("Cache-Control", "no-store");

		// Content-Security-Policy
		// X-Frame-Options is ignored if CSF defined but some browsers ignore so
		// just set this
		response.addHeader("Content-Security-Policy", "frame-ancestors 'none'");
	}

	/*
	 * Simple wrapper method to send error to the client
	 */
	public static void sendError(HttpServletResponse response,
			String errorMsg) {
		PrintWriter out = null;

		try {
			response.setContentType("application/json");
			out = response.getWriter();

			if (out != null) {
				JsonObject json = Json.createObjectBuilder()
						.add("status", "error").add("message", errorMsg)
						.build();
				out.println(json);
			}
		} catch (Exception e) {
			AppLogger.log("Unknown HTTP Error " + e.getMessage());
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	/*
	 * Create a session key from random
	 */
	public static byte[] createSecret() {
		SecureRandom random = new SecureRandom();
		byte[] sharedSecret = new byte[32];
		random.nextBytes(sharedSecret);

		return sharedSecret;
	}

	/*
	 * Provide a path to the User DB XML file
	 * @param HttpServletRequest
	 * @return String of the path
	 */
	public static String getUserDbPath(HttpServletRequest httpRequest) throws InvalidPathException {
		Path path = Paths.get(System.getProperty("catalina.base"),
				"webapps", httpRequest.getContextPath(),
				"resources", "users.xml");

		return path.toString();
	}

	/*
	 * Provide the path to the Derby DB
	 * @param ServletContext
	 * @return String of the path
	 */
	public static String getDerbyDbPath(ServletContext context) {
		String testDbPath = "src/main/webapp/db";

		//if context is null then use source testing path
		if(context == null) {
			return(testDbPath);
		}
		else {
			StringBuilder sb = new StringBuilder();

			sb.append(System.getProperty("catalina.home") );
			sb.append(File.separator);
			sb.append("webapps");
			sb.append(context.getContextPath());
			sb.append(File.separator);
			sb.append("db");

			return sb.toString();
		}
	}

}
