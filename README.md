# Android-Mobile-App
Android Mobile App using gradle

#
The application takes a string number from the user, and uses it to display the number of news the user request to view from the NEWS api.

**It implements a native Android application.**
It has at least two different kinds of views in the Layout (TextView, EditText, ImageView, etc.)
Using TextViews, EditText, Button, radioButtons and Switch. 
See content_main.xml for details of how they are incorporated into the LinearLayout. 

**Using an HttpServlet to implement a simple (can be a single path) API**
Model: Model.java
View: index.jsp
Controller: Servlet.java (With helper class QueryApp)

**Receives an HTTP request from the native Android application**
Servlet.java receives the HTTP GET request to get the news, and HTTP POST request passes this input on to the model.

**Executes business logic**
Model.java makes an HTTP request to: 
https://newsapi.org/v2/top- headlines?sources=techcrunch&apiKey=e9b0858dbd844885bb5d6ec1263a8ac8 
It then parses the JSON response and extracts the parts it needs to respond to the Android application.

**Replies to the Android application with an XML or JSON formatted response.** 
Model.java formats the response to the mobile application in a JSON format


