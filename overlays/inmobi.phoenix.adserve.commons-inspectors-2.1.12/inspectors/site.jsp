<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<%@ page  import="com.inmobi.phoenix.kernel.Cache,
    com.inmobi.phoenix.entity.site.Site,
    com.inmobi.phoenix.repository.site.SiteRepository,
    com.inmobi.phoenix.repository.site.SiteIdQuery,
    com.inmobi.phoenix.batteries.data.ObjectRepository,
    com.inmobi.phoenix.data.RepositoryQuery,
    java.util.Set,
    java.io.PrintWriter,
    com.google.gson.Gson,
    com.google.gson.GsonBuilder,
    java.util.Collection" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>site- test</title>
</head>
<body>
<%
    if(request.getParameter("siteId") == null)
    {
%>
    <hr></br>
    <form method="GET" target="_blank">
    <label for="siteId">Site inc id</label>
    <input type="text" id="siteId" name="siteId">
    <input type="submit">
    <input type="hidden" name="format" value="html">
    </form>
    </br><hr>
<%
    }
try
{
    String sId = request.getParameter("siteId");
    Cache c = Cache.getInstance("CacheInstance");
    SiteRepository r = (SiteRepository)c.getRepositoryLocatory().getRepositoryInstance("SiteRepository");
    if(sId != null )
    {
        Long siteId = Long.parseLong(sId);
        Site site = (Site) r.query(siteId);
        if (null == site)
        {
            out.println("</br>Site is not present in repo " + siteId);
            out.println("</br>Load failure reason (if dropped when loading to repo) : " + r.getErrorReason(siteId));
        }
        else
        {
            Gson gson = new GsonBuilder().serializeNulls().create();
            out.println(gson.toJson(site));
        }
    }
    else
    {
        out.println(r.getJSON());
        out.println("<hr>");
    }
}
catch (Exception e)
{
    e.printStackTrace(new PrintWriter(out));
    out.println("<hr>");
}
%>
</body>
</html>