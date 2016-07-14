package xyz.mattclifton.play.swagger.reflect

import com.iheart.playSwagger.{JsonHelper, DomainModelQualifier}
import play.api.libs.json.{Json, JsString, JsObject, JsArray}
import play.routes.compiler.Route

import scala.reflect.runtime.universe._

object EndPointSpecBuilderFactory {
  def apply(modelQualifier: DomainModelQualifier, defaultPostBodyFormat: String, route: Route, tag: Option[String], cl: ClassLoader): EndPointSpecBuilder =
    new EndPointSpecBuilder(modelQualifier, defaultPostBodyFormat, route, tag, cl)
}

class EndPointSpecBuilder(modelQualifier: DomainModelQualifier, defaultPostBodyFormat: String, route: Route, tag: Option[String], cl: ClassLoader) extends com.iheart.playSwagger.EndPointSpecBuilder(modelQualifier, defaultPostBodyFormat, route, tag, cl) {
  override protected def readControllerParams: JsArray = {
    val params = super.readControllerParams
    val className = s"${route.call.packageName}.${route.call.controller}"

    val mirror = runtimeMirror(cl)
    val controllerType = mirror.classSymbol(cl.loadClass(className)).toType
    val method = controllerType.members.collect({
      case m: MethodSymbol if m.name.toString == route.call.method => m
    }).head

    val typeArgs = getGenericTypeArgs(method.returnType)

    val bodyType = typeArgs.map(_.head)

    val bodyParam = JsonHelper.findByName(params, "body")
    val newParams = bodyParam.fold(JsArray(Seq(JsObject(Map("name" → JsString("body"), "schema" -> Json.obj("$ref" → JsString(s"#/definitions/${bodyType.get}")), "in" → JsString("body")))) ++ params.value)) { param ⇒
      params
    }
    newParams
  }

  protected def getGenericTypeArgs(t: Type): Option[List[Type]] = {
    t match {
      case TypeRef(_, _, args) => if (args.nonEmpty) Option(args) else None
      case AnnotatedType(_, underlying) => underlying match {
        case TypeRef(_, _, args) => if (args.nonEmpty) Option(args) else None
      }
      case _ => None
    }
  }
}
