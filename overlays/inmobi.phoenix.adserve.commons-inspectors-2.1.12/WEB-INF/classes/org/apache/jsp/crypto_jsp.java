package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.inmobi.phoenix.repository.security.CryptoHashRepository;
import com.inmobi.phoenix.repository.security.CryptoHashGenerator;
import com.inmobi.phoenix.kernel.Cache;

public final class crypto_jsp extends org.apache.sling.scripting.jsp.jasper.runtime.HttpJspBase
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
      out.write("<html>\n");
      out.write("<head>\n");
      out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=US-ASCII\">\n");
      out.write("<title>CryptoRepository</title>\n");
      out.write("</head>\n");
      out.write("<body>\n");
      out.write("<hr></br>\n");
      out.write("<form method=\"GET\">\n");
      out.write("<label for=\"key\">Key </label>\n");
      out.write("<input type=\"text\" id=\"key\" name=\"key\">\n");
      out.write("<label for=\"data\">Data To be hashed </label>\n");
      out.write("<input type=\"text\" id=\"data\" name=\"data\">\n");
      out.write("<input type=\"submit\">\n");
      out.write("<input type=\"hidden\" name=\"format\" value=\"html\">\n");
      out.write("</form>\n");
      out.write("</br><hr>\n");

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
