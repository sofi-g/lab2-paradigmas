package models

import org.json4s.JValue
import org.json4s.JInt
//import org.json4s.{DefaultFormats, JValue, JInt}
import org.json4s.DefaultFormats
import org.json4s.JsonAST.JString

object Freelancers extends ModelCompanion[Freelancers] {

  protected def dbTable: DatabaseTable[Freelancers] = Database.freelancer

  implicit lazy val formats = DefaultFormats

  def apply: Freelancers = new Freelancers
}

class Freelancers extends Model[Freelancers] {

  protected def dbTable: DatabaseTable[Freelancers] = Freelancers.dbTable

  protected[Freelancers] var username: String = "" 
  protected[Freelancers] var country_code: String = ""
  protected[Freelancers] var category_ids: List[Int] = List() 
  protected[Freelancers] var reputation: String = ""
  protected[Freelancers] var hourly_price: Int = 0
  // si ponemos que la categoria de freelancer sea un id de categoria? o como hacemos? 
  // las categorías en las que trabajan, 

  //private val category_ids = Category.getId("name", username) es una idea 
  
  def getUserName: String = username

  override def toMap: Map[String, Any] = 
    super.toMap + (
      "username" -> username, 
      "country_code" -> country_code, 
      "category_ids" -> category_ids, 
      "reputation" -> reputation,
      "hourly_price" -> hourly_price)
      
  override def fromJson(jsonValue: JValue): Freelancers = {
    super.fromJson(jsonValue)
    (jsonValue \ "username") match {
      case JString(value) => username = value.toString
      case _ =>  // Do nothing
    }
    (jsonValue \ "country_code") match {
      case JString(value) => country_code = value.toString
      case _ =>  // Do nothing
    }
    (jsonValue \ "reputation") match { 
      case JString(value) => reputation = value.toString
      case _ =>  // Do nothing
    }
    (jsonValue \ "hourly_price") match {
      case JInt(value) => hourly_price = value.toInt
      case _ =>  // Do nothing
    }
    this  // Return a reference to this object.
  }
}