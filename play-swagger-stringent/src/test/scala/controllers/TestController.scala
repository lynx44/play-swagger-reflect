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
}
