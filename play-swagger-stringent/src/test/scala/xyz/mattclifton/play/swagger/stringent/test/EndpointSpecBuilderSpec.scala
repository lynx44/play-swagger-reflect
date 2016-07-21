package xyz.mattclifton.play.swagger.stringent.test

import com.iheart.playSwagger.{PrefixDomainModelQualifier, SwaggerSpecGenerator}
import org.specs2.mutable.Specification
import play.api.libs.json.{JsString, JsArray, JsObject, JsValue}
import xyz.mattclifton.play.swagger.stringent.EndPointSpecBuilderFactory

class EndpointSpecBuilderSpec extends Specification {
  implicit val cl = getClass.getClassLoader

  "EndpointSpecBuilder" >> {
    lazy val json = SwaggerSpecGenerator(PrefixDomainModelQualifier(Seq("models"):_*), endpointSpecBuilder = EndPointSpecBuilderFactory.apply).generate("routes.routes").get
    lazy val pathJson = json \ "paths"
    lazy val definitionsJson = json \ "definitions"
    lazy val okJson = (pathJson \ "/ok" \ "get").as[JsObject]
    lazy val multipleResultsJson = (pathJson \ "/multiple" \ "post").as[JsObject]
    lazy val okWithContentJson = (pathJson \ "/okWithContent" \ "post").as[JsObject]

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

    "generate response with schema" >> {
      val status200 = (okWithContentJson \ "responses" \ "200").asOpt[JsObject]
      status200.nonEmpty === true
      val schema = (status200.get \ "schema").asOpt[JsValue]
      schema.nonEmpty === true
      val refValue = (status200.get \ "schema" \ "$ref").asOpt[JsString]
      refValue.nonEmpty === true
      refValue.get.value === "#/definitions/models.TestContent"
    }

    "includes expected definition" >> {
      val modelJson = definitionsJson \ "models.TestContent"
      val propertiesJson = modelJson \ "properties"
      val idProperty = (propertiesJson \ "id").asOpt[JsObject]
      val nameProperty = (propertiesJson \ "name").asOpt[JsObject]
      idProperty.nonEmpty === true
      nameProperty.nonEmpty === true
    }

    //generate with no doc

    //merge with existing response codes
  }
}
