package tests;

import org.junit.Test;
import utilities.URLEncodeDecode;

public class decodeID {
	@Test
	public void decodeTest(){
		String jobId = "20&#4504092";
		int len =jobId.length();
		System.out.println("jobId = " + jobId);
		System.out.println(URLEncodeDecode.decode(jobId));
	}

}
