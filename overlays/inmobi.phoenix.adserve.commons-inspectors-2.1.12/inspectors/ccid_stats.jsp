<%@page import="com.inmobi.phoenix.repository.ccid.CcidRepository,
        com.inmobi.phoenix.kernel.Cache" %>
<%@ page language="java"
        contentType="application/json; charset=ISO-8859-1"
        pageEncoding="ISO-8859-1"%>
<%
        out.println("{");
        try {
                Cache c = Cache.getInstance("CacheInstance");

                CcidRepository ccidRepo = (CcidRepository) c.getRepositoryLocatory().getRepositoryInstance("CcidRepository");
                out.println(ccidRepo.getJSON());
        } catch (Exception e) {
                out.println(e.getMessage());
        }

        out.println("}");
%>

