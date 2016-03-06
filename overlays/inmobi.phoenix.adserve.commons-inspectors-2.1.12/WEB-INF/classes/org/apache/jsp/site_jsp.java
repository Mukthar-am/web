package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.inmobi.phoenix.kernel.Cache;
import com.inmobi.phoenix.entity.site.Site;
import com.inmobi.phoenix.repository.site.SiteRepository;
import com.inmobi.phoenix.repository.site.SiteIdQuery;
import com.inmobi.phoenix.batteries.data.ObjectRepository;
import com.inmobi.phoenix.data.RepositoryQuery;
import java.util.Set;
import java.io.PrintWriter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Collection;

public final class site_jsp extends org.apache.sling.scripting.jsp.jasper.runtime.HttpJspBase
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
      response.setContentType("text/html; charset=US-ASCII");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n");
      out.write("\r\n");
      out.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n");
      out.write("<html>\r\n");
      out.write("<head>\r\n");
      out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=US-ASCII\">\r\n");
      out.write("<title>site- test</title>\r\n");
      out.write("</head>\r\n");
      out.write("<body>\r\n");

    if(request.getParameter("siteId") == null)
    {

      out.write("\r\n");
      out.write("    <hr></br>\r\n");
      out.write("    <form method=\"GET\" target=\"_blank\">\r\n");
      out.write("    <label for=\"siteId\">Site inc id</label>\r\n");
      out.write("    <input type=\"text\" id=\"siteId\" name=\"siteId\">\r\n");
      out.write("    <input type=\"submit\">\r\n");
      out.write("    <input type=\"hidden\" name=\"format\" value=\"html\">\r\n");
      out.write("    </form>\r\n");
      out.write("    </br><hr>\r\n");

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

      out.write("\r\n");
      out.write("</body>\r\n");
      out.write("</html>");
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
