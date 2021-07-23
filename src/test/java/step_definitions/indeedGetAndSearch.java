package step_definitions;

import io.cucumber.java.en.Given;
import pages.indeedSearchPage;

import java.io.IOException;

public class indeedGetAndSearch {//extends TestBase -- TestBase is invalid

	@Given("User is on the Indeed.com page and searches for keywords")
	public void userIsOnTheIndeedComPageAndSearchesForKeywords() throws IOException {
		indeedSearchPage indeedPage = new indeedSearchPage();
//		indeedPage.login();
		int pages = indeedPage.login();
		int iteration =1;
		do{
			System.out.println("==========================================\niteration = " + iteration);
			String url="https://www.indeed.com/jobs?q=Java+Selenium+testing&limit=50&fromage=1&radius=25&start="+(iteration-1)*50;
			System.out.println("url = " + url);
			indeedPage.jobDetails(url);
		iteration++;
		}while(iteration<=pages);

	}
}
