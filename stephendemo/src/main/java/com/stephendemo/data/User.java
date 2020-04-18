package com.stephendemo.data;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author jmfen
 * date 2020-04-13
 */

@Data
@Document
public class User {
    @Field
    private String name;

    @Field
    private Integer aget;

    @Field
    private String add;
}