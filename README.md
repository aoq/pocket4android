pocket4android
==============

Java wrapper around the Pocket API for Android

###Cloned by gregko Aug. 1, 2014:

Added PocketTest app. Actually to test apps now: PocketTestBrowser, which shows the recommended way of doing Pocket authentication, and PocketTestWebView, a better way - avoiding ugly flash - but read the concerns below: 

If the user already authorized the app for Pocket access, using the platform default browser for authentication causes an ugly flash on the screen: our own activity being shown, followed by a quick show of the default browser when we authenticate to https://getpocket.com/auth/authorize?blah, followed by a return to our own activity again.

Found a way to avoid it by using WebView control, see PocketTestWebView app. However, this is against Pocket's API guidelines, which say:

> *Please send the user to the URL via their default browser 
> in a new tab; do not present this page in a webview or screen 
> within your application. Doing so violates Pocket's API guidelines.
> If you feel like your app should have an exemption, please contact
> us at api@getpocket.com with your request.*  
> (see http://getpocket.com/developer/docs/authentication)

So, one must ask Pocket for permission to use WebView, or live with the ugly flash... 

*~gregko*

