package controllers

import models.TestContent
import play.api.http.Writeable
import play.api.libs.json.Reads
import play.api.mvc.{Controller, Action}
import xyz.mattclifton.play.stringent.StringentActions

class TestController(implicit reader: Reads[TestContent], writeable: Writeable[TestContent]) extends Controller with StringentActions {
  def okAction = Action.stringent[OkResult] {
    Ok
  }

  def multipleResults = Action.stringent[OkResult, BadRequestResult] {
    Ok
  }

  def okWithContent = Action.stringent[OkWithContent[TestContent]] {
    Ok.withContent(TestContent(1, "test"))
  }

  def noDoc = Action.stringent[OkWithContent[TestContent]] {
    Ok.withContent(TestContent(1, "test"))
  }

  def existingStatus = Action.stringent[OkWithContent[TestContent]] {
    Ok.withContent(TestContent(1, "test"))
  }

  def createdResult = Action.stringent[CreatedResult] {
    Created
  }

  def createdWithContent = Action.stringent[CreatedWithContent[TestContent]] {
    Created.withContent(TestContent(1, "name"))
  }

  def acceptedResult = Action.stringent[AcceptedResult] {
    Accepted
  }

  def acceptedWithContent = Action.stringent[AcceptedWithContent[TestContent]] {
    Accepted.withContent(TestContent(1, "name"))
  }

  def nonAuthoritativeInformationResult = Action.stringent[NonAuthoritativeInformationResult] {
    NonAuthoritativeInformation
  }

  def nonAuthoritativeInformationWithContent = Action.stringent[NonAuthoritativeInformationWithContent[TestContent]] {
    NonAuthoritativeInformation.withContent(TestContent(1, "name"))
  }

  def noContentResult = Action.stringent[NoContentResult] {
    NoContent
  }

  def resetContentResult = Action.stringent[ResetContentResult] {
    ResetContent
  }

  def partialContentResult = Action.stringent[PartialContentResult] {
    PartialContent
  }

  def partialContentWithContent = Action.stringent[PartialContentWithContent[TestContent]] {
    PartialContent.withContent(TestContent(1, "name"))
  }

  def multiStatusResult = Action.stringent[MultiStatusResult] {
    MultiStatus
  }

  def multiStatusWithContent = Action.stringent[MultiStatusWithContent[TestContent]] {
    MultiStatus.withContent(TestContent(1, "name"))
  }

  def movedPermanentlyResult = Action.stringent[MovedPermanentlyResult] {
    MovedPermanently("/")
  }

  def foundResult = Action.stringent[FoundResult] {
    Found("/")
  }

  def seeOtherResult = Action.stringent[SeeOtherResult] {
    SeeOther("/")
  }

  def notModifiedResult = Action.stringent[NotModifiedResult] {
    NotModified
  }

  def temporaryRedirectResult = Action.stringent[TemporaryRedirectResult] {
    TemporaryRedirect("/")
  }

  def permanentRedirectResult = Action.stringent[PermanentRedirectResult] {
    PermanentRedirect("/")
  }

  def badRequestResult = Action.stringent[BadRequestResult] {
    BadRequest
  }

  def badRequestWithContent = Action.stringent[BadRequestWithContent[TestContent]] {
    BadRequest.withContent(TestContent(1, "name"))
  }

  def unauthorizedResult = Action.stringent[UnauthorizedResult] {
    Unauthorized
  }

  def unauthorizedWithContent = Action.stringent[UnauthorizedWithContent[TestContent]] {
    Unauthorized.withContent(TestContent(1, "name"))
  }

  def paymentRequiredResult = Action.stringent[PaymentRequiredResult] {
    PaymentRequired
  }

  def paymentRequiredWithContent = Action.stringent[PaymentRequiredWithContent[TestContent]] {
    PaymentRequired.withContent(TestContent(1, "name"))
  }

  def forbiddenResult = Action.stringent[ForbiddenResult] {
    Forbidden
  }

  def forbiddenWithContent = Action.stringent[ForbiddenWithContent[TestContent]] {
    Forbidden.withContent(TestContent(1, "name"))
  }

  def notFoundResult = Action.stringent[NotFoundResult] {
    NotFound
  }

  def notFoundWithContent = Action.stringent[NotFoundWithContent[TestContent]] {
    NotFound.withContent(TestContent(1, "name"))
  }

  def methodNotAllowedResult = Action.stringent[MethodNotAllowedResult] {
    MethodNotAllowed
  }

  def methodNotAllowedWithContent = Action.stringent[MethodNotAllowedWithContent[TestContent]] {
    MethodNotAllowed.withContent(TestContent(1, "name"))
  }

  def notAcceptableResult = Action.stringent[NotAcceptableResult] {
    NotAcceptable
  }

  def notAcceptableWithContent = Action.stringent[NotAcceptableWithContent[TestContent]] {
    NotAcceptable.withContent(TestContent(1, "name"))
  }

  def requestTimeoutResult = Action.stringent[RequestTimeoutResult] {
    RequestTimeout
  }

  def requestTimeoutWithContent = Action.stringent[RequestTimeoutWithContent[TestContent]] {
    RequestTimeout.withContent(TestContent(1, "name"))
  }

