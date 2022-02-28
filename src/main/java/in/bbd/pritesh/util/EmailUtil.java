package in.bbd.pritesh.util;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class EmailUtil {

	@Autowired
	private JavaMailSender sender;

	public boolean sendEmail(
			String to,
			String subject,
			String text,
			String[] cc,
			String[] bcc,
			MultipartFile file) 
	{
		boolean flag = false;

		try {
			//1. Create Empty Message (MIME= Multi purpose Internet Mail Extension)
			MimeMessage message = sender.createMimeMessage();

			//2. Use helper class object
			MimeMessageHelper helper = new MimeMessageHelper(message, file!=null);

			// set message details
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(text);

			if(cc!=null)
				helper.setCc(cc);
			if(bcc!=null)
				helper.setCc(bcc);
			
			if(file!=null) {
				helper.addAttachment(file.getOriginalFilename(), file);
			}

			//3. send email
			sender.send(message);
			
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}

		return flag;
	}
	
	//overloaded methods
	public boolean sendEmail(
			String to,
			String subject,
			String text
			) 
	{
		return sendEmail(to, subject, text,null,null,null);
	}
	
}
