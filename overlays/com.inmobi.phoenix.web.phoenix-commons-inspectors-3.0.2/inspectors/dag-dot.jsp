<%@ page language="java" contentType="text/plain; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<%  response.setHeader("Access-Control-Allow-Origin","*"); %>
<%@ page import="java.io.OutputStream" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="org.springframework.context.ApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="com.google.common.collect.ImmutableSet" %>
<%@ page import="com.google.common.collect.ImmutableList" %>
<%@ page import="com.inmobi.phoenix.api.WorkerFactory" %>
<%@ page import="com.inmobi.phoenix.fsm.Fsm" %>
<%@ page import="com.inmobi.phoenix.fsm.Edge" %>
<%@ page import="com.inmobi.phoenix.fsm.ActivityState" %>
<%@ page import="com.inmobi.phoenix.composites.SerialWorkerChain" %>
<%!
String scrubName(String s) {
    return  s.replaceAll("[^a-zA-Z0-9]", "_");
}
%>
<%!
void stateSorter(ActivityState as, Set<String> nodes, boolean summaryOnly) {
    nodes.add(formatRns(as, summaryOnly));
}
%>
<%!
String formatRns(ActivityState as, boolean summaryOnly) {
    return summaryOnly ? stateSummary(as) : stateDetails(as);
}
%>
<%!
String stateSummary(ActivityState as) {
    final String s = as.getName();
    final String shape = as.isEndState() ? "doublecircle" : "circle";
    return String.format("node [shape = %s]; rns__%s [ label=\"%s\"];", shape, scrubName(s), s);
}
%>
<%!
String stateDetails(ActivityState as) {
    final String s = as.getName();
    StringBuilder contents = new StringBuilder();
    WorkerFactory awf = as.getWorkerFactory();
    if(awf instanceof SerialWorkerChain) {
        SerialWorkerChain sas = (SerialWorkerChain) awf;
        contents.append(String.format("<tr><td BGCOLOR=\"bisque\">%s</td></tr>", s ));
        for(WorkerFactory wf: sas.getFactoryChain() ) {
            contents.append(String.format("<tr><td>%s</td></tr>", wf.getClass().getName() ));
        }
        return String.format("node [shape=plaintext];rns__%s [ label=" +
                "<\n<table  BORDER=\"0\" CELLBORDER=\"1\" CELLSPACING=\"0\" STYLE=\"ROUNDED\">" +
                "\n%s\n</table>\n>];",
            scrubName(s), contents.toString());
    } else {
        return stateSummary(as);
    }
}
%>
<%
final ApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
final String beanName = request.getParameter("fsm");
if(null == beanName || "".equals(beanName)) {
    out.println("query string fsm has no value");
} else {
    final boolean summaryOnly = "v2".equals(request.getParameter("mode"));

    final Fsm fsm = context.getBean(beanName, Fsm.class);
    out.println("digraph " + fsm.getName() + '{');

    if(summaryOnly) {
        out.println("rankdir=LR;");
    }

    final List<String> edges = new ArrayList<String>();
    final Set<String> nodes = new HashSet<String>();

    for(Edge e: fsm.getTransitions()) {
        final ActivityState fromState = e.getFromState();
        final ActivityState toState = e.getToState();

        stateSorter(fromState, nodes, summaryOnly);
        stateSorter(toState, nodes, summaryOnly);

        edges.add(String.format("rns__%s -> rns__%s [ label=\"%s\"];",
            scrubName(fromState.getName()),
            scrubName(toState.getName()),
            e.getPreCondition()
            ));
    }

    for(String s: nodes) {
        out.println(s);
    }

    for(String s: edges) {
        out.println(s);
    }
	out.println('}');
}
%>