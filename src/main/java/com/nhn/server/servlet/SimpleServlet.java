package com.nhn.server.servlet;


import com.nhn.server.config.ServletConfig;

public abstract class SimpleServlet {
    private ServletConfig servletConfig;

    public abstract void service(HttpServletRequest req, HttpServletResponse res);

    public void init(ServletConfig servletConfig) {
        this.servletConfig = servletConfig;
    }

    public ServletConfig getServletConfig() {
        return servletConfig;
    }
}
