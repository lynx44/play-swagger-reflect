## Purpose

The goal of play-swagger-reflect and play-swagger-stringent is to automatically generate swagger documentation from Play Framework controller actions. This ensures your documentation is always up to date without manual effort.

To get the most out of this project, you'll want to use [play-stringent](https://github.com/lynx44/play-stringent) with play-swagger-stringent.

### Example

Create an action using stringent actions:

    def test = Action.stringent.withContent[TestContent, OkWithContent[TestResponse], BadRequestResult](parse.json[TestContent]){ request =>
        if(!requestIsValid(request)) {
            BadRequest
        } else {
            Ok.withContent(TestResponse(1, "test"))
        }
    }

This will create the following swagger documentation:

    {  
       // ...
       "/test":{
          "post":{
             "responses":{
                "400":{
                   "description":"400 response"
                },
                "200":{
                   "description":"200 response",
                   "schema":{
                      "$ref":"#/definitions/org.example.project.TestResource"
                   }
                }
             },
             "tags":[
                "routes"
             ],
             "parameters":[
                {
                   "name":"body",
                   "schema":{
                      "$ref":"#/definitions/org.example.project.TestContent"
                   },
                   "in":"body"
                }
             ],
             "consumes":[
                "application/json"
             ]
          }
       }
       // ...
    }

## Configuration

### sbt

    resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"

    libraryDependencies ++= Seq(
        "xyz.mattclifton" %% "play-swagger" % "0.3.4-PLAY2.5-SNAPSHOT",
        "xyz.mattclifton" %% "play-swagger-stringent" % "0.1-SNAPSHOT"
    )

### Swagger Configuration

Swagger generation is based on [play-swagger](https://github.com/iheartradio/play-swagger) (currently this [forked version](https://github.com/lynx44/play-swagger)), so configuration remains similar.

To use play-swagger stringent, you need to inject the stringent endpoint builder:

    import com.iheart.playSwagger.{PrefixDomainModelQualifier, SwaggerSpecGenerator}
    import xyz.mattclifton.play.swagger.stringent.EndPointSpecBuilderFactory
    
    class ApplicationController {
    
        implicit val cl = getClass.getClassLoader
        private lazy val generator = new SwaggerSpecGenerator(PrefixDomainModelQualifier(Seq("org.example.project"):_*), endpointSpecBuilder = EndPointSpecBuilderFactory.apply)
        
        def spec = Action.async { _ =>
            Future.fromTry(generator.generate()).map(Ok(_)) //generate() can also taking in an optional arg of the route file name.
        }
    }

## License

[MIT](https://github.com/lynx44/play-stringent/blob/master/LICENSE)