package org.yajcms.controller.pages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yajcms.core.PageController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class MainPatternsController {

    PageController pageController;

    @Autowired
    public void setPageController(PageController pageController) {
        this.pageController = pageController;
    }

    @RequestMapping(value = "/**")
    @ResponseBody
    public String getPage(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return pageController.renderPage(httpServletRequest, httpServletResponse);
    }
}
