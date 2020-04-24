package com.stephendemo.elasticsearch;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

/**
 * @author jmfen
 * date 2020-04-24
 */

@Data
@Document(indexName = "company", type = "employee", shards = 1, replicas = 0, refreshInterval = "-1")
public class Employee {
    @Id
    private String id;

    private String firstName;

    private String lastName;

    private Integer age = 0;

    private String about;
}