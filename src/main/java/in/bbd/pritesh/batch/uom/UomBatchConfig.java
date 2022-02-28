package in.bbd.pritesh.batch.uom;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import in.bbd.pritesh.model.Uom;

@Configuration
@EnableBatchProcessing
public class UomBatchConfig {
	
	@Bean
	public ItemReader<Uom> reader() {
		FlatFileItemReader<Uom> reader = new FlatFileItemReader<Uom>();
		reader.setResource(new FileSystemResource("F:\\whapp\\uoms.csv"));
		
		reader.setLineMapper(new DefaultLineMapper<Uom>() {{
			setLineTokenizer(new DelimitedLineTokenizer() {{
				setNames("uomType","uomModel","description");
			}});
			setFieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
				setTargetType(Uom.class);
			}});
		}});
		
		return reader;
	}
	
	@Bean
	public ItemProcessor<Uom, Uom> processor() {
		return new UomProcessor();
	}

	@Autowired
	private EntityManagerFactory entityManagerFactory;
	
	@Bean
	public ItemWriter<Uom> writer() {
		JpaItemWriter<Uom> writer = new JpaItemWriter<>();
		writer.setEntityManagerFactory(entityManagerFactory);
		return writer;
	}
	
	@Autowired
	public StepBuilderFactory sf;
	
	@Bean
	public Step stepUom() {
		return sf.get("stepUom")
				.<Uom,Uom>chunk(10)
				.reader(reader())
				.processor(processor())
				.writer(writer())
				.build();
	}
	@Autowired
	private JobBuilderFactory jf;
	
	@Bean
	public Job jobUom() {
		return jf.get("jobUom")
				.listener(new UomListener())
				.incrementer(new RunIdIncrementer())
				.start(stepUom())
				.build();
	}
	
}
