package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.inmobi.phoenix.kernel.Cache;
import com.inmobi.phoenix.entity.metricmanager.MetricEvent;
import com.inmobi.phoenix.terminationcodes.TerminationCode;
import com.inmobi.phoenix.terminationcodes.TerminationUtils;
import com.inmobi.phoenix.util.metricmanager.MetricManagerUtility;
import com.inmobi.adserve.ccid.CcidResolver;
import com.inmobi.adserve.ccid.BlackberryCcidResolver;
import com.inmobi.adserve.ccid.IpBasedCcidResolver;
import com.inmobi.adserve.ccid.CompositeCcidDetectionService;
import com.inmobi.phoenix.repository.ccid.CcidRepositoryInterface;
import com.inmobi.phoenix.repository.blackberry.BlackberryVendorIDRepositoryInterface;
import com.inmobi.phoenix.entity.Carrier;
import com.inmobi.adserve.ccid.CcidRequest;
import com.inmobi.adserve.ccid.CcidResponse;
import java.util.*;

public final class ccidLookup_jsp extends org.apache.sling.scripting.jsp.jasper.runtime.HttpJspBase
    implements org.apache.sling.scripting.jsp.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.sling.scripting.jsp.jasper.runtime.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.sling.scripting.jsp.jasper.runtime.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.sling.scripting.jsp.jasper.runtime.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      response.setContentType("application/json; charset=ISO-8859-1");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write('\n');
      out.write('\n');

        //This is an optional request param.
        //If you are trying to find carrier details for a blackberry vendor
        // you should provide the userAgent as a request param.
        String userAgent = request.getParameter("userAgent");

        //This is a compulsory request param for ip based ccid resolution.
        String ipStr = request.getParameter("ipStr");
        
        //This is an optional parameter.
        //Specify it only if you want to query the older feed file.
        String ipFileVersionStr = request.getParameter("feedFileVersion");
        Long ipFileVersion = null;
        try {
          if (ipFileVersionStr != null) {
              ipFileVersion = Long.parseLong(ipFileVersionStr);
          }
        } catch (NumberFormatException e) {
          out.println("NumberFormatException encountered in parsing feedFileVersion");
        }

        Byte fileVersion = ipFileVersion == null ? null : ipFileVersion.byteValue();
        try {
                Cache c = Cache.getInstance("CacheInstance");
                //1. CarrierRepo
                CcidRepositoryInterface carrierRepo = (CcidRepositoryInterface) 
                                       c.getRepositoryLocatory().
                                       getRepositoryInstance("CcidRepository");

                //2. Blackberry Repo
                BlackberryVendorIDRepositoryInterface blackberryRepo = 
                            (BlackberryVendorIDRepositoryInterface)  c.getRepositoryLocatory().
                                       getRepositoryInstance("BlackberryVendorIDRepository");
                //metricEventList
                List<MetricEvent> listMetricEvent = MetricManagerUtility.
                            createNewOrReturnEventListIfNotPresent(null, null);
                
                //terminationCodeList
                @SuppressWarnings("unchecked")
                List<TerminationCode> terminationReasons = TerminationUtils.createNewIfNull(null);
                
                List<CcidResolver> resolvers = new ArrayList<CcidResolver>(2);
                resolvers.add(new BlackberryCcidResolver(blackberryRepo));
                resolvers.add(new IpBasedCcidResolver(carrierRepo));
                CompositeCcidDetectionService ccidDetectionService = 
                            new CompositeCcidDetectionService(Collections.unmodifiableList(resolvers));

                CcidRequest ccidRequest = CcidRequest.builder()
                                .userAgent(userAgent)
                                .fileVersion(fileVersion)
                                .ipv4(ipStr)
                                .requestIpv4(null)
                                .traceMarker(null)
                                .metricEventList(listMetricEvent)
                                .terminationCodeList(terminationReasons)
                                .build();

                CcidResponse ccidResponse = ccidDetectionService.getCarrier(ccidRequest);
                if (ccidResponse.isSuccess()) {
                      Carrier carrier = ccidResponse.getCarrier();
                      if (carrier != null) {
                        out.println(carrier.toString());
                      } else {
                        out.println("Carrier Not Found");
                      }
                
                } else {
                        out.println("Carrier Not Found");
                }

        } catch (Exception e) {
                out.println(e.getMessage());
        }

      out.write('\n');
      out.write('\n');
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try { out.clearBuffer(); } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
