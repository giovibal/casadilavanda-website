<%@ page import="java.net.URL" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
  URL url = new URL(request.getScheme(),
          request.getServerName(),
          request.getServerPort(),
          request.getContextPath());
  String lang = request.getLocale().getLanguage();
  String redirectUrl = url.toExternalForm() + "/en/";
  if("de".equalsIgnoreCase(lang)) {
    redirectUrl = url.toExternalForm() + "/de/";
  } else {
    redirectUrl = url.toExternalForm() + "/en/";
  }
%>
<%--Redirecting to <%= redirectUrl %> ...--%>
<meta http-equiv="refresh" content="0;URL='<%= redirectUrl %>'" />
