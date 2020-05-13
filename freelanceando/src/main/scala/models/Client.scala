package models

import org.json4s.{DefaultFormats, JValue, JInt, JNothing}
import org.json4s.JsonAST.JString

object Client extends ModelCompanion[Client] {

  implicit lazy val formats = DefaultFormats

  def apply: Client = new Client

}

class Client extends Model[Client] {

  protected[Client] var username: String = "" 
  protected[Client] var country_code: String = ""
  protected[Client] var total_spend: Int = 0 
  protected[Client] var job_ids: List[Int] = List()

  def getUserName: String = username
  def increment(amount: Int): Unit = total_spend += amount

  override def toMap: Map[String, Any] = 
    super.toMap + (
      "username" -> username, 
      "country_code" -> country_code, 
      "total_spend" -> total_spend,
      "job_ids" -> job_ids
    )
      
  override def fromJson(jsonValue: JValue): Client = {
    super.fromJson(jsonValue)
    username = (jsonValue \ "username").extract[String]
    country_code = (jsonValue \ "country_code").extract[String]  
    (jsonValue \ "total_spend") match { 
      case value => total_spend = value.extract[Int] 
      case JNothing => total_spend = 0
    }
    (jsonValue \ "job_ids") match {  
      case value => job_ids = value.extract[List[Int]] 
      case JNothing => job_ids = List()
    }
    this 
  }
   
}