package in.bbd.pritesh;

import java.util.Random;

public class Test {

	public static void main(String[] args) {
		//String pwd = UUID.randomUUID().toString().replaceAll("-", "").substring(0,8);
		String otp = String.format("%04d",new Random().nextInt(10000));

		System.out.println(otp);
	}
}
