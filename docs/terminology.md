#Terminology

##Terminal

*inland (hinterland) container terminal*

A facility where cargo containers are handled between different transport vehicles for onward transportation. 
The handling of containers is typically between barges or cargo trains and land vehicles (trucks).

* **Region:** An area defined by specific parameters. 
Usually geographical division. (*Oberrhein, Mittelrhein, Niederrhein*) / (*Rhein-Neckar, Rhein-Main*).
Contargo uses three regions among other things to assign and store the CO₂-parameter of our barge fleet.


##Seaport
Location on a coast where sea vessels and barges can dock and transfer cargo from or to land. 
Container seaports handle cargo in containers by different mechanical means. (crane, AGV, reach stacker)


##Connections
This shows the possibilities to connect a seaport and an inland terminal.
Different and multiple connections are possible. For example barge or rail or both.

* **Barge-Diesel-km:** The kilometers traveled on the barge route by use of diesel fuel.
* **Rail-Diesel-km:** The kilometers traveled on the train route by use of diesel fuel.
* **Electrical-km:** The kilometers on the train route traveled by use of electricity only.
* **Subconnections:** Subconnections model parts of connections. Those parts are only relevant if it is a connection of type Barge-Rail.


##Static Addresses
A static address is a city with its corresponding postal code and country. For example *68159 Mannheim, Germany*.


##Route types

* **Barge:** Transport of goods / cargo on barge. 
Barge transport only happens between seaports and inland terminals or between inland terminals that are connected by a major river or canals.
* **Rail:** Transport of goods / cargo between seaports and inland terminals on a freight train using rail roads.
* **Barge-Rail:** Combined transport of goods / cargo between seaports and inland terminals on barge using major rivers or canals and on freight train using rail roads.
* **Truck:** Transport of goods / cargo from and to seaports and inland terminals and the loading / unloading site.


##Route Combination

* **Waterway:** Transport per barge between seaport and inland terminal and additional transport per truck to the loading / unloading site.
* **Railway:** Transport per rail between seaport and inland terminal and additional transport per truck to the loading / unloading site.
* **Waterway-Rail:** Combined transport per barge and rail between seaport and inland terminal and additional transport per truck to loading / unloading site.
* **Direct Truck:** Transport only per truck from or to the seaport and the loading site.
* **Roundtrip:** Waterway, Railway, Waterway-Rail or Direct Truck transport from seaport to loading / unloading site and back to seaports.
* **All:** A list of all the possibilities for transport for a given loading site.

##Route Revision

A Route Revision gives the possibility to override the calculated distances between a geolocation and a terminal given by OSRM.
It is defined by a Geolocation, the responsible terminal, a radius and the three distances that would replace the distances provided by OSRM - Truckdistance Oneway, Tolldistance Oneway, Airline Distance.

###Example

1. A Route Revision exists with Terminal **Mannheim**, geolocation 49.451369,8.1030178 and radius 100m.
2. There is a routing between the Terminal **Mannheim** and the geolocation 49.451367,8.1030177.
3. The Routing Geolocation is in the radius of the Route Revision, so the distances defined are used.

If a destination is in more than one radius of different Route Revisions, than the nearest (calculated by the air line distance) will override the values described above.