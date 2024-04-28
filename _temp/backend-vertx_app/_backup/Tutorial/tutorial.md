# Multiple Verticles

06:15:51.430 [vert.x-eventloop-thread-0] INFO  c.a.t.MainVerticle&nbsp;-&nbsp;<span style="color:red">MainVerticle::verticle started...</span><br/>
06:15:51.440 [vert.x-eventloop-thread-1] INFO  c.a.t.VerticleA&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-&nbsp;<span style="color:blue">VerticleA::verticle started...</span><br/>
06:15:51.455 [vert.x-eventloop-thread-2] INFO  c.a.t.VerticleB&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-&nbsp;<span style="color:green">VerticleB::verticle started...</span><br/>
06:15:51.462 [vert.x-eventloop-thread-0] INFO  c.a.t.MainVerticle&nbsp;-&nbsp;VerticleA::verticle deployed.<br/>
06:15:51.463 [vert.x-eventloop-thread-3] INFO  c.a.t.VerticleAx&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:lightblue">VerticleAx::verticle started...</span><br/>
06:15:51.465 [vert.x-eventloop-thread-1] INFO  c.a.t.VerticleA&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-&nbsp;VerticleAx::verticle deployed.<br/>
06:15:51.465 [vert.x-eventloop-thread-3] INFO  c.a.t.MainVerticle&nbsp;-&nbsp;MainVerticle::verticle deployed.<br/>
06:15:51.475 [vert.x-eventloop-thread-3] INFO  c.a.t.VerticleAx&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-&nbsp;VerticleAx::verticle un-deployed.<br/>
06:15:51.480 [vert.x-eventloop-thread-0] INFO  c.a.t.VerticleBx&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:lightgreen">VerticleBx::verticle started...</span><br/>
06:15:51.490 [vert.x-eventloop-thread-1] INFO  c.a.t.VerticleBx&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:lightgreen">VerticleBx::verticle started...</span><br/>
06:15:51.491 [vert.x-eventloop-thread-0] INFO  c.a.t.MainVerticle&nbsp;-&nbsp;VerticleB::verticle deployed.<br/>
06:15:51.493 [vert.x-eventloop-thread-2] INFO  c.a.t.VerticleBx&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:lightgreen">VerticleBx::verticle started...</span><br/>
06:15:51.494 [vert.x-eventloop-thread-2] INFO  c.a.t.VerticleB&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-&nbsp;VerticleBx::verticle deployed.<br/>
<br/>  
  
# Worker Threads
Option 1 (Verticle: This will deploy automatically within a worker thread without the need to create a dedicated class)<br/>
    ```
    vertx.executeBlocking(
    ```
    <br/>&nbsp;&nbsp;&nbsp;&nbsp;//code<br/>
    ```
    )
    ```

Option 2 (Worker Verticle: Need to create a dedicated class)<br/>
    ```
    vertx.deployVerticle(
    ```
    <br/>&nbsp;&nbsp;&nbsp;&nbsp;$workerVerticle,
    <br/>&nbsp;&nbsp;&nbsp;&nbsp;new DeploymentOptions().setWorker(true)<br/>
    ```    
    )
    ```            
<br/>

## Object Conversion (JSON <-> JavaClass)
1. Java Class to JSON object<br/>
    ```java
    var jsonObj = JsonObject.mapFrom($javaClassObject);
    ``` 
2. JSON object to Java Class<br/>
    ```java
    var javaClassObject = jsonObj.mapTo($javaClass.class);
    ```
<br/>  


## Custom Messaging 
> The EventBus only supports JsonObject and JsonArray as the messaging objects. If you need to use your custom Java class instead, then you need to use Codec and register it in the Vertx.
<br/>


