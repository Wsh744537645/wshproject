package com.stephendemo.controller;

import com.stephendemo.elasticsearch.employee.Employee;
import com.stephendemo.elasticsearch.employee.EmployeeRepository;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author jmfen
 * date 2020-04-24
 */

@RestController
public class EmployeeController {
    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("/es/employee/add")
    public Employee add(@RequestBody Employee employee){
        return employeeRepository.save(employee);
    }

    @GetMapping("/es/employee/delete")
    public Employee delete(String id){
        Employee employee = employeeRepository.queryEmployeeById(id);
        if(employee != null){
            employeeRepository.delete(employee);
        }
        return employee;
    }

    @GetMapping("/es/employee/update")
    public Employee update(String id, String firstName){
        Employee employee = employeeRepository.queryEmployeeById(id);
        if(employee != null){
            employee.setFirstName(firstName);
            employeeRepository.save(employee);
        }
        return employee;
    }

    @GetMapping("/es/employee/query")
    public Employee query(String id){
        //return queryEmployeeById(id);
        return employeeRepository.queryEmployeeById(id);
    }

    private Employee queryEmployeeById(String id){
        return employeeRepository.findById(id).get();
    }


    //以下分页处理参照 https://juejin.im/post/5aec0b386fb9a07abb23784d

    //每页数量
    private Integer PAGESIZE=10;

    //http://localhost:8888/getGoodsList?query=商品
    //http://localhost:8888/getGoodsList?query=商品&pageNumber=1
    //根据关键字"商品"去查询列表，name或者description包含的都查询
//    @GetMapping("getGoodsList")
//    public List<Employee> getList(Integer pageNumber, String query){
//        if(pageNumber==null) pageNumber = 0;
//        //es搜索默认第一页页码是0
//        SearchQuery searchQuery=getEntitySearchQuery(pageNumber,PAGESIZE,query);
//        Page<Employee> goodsPage = employeeRepository.search(searchQuery);
//        return goodsPage.getContent();
//    }
//
//
//    private SearchQuery getEntitySearchQuery(int pageNumber, int pageSize, String searchContent) {
//        FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery()
//                .add(QueryBuilders.matchPhraseQuery("name", searchContent),
//                        ScoreFunctionBuilders.weightFactorFunction(100))
//                .add(QueryBuilders.matchPhraseQuery("description", searchContent),
//                        ScoreFunctionBuilders.weightFactorFunction(100))
//                //设置权重分 求和模式
//                .scoreMode("sum")
//                //设置权重分最低分
//                .setMinScore(10);
//
//        // 设置分页
//        Pageable pageable = new PageRequest(pageNumber, pageSize);
//        return new NativeSearchQueryBuilder()
//                .withPageable(pageable)
//                .withQuery(functionScoreQueryBuilder).build();
//    }
}