  def conflictResult = Action.stringent[ConflictResult] {
    Conflict
  }

  def conflictWithContent = Action.stringent[ConflictWithContent[TestContent]] {
    Conflict.withContent(TestContent(1, "name"))
  }

  def goneResult = Action.stringent[GoneResult] {
    Gone
  }

  def goneWithContent = Action.stringent[GoneWithContent[TestContent]] {
    Gone.withContent(TestContent(1, "name"))
  }

  def preconditionFailedResult = Action.stringent[PreconditionFailedResult] {
    PreconditionFailed
  }

  def preconditionFailedWithContent = Action.stringent[PreconditionFailedWithContent[TestContent]] {
    PreconditionFailed.withContent(TestContent(1, "name"))
  }

  def entityTooLargeResult = Action.stringent[EntityTooLargeResult] {
    EntityTooLarge
  }

  def entityTooLargeWithContent = Action.stringent[EntityTooLargeWithContent[TestContent]] {
    EntityTooLarge.withContent(TestContent(1, "name"))
  }

  def uriTooLongResult = Action.stringent[UriTooLongResult] {
    UriTooLong
  }

  def uriTooLongWithContent = Action.stringent[UriTooLongWithContent[TestContent]] {
    UriTooLong.withContent(TestContent(1, "name"))
  }

  def unsupportedMediaTypeResult = Action.stringent[UnsupportedMediaTypeResult] {
    UnsupportedMediaType
  }

  def unsupportedMediaTypeWithContent = Action.stringent[UnsupportedMediaTypeWithContent[TestContent]] {
    UnsupportedMediaType.withContent(TestContent(1, "name"))
  }

  def expectationFailedResult = Action.stringent[ExpectationFailedResult] {
    ExpectationFailed
  }

  def expectationFailedWithContent = Action.stringent[ExpectationFailedWithContent[TestContent]] {
    ExpectationFailed.withContent(TestContent(1, "name"))
  }

  def unprocessableEntityResult = Action.stringent[UnprocessableEntityResult] {
    UnprocessableEntity
  }

  def unprocessableEntityWithContent = Action.stringent[UnprocessableEntityWithContent[TestContent]] {
    UnprocessableEntity.withContent(TestContent(1, "name"))
  }

  def lockedResult = Action.stringent[LockedResult] {
    Locked
  }

  def lockedWithContent = Action.stringent[LockedWithContent[TestContent]] {
    Locked.withContent(TestContent(1, "name"))
  }

  def failedDependencyResult = Action.stringent[FailedDependencyResult] {
    FailedDependency
  }

  def failedDependencyWithContent = Action.stringent[FailedDependencyWithContent[TestContent]] {
    FailedDependency.withContent(TestContent(1, "name"))
  }

  def tooManyRequestsResult = Action.stringent[TooManyRequestsResult] {
    TooManyRequests
  }

  def tooManyRequestsWithContent = Action.stringent[TooManyRequestsWithContent[TestContent]] {
    TooManyRequests.withContent(TestContent(1, "name"))
  }

  def internalServerErrorResult = Action.stringent[InternalServerErrorResult] {
    InternalServerError
  }

  def internalServerErrorWithContent = Action.stringent[InternalServerErrorWithContent[TestContent]] {
    InternalServerError.withContent(TestContent(1, "name"))
  }

  def notImplementedResult = Action.stringent[NotImplementedResult] {
    NotImplemented
  }

  def notImplementedWithContent = Action.stringent[NotImplementedWithContent[TestContent]] {
    NotImplemented.withContent(TestContent(1, "name"))
  }

  def badGatewayResult = Action.stringent[BadGatewayResult] {
    BadGateway
  }

  def badGatewayWithContent = Action.stringent[BadGatewayWithContent[TestContent]] {
    BadGateway.withContent(TestContent(1, "name"))
  }

  def serviceUnavailableResult = Action.stringent[ServiceUnavailableResult] {
    ServiceUnavailable
  }

  def serviceUnavailableWithContent = Action.stringent[ServiceUnavailableWithContent[TestContent]] {
    ServiceUnavailable.withContent(TestContent(1, "name"))
  }

  def gatewayTimeoutResult = Action.stringent[GatewayTimeoutResult] {
    GatewayTimeout
  }

  def gatewayTimeoutWithContent = Action.stringent[GatewayTimeoutWithContent[TestContent]] {
    GatewayTimeout.withContent(TestContent(1, "name"))
  }

  def httpVersionNotSupportedResult = Action.stringent[HttpVersionNotSupportedResult] {
    HttpVersionNotSupported
  }

  def httpVersionNotSupportedWithContent = Action.stringent[HttpVersionNotSupportedWithContent[TestContent]] {
    HttpVersionNotSupported.withContent(TestContent(1, "name"))
  }

  def insufficientStorageResult = Action.stringent[InsufficientStorageResult] {
    InsufficientStorage
  }

  def insufficientStorageWithContent = Action.stringent[InsufficientStorageWithContent[TestContent]] {
    InsufficientStorage.withContent(TestContent(1, "name"))
  }
}
