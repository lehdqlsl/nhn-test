package com.nhn.server.servlet;

import com.nhn.server.exception.NotFoundException;

import java.io.*;

public class DefaultServlet extends SimpleServlet {

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) {
        try {
            PrintWriter out = res.getWriter();
            InputStream resourceAsStream = getClass()
                    .getClassLoader()
                    .getResourceAsStream(getServletConfig().rootDirectory(req.host()) + req.getUri());

            if (resourceAsStream == null) {
                throw new NotFoundException();
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(resourceAsStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            String html = stringBuilder.toString();
            res.setHeader("HTTP/1.0 200 OK", "text/html; charset=utf-8", html.length());
            out.write(html);
            out.flush();
        } catch (IOException e) {

        }
    }
}