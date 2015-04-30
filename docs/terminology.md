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

* **Diesel-km:** The kilometers traveled on the train route or the barge route by use of diesel fuel.
* **Electrical-km:** The kilometers on the train route traveled by use of electricity only.


##Static Addresses
A static address is a city with its corresponding postal code and country. For example *68159 Mannheim, Germany*.


##Cloud distance
A previously defined area (radius) around a static address.


##Route types

* **Barge:** Transport of goods / cargo on barge. 
Barge transport only happens between seaports and inland terminals or between inland terminals that are connected by a major river or canals.
* **Rail:** Transport of goods / cargo between seaports and inland terminals on a freight train using rail roads.
* **Truck:** Transport of goods / cargo from and to seaports and inland terminals and the loading / unloading site.


##Route Combination

* **Waterway:** Transport per barge between seaport and inland terminal and additional transport per truck to the loading / unloading site.
* **Railway:** Transport per rail between seaport and inland terminal and additional transport per truck to the loading / unloading site.
* **Direct Truck:** Transport only per truck from or to the seaport and the loading site.
* **Roundtrip:** Waterway, Railway or Direct Truck transport from seaport to loading / unloading site and back to seaports.
* **All:** A list of all the possibilities for transport for a given loading site.