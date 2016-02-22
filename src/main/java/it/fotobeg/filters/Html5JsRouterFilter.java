package it.fotobeg.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by giova_000 on 01/01/2015.
 */
public class Html5JsRouterFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        if(request instanceof HttpServletRequest) {
            HttpServletRequest req = (HttpServletRequest) request;
            String cp = req.getContextPath();
            String uri = req.getRequestURI();
            String path = uri.substring(cp.length());
            if(path.startsWith("/assets")
                    || path.startsWith("/_ah")
                    || path.startsWith("/css")
                    || path.startsWith("/js")
                    || path.startsWith("/partials")
                    || path.startsWith("/api")
                    || path.startsWith("/img")
                    || path.startsWith("/index.html")
                    || path.startsWith("/index.jsp")
                    )
            {
                chain.doFilter(request, response);
            }
            else {
                request.getRequestDispatcher("/index.jsp").include(request, response);
            }
        }
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
