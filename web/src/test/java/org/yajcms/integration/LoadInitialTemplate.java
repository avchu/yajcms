package org.yajcms.integration;

import lombok.extern.java.Log;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.yajcms.beans.entities.Entity;
import org.yajcms.beans.entities.blobs.TemplateApiImpl;
import org.yajcms.beans.entities.nosql.EntitiesInitializer;
import org.yajcms.beans.pipeline.EntitiesDao;
import org.yajcms.beans.template.TemplateEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest
@Log
public class LoadInitialTemplate {

    @Autowired
    private TemplateApiImpl templateApi;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private EntitiesInitializer entitiesInitializer;

    @Autowired
    private EntitiesDao entitiesDao;


    @Test
    public void load() {
        File dir = new File("../front/templates/");
        System.out.println(dir.getAbsolutePath());
        File[] files = dir.listFiles();
        List<File> fileList = new ArrayList<>();
        listFiles(files, fileList);
        fileList.forEach(file -> {
            Entity template = entitiesInitializer.createEntity("Template");
            template.putProperty("filePath", file.getAbsolutePath().split("templates")[1]);
            try {
                System.out.println(templateApi.put(template, IOUtils.toByteArray(file.toURI())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void listFiles(File[] files, List<File> fileList) {
        for (File file : files) {
            if (file.isDirectory()) {
                listFiles(file.listFiles(), fileList);
            }
            if (file.getName().endsWith("ftl")) {
                fileList.add(file);
            }
        }
    }

    @Test
    public void testTemplates() {
        Optional<Entity> template = entitiesDao.getOneByQuery("filePath:/main/main_page.ftl", "Template");
        assertTrue(template.isPresent());
        assertFalse(templateEngine.render(template.get(), new HashMap<>()).isEmpty());
    }
}
