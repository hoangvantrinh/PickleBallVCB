set JAVA_HOME=C:\Program Files\Java\jdk-17
echo %JAVA_HOME%
"%JAVA_HOME%\bin\java" -Xms128m -Xmx1536m -jar -Dspring.profiles.active=local  app.jar --spring.config.name=application-local