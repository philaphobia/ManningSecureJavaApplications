<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="org.owasp.encoder.Encode"%>
<% 	/**
 * Project 4, Milestone 1, Task 2
 * 
 * TITLE: Encoding data and escaping output for display
 * 
 * RISK: Untrusted data must not be included in the web browser since it may contain unsafe code.
 *       In a more complex attack, a malicious user may include JavaScript and HTML. types of attacks.
 *       Untrusted data displayed to the user should neutralize JavaScript and HTML. Use the OWASP
 *       Encoder protect to filter both.
 * 
 * REF: CMU Software Engineering Institute IDS14-J
 * 
 * IMPORTANT: The encoding is applicable in Java as well if you are returning data which needs to
 *            be encoded. This JSP form takes data from the comments parameter and displays it to the user
 *            as a confirmation before final submission.
 *
 *            A hint is provided with the import statement above.
 */ 
 %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>
	<H1>Comment verification for Johnson Autoparts</H1>
	<br>
	<form action="<%=request.getServletContext().getContextPath() %>/app" method='GET'>
	<table>
		<tr><td>Please verify your comment before submission:</td></tr>
		<tr><td><textarea name='param1'><%= request.getParameter("comments") %></textarea>
		<tr><td><br/></td></tr>
			</table>
		<input type='hidden' name='project' value='project4'/>
		<input type='hidden' name='task' value='postComments'/>
		<input type='submit' name='Post Comment'/>
	</form>
</body>
</html>
