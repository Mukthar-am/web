<%@page import="com.inmobi.phoenix.repository.handset.wurfl.HandsetAttributesRepository,
	com.inmobi.phoenix.repository.handset.wurfl.HandsetDetectionRepository,
	com.inmobi.phoenix.kernel.Cache" %>
<%@ page language="java"
	contentType="application/json; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
	out.println("{");
	try {
		Cache c = Cache.getInstance("CacheInstance");

		HandsetAttributesRepository attrRepo = (HandsetAttributesRepository) c.getRepositoryLocatory().getRepositoryInstance("HandsetAttributesRepository");
		out.println(attrRepo.getJSON() + ",");

		HandsetDetectionRepository detectionRepo = (HandsetDetectionRepository) c.getRepositoryLocatory().getRepositoryInstance("HandsetDetectionRepository");
		out.println(detectionRepo.getJSON());

	} catch (Exception e) {
		out.println(e.getMessage());
	}

	out.println("}");
%>

