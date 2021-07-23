package tests;

public class jobCountTest {
	public static void main(String[] args) {
		String jc = "Page 1 of 77 jobs";
		jc=jc.substring(10).replaceAll("[^0-9.]", "");
//		jc=jc.replaceAll("[^\\d.]", "");
		System.out.println("jc :" + jc);

		int jobCount = Integer.parseInt(jc);
		System.out.println("jobCount = " + jobCount);
		int pages=jobCount/50+1;
		System.out.println("pages = " + pages);
	}
}
