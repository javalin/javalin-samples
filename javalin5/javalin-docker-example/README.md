# Docker tutorial example

This is a simple example of how to use Docker to run a Javalin application.

The tutorial is available at [https://javalin.io/tutorials/docker](https://javalin.io/tutorials/docker).

## Running the example

To run the example, you need to have Docker installed. You can download it from [https://www.docker.com/products/docker-desktop](https://www.docker.com/products/docker-desktop).

First you need to package the application into a jar file. You can do this by running the `package` goal in Maven:
    
    mvn package

Then you can build the Docker image:
        
    docker build -t javalin-docker-example .

Finally, you can run the Docker image:

    docker run -p 7000:7000 javalin-docker-example
