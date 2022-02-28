package in.bbd.pritesh.batch.uom;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class UomListener implements JobExecutionListener {

	@Override
	public void beforeJob(JobExecution je) {
		System.out.println("While Running:"+je.getStatus());
	}

	@Override
	public void afterJob(JobExecution je) {
		System.out.println("at the end:"+je.getStatus());

	}

}
