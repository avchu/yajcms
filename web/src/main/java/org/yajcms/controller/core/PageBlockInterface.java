package org.yajcms.controller.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface PageBlockInterface {
    String renderPage(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);
}
