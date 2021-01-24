Feature: Book flights 
	Users need to be able to book flights from Ryanair's main website

	Scenario: Adjust depart and return dates
		Given Sónia has navigated to the main website
		When she searches for a flight with:
			| from		| to				| depart		| return		| adults	| teens	| children	| infants	|
			| Lisbon	| Paris Beauvais	| 2021-06-06	| 2021-10-30	| 2			| 0		| 1			| 0			|
		When she changes the depart date for "2021-06-18" with "Value" fare
		When she changes the return date for "2021-10-19" with "Value" fare
		When she fills the passengers information with:
			| adults								| teens	| children		| infants	|
			| Ms Sónia Pereira,Mr Diogo Bettencourt	|		| Inês Marçal	|			|
		When she selects the same seats for both flights
		When she selects 1 Small Bag only for all passengers
		Then she should be able to see the overview screen
			
# return > depart