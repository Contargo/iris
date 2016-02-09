[![Build Status](https://travis-ci.org/Contargo/iris.svg?branch=master)](https://travis-ci.org/Contargo/iris)

IRIS - Intermodal Routing Information System
=========

The goal of IRIS is to create a centralized and transparent database for distance calculations in short-haul goods transport.
IRIS aims to provide such a platform, for both clients and service providers.

IRIS is used to calculate truckings for transports from a hinterland terminal to the place of loading and the other way around.
The place of loading can be set very detailed by adjusting its coordinates.
IRIS uses these coordinates to calculate the truck route, distance, toll kilometers and the duration of the trucking.
An extension for barge and rail routings is already planned.

Example: A freight container arrives in Mannheim via cargo ship and needs to be transported to 48 Berliner Str., Heidelberg, Germany.
After arriving at the Contargo terminal in Mannheim it needs to be loaded on a truck to be transported to its destination.
There, the recipient unloads the content and the truck transports the empty container back to the terminal.

# Features
* Management of seaports, terminals and connections between them (GUI)
* Automatic creation of truck and barge routes between seaports, terminals and destinations (GUI & REST-API)
* Provision of detailed information for each route like distance, toll distance, duration, CO2 output (REST-API)
* Support for different container sizes and states (REST-API)
* Open Street Map Address resolution for destination points (GUI & REST-API)
* Management and resolution of custom static addresses as destination points (GUI & REST-API)
* Possibility to implement any authentication mechanism via Spring Security

Further documentation about the IRIS-terminology can be found here: [terminology](docs/terminology.md).
If you are interested to use IRIS in Switzerland read this: [ASTAG](docs/astag.md)


#Prerequisites
 - MariaDb 5.5 or higher
 - Maven 3 or higher
 - JDK 8 or higher


#Getting started

Simply clone this repository
```sh
$ git clone https://github.com/Contargo/iris.git
```

## Configuration

Configuration is located in ```src/main/resources/```

The "general" configuration file is ```src/main/resources/application.properties```


### Environment Properties

Environment (System) specific configurations go to ```src/main/resources/application-<envname>.properties``` 

The environment-specific (e.g. ```application-dev.properties```) file overrides and adds to properties defined in the "general" properties-file (```application.properties```). Not overridden properties of ```application.properties``` remain valid.

Default Environment is "dev" (so ```application-dev.properties``` is loaded by default). Environments can be set using System-Property "environment" 

```sh
mvn jetty:run -Denvironment=myenv  # -> leads to use application-myenv.properties
```
or environment-parameter environment:
```sh
export environment=superdev
mvn jetty:run  # -> leads to use application-superdev.properties
```

#### Database

All database connection settings are configured in ```application-<envname>.properties```. Adapt the corresponding properties to match your MariaDb database connection settings.

All needed database tables are created on application start using [Liquibase](http://www.liquibase.org/).


### Roles

There are two different roles defined in IRIS:

- ROLE_ADMIN
  - can do anything
- ROLE_USER
  - no GUI access
  - limited REST Api access


### User Credentials

User Credentials are located in ```src/main/resources/usercredentials-<envname>.properties```.

For development IRIS ships with two predefined users:
- admin@example.com with password admin: ROLE_ADMIN
- user@example.com with password user: ROLE_USER

These are located in ```src/main/resources/usercredentials-dev.properties```.

To add own user credentials do the following:
```sh
$ echo -n "yourpassword" | sha256sum
e3c652f0ba0b4801205814f8b6bc49672c4c74e25b497770bb89b22cdeb4e951  -
```
Edit the file ```src/main/resources/usercredentials-<envname>.properties``` and add your user like this:
```
youruser@example.com=e3c652f0ba0b4801205814f8b6bc49672c4c74e25b497770bb89b22cdeb4e951,ROLE_USER,enabled
```

You can specify login, password, role and set the enabled flag.

### Logging

Logging in IRIS uses Logback.

The default logback configuration is located in ```src/main/resources/logback.xml```. It configures a console appender and a rolling file appender. The log file is located in ```logs/iris.log```.

Additionally, there is another logback configuration suited for production use located in ```src/main/resources/logback-prod.xml```. It can be activated using the ```logback.configuration.file``` property. This logging configuration does not include the console appender. An extra GELF appender can be activated by providing the property ```graylog.server``` that points to your graylog host.

## Application Start

In order to build the application you need Maven 3 and Oracle JDK 7. You also need your MariaDb database set up as described above. You can then start the local web server:
```sh
$ mvn jetty:run
```
You can run a full build including all tests with
```sh
$ mvn clean install
```
Finally, point your browser to the url http://localhost:8082/. IRIS has both a basic user interface and a JSON API. Documentation for the API is located at http://localhost:8082/api/docs.html.


# Contributing

If you want to contribute to IRIS, see our [contribution guidelines](CONTRIBUTING.md).


# Licensing

IRIS is licensed under the GNU Affero General Public License, Version 3. See [LICENCE](LICENSE) for the full license text.
