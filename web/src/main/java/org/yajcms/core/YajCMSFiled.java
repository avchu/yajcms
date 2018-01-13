package org.yajcms.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class YajCMSFiled {
    private String id;
    private String name;
    private Object value;
    private String type;
    private String ref;
}
