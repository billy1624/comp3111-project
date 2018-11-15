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

## Assumptions & Special Note

1. Basic Task 2
	- The items from Craigslist, they are in USD, assume `1 USD = 7.8 HKD`, price will be converted to HKD before displaying.
	- The items from Preloved, they are in GBP, assume `1 GBP = 10 HKD`, price will be converted to HKD before displaying.
	- Item `postedDate` on newyork.craigslist.org will be fetched from ```<time class="result-date" datetime="2018-11-06 11:39" title="Tue 06 Nov 11:39:30 AM">Nov  6</time>```, this time tag from the row of item on search result grid. For this particular item the `posted date` is the value inside `datetime`, that is `2018-11-06 11:39`.
	- Item `postedDate` on www.preloved.co.uk only show `xx days ago` or `xx hours ago` this will be treated as `postedDate`. And the actual `postedDate` will be calculated based on that. `xx days ago` or `xx hours ago` will be consider as `HK Time`, no time zone conversion will be done.
	- Will skip fetching all the ads on search result.
2. Basic Task 3
	- The total number of search result is not accurate on newyork.craigslist.org. For example, when I search for `toaster` on the site. On the first page of search result, it is showing item 1 to 120, and 503 items in total was show on the page. However, when I click next page, it now show there are 506 items in total. In fact, if I go to the last page of search result, it only contain 503 items instead of 506 items. **The message showing the current processing item is accurate, i.e. `Fetching... item 121 - 240`. However, the total number of items might not be accurate, i.e. `506 in total`.**
