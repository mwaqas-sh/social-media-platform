# social-media-platform
Backend implementation of Social Media platform   

# Environment Variables
mysql_url=jdbc:mysql://localhost:3306/social_media_platform?createDatabaseIfNotExist=true  
mysql_username=root  
mysql_password=test@123  
redis_host_name=localhost  
redis_password=  
redis_port=6379  
redis_user_name=  
redis_withouttls=false  

# Java Version

17

# MySql Version Requirement

8.0

# Redis Version Requirement

3.0.504  

## Development

During development, it is recommended to use the profile `local`. In IntelliJ, `-Dspring.profiles.active=local` can be
added in the VM options of the Run configuration after enabling this property in "Modify options".

Update your local database connection in `application.properties` or create your own `application-local.properties` file
to override settings for development.

After starting the application it is accessible under `localhost:9010`.

## Build

The application can be built using the following command:

```
mvn clean package
```

The application can then be started with the following command 

```
java  -jar ./target/social.media.platform-0.0.1-SNAPSHOT
```
