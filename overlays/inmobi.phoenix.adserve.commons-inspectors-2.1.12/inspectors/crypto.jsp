<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<%@page import="com.inmobi.phoenix.repository.security.CryptoHashRepository,
com.inmobi.phoenix.repository.security.CryptoHashGenerator,
com.inmobi.phoenix.kernel.Cache" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>CryptoRepository</title>
</head>
<body>
<hr></br>
<form method="GET">
<label for="key">Key </label>
<input type="text" id="key" name="key">
<label for="data">Data To be hashed </label>
<input type="text" id="data" name="data">
<input type="submit">
<input type="hidden" name="format" value="html">
</form>
</br><hr>
<%
        out.println("</br><hr>");
        try {
                out.println("CryptoHashRepository");
                String keyString = request.getParameter("key");
                Long key = Long.parseLong(keyString);
                String data = request.getParameter("data");

                Cache c = Cache.getInstance("CacheInstance");
                CryptoHashRepository repo = (CryptoHashRepository) c.getRepositoryLocatory().getRepositoryInstance("CryptoHashRepository");
                CryptoHashGenerator cryptoGenerator = repo.query(key);
                String hash = cryptoGenerator.sign(data);

                out.println("Hash value  : " + hash);

        } catch (Exception e) {
                out.println(e);
        }

        out.println("</br><hr>");
%>
</body>
</html>
