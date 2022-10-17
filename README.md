# Question Recommender Data Converter

Welcome to the Question Recommender Data Converter repository.

This application was developed with Java and Quarkus. 
The purpose of this application is to enable the integration of xml data with the Question Recommender software. 
To learn more about Question recommender visit the link: https://github.com/jonatan-andrei/Question-Recommender

This application is especially useful for synchronizing Stack Exchange Dumps. 
Learn more about Stack Exchange Dumps: https://archive.org/details/stackexchange

To use this application, review the port where Question Recommender is running on your computer and configure in application.properties.

Now, just run the application:
```
mvn compile quarkus:dev
```

Now you can call the endpoint POST localhost:8077/automated-tests and everything should work!

In case you want to test a specific scenario, try this endpoint: POST localhost:8077/automated-tests/test-information?testInformation={testInformation}&settings={settings}&clearQR=true&clearQRDatabase=true

