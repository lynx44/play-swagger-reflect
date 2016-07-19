package xyz.mattclifton.play.swagger.stringent.test

import com.iheart.playSwagger.{PrefixDomainModelQualifier, SwaggerSpecGenerator}
import org.specs2.mutable.Specification
import play.api.libs.json.{JsArray, JsObject, JsValue}
import xyz.mattclifton.play.swagger.stringent.EndPointSpecBuilderFactory

class EndpointSpecBuilderSpec extends Specification {
  implicit val cl = getClass.getClassLoader

  "EndpointSpecBuilder" >> {
    lazy val json = SwaggerSpecGenerator(PrefixDomainModelQualifier(Seq("models"):_*), endpointSpecBuilder = EndPointSpecBuilderFactory.apply).generate("routes.routes").get
    lazy val pathJson = json \ "paths"
    lazy val definitionsJson = json \ "definitions"
    lazy val okJson = (pathJson \ "/ok" \ "get").as[JsObject]
    lazy val multipleResultsJson = (pathJson \ "/multiple" \ "post").as[JsObject]

    def parametersOf(json: JsValue): Option[JsArray] = {
      (json \ "parameters").asOpt[JsArray]
    }

    "generate basic response code" >> {
      (okJson \ "responses" \ "200").asOpt[JsValue].nonEmpty === true
    }

    "generate multiple response codes" >> {
      (multipleResultsJson \ "responses" \ "200").asOpt[JsValue].nonEmpty === true
      (multipleResultsJson \ "responses" \ "400").asOpt[JsValue].nonEmpty === true
    }
  }
}
