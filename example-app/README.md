# Example App

The first thing you'll see is the MainActivity. This screen shows you what the TokenStore knows, and since right now it knows nothing, it shows you nothing.

<img src="https://i.imgur.com/Pr2T7mF.png" width="40%" />

Click the FAB in the bottom right to authenticate a new user. It'll launch the NewUserActivity, which will show you a WebView. This WebView will automatically be loaded with the correct authorization URL for the example OAuth2 app credentials.

<img src="https://i.imgur.com/8JXyBp6.png" width="40%" />

Once you enter your username and password, you'll be prompted by reddit to allow the example app to allow the app to fetch information about your account.

<img src="https://i.imgur.com/07AnD15.jpg" width="40%" />

If you press "deny" you'll be taken back to the previous screen. If you press "allow," you'll be taken to the UserOverviewActivity.

<img src="https://i.imgur.com/01Qdqpv.png" width="40%" />

From here, if you press the back or logout button, you'll be taken back to the MainActivity. This time, you'll notice that there is an entry present for the account you just logged in to. Clicking on the entry will take you back to the user overview.

<img src="https://i.imgur.com/q4N5LiE.png" width="40%" />

Once the access token expires, the text in the bottom left-hand corner of the entry will read "Access token expired." If you now click on the entry, JRAW will automatically request a new access token for that user and you will be shown the UserOverviewActivity.

<img src="https://i.imgur.com/11CtH6c.png" width="40%" />
