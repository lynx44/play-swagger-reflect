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

    "includes description" >> {
      (okJson \ "responses" \ "200" \ "description").asOpt[JsValue].nonEmpty === true
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

    lazy val movedPermanentlyResultJson = (pathJson \ "/movedPermanentlyResult" \ "post").as[JsObject]

    // MovedPermanently
    "MovedPermanently result" >> {
      (movedPermanentlyResultJson \ "responses" \ "301").asOpt[JsValue].nonEmpty === true
    }

    lazy val foundResultJson = (pathJson \ "/foundResult" \ "post").as[JsObject]

    // Found
    "Found result" >> {
      (foundResultJson \ "responses" \ "302").asOpt[JsValue].nonEmpty === true
    }

    lazy val seeOtherResultJson = (pathJson \ "/seeOtherResult" \ "post").as[JsObject]

    // SeeOther
    "SeeOther result" >> {
      (seeOtherResultJson \ "responses" \ "303").asOpt[JsValue].nonEmpty === true
    }

    lazy val notModifiedResultJson = (pathJson \ "/notModifiedResult" \ "post").as[JsObject]

    // NotModified
    "NotModified result" >> {
      (notModifiedResultJson \ "responses" \ "304").asOpt[JsValue].nonEmpty === true
    }

    lazy val temporaryRedirectResultJson = (pathJson \ "/temporaryRedirectResult" \ "post").as[JsObject]

    // TemporaryRedirect
    "TemporaryRedirect result" >> {
      (temporaryRedirectResultJson \ "responses" \ "307").asOpt[JsValue].nonEmpty === true
    }

    lazy val permanentRedirectResultJson = (pathJson \ "/permanentRedirectResult" \ "post").as[JsObject]

    // PermanentRedirect
    "PermanentRedirect result" >> {
      (permanentRedirectResultJson \ "responses" \ "308").asOpt[JsValue].nonEmpty === true
    }

    lazy val badRequestResultJson = (pathJson \ "/badRequestResult" \ "post").as[JsObject]
    lazy val badRequestWithContentJson = (pathJson \ "/badRequestWithContent" \ "post").as[JsObject]

    // BadRequest
    "BadRequest result" >> {
      (badRequestResultJson \ "responses" \ "400").asOpt[JsValue].nonEmpty === true
    }

    "BadRequest with content" >> {
      (badRequestWithContentJson \ "responses" \ "400").asOpt[JsValue].nonEmpty === true
      (badRequestWithContentJson \ "responses" \ "400" \ "schema" \ "$ref").as[JsString].value === "#/definitions/models.TestContent"
    }

    lazy val unauthorizedResultJson = (pathJson \ "/unauthorizedResult" \ "post").as[JsObject]
    lazy val unauthorizedWithContentJson = (pathJson \ "/unauthorizedWithContent" \ "post").as[JsObject]

    // Unauthorized
    "Unauthorized result" >> {
      (unauthorizedResultJson \ "responses" \ "401").asOpt[JsValue].nonEmpty === true
    }

    "Unauthorized with content" >> {
      (unauthorizedWithContentJson \ "responses" \ "401").asOpt[JsValue].nonEmpty === true
      (unauthorizedWithContentJson \ "responses" \ "401" \ "schema" \ "$ref").as[JsString].value === "#/definitions/models.TestContent"
    }

    lazy val paymentRequiredResultJson = (pathJson \ "/paymentRequiredResult" \ "post").as[JsObject]
    lazy val paymentRequiredWithContentJson = (pathJson \ "/paymentRequiredWithContent" \ "post").as[JsObject]

    // PaymentRequired
    "PaymentRequired result" >> {
      (paymentRequiredResultJson \ "responses" \ "402").asOpt[JsValue].nonEmpty === true
    }

    "PaymentRequired with content" >> {
      (paymentRequiredWithContentJson \ "responses" \ "402").asOpt[JsValue].nonEmpty === true
      (paymentRequiredWithContentJson \ "responses" \ "402" \ "schema" \ "$ref").as[JsString].value === "#/definitions/models.TestContent"
    }

    lazy val forbiddenResultJson = (pathJson \ "/forbiddenResult" \ "post").as[JsObject]
    lazy val forbiddenWithContentJson = (pathJson \ "/forbiddenWithContent" \ "post").as[JsObject]

    // Forbidden
    "Forbidden result" >> {
      (forbiddenResultJson \ "responses" \ "403").asOpt[JsValue].nonEmpty === true
    }

    "Forbidden with content" >> {
      (forbiddenWithContentJson \ "responses" \ "403").asOpt[JsValue].nonEmpty === true
      (forbiddenWithContentJson \ "responses" \ "403" \ "schema" \ "$ref").as[JsString].value === "#/definitions/models.TestContent"
    }

    lazy val notFoundResultJson = (pathJson \ "/notFoundResult" \ "post").as[JsObject]
    lazy val notFoundWithContentJson = (pathJson \ "/notFoundWithContent" \ "post").as[JsObject]

    // NotFound
    "NotFound result" >> {
      (notFoundResultJson \ "responses" \ "404").asOpt[JsValue].nonEmpty === true
    }

    "NotFound with content" >> {
      (notFoundWithContentJson \ "responses" \ "404").asOpt[JsValue].nonEmpty === true
      (notFoundWithContentJson \ "responses" \ "404" \ "schema" \ "$ref").as[JsString].value === "#/definitions/models.TestContent"
    }

    lazy val methodNotAllowedResultJson = (pathJson \ "/methodNotAllowedResult" \ "post").as[JsObject]
    lazy val methodNotAllowedWithContentJson = (pathJson \ "/methodNotAllowedWithContent" \ "post").as[JsObject]

    // MethodNotAllowed
    "MethodNotAllowed result" >> {
      (methodNotAllowedResultJson \ "responses" \ "405").asOpt[JsValue].nonEmpty === true
    }

    "MethodNotAllowed with content" >> {
      (methodNotAllowedWithContentJson \ "responses" \ "405").asOpt[JsValue].nonEmpty === true
      (methodNotAllowedWithContentJson \ "responses" \ "405" \ "schema" \ "$ref").as[JsString].value === "#/definitions/models.TestContent"
    }

    lazy val notAcceptableResultJson = (pathJson \ "/notAcceptableResult" \ "post").as[JsObject]
    lazy val notAcceptableWithContentJson = (pathJson \ "/notAcceptableWithContent" \ "post").as[JsObject]

    // NotAcceptable
    "NotAcceptable result" >> {
      (notAcceptableResultJson \ "responses" \ "406").asOpt[JsValue].nonEmpty === true
    }

    "NotAcceptable with content" >> {
      (notAcceptableWithContentJson \ "responses" \ "406").asOpt[JsValue].nonEmpty === true
      (notAcceptableWithContentJson \ "responses" \ "406" \ "schema" \ "$ref").as[JsString].value === "#/definitions/models.TestContent"
    }

    lazy val requestTimeoutResultJson = (pathJson \ "/requestTimeoutResult" \ "post").as[JsObject]
    lazy val requestTimeoutWithContentJson = (pathJson \ "/requestTimeoutWithContent" \ "post").as[JsObject]

    // RequestTimeout
    "RequestTimeout result" >> {
      (requestTimeoutResultJson \ "responses" \ "408").asOpt[JsValue].nonEmpty === true
    }

    "RequestTimeout with content" >> {
      (requestTimeoutWithContentJson \ "responses" \ "408").asOpt[JsValue].nonEmpty === true
      (requestTimeoutWithContentJson \ "responses" \ "408" \ "schema" \ "$ref").as[JsString].value === "#/definitions/models.TestContent"
    }

    lazy val conflictResultJson = (pathJson \ "/conflictResult" \ "post").as[JsObject]
    lazy val conflictWithContentJson = (pathJson \ "/conflictWithContent" \ "post").as[JsObject]

    // Conflict
    "Conflict result" >> {
      (conflictResultJson \ "responses" \ "409").asOpt[JsValue].nonEmpty === true
    }

    "Conflict with content" >> {
      (conflictWithContentJson \ "responses" \ "409").asOpt[JsValue].nonEmpty === true
      (conflictWithContentJson \ "responses" \ "409" \ "schema" \ "$ref").as[JsString].value === "#/definitions/models.TestContent"
    }

    lazy val goneResultJson = (pathJson \ "/goneResult" \ "post").as[JsObject]
    lazy val goneWithContentJson = (pathJson \ "/goneWithContent" \ "post").as[JsObject]

    // Gone
    "Gone result" >> {
      (goneResultJson \ "responses" \ "410").asOpt[JsValue].nonEmpty === true
    }

    "Gone with content" >> {
      (goneWithContentJson \ "responses" \ "410").asOpt[JsValue].nonEmpty === true
      (goneWithContentJson \ "responses" \ "410" \ "schema" \ "$ref").as[JsString].value === "#/definitions/models.TestContent"
    }

    lazy val preconditionFailedResultJson = (pathJson \ "/preconditionFailedResult" \ "post").as[JsObject]
    lazy val preconditionFailedWithContentJson = (pathJson \ "/preconditionFailedWithContent" \ "post").as[JsObject]

    // PreconditionFailed
    "PreconditionFailed result" >> {
      (preconditionFailedResultJson \ "responses" \ "412").asOpt[JsValue].nonEmpty === true
    }

    "PreconditionFailed with content" >> {
      (preconditionFailedWithContentJson \ "responses" \ "412").asOpt[JsValue].nonEmpty === true
      (preconditionFailedWithContentJson \ "responses" \ "412" \ "schema" \ "$ref").as[JsString].value === "#/definitions/models.TestContent"
    }

    lazy val entityTooLargeResultJson = (pathJson \ "/entityTooLargeResult" \ "post").as[JsObject]
    lazy val entityTooLargeWithContentJson = (pathJson \ "/entityTooLargeWithContent" \ "post").as[JsObject]

    // EntityTooLarge
    "EntityTooLarge result" >> {
      (entityTooLargeResultJson \ "responses" \ "413").asOpt[JsValue].nonEmpty === true
    }

    "EntityTooLarge with content" >> {
      (entityTooLargeWithContentJson \ "responses" \ "413").asOpt[JsValue].nonEmpty === true
      (entityTooLargeWithContentJson \ "responses" \ "413" \ "schema" \ "$ref").as[JsString].value === "#/definitions/models.TestContent"
    }

    lazy val uriTooLongResultJson = (pathJson \ "/uriTooLongResult" \ "post").as[JsObject]
    lazy val uriTooLongWithContentJson = (pathJson \ "/uriTooLongWithContent" \ "post").as[JsObject]

    // UriTooLong
    "UriTooLong result" >> {
      (uriTooLongResultJson \ "responses" \ "414").asOpt[JsValue].nonEmpty === true
    }

    "UriTooLong with content" >> {
      (uriTooLongWithContentJson \ "responses" \ "414").asOpt[JsValue].nonEmpty === true
      (uriTooLongWithContentJson \ "responses" \ "414" \ "schema" \ "$ref").as[JsString].value === "#/definitions/models.TestContent"
    }

    lazy val unsupportedMediaTypeResultJson = (pathJson \ "/unsupportedMediaTypeResult" \ "post").as[JsObject]
    lazy val unsupportedMediaTypeWithContentJson = (pathJson \ "/unsupportedMediaTypeWithContent" \ "post").as[JsObject]

    // UnsupportedMediaType
    "UnsupportedMediaType result" >> {
      (unsupportedMediaTypeResultJson \ "responses" \ "415").asOpt[JsValue].nonEmpty === true
    }

    "UnsupportedMediaType with content" >> {
      (unsupportedMediaTypeWithContentJson \ "responses" \ "415").asOpt[JsValue].nonEmpty === true
      (unsupportedMediaTypeWithContentJson \ "responses" \ "415" \ "schema" \ "$ref").as[JsString].value === "#/definitions/models.TestContent"
    }

    lazy val expectationFailedResultJson = (pathJson \ "/expectationFailedResult" \ "post").as[JsObject]
    lazy val expectationFailedWithContentJson = (pathJson \ "/expectationFailedWithContent" \ "post").as[JsObject]

    // ExpectationFailed
    "ExpectationFailed result" >> {
      (expectationFailedResultJson \ "responses" \ "417").asOpt[JsValue].nonEmpty === true
    }

    "ExpectationFailed with content" >> {
      (expectationFailedWithContentJson \ "responses" \ "417").asOpt[JsValue].nonEmpty === true
      (expectationFailedWithContentJson \ "responses" \ "417" \ "schema" \ "$ref").as[JsString].value === "#/definitions/models.TestContent"
    }

    lazy val unprocessableEntityResultJson = (pathJson \ "/unprocessableEntityResult" \ "post").as[JsObject]
    lazy val unprocessableEntityWithContentJson = (pathJson \ "/unprocessableEntityWithContent" \ "post").as[JsObject]

    // UnprocessableEntity
    "UnprocessableEntity result" >> {
      (unprocessableEntityResultJson \ "responses" \ "422").asOpt[JsValue].nonEmpty === true
    }

    "UnprocessableEntity with content" >> {
      (unprocessableEntityWithContentJson \ "responses" \ "422").asOpt[JsValue].nonEmpty === true
      (unprocessableEntityWithContentJson \ "responses" \ "422" \ "schema" \ "$ref").as[JsString].value === "#/definitions/models.TestContent"
    }

    lazy val lockedResultJson = (pathJson \ "/lockedResult" \ "post").as[JsObject]
    lazy val lockedWithContentJson = (pathJson \ "/lockedWithContent" \ "post").as[JsObject]

    // Locked
    "Locked result" >> {
      (lockedResultJson \ "responses" \ "423").asOpt[JsValue].nonEmpty === true
    }

    "Locked with content" >> {
      (lockedWithContentJson \ "responses" \ "423").asOpt[JsValue].nonEmpty === true
      (lockedWithContentJson \ "responses" \ "423" \ "schema" \ "$ref").as[JsString].value === "#/definitions/models.TestContent"
    }

    lazy val failedDependencyResultJson = (pathJson \ "/failedDependencyResult" \ "post").as[JsObject]
    lazy val failedDependencyWithContentJson = (pathJson \ "/failedDependencyWithContent" \ "post").as[JsObject]

    // FailedDependency
    "FailedDependency result" >> {
      (failedDependencyResultJson \ "responses" \ "424").asOpt[JsValue].nonEmpty === true
    }

    "FailedDependency with content" >> {
      (failedDependencyWithContentJson \ "responses" \ "424").asOpt[JsValue].nonEmpty === true
      (failedDependencyWithContentJson \ "responses" \ "424" \ "schema" \ "$ref").as[JsString].value === "#/definitions/models.TestContent"
    }

    lazy val tooManyRequestsResultJson = (pathJson \ "/tooManyRequestsResult" \ "post").as[JsObject]
    lazy val tooManyRequestsWithContentJson = (pathJson \ "/tooManyRequestsWithContent" \ "post").as[JsObject]

    // TooManyRequests
    "TooManyRequests result" >> {
      (tooManyRequestsResultJson \ "responses" \ "429").asOpt[JsValue].nonEmpty === true
    }

    "TooManyRequests with content" >> {
      (tooManyRequestsWithContentJson \ "responses" \ "429").asOpt[JsValue].nonEmpty === true
      (tooManyRequestsWithContentJson \ "responses" \ "429" \ "schema" \ "$ref").as[JsString].value === "#/definitions/models.TestContent"
    }

    lazy val internalServerErrorResultJson = (pathJson \ "/internalServerErrorResult" \ "post").as[JsObject]
    lazy val internalServerErrorWithContentJson = (pathJson \ "/internalServerErrorWithContent" \ "post").as[JsObject]

    // InternalServerError
    "InternalServerError result" >> {
      (internalServerErrorResultJson \ "responses" \ "500").asOpt[JsValue].nonEmpty === true
    }

    "InternalServerError with content" >> {
      (internalServerErrorWithContentJson \ "responses" \ "500").asOpt[JsValue].nonEmpty === true
      (internalServerErrorWithContentJson \ "responses" \ "500" \ "schema" \ "$ref").as[JsString].value === "#/definitions/models.TestContent"
    }

    lazy val notImplementedResultJson = (pathJson \ "/notImplementedResult" \ "post").as[JsObject]
    lazy val notImplementedWithContentJson = (pathJson \ "/notImplementedWithContent" \ "post").as[JsObject]

    // NotImplemented
    "NotImplemented result" >> {
      (notImplementedResultJson \ "responses" \ "501").asOpt[JsValue].nonEmpty === true
    }

    "NotImplemented with content" >> {
      (notImplementedWithContentJson \ "responses" \ "501").asOpt[JsValue].nonEmpty === true
      (notImplementedWithContentJson \ "responses" \ "501" \ "schema" \ "$ref").as[JsString].value === "#/definitions/models.TestContent"
    }
    
    lazy val badGatewayResultJson = (pathJson \ "/badGatewayResult" \ "post").as[JsObject]
    lazy val badGatewayWithContentJson = (pathJson \ "/badGatewayWithContent" \ "post").as[JsObject]

    // BadGateway
    "BadGateway result" >> {
      (badGatewayResultJson \ "responses" \ "502").asOpt[JsValue].nonEmpty === true
    }

    "BadGateway with content" >> {
      (badGatewayWithContentJson \ "responses" \ "502").asOpt[JsValue].nonEmpty === true
      (badGatewayWithContentJson \ "responses" \ "502" \ "schema" \ "$ref").as[JsString].value === "#/definitions/models.TestContent"
    }
    
    lazy val serviceUnavailableResultJson = (pathJson \ "/serviceUnavailableResult" \ "post").as[JsObject]
    lazy val serviceUnavailableWithContentJson = (pathJson \ "/serviceUnavailableWithContent" \ "post").as[JsObject]

    // ServiceUnavailable
    "ServiceUnavailable result" >> {
      (serviceUnavailableResultJson \ "responses" \ "503").asOpt[JsValue].nonEmpty === true
    }

    "ServiceUnavailable with content" >> {
      (serviceUnavailableWithContentJson \ "responses" \ "503").asOpt[JsValue].nonEmpty === true
      (serviceUnavailableWithContentJson \ "responses" \ "503" \ "schema" \ "$ref").as[JsString].value === "#/definitions/models.TestContent"
    }
    
    lazy val gatewayTimeoutResultJson = (pathJson \ "/gatewayTimeoutResult" \ "post").as[JsObject]
    lazy val gatewayTimeoutWithContentJson = (pathJson \ "/gatewayTimeoutWithContent" \ "post").as[JsObject]

    // GatewayTimeout
    "GatewayTimeout result" >> {
      (gatewayTimeoutResultJson \ "responses" \ "504").asOpt[JsValue].nonEmpty === true
    }

    "GatewayTimeout with content" >> {
      (gatewayTimeoutWithContentJson \ "responses" \ "504").asOpt[JsValue].nonEmpty === true
      (gatewayTimeoutWithContentJson \ "responses" \ "504" \ "schema" \ "$ref").as[JsString].value === "#/definitions/models.TestContent"
    }
    
    lazy val httpVersionNotSupportedResultJson = (pathJson \ "/httpVersionNotSupportedResult" \ "post").as[JsObject]
    lazy val httpVersionNotSupportedWithContentJson = (pathJson \ "/httpVersionNotSupportedWithContent" \ "post").as[JsObject]

    // HttpVersionNotSupported
    "HttpVersionNotSupported result" >> {
      (httpVersionNotSupportedResultJson \ "responses" \ "505").asOpt[JsValue].nonEmpty === true
    }

    "HttpVersionNotSupported with content" >> {
      (httpVersionNotSupportedWithContentJson \ "responses" \ "505").asOpt[JsValue].nonEmpty === true
      (httpVersionNotSupportedWithContentJson \ "responses" \ "505" \ "schema" \ "$ref").as[JsString].value === "#/definitions/models.TestContent"
    }
    
    lazy val insufficientStorageResultJson = (pathJson \ "/insufficientStorageResult" \ "post").as[JsObject]
    lazy val insufficientStorageWithContentJson = (pathJson \ "/insufficientStorageWithContent" \ "post").as[JsObject]

    // InsufficientStorage
    "InsufficientStorage result" >> {
      (insufficientStorageResultJson \ "responses" \ "507").asOpt[JsValue].nonEmpty === true
    }

    "InsufficientStorage with content" >> {
      (insufficientStorageWithContentJson \ "responses" \ "507").asOpt[JsValue].nonEmpty === true
      (insufficientStorageWithContentJson \ "responses" \ "507" \ "schema" \ "$ref").as[JsString].value === "#/definitions/models.TestContent"
    }
  }
}
