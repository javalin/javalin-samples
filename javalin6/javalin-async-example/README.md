# javalin-async-example

This repo contains a benchmarking tool created for playing around with futures in [Javalin](https://javalin.io).

The plugin [javalin-ssl](https://github.com/javalin/javalin-ssl) is used to enable HTTP/2.

It's only intended as an illustration of the concepts of running requests asynchronously. Since your browser is being used
as a benchmarking tool results will become wildly inaccurate if you try to send too many requests.

![bench](https://user-images.githubusercontent.com/1521451/38759263-55f68e5e-3f75-11e8-93df-1185afd0120c.png)
