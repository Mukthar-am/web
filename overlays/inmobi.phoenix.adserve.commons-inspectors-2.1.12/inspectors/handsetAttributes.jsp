<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<%@ page import="com.inmobi.phoenix.kernel.Cache,
	com.inmobi.phoenix.repository.handset.wurfl.HandsetInternalIdQuery,
	com.inmobi.phoenix.data.RepositoryQuery,
	com.inmobi.phoenix.entity.handset.Handset,
	com.inmobi.phoenix.repository.handset.wurfl.HandsetAttributesRepository,
    com.google.gson.Gson,
	com.google.gson.GsonBuilder,
    java.util.Collection,
    java.io.PrintWriter,
	java.util.Set" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose
 .dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Handset Attributes Repository</title>
</head>
<body>
<%
if(request.getParameter("externalId") == null && request.getParameter("internalId") == null) {
%>
    <hr></br>
    <form method="GET">
    <label for="externalId">Handset External id</label>
    <input type="text" id="externalId" name="externalId">
    <label for="internalId"> &nbsp&nbsp&nbsp&nbsp&nbsp OR &nbsp&nbsp&nbsp&nbsp&nbsp Handset
    Internal id</label>
    <input type="text" id="internalId" name="internalId">
    <input type="submit">
    <input type="hidden" name="format" value="html">
    </form>
    </br><hr>
<%
}
try {
	String externalId = request.getParameter("externalId");
	String internalId = request.getParameter("internalId");
	Cache cache = Cache.getInstance("CacheInstance");
    HandsetAttributesRepository repo = (HandsetAttributesRepository)cache.getRepositoryLocatory()
                                        .getRepositoryInstance("HandsetAttributesRepository");
    if(request.getParameter("externalId") == null &&  request.getParameter("internalId") == null) {
        out.println(repo.getJSON());
        out.println("<br><hr>");
    } else {
		if( null != externalId && !externalId.isEmpty() ) {
			Handset handset = (Handset) repo.query(externalId);
			if( null != handset ) {
				Gson gson = new GsonBuilder().serializeNulls().create();
				out.println(gson.toJson(handset));
            } else {
                out.println("Failed to fetch Handset Attributes from external id");
            }
		} else if(null != internalId && !internalId.isEmpty()) {
            RepositoryQuery internalIdQuery = new HandsetInternalIdQuery(Long.parseLong(internalId));
            Handset handset = (Handset) repo.queryUniqueResult(internalIdQuery);
            if( null != handset ) {
                Gson gson = new GsonBuilder().serializeNulls().create();
                out.println(gson.toJson(handset));
            } else {
                out.println("Failed to fetch Handset Attributes from internal id");
            }
        } else {
            out.println("Failed to fetch Handset Attributes both ids are empty");
        }
	}
} catch (Exception e) {
    e.printStackTrace(new PrintWriter(out));
    out.println("<hr>");
}
%>
</body>
</html>
