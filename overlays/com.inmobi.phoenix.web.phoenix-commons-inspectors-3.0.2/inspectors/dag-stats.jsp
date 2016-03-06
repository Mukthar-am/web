<%@ page language="java" contentType="text/plain; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<%@ page import="java.net.URL" %>
<%@ page import="java.net.URLConnection" %>
<%@ page import="java.util.SortedMap" %>
<%@ page import="java.util.Map.Entry" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.TreeMap" %>
<%@ page import="java.lang.Object" %>
<%@ page import="java.io.InputStreamReader" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %> 
<%@ page import="org.json.simple.JSONValue" %>
<%@ page import="org.json.simple.JSONObject" %>
<%@ page import="org.json.simple.parser.JSONParser" %>
<%
    try {
            URL yammerOutput = new URL("http://localhost:8080/phoenix/metrics3");
            URLConnection yc = yammerOutput.openConnection();
            JSONParser parser = new JSONParser();
            Object allMetricsObject = parser.parse(new InputStreamReader(yc.getInputStream()));
            JSONObject allMetrics =  (JSONObject) allMetricsObject;
            JSONObject allMeters = (JSONObject) allMetrics.get("meters");
            JSONObject allTimers = (JSONObject) allMetrics.get("timers");

            //getting service level metrics
            JSONObject serviceObj = (JSONObject) allTimers.get(
                            "com.inmobi.phoenix.afterlife.LifeAfterLifeFsm.adserve.earth.overall");
            Double overallMean = (Double) serviceObj.get("mean");
            Long overallInvocations = (Long) serviceObj.get("count");
            Double overallNanos = (Double) overallMean * overallInvocations * 1000000;
            JSONObject newServiceObj = new JSONObject();
            newServiceObj.put("nanos", overallNanos.longValue());
            newServiceObj.put("invocations", overallInvocations);
            newServiceObj.put("in-flight", 0);
            newServiceObj.put("lost", 0);
            newServiceObj.put("retries", 0);
            newServiceObj.put("reconnections", 0);

            //To get service level failures
            JSONObject hell = (JSONObject) allTimers.get(
                            "com.inmobi.phoenix.afterlife.LifeAfterLifeFsm.adserve.hell.overall");
            Long errorInvocations = (Long) hell.get("count");
            Long successInvocations = overallInvocations - errorInvocations;
            newServiceObj.put("success", successInvocations);
            newServiceObj.put("failures", errorInvocations);

            //To get service level terminates
            String terminateStr =
                            "com.inmobi.phoenix.afterlife.LifeAfterLifeFsm.adserve.earth.state.REQUEST_VALIDATION";
            JSONObject terminate = (JSONObject) allMeters.get(terminateStr + ".symbolEmit.TERMINATE");
            JSONObject sandboxTerminate = (JSONObject) allMeters.get(terminateStr + ".symbolEmit.SANDBOX_TERMINATE");
            Long terminatedRequests = (Long) terminate.get("count");
            Long sandboxTerminatedRequests = 0l;
            if (sandboxTerminate != null) {
                    sandboxTerminatedRequests = (Long) sandboxTerminate.get("count");
            }
            newServiceObj.put("terminates", terminatedRequests);
            newServiceObj.put("sandbox-terminates", sandboxTerminatedRequests);
            JSONObject serviceStatsObject = new JSONObject(); 
            serviceStatsObject.put("stats", newServiceObj); 
            SortedMap<String, JSONObject> map = new TreeMap<String, JSONObject>();
            map.put("service", serviceStatsObject);


            JSONObject workers = new JSONObject(); 
            Iterator i = allTimers.entrySet().iterator();
            while (i.hasNext()) {
                try {
                        Entry entry =(Entry) i.next(); 
                        String field = entry.getKey().toString();
                        if(field.startsWith("com.inmobi.phoenix.afterlife.LifeAfterLifeFsm.adserve.earth.worker") || field.startsWith("com.inmobi.phoenix.kernel.fsm.TungstenCoordinator.worker")) {
                              JSONObject overall = (JSONObject)  allTimers.get(field);
                              Long invocations = (Long) overall.get("count");
                              Double mean = (Double) overall.get("mean");
                              Double nanos = (Double) mean * invocations * 1000000;
                              //get the error metrics
                              String base = StringUtils.removeEnd(field, ".overall");
                              JSONObject error = (JSONObject) allMeters.get(base + ".error");
                              Long errorCount = (Long) error.get("count");
                              Long successCount = invocations - errorCount;
                              JSONObject newinnerObj = new JSONObject();
                              newinnerObj.put("nanos", nanos.longValue());
                              newinnerObj.put("invocations", invocations);
                              newinnerObj.put("success", successCount);
                              newinnerObj.put("failures", errorCount);
                              newinnerObj.put("terminates", 0);
                              newinnerObj.put("in-flight", 0);
                              newinnerObj.put("lost", 0);
                              newinnerObj.put("retries", 0);
                              newinnerObj.put("reconnections", 0);
                              JSONObject statsObj = new JSONObject();
                              statsObj.put("stats", newinnerObj);
                              String[] classTokens = StringUtils.split(field, '.'); 
                              String workerName = classTokens[classTokens.length - 2];
                              workers.put(workerName, statsObj);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }

            map.put("workers", workers);
            String jsonText = JSONValue.toJSONString(map);
            out.println(jsonText);
    }
    catch(Exception e) {
        e.printStackTrace();
    }
%>
