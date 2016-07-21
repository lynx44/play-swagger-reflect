package xyz.mattclifton.play.swagger.stringent

import com.iheart.playSwagger.{JsonHelper, DomainModelQualifier}
import play.api.libs.json.Json.JsValueWrapper
import play.api.libs.json._
import play.api.mvc.AnyContent
import play.routes.compiler.Route

import scala.reflect.runtime.universe._

object EndPointSpecBuilderFactory {
  def apply(modelQualifier: DomainModelQualifier, defaultPostBodyFormat: String, route: Route, tag: Option[String], cl: ClassLoader): EndPointSpecBuilder =
    new EndPointSpecBuilder(modelQualifier, defaultPostBodyFormat, route, tag, cl)
}

class EndPointSpecBuilder(modelQualifier: DomainModelQualifier, defaultPostBodyFormat: String, route: Route, tag: Option[String], cl: ClassLoader) extends xyz.mattclifton.play.swagger.reflect.EndPointSpecBuilder(modelQualifier, defaultPostBodyFormat, route, tag, cl) {
  override protected def transformFinalDoc(finalDoc: JsObject): JsObject = {
    controllerMethod.map(method => {
      println(s"controller method: $method")
      if(method.returnType.typeSymbol.name.decodedName.toString == "StringentReturn") {
        val typeArgs = getGenericTypeArgs(method.returnType)
        println(s"type args: $typeArgs")
        typeArgs.flatMap(args => {
          val responseTypes = args.filter(arg => arg.baseClasses.exists(b => b.name.decodedName.toString == "StringentResult"))
          println(s"response types: $responseTypes")
          val newDoc = Some(finalDoc).map(doc => {
            val responses = (doc \ "responses").asOpt[JsObject].getOrElse(JsObject(Seq()))
            val updatedResponses = responseTypes.foldLeft(responses)((response, arg) => arg.typeSymbol.name.decodedName.toString match {
              case "OkResult" => mergeByResponseCode(play.api.http.Status.OK, response)
              case "CreatedResult" => mergeByResponseCode(play.api.http.Status.CREATED, response)
              case "AcceptedResult" => mergeByResponseCode(play.api.http.Status.ACCEPTED, response)
              case "NonAuthoritativeInformationResult" => mergeByResponseCode(play.api.http.Status.NON_AUTHORITATIVE_INFORMATION, response)
              case "NoContentResult" => mergeByResponseCode(play.api.http.Status.NO_CONTENT, response)
              case "ResetContentResult" => mergeByResponseCode(play.api.http.Status.RESET_CONTENT, response)
              case "PartialContentResult" => mergeByResponseCode(play.api.http.Status.PARTIAL_CONTENT, response)
              case "MultiStatusResult" => mergeByResponseCode(play.api.http.Status.MULTI_STATUS, response)
              case "BadRequestResult" => mergeByResponseCode(play.api.http.Status.BAD_REQUEST, response)
              case "OkWithContent" => mergeByResponseCode(play.api.http.Status.OK, response, Some(arg))
              case "CreatedWithContent" => mergeByResponseCode(play.api.http.Status.CREATED, response, Some(arg))
              case "AcceptedWithContent" => mergeByResponseCode(play.api.http.Status.ACCEPTED, response, Some(arg))
              case "NonAuthoritativeInformationWithContent" => mergeByResponseCode(play.api.http.Status.NON_AUTHORITATIVE_INFORMATION, response, Some(arg))
              case "PartialContentWithContent" => mergeByResponseCode(play.api.http.Status.PARTIAL_CONTENT, response, Some(arg))
              case "MultiStatusWithContent" => mergeByResponseCode(play.api.http.Status.MULTI_STATUS, response, Some(arg))
            })

            println(updatedResponses)

            JsObject(("responses" -> updatedResponses) +: doc.fieldSet.filterNot(_._1 == "responses").toSeq)
          })
          newDoc
        }).getOrElse(finalDoc)
      } else {
        finalDoc
      }
    }).getOrElse(finalDoc)
  }

  private def mergeByResponseCode(statusCode: Int, responses: JsObject, resultType: Option[Type] = None): JsObject = {
    mergeByResponseCode(statusCode, buildSwaggerResponseItem(statusCode, resultType.map(t => getGenericTypeArgs(t).get.head)), responses)
  }

  private def mergeByResponseCode(statusCode: Int, status: JsObject, responses: JsObject): JsObject = {
    val statusCodeValue = (responses \ statusCode.toString).asOpt[JsObject].fold(status)(value => value)
    JsObject(((statusCode.toString -> statusCodeValue) +: responses.fields.filterNot(_._1 == statusCode.toString)))
  }

  private def buildSwaggerResponseItem(statusCode: Int, schemaType: Option[Type] = None): JsObject = {
//    val schema = schemaType.map(t => Seq("schema" -> JsObject(Seq("$ref" -> JsString(s"#/definitions/${packageName(t.typeSymbol)}.${t.typeSymbol.name.decodedName}")))))

//    val toSeq = schema.flatMap(a => a).toSeq
    if(schemaType.nonEmpty) {
      JsObject(Seq("schema" -> JsObject(Seq("$ref" -> JsString(s"#/definitions/${packageName(schemaType.get.typeSymbol)}.${schemaType.get.typeSymbol.name.decodedName}")))))
    } else {
      JsObject(Seq())
    }

  }

  private def packageName(sym: Symbol) = {
    def enclosingPackage(sym: Symbol): Symbol = {
      if (sym == NoSymbol) NoSymbol
      else if (sym.isPackage) sym
      else enclosingPackage(sym.owner)
    }
    val pkg = enclosingPackage(sym)
    if (pkg == scala.reflect.runtime.currentMirror.EmptyPackageClass) ""
    else pkg.fullName
  }
}
