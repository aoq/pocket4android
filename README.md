pocket4android
==============

Java wrapper around the Pocket API for Android

###Cloned by gregko Aug. 1, 2014:

Added PocketTest app. However, if the user already authorized the app for Pocket access, using the platform default browser for authentication causes an ugly flash on the screen: our own activity being shown, followed by a quick show of the default browser when we authenticate to https://getpocket.com/auth/authorize?blah, followed by a return to my own activity again.

Found a way to avoid it by using WebView control. This is against Pocket's API guidelines, which say:

> *Please send the user to the URL via their default browser 
> in a new tab; do not present this page in a webview or screen 
> within your application. Doing so violates Pocket's API guidelines.
> If you feel like your app should have an exemption, please contact
> us at api@getpocket.com with your request.*

So, one must ask Pocket for permission to use WebView, or live with an ugly flash... The PocketTest app uses WebView. Will try to add another branch with the standard way of using the default platform browser, time permitting...

~gregko

