# Question Recommender Data Converter

Welcome to the Question Recommender Data Converter repository.

This application was developed with Java and Quarkus. 
The purpose of this application is to enable the integration of xml data with the Question Recommender software. 
To learn more about Question recommender visit the link: https://github.com/jonatan-andrei/Question-Recommender

This application is especially useful for synchronizing Stack Exchange Dumps. 
Learn more about Stack Exchange Dumps: https://archive.org/details/stackexchange

To use this application, you need to define in the application.properties the folder path of the xml files that the application should read:
```
xml.folder: 'C:\dump'
```

Now, review the port where Question Recommender is running on your computer:
```
question-recommender.url=localhost
question-recommender.port=8079
```

Now, just run the application:
```
mvn compile quarkus:dev
```

Now you can call the endpoint POST localhost:8078/convert-data and everything should work!