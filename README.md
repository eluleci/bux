# Bux Assignment

This project is created by Emrullah Lüleci for BUX BV.
 
## Features

### Product list

* On main page, loads product list from RESTful API and shows list of products.
* Shows errors on a dialog.
* Allows to retry if the error is not related to authorization.
* Opens product detail page on item list item selection.

### Product detail

* Gets product detail from cache(if available) or from RESTful API and shows on page.
* Connects to Web Socket API to get product updates.
* Shows error on bottom of the page to allow user to see the existing info.
* Allows to retry if the error is not related to authorization.

## Architecture

* Project's primary language is Kotlin.
* Project is created based on MVP architectural pattern.
* Uses Dagger for dependency injection.
* Uses RxJava for composing asynchronous and event-based data flow.
* Uses OkHttp + Gson + Retrofit for consuming RESTful HTTP service.
* Uses OkHttp + Gson for consuming Web Socket service.

## IMPORTANT

In the documentation, the Web Socket update messages are defined as **trading.quote** messages like:

```
{
   "t": "trading.quote",
   "body": {
      "securityId": "{productId}",
      "currentPrice": "10692.3"
   }
}
```

but the Web Socket API pushes **portfolio.performance** messages like:

```
{
	"t":"portfolio.performance",
	"id":"e5b806df-db32-11e7-bba2-bb6f00966e09",
	"v":1,
	"body":{
		"accountValue":{
			"currency":"BUX",
			"decimals":2,
			"amount":"103993.39"
		},
		"performance":"0.0415",
		"suggestFunding":false
	}
}
```

Because of that some of the requirements told in the documentation are not satisfied.
Ex: "show the difference of the previous day closing price to the current price, in %".
Please consider this while evaluating the assignment output.