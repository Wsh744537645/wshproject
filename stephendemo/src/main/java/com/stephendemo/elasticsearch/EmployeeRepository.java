package com.stephendemo.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

/**
 * @author jmfen
 * date 2020-04-24
 */

@Component
public interface EmployeeRepository extends ElasticsearchRepository<Employee, String> {

    Employee queryEmployeeById(String id);
}
