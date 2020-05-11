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
  // When no ID is given, we assign the value 0.
  protected[models] var id: Int = 0

  implicit lazy val formats = DefaultFormats

  def getId: Int = id  
  
  // Returns a dictionary where keys are the object attributes' names and
  // values are the object attributes' values
  def toMap: Map[String, Any] = Map("id" -> id)

  /* This function fills the object with the attributes in @jsonValue.
   * Every subclass must override this function, because they'll know the
   * types of the new attributes they define.
   */
  def fromJson(jsonValue: JValue): M = {
    (jsonValue \ "id") match {
      case JInt(value) => id = value.toInt
      case _ =>  // Do nothing, things may not have an id
    }
    self
  }

}
