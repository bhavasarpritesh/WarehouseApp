package in.bbd.pritesh.batch.uom;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class UomBatchRunner 
	//implements CommandLineRunner 
{
	@Autowired
	private Job jobUom;
	
	@Autowired
	private JobLauncher launcher;
	
	//public void run(String... args) throws Exception {
	@Scheduled(cron = "0 0 21 * * *")
	public void run() throws Exception{
		launcher.run(jobUom, 
				new JobParametersBuilder()
				.addLong("time", System.currentTimeMillis())
				.toJobParameters()
				);
	}
}
