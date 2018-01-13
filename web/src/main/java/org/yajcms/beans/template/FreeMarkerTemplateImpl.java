package org.yajcms.beans.template;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.yajcms.beans.entities.Entity;
import org.yajcms.beans.entities.blobs.TemplateApiImpl;
import org.yajcms.db.entities.BlobEntity;

import javax.annotation.PostConstruct;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Slf4j
public class FreeMarkerTemplateImpl implements TemplateEngine {

    private TemplateApiImpl templateApi;

    private Configuration cfg;

    @Autowired
    public void setTemplateApi(TemplateApiImpl templateApi) {
        this.templateApi = templateApi;
    }

    @PostConstruct
    public void init() {
        cfg = new Configuration(Configuration.VERSION_2_3_27);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);
        StringTemplateLoader stringLoader = new StringTemplateLoader();

        getAllTemplates().forEach(template -> {
            Optional<BlobEntity> blobEntity = templateApi.get(template);

            blobEntity.ifPresent(ftl -> {
                String filePath = template.getPropertyString("filePath");
                if (filePath.startsWith("/")) {
                    filePath = filePath.replaceFirst("/", "");
                }
                stringLoader.putTemplate(filePath,
                        new String(ftl.getSource(), Charset.forName("utf-8")));
            });
        });
        cfg.setTemplateLoader(stringLoader);
    }

    @Override
    public List<Entity> getAllTemplates() {
        return templateApi.getAllTemplates();
    }

    @Override
    public String render(Entity template, HashMap<String, Object> data) {
        try {
            StringWriter output = new StringWriter();
            Template temp = cfg.getTemplate(template.getPropertyString("filePath"));
            temp.process(data, output);
            return output.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
