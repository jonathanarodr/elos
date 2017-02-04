package br.com.elos.route;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(filterName = "middleware", servletNames = {"route"})
public class RouteFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest requestFilter, ServletResponse responseFilter, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) requestFilter;
        HttpServletResponse httpResponse = (HttpServletResponse) responseFilter;
        HttpSession session = httpRequest.getSession();
       
        chain.doFilter(requestFilter, responseFilter);
        
        //csrf-token
    }

    @Override
    public void destroy() {}
    
}