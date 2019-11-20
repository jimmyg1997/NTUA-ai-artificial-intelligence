# 🤖Artificial Intelligence : Smart Taxi Navigation System using A* Algorithm.
This repository contains the Assignments for ECE NTUA course *Artificial Intelligence*

## 🚗Project Description
The general scope of the project was: \\
*Given the position $(X,Y)$ of a client this $(X_i, Y_i)$ of specific taxis, find (a) the closest taxi to the client and (b) the best route that corresponds to this taxi*
The project was implemented into two (2) parts:
  1. In the **first part** we implemented the known $A^{\ast}$ *search algorithm* using the programming language **Java** to solve the previous navigation problem. The **input data** (the coordinates of the map for the city of Athens, the coordinates of the client and the coordinates of thet taxis) were harvested from [*OpenStreetMaps*](https://www.openstreetmap.org/#map=7/38.359/23.810), whereas the **output data** (the coordinates of the route followed by the chosen taxi) were plotted using [*MyMaps*](https://www.google.com/maps/d/u/0/)
  2. In the **second part** we used **Prolog** along with **Java** via **JIProlog** in order to incorporate the capabilites of a *logic programming language*. More specifically, Prolog offered new rules so we could serve multiple alternative (equivalent) routes (using the same or different taxis) while taking into consideration specific preferencies of the client.
  
  
## 📋Contents
* **project-description** : Description of the project in Greek language
* **clients.csv** : The file contains the coordinates of the *location* of the client:
$$(X,Y) = 23.733912, 37.975687 $$
* **taxis.csv** : The file contains the coordinates of the *location*, the *ids* of all taxis:
$$(X,Y, id) = 23.741587, 37.984125, 100 $$
* **nodes.csv** : The file contains the *coordinates* of some parts of the streets of the Athens city. Each street is characterized by an *id*, so parts with the same *id* belong to the same street.
* **client2.csv, taxis2.csv** : Randomly generated coordinates for the client and the taxis to test the validity of our implemented algorithm.
* **best-route1.kml, best-route2.kml, best-route-advanced.kml**: The best routes for each taxi in kml form. The files were therefore uploaded into *MyMaps* to visualize the routes.


## 👱Project Collaborators
The project was implemented by:
* Georgiou Dimitrios (jimmyg1997)
* Sarmas Elissaios
