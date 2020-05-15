package models

import org.json4s.{DefaultFormats, JValue, JInt, JNothing}
import org.json4s.DefaultFormats
import org.json4s.JsonAST.JString

object Freelancer extends ModelCompanion[Freelancer] {

  implicit lazy val formats = DefaultFormats

  def apply: Freelancer = new Freelancer

}

class Freelancer extends Model[Freelancer] {

  protected[Freelancer] var username: String = "" 
  protected[Freelancer] var country_code: String = ""
  protected[Freelancer] var category_ids: List[Int] = List()
  protected[Freelancer] var reputation: String = "junior" 
  protected[Freelancer] var hourly_price: Int = 0
  protected[Freelancer] var total_earnings: Int = 0
  
  def getUserName: String = username
 
  def getCategoryIds: List[Int] = category_ids
 
  def increment(amount: Int): Freelancer = {
     total_earnings += amount
     this
  }

  override def toMap: Map[String, Any] = 
    super.toMap + ( 
      "username" -> username, 
      "country_code" -> country_code, 
      "category_ids" -> category_ids, 
      "reputation" -> reputation,
      "hourly_price" -> hourly_price,
      "total_earnings" -> total_earnings
    )
 
  override def fromJson(jsonValue: JValue): Freelancer = {  
    super.fromJson(jsonValue) 
    username = (jsonValue \ "username").extract[String]
    country_code = (jsonValue \ "country_code").extract[String]  
    category_ids = (jsonValue \ "category_ids").extract[List[Int]]
    hourly_price = (jsonValue \ "hourly_price").extract[Int]
    (jsonValue \ "reputation") match { 
      case JString(value) => reputation = value.toString
      case _ => reputation = "junior"
    }
    (jsonValue \ "total_earnings") match {
      case JInt(value) => total_earnings = value.toInt
      case _ => total_earnings = 0
    }
    
    this
  }
}