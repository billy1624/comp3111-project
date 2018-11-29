# COMP3111: Software Engineering Project - Webscrapper

## Group Info

Group Name: 404_Girl_Not_Found  
Group Number: 77  

## Group Members

|  Member | Task | Task |
|----|----|----|
| cwchanbf | Basic 2 | Basic 3 |
| chnganaa | Basic 1 | Basic 4 |
| chyeungam | Basic 5 | Basic 6 |

- **Chan Chi Wa**
	- Tasks: Basic 2 & Basic 3
	- Email: cwchanbf@connect.ust.hk
- **Ngan Cheuk Hei**
	- Tasks: Basic 1 & Basic 4
	- Email: chnganaa@connect.ust.hk
- **Yeung Chak Ho**
	- Tasks: Basic 5 & Basic 6
	- Email: chyeungam@connect.ust.hk

## Javadoc
Javadoc available on: https://billy1624.github.io/COMP3111_Proj/javadoc

## Assumptions & Special Note

1. Basic Task 2
	- The items from Craigslist, they are in USD, assume `1 USD = 7.8 HKD`, price will be converted to HKD before displaying.
	- The items from Preloved, they are in GBP, assume `1 GBP = 10 HKD`, price will be converted to HKD before displaying.
	- Item `postedDate` on newyork.craigslist.org will be fetched from ```<time class="result-date" datetime="2018-11-06 11:39" title="Tue 06 Nov 11:39:30 AM">Nov  6</time>```, this time tag from the row of item on search result grid. For this particular item the `posted date` is the value inside `datetime`, that is `2018-11-06 11:39`.
	- Item `postedDate` on www.preloved.co.uk only show `xx days ago` or `xx hours ago` this will be treated as `postedDate`. And the actual `postedDate` will be calculated based on that. `xx days ago` or `xx hours ago` will be consider as `HK Time`, no time zone conversion will be done.
	- Will skip fetching all the ads on search result.
2. Basic Task 3
	- The total number of search result is not accurate on newyork.craigslist.org. For example, when I search for `toaster` on the site. On the first page of search result, it is showing item 1 to 120, and 503 items in total was show on the page. However, when I click next page, it now show there are 506 items in total. In fact, if I go to the last page of search result, it only contain 503 items instead of 506 items. **The message showing the current processing item is accurate, i.e. `Fetching... item 121 - 240`. However, the total number of items might not be accurate, i.e. `506 in total`.**
		- For more details, please refer to our team's opened issue on https://github.com/khwang0/2018F-COMP3111/issues/68, [Basic 2 & 3] Incorrect Number of Item Display on Craigslist Search Result #68

3. Basic Task 2 & 3 on JUnit Test
	- When running scrape test there are warning message like below showing up, which is normal
		```
		Nov 29, 2018 1:33:59 PM com.gargoylesoftware.htmlunit.WebClient printContentIfNecessary
		INFO: statusCode=[404] contentType=[text/html]
		Nov 29, 2018 1:34:00 PM com.gargoylesoftware.htmlunit.WebClient printContentIfNecessary
		INFO: File: /www.googletagmanager.com/ns.html
		```

4. Basic Task 6

   - The `Last Search Function` 

     - Refined record is __NOT__ counted as a search record (which means last search will not revert the dataset from a refined record, but it does revert to a record that has not been refined (the original dataset scrape from Portal). __Only__ the search which is scape from Portal and `Initial Empty Record` count as a  `Search Record`)
     - The initial `Last Search` is `Initial Empty Record`. After the first search then click last search, it should revert every page to empty.

   - The `Close` Item in Menu Bar

     - It will clear all the record and restore all tabs to initial state (Requirement)

     - During scraping process, pressing `Close` will stop the scraping process (Extra)

     - `Close` will ask user whether they want to _Remove_ all search history or not. If yes, then the whole application will restore the initial state with _NO_ search record (Extra)

       - Junit Test report this standard error occur in Test Case because it requires a event thread but not JavaFx thread. It's not a bug of released program.

         ```java
         Exception in thread "JavaFX Application Thread" java.lang.IllegalStateException: This operation is permitted on the event thread only; currentThread = JavaFX Application Thread
         ```
