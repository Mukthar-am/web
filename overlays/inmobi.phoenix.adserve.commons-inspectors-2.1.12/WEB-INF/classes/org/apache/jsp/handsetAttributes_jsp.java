package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.inmobi.phoenix.kernel.Cache;
import com.inmobi.phoenix.repository.handset.wurfl.HandsetInternalIdQuery;
import com.inmobi.phoenix.data.RepositoryQuery;
import com.inmobi.phoenix.entity.handset.Handset;
import com.inmobi.phoenix.repository.handset.wurfl.HandsetAttributesRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Collection;
import java.io.PrintWriter;
import java.util.Set;

public final class handsetAttributes_jsp extends org.apache.sling.scripting.jsp.jasper.runtime.HttpJspBase
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

      out.write("\n");
      out.write("\n");
      out.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose\n");
      out.write(" .dtd\">\n");
      out.write("<html>\n");
      out.write("<head>\n");
      out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=US-ASCII\">\n");
      out.write("<title>Handset Attributes Repository</title>\n");
      out.write("</head>\n");
      out.write("<body>\n");

if(request.getParameter("externalId") == null && request.getParameter("internalId") == null) {

      out.write("\n");
      out.write("    <hr></br>\n");
      out.write("    <form method=\"GET\">\n");
      out.write("    <label for=\"externalId\">Handset External id</label>\n");
      out.write("    <input type=\"text\" id=\"externalId\" name=\"externalId\">\n");
      out.write("    <label for=\"internalId\"> &nbsp&nbsp&nbsp&nbsp&nbsp OR &nbsp&nbsp&nbsp&nbsp&nbsp Handset\n");
      out.write("    Internal id</label>\n");
      out.write("    <input type=\"text\" id=\"internalId\" name=\"internalId\">\n");
      out.write("    <input type=\"submit\">\n");
      out.write("    <input type=\"hidden\" name=\"format\" value=\"html\">\n");
      out.write("    </form>\n");
      out.write("    </br><hr>\n");

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

      out.write("\n");
      out.write("</body>\n");
      out.write("</html>\n");
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
