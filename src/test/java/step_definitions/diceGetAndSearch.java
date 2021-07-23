package step_definitions;

import io.cucumber.java.en.Given;
import pages.diceSearchPage;

import java.io.IOException;

public class diceGetAndSearch {//extends TestBase -- TestBase is invalid

	@Given("User is on the Dice.com page and searches for keywords")
	public void userIsOnTheDiceComPageAndSearchesForKeywords() throws Exception {
		int numPerPage = 500;
		int iteration =1;
		diceSearchPage dicePage = new diceSearchPage();
		dicePage.login();
//		dicePage.findJobDates(); //PART 2
		String url="https://www.dice.com/jobs?q=Selenium%20Java%20Testing&countryCode=US&radius=30&radiusUnit=mi&page=1&pageSize="+numPerPage+"&filters.postedDate=ONE&language=en";
		int pages=dicePage.countVacancies(url, numPerPage);
		System.out.println("pages = " + pages);

		do{
			System.out.println("==========================================\niteration = " + iteration);
			url="https://www.dice.com/jobs?q=Selenium%20Java%20Testing&countryCode=US&radius=30&radiusUnit=mi&page="+iteration+"&pageSize="+numPerPage+"&filters.postedDate=ONE&language=en";
//			dicePage.jobDetails(url);
			dicePage.someJobDetails(url);
		iteration++;
		}while(iteration<=pages);

	}
}
