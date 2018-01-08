package org.yajcms.beans.template;

import org.yajcms.beans.entities.Entity;

import java.util.HashMap;
import java.util.List;

public interface TemplateEngine {
    List<Entity> getAllTemplates();

    String render(Entity template, HashMap<String, Object> data);
}
