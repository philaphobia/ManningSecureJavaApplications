<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="org.owasp.html.PolicyFactory"%>
<%@page import="org.owasp.html.HtmlPolicyBuilder" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>
	<H1>Blog verification for Johnson Autoparts</H1>
	<br>
	<form action="<%=request.getServletContext().getContextPath() %>/app" method='GET'>
	<table>
		<tr><td>Please add the blog post in the text area below (HTML allowed):</td></tr>
		<tr><td><textarea name='param1'></textarea>
		<tr><td><br/></td></tr>
			</table>
		<input type='hidden' name='project' value='project4'/>
		<input type='hidden' name='task' value='postBlog'/>
		<input type='submit' name='Blog'/>
	</form>
</body>
</html>
