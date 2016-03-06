<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<%@ page  import="com.inmobi.adserve.repository.backfill.BackFillRepository,
                  com.inmobi.phoenix.kernel.Cache"%>

<%
try
{
    Cache            c = Cache.getInstance("CacheInstance");
    BackFillRepository r = (BackFillRepository)c.getRepositoryLocatory().getRepositoryInstance("BackFillRepository");

    out.println( r.getStats() );
}
catch (Exception e)
{
        out.println(e.getMessage());
}
%>

