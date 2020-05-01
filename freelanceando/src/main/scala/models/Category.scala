package models

import org.json4s.JValue
import org.json4s.DefaultFormats
import org.json4s.JsonAST.JString

/* Object companion of class Category */
object Category extends ModelCompanion[Category] {

  implicit lazy val formats = DefaultFormats

  /* This class constructor needs to be overwritten in every subclass of
   * ModelCompanion, because it needs the direct reference to each subclass,
   * in this case, Category.
   */
  def apply: Category = new Category
  
  //def apply(name: String): Category = new Category(name)
}

class Category(/*name:String*/) extends Model[Category] {
    
  protected[models] var name: String = "" // la idea es que ese es el valor inicial
  
  def getName: String = name

  override def toMap: Map[String, Any] = Map("name" -> name)

  override def fromJson(jsonValue: JValue): Category = {
    (jsonValue \ "name") match {
      case JString(value) => name = value.toString
      case _ =>  // Do nothing
    }
    this  // Return a reference to this object.
  }

}
