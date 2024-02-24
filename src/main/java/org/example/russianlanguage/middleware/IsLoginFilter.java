package org.example.russianlanguage.middleware;

import org.example.russianlanguage.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = "/")
public class IsLoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig){

    }
    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        User user = getUserFromSession(httpRequest);
        if (user == null) {
            httpResponse.sendRedirect("/req");
            return;
        }
        chain.doFilter(request, response);
    }

    private User getUserFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return null;
        }

        return (User) session.getAttribute("user");
    }

}