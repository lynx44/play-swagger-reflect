package controllers

import models.TestContent
import play.api.libs.json.Reads
import play.api.mvc.{Controller, Action}

class TestController(implicit reader: Reads[TestContent]) extends Controller {
  def bodyAction = Action(parse.json[TestContent]) {
    ???
  }

  def noBodyAction = Action {
    Ok
  }
}
