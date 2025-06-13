package filter;

import java.io.IOException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;

@WebFilter("/*")
public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest  request  = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String uri = request.getRequestURI();
        
        boolean isLoginPage     = uri.endsWith("/login.jsp");
        boolean isLoginAction   = uri.endsWith("/logincontroller");
        boolean isStaticResource= uri.contains("/css/") || uri.contains("/js/") || uri.contains("/images/");
        
        HttpSession session = request.getSession(false);
        boolean loggedIn = (session != null && session.getAttribute("loggedUser") != null);
        
        if (loggedIn || isLoginPage || isLoginAction || isStaticResource) {
            chain.doFilter(req, res);
        } else {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        }
    }
}
