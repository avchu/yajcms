package org.yajcms.controller.pages;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yajcms.beans.entities.blobs.FileApi;
import org.yajcms.controller.utils.ResourceNotFoundException;
import org.yajcms.core.PageController;
import org.yajcms.db.entities.BlobEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

@Controller
@Slf4j
public class MainPatternsController {

    private PageController pageController;

    private FileApi fileApi;

    @Autowired
    public void setFileApi(FileApi fileApi) {
        this.fileApi = fileApi;
    }

    @Autowired
    public void setPageController(PageController pageController) {
        this.pageController = pageController;
    }

    @RequestMapping(value = "/_file/{file_name}")
    public void getPage(@PathVariable("file_name") String fileName, HttpServletResponse httpServletResponse) {
        try {
            Optional<BlobEntity> blobEntity = fileApi.get(fileName);
            if (blobEntity.isPresent()) {
                throw new ResourceNotFoundException();
            }
            IOUtils.copy(new ByteArrayInputStream(blobEntity.get().getSource()), httpServletResponse.getOutputStream());
            httpServletResponse.flushBuffer();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @RequestMapping(value = "/**")
    @ResponseBody
    public String getPage(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return pageController.renderPage(httpServletRequest, httpServletResponse);
    }
}
