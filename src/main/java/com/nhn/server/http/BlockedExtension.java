package com.nhn.server.http;

import java.util.Arrays;

public enum BlockedExtension {
    EXE("exe"),
    DLL("dll"),
    BAT("bat"),
    CMD("cmd"),
    SH("sh"),
    PHP("php"),
    PY("py"),
    ASP("asp"),
    ASPX("aspx"),
    JSP("jsp"),
    CGI("cgi"),
    JAR("jar"),
    WAR("war"),
    MSI("msi"),
    COM("com"),
    VBS("vbs"),
    REG("reg"),
    SWF("swf"),
    PIF("pif"),
    SCR("scr");

    private final String extension;

    BlockedExtension(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }

    public static boolean contains(String extension) {
        return Arrays.stream(BlockedExtension.values())
                .map(BlockedExtension::getExtension)
                .anyMatch(ext -> ext.equalsIgnoreCase(extension));
    }
}
