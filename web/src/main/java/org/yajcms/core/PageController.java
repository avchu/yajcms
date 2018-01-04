package org.yajcms.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yajcms.beans.blocks.PageBlockInterface;
import org.yajcms.beans.pipeline.EntitiesDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class PageController implements PageBlockInterface {

    private EntitiesDao entitiesDao;

    @Autowired
    public void setEntitiesDao(EntitiesDao entitiesDao) {
        this.entitiesDao = entitiesDao;
    }

    @Override
    public String renderPage(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        return null;
    }
}
