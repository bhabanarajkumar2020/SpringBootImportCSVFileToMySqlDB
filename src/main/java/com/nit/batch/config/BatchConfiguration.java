package com.nit.batch.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.nit.batch.model.Product;
import com.nit.batch.processor.ProductProcessor;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	//Create STEP	
	@Bean
	public Step itemStepA() {
		return stepBuilderFactory.get("itemStepA")
				.<Product,Product>chunk(3)
				.reader(reader())
				.processor(processor())
				.writer(writer())
				.build();	
	}
	//Create JOB	
	@Bean
	public Job itemJobA() {		
		return jobBuilderFactory.get("itemJobA")
				.incrementer(new RunIdIncrementer())
				.start(itemStepA())
				.build();		
	}		
	//1.Create Reader
	@Bean
	public ItemReader<Product>reader(){
	//Create FlatFileItemReader to read and load csv file
		FlatFileItemReader<Product>reader=new FlatFileItemReader<>();
		//Load CSV file
		reader.setResource(new ClassPathResource("persons.csv"));
		//Read Line by Line
		reader.setLineMapper(new DefaultLineMapper<>() {{
			setLineTokenizer(new DelimitedLineTokenizer() {{
				setNames("prodId","prodName","prodCost");			
			}});
			//Set value to variable
			setFieldSetMapper(new BeanWrapperFieldSetMapper<Product>() {{			
				setTargetType(Product.class);			
			}});		
		}});
		return reader;
	}	
	//2.Processor
	@Bean
	public ItemProcessor<Product, Product>processor(){
		return new ProductProcessor();		
	}
	//3.Create Writer
	@Bean
	public ItemWriter<Product>writer(){
	 JdbcBatchItemWriter<Product>writer=new JdbcBatchItemWriter<>(); 
	 writer.setDataSource(dataSource);
	 writer.setSql("INSERT INTO PRODUCT(PRODID,PRODNAME,PRODCOST,PRODGST,PRODDISC)"
	 		                        + "VALUES(:prodId,:prodName,:prodCost,:prodGst,:prodDisc)");
	 writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Product>());
	 return writer;			
	}
		
}		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	

