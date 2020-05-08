package models

import org.json4s.JValue
import org.json4s.DefaultFormats
import org.json4s.JsonAST.JString

object Category extends ModelCompanion[Category] {

  implicit lazy val formats = DefaultFormats

  def apply: Category = new Category
  
}

class Category extends Model[Category] {
    
  protected[Category] var name: String = "" 

  def getName: String = name

  override def toMap: Map[String, Any] = 
    super.toMap +
      ("name" -> name)

  override def fromJson(jsonValue: JValue): Category = {
    super.fromJson(jsonValue)
    name = (jsonValue \ "name").extract[String]

    this  
  }
}

