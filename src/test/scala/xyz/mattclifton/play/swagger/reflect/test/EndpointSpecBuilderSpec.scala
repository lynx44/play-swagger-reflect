package xyz.mattclifton.play.swagger.reflect.test

import com.iheart.playSwagger.{PrefixDomainModelQualifier, SwaggerSpecGenerator}
import org.specs2.mutable.Specification
import play.api.libs.json.{JsArray, JsValue, JsObject}
import xyz.mattclifton.play.swagger.reflect.EndPointSpecBuilderFactory

class EndpointSpecBuilderSpec extends Specification {
  implicit val cl = getClass.getClassLoader

  "EndpointSpecBuilder" >> {
    lazy val json = SwaggerSpecGenerator(PrefixDomainModelQualifier(Seq("models"):_*), endpointSpecBuilder = EndPointSpecBuilderFactory.apply).generate("routes.routes").get
    lazy val pathJson = json \ "paths"
    lazy val definitionsJson = json \ "definitions"
    lazy val postBodyJson = (pathJson \ "/body" \ "post").as[JsObject]

    def parametersOf(json: JsValue): Seq[JsValue] = {
      (json \ "parameters").as[JsArray].value
    }

    "generate body parameter with in " >> {
      val params = parametersOf(postBodyJson)
      params.length === 1
      (params.head \ "in").asOpt[String] === Some("body")
      (params.head \ "schema" \ "$ref").asOpt[String] === Some("#/definitions/models.TestContent")
    }
  }
}
