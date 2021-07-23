Feature: Real estate search on Zillow.com
    Agile Story: as a user, I should be able navigate to www.zillow.com website and search for properties

@zillow
Scenario: User searches properties in a given zipcode
  Given User is on the Zillow.com webpage
  Given User verifies the title
  And  User inputs "zipcode" in the Zillow.com home page
  And User presses search button
  Then User should see number of properties