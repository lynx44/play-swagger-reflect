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
    lazy val noDocJson = (pathJson \ "/noDoc" \ "post").as[JsObject]
    lazy val existingStatusJson = (pathJson \ "/existingStatus" \ "post").as[JsObject]

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

    "includes status when no doc specified" >> {
      (noDocJson \ "responses" \ "200").asOpt[JsValue].nonEmpty === true
    }

    "includes other status when existing status is already documented" >> {
      (existingStatusJson \ "responses" \ "200").asOpt[JsValue].nonEmpty === true
      (existingStatusJson \ "responses" \ "400").asOpt[JsValue].nonEmpty === true
    }

    // throw error when response type doesn't match existing user doc?

    // status codes

    lazy val createdResultJson = (pathJson \ "/createdResult" \ "post").as[JsObject]
    lazy val createdWithContentJson = (pathJson \ "/createdWithContent" \ "post").as[JsObject]

    // Created
    "created result" >> {
      (createdResultJson \ "responses" \ "201").asOpt[JsValue].nonEmpty === true
    }

    "created with content" >> {
      (createdWithContentJson \ "responses" \ "201").asOpt[JsValue].nonEmpty === true
      (createdWithContentJson \ "responses" \ "201" \ "schema" \ "$ref").as[JsString].value === "#/definitions/models.TestContent"
    }

    lazy val acceptedResultJson = (pathJson \ "/acceptedResult" \ "post").as[JsObject]
    lazy val acceptedWithContentJson = (pathJson \ "/acceptedWithContent" \ "post").as[JsObject]

    // Accepted
    "accepted result" >> {
      (acceptedResultJson \ "responses" \ "202").asOpt[JsValue].nonEmpty === true
    }

    "created with content" >> {
      (acceptedWithContentJson \ "responses" \ "202").asOpt[JsValue].nonEmpty === true
      (acceptedWithContentJson \ "responses" \ "202" \ "schema" \ "$ref").as[JsString].value === "#/definitions/models.TestContent"
    }

    lazy val nonAuthoritativeInformationResultJson = (pathJson \ "/nonAuthoritativeInformationResult" \ "post").as[JsObject]
    lazy val nonAuthoritativeInformationWithContentJson = (pathJson \ "/nonAuthoritativeInformationWithContent" \ "post").as[JsObject]

    // NonAuthoritativeInformation
    "NonAuthoritativeInformation result" >> {
      (nonAuthoritativeInformationResultJson \ "responses" \ "203").asOpt[JsValue].nonEmpty === true
    }

    "NonAuthoritativeInformation with content" >> {
      (nonAuthoritativeInformationWithContentJson \ "responses" \ "203").asOpt[JsValue].nonEmpty === true
      (nonAuthoritativeInformationWithContentJson \ "responses" \ "203" \ "schema" \ "$ref").as[JsString].value === "#/definitions/models.TestContent"
    }

    lazy val noContentResultJson = (pathJson \ "/noContentResult" \ "post").as[JsObject]

    // NoContent
    "NoContent result" >> {
      (noContentResultJson \ "responses" \ "204").asOpt[JsValue].nonEmpty === true
    }

    lazy val resetContentResultJson = (pathJson \ "/resetContentResult" \ "post").as[JsObject]

    // ResetContent
    "ResetContent result" >> {
      (resetContentResultJson \ "responses" \ "205").asOpt[JsValue].nonEmpty === true
    }

    lazy val partialContentResultJson = (pathJson \ "/partialContentResult" \ "post").as[JsObject]
    lazy val partialContentWithContentJson = (pathJson \ "/partialContentWithContent" \ "post").as[JsObject]

    // PartialContent
    "PartialContent result" >> {
      (partialContentResultJson \ "responses" \ "206").asOpt[JsValue].nonEmpty === true
    }

    "PartialContent with content" >> {
      (partialContentWithContentJson \ "responses" \ "206").asOpt[JsValue].nonEmpty === true
      (partialContentWithContentJson \ "responses" \ "206" \ "schema" \ "$ref").as[JsString].value === "#/definitions/models.TestContent"
    }

    lazy val multiStatusResultJson = (pathJson \ "/multiStatusResult" \ "post").as[JsObject]
    lazy val multiStatusWithContentJson = (pathJson \ "/multiStatusWithContent" \ "post").as[JsObject]

    // MultiStatus
    "MultiStatus result" >> {
      (multiStatusResultJson \ "responses" \ "207").asOpt[JsValue].nonEmpty === true
    }

    "MultiStatus with content" >> {
      (multiStatusWithContentJson \ "responses" \ "207").asOpt[JsValue].nonEmpty === true
      (multiStatusWithContentJson \ "responses" \ "207" \ "schema" \ "$ref").as[JsString].value === "#/definitions/models.TestContent"
    }

  }
}
