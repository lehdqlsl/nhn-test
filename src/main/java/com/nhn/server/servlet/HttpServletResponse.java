package com.nhn.server.servlet;

import java.io.PrintWriter;

public interface HttpServletResponse {

    void sendError(String forbidden);

    PrintWriter getWriter();

    void setHeader(String responseCode, String contentType, int length);

}
