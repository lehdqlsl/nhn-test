package com.nhn.server.servlet;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class ServletConfig {
    private final List<ServletMapping> servletMappings;

    public ServletConfig(List<ServletMapping> servletMappings) {
        this.servletMappings = servletMappings;
    }

    public boolean isServlet(String uri) {
        return servletMappings.stream()
                .anyMatch(servletMapping -> servletMapping.isMapping(uri));
    }

    private String getServletName(String uri) {
        return servletMappings.stream()
                .filter(servletMapping -> servletMapping.isMapping(uri))
                .map(ServletMapping::getServletClass)
                .findFirst()
                .orElse(null);
    }

    public SimpleServlet getServlet(String uri) {
        String servletName = getServletName(uri);
        try {
            Class<?> servlet = getClass().getClassLoader().loadClass(servletName);
            return (SimpleServlet) servlet.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
