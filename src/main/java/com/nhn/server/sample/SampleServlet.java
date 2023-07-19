package com.nhn.server.sample;

import com.nhn.server.servlet.HttpServletRequest;
import com.nhn.server.servlet.HttpServletResponse;
import com.nhn.server.servlet.SimpleServlet;

import java.io.PrintWriter;

public class SampleServlet extends SimpleServlet {
    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) {
        PrintWriter out = res.getWriter();

        String body = "<HTML>\r\n" +
                "<HEAD><TITLE> Dooray </TITLE>\r\n" +
                "</HEAD>\r\n" +
                "<BODY>" +
                "<H1> service.Hello Dooray </H1>\r\n" +
                "</BODY></HTML>\r\n";

        res.setHeader("HTTP/1.0 200 OK", "text/html; charset=utf-8", body.getBytes().length);
        out.write(body);
        out.flush();
    }
}
