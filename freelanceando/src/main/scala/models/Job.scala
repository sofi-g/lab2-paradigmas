package models

import org.json4s.JValue
import org.json4s.JInt
import org.json4s.DefaultFormats
import org.json4s.JsonAST.JString

object Job extends ModelCompanion[Job] {

  implicit lazy val formats = DefaultFormats

  def apply: Job = new Job
}

class Job extends Model[Job] {

  protected[Job] var title: String = "" 
  protected[Job] var category_id: Int = 0 
  protected[Job] var client_id: Int = 0 
  protected[Job] var preferred_expertise: String = "" 
  protected[Job] var preferred_country: String = "" 
  protected[Job] var hourly_price: Int = 0
  
  //client_id: int, preferred_expertise: str, preferred_country: str, hourly_price: int}]

  def getUserName: String = title

  override def toMap: Map[String, Any] = 
    super.toMap + (
      "title" -> title, 
      "category_id" -> category_id,
      "client_id"-> client_id,
      "preferred_expertise" -> preferred_expertise,
      "preferred_country" -> preferred_country,
      "hourly_price" -> hourly_price
    )
      
  override def fromJson(jsonValue: JValue): Job = {
    super.fromJson(jsonValue)
    (jsonValue \ "title") match {
      case JString(value) => title = value.toString
      case _ =>  
    }
    (jsonValue \ "category_id") match {
      case JInt(value) => category_id = value.toInt
      case _ => 
    }
    (jsonValue \ "client_id") match {
      case JInt(value) => client_id = value.toInt
      case _ => 
    }
    (jsonValue \ "preferred_expertise") match {
      case JString(value) => preferred_expertise = value.toString
      case _ => 
    }
    (jsonValue \ "preferred_country") match {
      case JString(value) => preferred_country = value.toString
      case _ => 
    }
    this  
  }
}

