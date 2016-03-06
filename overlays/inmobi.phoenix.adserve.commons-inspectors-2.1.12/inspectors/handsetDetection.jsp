<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<%@ page import="com.inmobi.phoenix.kernel.Cache,
	com.inmobi.phoenix.repository.handset.wurfl.UserAgentQuery,
	com.inmobi.phoenix.repository.handset.wurfl.HandsetDetectionRepository,
	com.inmobi.phoenix.entity.handset.HandsetIdEntity,	
	java.net.URLDecoder,
        java.util.Collection,
	java.util.Set" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Handset Detection Repository</title>
</head>
<body>
<%
	String userAgent = request.getParameter("userAgent");
	if( null != userAgent && !userAgent.isEmpty() )
	{
		Cache cache = Cache.getInstance("CacheInstance");
		if( null != cache )
		{
			HandsetDetectionRepository repo = (HandsetDetectionRepository)cache.getRepositoryLocatory().getRepositoryInstance("HandsetDetectionRepository");
			
			UserAgentQuery query = new UserAgentQuery(URLDecoder.decode(userAgent, "UTF-8"));
			HandsetIdEntity handsetExternalIdEntity = repo.queryUniqueResult(query);
			if( null != handsetExternalIdEntity )
			{
				if( handsetExternalIdEntity.isBot() )
				{
					out.println("User agent: " + userAgent + " is detected as a BOT");
				}
				else
				{
					out.println("User Agent: " + userAgent);
					out.println("External id corresponding to the above User agent: " + handsetExternalIdEntity.getId());
				}
			}
			else
			{
				out.println("Handset Detection failed. HandsetIDEntity returned was null for UA: " + userAgent);
			}
		}
		else
		{
			out.println("CacheInstance retrieved via kernel is null");
		}
		
	}
	else
	{
		out.println("Empty or null value in parameter: userAgent");
	}
%>
<br>
</body>
</html>
