package org.yajcms.beans.blocks;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface PageBlockInterface {
    String renderPage(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);
}
