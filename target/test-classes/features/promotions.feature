Feature: Validate Get Promotions API

Scenario: Verify if promotions data is retreived successfully using Get Promotions API
Given GetPromotions payload with "apikey" as "GDMSTGExy0sVDlZMzNDdUyZ"
When user calls "GetPromotionsAPI" with "Get" http request
Then Response HTTP Status code should be "200"
And "promotions" in response body should exist
And "promotionId" in response body should exist
And "orderId" in response body should exist
And "promoArea" in response body should exist
And "promoType" in response body should exist
And "showPrice" should have value as True or false
And "showText" should have value as True or false
And "localizedTexts" should exist with “ar” and “en” json objects


Scenario: Verify promotionId be any string value and programType as EPISODE or MOVIE or SERIES or SEASON 	  
Given GetPromotions payload with "apikey" as "GDMSTGExy0sVDlZMzNDdUyZ"
When user calls "GetPromotionsAPI" with "Get" http request
Then Response HTTP Status code should be "200"
And "promotionId" should have any string value
And "programType" have value as as EPISODE or MOVIE or SERIES or SEASON


Scenario: Verify invalid response for a request with invalid value passed for “apikey” query parameter 	  
Given GetPromotions payload with "apikey" as "12345"
When user calls "GetPromotionsAPI" with "Get" http request
Then Response HTTP Status code should be "403"
And "requestId" should not be null
And "code" should be "8001"
And "message" should be "invalid api key" 