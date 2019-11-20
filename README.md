# 🤖Artificial Intelligence : Smart Taxi Navigation System using A* Algorithm.
This repository contains the Assignments for ECE NTUA course *Artificial Intelligence*

## 🚗Project Description
The general scope of the project was: \
*Given the location **(X,Y)** of a client and the location of specific taxis, find (a) the closest taxi to the client and (b) the best route that corresponds to this taxi*
The project was implemented into two (2) parts:
  1. In the **first part** we implemented the known ![equation](http://www.sciweavers.org/tex2img.php?eq=A%5E%7B%5Cast%7D&bc=White&fc=Black&im=jpg&fs=12&ff=arev&edit=0) *search algorithm* using the programming language **Java** to solve the previous navigation problem. The **input data** (the coordinates of the map for the city of Athens, the coordinates of the client and the coordinates of thet taxis) were harvested from [*OpenStreetMaps*](https://www.openstreetmap.org/#map=7/38.359/23.810), whereas the **output data** (the coordinates of the route followed by the chosen taxi) were plotted using [*MyMaps*](https://www.google.com/maps/d/u/0/)
  2. In the **second part** we used **Prolog** along with **Java** via **JIProlog** in order to incorporate the capabilites of a *logic programming language*. More specifically, Prolog offered new rules so we could serve multiple alternative (equivalent) routes (using the same or different taxis) while taking into consideration specific preferencies of the client.
  
  
## 📋Contents
* **project-description** : Description of the project in Greek language
* **clients.csv** : The file contains the coordinates of the *location* of the client: \

![equation](http://www.sciweavers.org/tex2img.php?eq=%28X%2C%20Y%29%20%3D%2023.733912%2C%2037.975687&bc=White&fc=Black&im=jpg&fs=12&ff=arev&edit=0)

* **taxis.csv** : The file contains the coordinates of the *location*, the *ids* of all taxis:

![equation](http://www.sciweavers.org/tex2img.php?eq=%28X%2CY%2C%20id%29%20%3D%2023.741587%2C%2037.984125%2C%20100&bc=White&fc=Black&im=jpg&fs=12&ff=arev&edit=0)

* **nodes.csv** : The file contains the *coordinates* of some parts of the streets of the Athens city. Each street is characterized by an *id*, so parts with the same *id* belong to the same street.
* **client2.csv, taxis2.csv** : Randomly generated coordinates for the client and the taxis to test the validity of our implemented algorithm.
* **best-route1.kml, best-route2.kml, best-route-advanced.kml**: The best routes for each taxi in kml form. The files were therefore uploaded into *MyMaps* to visualize the routes.


## 👱Project Collaborators
The project was implemented by:
* Georgiou Dimitrios (jimmyg1997)
* Sarmas Elissaios
