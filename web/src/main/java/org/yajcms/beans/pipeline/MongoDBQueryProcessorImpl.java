package org.yajcms.beans.pipeline;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.yajcms.db.utils.exceptions.NullQueryException;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class MongoDBQueryProcessorImpl implements QueryLanguageProcessor {

    private String propertiesPath;

    @Value("${yajcms.entity.query.property.tag:properties}")
    public void setPropertiesPath(String propertiesPath) {
        this.propertiesPath = propertiesPath;
    }

    /**
     * Waste a lot of time to create own language parser.
     * Will be nice work for University
     *
     * @param query
     * @return
     */
    //TODO:AST
    @Override
    public Object makeQuery(String query) {
        Query mongoQuery = new Query();
        if (query == null || query.isEmpty()) {
            throw new NullQueryException();
        }
        log.debug("Parsing query: {}", query);
        List<String> tokens = Arrays.asList(query.split(":"));
        for (int i = 0; i < tokens.size(); i++) {
            mongoQuery.addCriteria(new Criteria(String.format("%s.%s.value", propertiesPath, tokens.get(i)))
                    .is(tokens.get(i + 1)));
            i++;
        }
        return mongoQuery;
    }
}
