# Setting up the MySQL server container
Go to the [Docker](https://www.docker.com/get-started) website and download the version of Docker suited to your OS.
Once it is set up, launch it and go to the root folder of the Hearthstone project in the terminal.
There you can find a MySQL dump as well as a docker-compose file. </br>
Once you are in the root folder, enter the following commands : </br>
$docker-compose build </br> 
$docker-compose up -d </br>
$docker start HSDock </br>
</br>
This should build and start the container containing the MySQL server running the game's database. More information is available on this [website](https://runbook.readthedocs.io/en/latest/install_docker_compose/).
The information required for the game engine to log into the database are already included in the Application.properties file.
You can now start the engine your IDE.