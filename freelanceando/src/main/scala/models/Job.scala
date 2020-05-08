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
    title = (jsonValue \ "title").extract[String]
    category_id = (jsonValue \ "category_id").extract[Int]
    client_id = (jsonValue \ "client_id").extract[Int]
    preferred_expertise = (jsonValue \ "preferred_expertise").extract[String]
    preferred_country = (jsonValue \ "preferred_country").extract[String]
    hourly_price = (jsonValue \ "hourly_price").extract[Int]
    
    this  
  }
}

