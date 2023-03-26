package com.springproj.demo;

import com.springproj.demo.BatchJob.CustomerBatchJob;
import com.springproj.demo.Repository.CreateSQLTable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

@RestController
@SpringBootApplication
public class MyApplication {

	@RequestMapping("/")
	String home() {
		return "Hello World!";
	}

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(MyApplication.class, args);

		JdbcTemplate jdbcTemplate = context.getBean(JdbcTemplate.class);
		 // create customerTable
		CreateSQLTable createTable = new CreateSQLTable(jdbcTemplate);
		createTable.createTablesAndRelationships();

		// Create a batch job for Customer
		CustomerBatchJob cusBatchJob = context.getBean(CustomerBatchJob.class);
		String fileName = "dataSource.txt";
		cusBatchJob.readTextFile(fileName);
	}


}