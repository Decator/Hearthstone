# Setting up the MySQL server container
Go to the [Docker](https://www.docker.com/get-started) website and download the version of Docker suited to your OS.
Once it is set up, launch it and go to the root folder of the Hearthstone project in the terminal.
There you can find a MySQL dump as well as a docker-compose file. </br>
Once you are in the root folder, enter the following commands : </br>
```
$docker-compose build 
$docker-compose up -d
$docker start HSDock 
```
To check if HSDock is running :
```
$docker ps
```
This should build and start the container containing the MySQL server running the game's database. More information is available on this [website](https://runbook.readthedocs.io/en/latest/install_docker_compose/).
The information required for the game engine to log into the database are already included in the Application.properties file. </br>
<strong>If you are using Docker toolbox on Windows 10, you may need to change the following information in the Application.properties : </strong> </br>
spring.datasource.url=jdbc:mysql://<strong>0.0.0.0:3307</strong>/Hearthstone?allowPublicKeyRetrieval=true&useSSL=false to</br>
spring.datasource.url=jdbc:mysql://<strong>ip-adress-of-your-virtual-machine</strong>/Hearthstone?allowPublicKeyRetrieval=true&useSSL=false </br>
</br>
You can find this ip-adress by using the following command :
```
$docker-machine ip
``` 
You can now start the engine in your IDE.
</br>
To kill HSDock : 
```
$docker kill HSDock
```
To remove HSDock completely (for a cleaner rebuild) : 
```
$docker rm HSDock
``` 
