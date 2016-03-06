package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.inmobi.phoenix.kernel.Cache;
import com.inmobi.phoenix.repository.handset.wurfl.UserAgentQuery;
import com.inmobi.phoenix.repository.handset.wurfl.HandsetDetectionRepository;
import com.inmobi.phoenix.entity.handset.HandsetIdEntity;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Set;

public final class handsetDetection_jsp extends org.apache.sling.scripting.jsp.jasper.runtime.HttpJspBase
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
      out.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n");
      out.write("<html>\n");
      out.write("<head>\n");
      out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=US-ASCII\">\n");
      out.write("<title>Handset Detection Repository</title>\n");
      out.write("</head>\n");
      out.write("<body>\n");

	String userAgent = request.getParameter("userAgent");
	if( null != userAgent && !userAgent.isEmpty() )
	{
		Cache cache = Cache.getInstance("CacheInstance");
		if( null != cache )
		{
			HandsetDetectionRepository repo = (HandsetDetectionRepository)cache.getRepositoryLocatory().getRepositoryInstance("HandsetDetectionRepository");
			
			UserAgentQuery query = new UserAgentQuery(URLDecoder.decode(userAgent, "UTF-8"));
			HandsetIdEntity handsetExternalIdEntity = repo.queryUniqueResult(query);
			if( null != handsetExternalIdEntity )
			{
				if( handsetExternalIdEntity.isBot() )
				{
					out.println("User agent: " + userAgent + " is detected as a BOT");
				}
				else
				{
					out.println("User Agent: " + userAgent);
					out.println("External id corresponding to the above User agent: " + handsetExternalIdEntity.getId());
				}
			}
			else
			{
				out.println("Handset Detection failed. HandsetIDEntity returned was null for UA: " + userAgent);
			}
		}
		else
		{
			out.println("CacheInstance retrieved via kernel is null");
		}
		
	}
	else
	{
		out.println("Empty or null value in parameter: userAgent");
	}

      out.write("\n");
      out.write("<br>\n");
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
