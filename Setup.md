# Tic-Tac-Toe Setup Guide
## Compilation
The server is written in Java. Java is a compiled langauge, and will need to be compiled before running.

To compile the code, follow these steps.
1. [Install Java](https://www3.ntu.edu.sg/home/ehchua/programming/howto/JDK_Howto.html)
2. Clone the Source Code to a development environment
3. Open a terminal shell, and run the command below to compile the code.

```
javac src/*.java
```

## Running
Once the code has been compiled, run the command seend below in the terminal to run the application.

```
cd src
java Main 
```

By default the application will open on port 3000, to open it on another port of your choice, run the following command with an example of port 5000:
```
cd src
java Main 5000
```


This will start a server at [`localhost:3000`](http://localhost:3000) if you used the default port. 

To play against yourself, you will need to open two instances of the application by entering either the URL in your browser of choice, or clicking the link in the terminal after running. 

In the first instance, click `"Host Game"` and then copy the access code that appeared next to the host game button. 

In the second instance, paste the access code in the `Access Code` field and then click `"Find Game"`. After the connection is made, you will be able to take turns placing your markers. 

> Remember, the host always goes first!

## Hot-Reload Development
Hot Reload will automatically reflect changes to the source code by recompiling, and rerunning the code to reflect the changes. This will ensure that you do not have to close the app, recompile, and rerun with each change manually.

Note that you will likely have to install [entr](http://eradman.com/entrproject/) before you can run the following command. It can be easily downloaded and installed using the link above or installed using your system's package manager. The following subsections show the commands for a few operating systems.

### Ubuntu
```
sudo apt-get update -y
sudo apt-get install -y entr
```

### Mac OS
```
brew install entr
```

If you are on a Unix based system such as Linux or Mac, you can use the following command to start a hot reload server.
```
# -r because the child process is persistent and -s because we are passing in a shell command
ls src/**/*.java src/**/*.html src/**/*.js src/**/*.css | entr -rs 'make && make serve'
```

## Swagger
This project defines a `swagger.yml` file which can be converted into a website for visualization using OpenAPI specifications.

This will allow you to view detailed information on the API endpoints provided by the application.

To view this information, open up `index.html` in the browser of your choice. Alternatively, go to the [Swagger Editor](https://editor.swagger.io/), copy the contents of `swagger.yml` and paste the contents in the online editor.

#### Swagger Generation on Change
If you have changed the contents of `swagger.yml` you will need to regenerate the index.html file to reflect the changes.

To do this, follow the steps below: 
1. First, ensure `redoc-cli` is installed, if it is not run the following command in your terminal: 
```
npm install -g redoc-cli
```

2. Once redoc-cli is installed, run the following command in the terminal to generate the index.html file:
```
redoc-cli bundle -o index.html swagger.yml
```
