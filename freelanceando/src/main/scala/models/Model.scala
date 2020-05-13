package models

import org.json4s.{DefaultFormats, JValue, JInt}
import models.database.DatabaseTable

/* This companion allows us to create models from
 * the DatabaseTable class.
 */
trait ModelCompanion[M <: Model[M]] {

  /* In scala, the apply method is a class constructor. It is used to build
   * new instances of class M with different parameters from the regular
   * constructor.
   */
  def apply: M

}


trait Model[M <: Model[M]] { self: M =>
  protected[models] var id: Int = 0

  implicit lazy val formats = DefaultFormats

  def getId: Int = id  
  
  def toMap: Map[String, Any] = Map("id" -> id)

  def fromJson(jsonValue: JValue): M = {
    (jsonValue \ "id") match {
      case JInt(value) => id = value.toInt
      case _ =>  
    }
    self
  }

}
