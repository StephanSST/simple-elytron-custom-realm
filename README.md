# INSTALL

## build
Build the project, copy the 

  target/simple-elytron-custom-realm.jar

to

  %JBOSS_HOME%\modules\stephan\realm\main

## config
Copy the following xml snippet to a new file called  %JBOSS_HOME%\modules\stephan\realm\main\module.xml

````
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.3" name="stephan.realm">
  <resources>
    <!-- elytron custom realm -->
    <resource-root path="simple-elytron-custom-realm.jar" />
  </resources>

  <dependencies>
    <!-- wldfly security -->
    <module name="org.wildfly.security.elytron" />
    <module name="org.wildfly.extension.elytron" />
  </dependencies>

</module>
````
