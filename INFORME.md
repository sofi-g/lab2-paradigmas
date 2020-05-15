# Informe Lab2: Programación Orientada a Objetos
  
## Introducción
Para este laboratorio implementamos una API RESTful para un directorio de freelancers donde el objetivo es poner en práctica el paradigma de programación basado en el concepto de `objetos` y seguir desarrollando la programación de alto orden del paradigma funcional. 

* * * 

Al comenzar teníamos el archivo Model.scala donde se implementaban los modelos que nos ayudarían a representar las diferentes entidades de la aplicación: Category, Freelancers, Clients y Jobs.

### Clases

Para cada una de las entidades definimos su propia clase extendiéndola de la clase Model implementada en el archivo Models.scala. 
En cada clase utilizamos los siguientes métodos:
- Método *`toMap`* (que devuelve un diccionario donde las claves son los nombres de los atributos del objeto y los valores son sus de los atributos) 
- Método *`fromJson`* llena el objeto con los atributos correspondientes de cada entidad en formato JSON. 

Estos fueron **dos casos en los que utilizamos herencia para evitar duplicar código**

Para ello nos ayudamos de la función *`super`* para llamar a los métodos de la superclase y así no perder la implementación anterior del `trait Model` de estos metodos.

```
override def toMap: Map[String, Any] = 
	super.toMap +
		("name" -> name)
		.
		.
		.
```
```
override  def  fromJson(jsonValue: JValue):  Category  = {
	super.fromJson(jsonValue)
	name = (jsonValue \ "name").extract[String]
	.
	.
	.
	this
}
```
#### Clase Category 

**atributos**

* *name*: indica el nombre de la categoría 

#### Clase Freelancer

**atributos**

* *username*: nombre de usuario del freelancer
* *country_code*: país del usuario
* *category_ids*: identificador de la categoría en la que trabaja el freelancer
* *reputation*: nivel de experiencia del freelancer 
* *hourly_price*: muestra lo que cobra por hora
* *total_earnings*: indica cuánto gano en la plataforma


#### Clase Client

**atributos**

* *username*: nombre de usuario del cliente
* *country_code*: país del cliente
* *job_ids*: identificador de los distintos trabajos 
* *total_spend*: muestra cuánto gastaron en la plataforma 

#### Clase Job

**atributos**

* *title*: título del trabajo
* *category_id*: identificador de la categoría a la que pertenece el trabajo
* *client_id*: muestra el id del cliente 
* *preferred_expertise*: preferencia del nivel de experiencia del freelancer
* *preferred_country*: preferencia del país del que debe ser el freelancer
* *hourly_price*: monto que esta dispuesto a pagar el cliente por el trabajo

***
### Endpoints 

En el archivo FreelanceandoServlet.scala definimos los endpoints GET y POST para las entidades de nuestra aplicación. Antes de comenzar con esto, implementamos los siguientes métodos en DatabaseTable.scala:

- *def all*: Devuelve una copia de la lista de instancias.

- *def get(id: Int)*: Devuelve una instancia si hay una en _instances con id

- *def filter(attributes: Map[String, Any])*: Devuelve una lista de instancias que coinciden con los pares: attributeName, attributeValue.
 
**Gets**

`get("/api/categories")`: Obtenemos la lista de instancias de las categorías del servidor.

`get("/api/freelancers")`: 
*(country_code="", category_id="", reputation="")*
Este método obtiene una lista de los freelancers dependiendo de los parámetros de filtro adicionales. Si no se da ningún argumento por parámetro, se van a mostrar todos los freelancers cargados en la base de datos. En caso de que haya más de un filtro, se devuelven las instancias que cumplan con todos. 

`get("/api/clients")`: Obtenemos la lista de clientes del servidor.

`get("/api/freelancers/:id")`: Obtenemos la información detallada del freelancer con el id dado por parámetro.  

`get("/api/clients/:id")`: Obtenemos la información detallada del cliente con el id dado por parámetro.  

`get("/api/jobs")`:
*{preferred_country="", category_id="", preferred_reputation="", hourly_price=""}*
Este método obtiene una lista de los jobs dependiendo de los parámetros de filtro adicionales. Si no se da ningún argumento por parámetro, se van a mostrar todos los que están cargados en la base de datos. En caso de que haya más de un filtro, se devuelven las instancias que cumplan con todos. 

**Posts**

`post("/api/freelancers")`: Creamos un nuevo freelancer con los argumentos pasados por parámetro. Para esto, primero verificamos que los parámetros sean válidos, como por ejemplo que pertenezca a una categoría que exista o que tenga un nombre de usuario válido, etc. Luego, los guardamos en la base de datos del servidor, de lo contrario devolvemos un mensaje de error.

`post("/api/clients")`: Creamos un nuevo cliente con los argumentos pasados por parámetro. Para esto, primero verificamos que los parámetros sean válidos, como por ejemplo que pertenezca a una categoría que exista o que tenga un nombre de usuario válido, etc. Luego, los guardamos en la base de datos del servidor, de lo contrario devolvemos un mensaje de error.

`post("/api/jobs")`: Creamos un nuevo trabajo con los argumentos pasados por parámetro. Para esto, primero verificamos que los parámetros sean válidos, luego comprobamos que el cliente que creó el trabajo y que la categoría a la que pertence efectivamente existan, en ese caso guardamos el trabajo en la base de datos del servidor, de lo contrario devolvemos un mensaje de error.

`post("/api/jobs/pay")`: Para efectuar los pagos de los trabajos primero verificamos que los argumentos pasados por parámetros sean válidos. Luego buscamos en la base de datos la instancia del trabajo y al freelancer que lo realizó, si no existe alguno de los dos, devolvemos un mensaje de error, de lo contrario, buscamos al cliente que creó el trabajo y efectuamos el pago aumentando el valor del atributo que le corresponde a cada entidad y guardamos en la base de datos la información actualizada.

* * *

### DECISIONES DE DISEÑO 

Para los endpoints tuvimos que tomar algunas decisiones de diseño en particular:

- Para evitar que en los endpoints saltara un error al parsear los argumentos que faltaban en el método post, decidimos hacer pattern matching en algunos de los atributos de `client` y `freelancer`.

Por ejemplo:
```
    (jsonValue \ "reputation") match { 
      case JString(value) => reputation = value.toString
      case _ => reputation = "junior"
    }
```


- Para definir los endpoints sólo tomamos como válidos los parámetros especificados en la consigna. Cualquier otro parámetro ingresado dará ERROR (salvo para el caso del atributo _reputation_ que en el enunciado aclaraba que podía no estar presente).

- En en el archivo DatabaseTable.scala definimos el metodo `exists` para tratar algunos casos que podrían llevar a un error en los endpoints. Por ejemplo: **que las categorías asignadas a un freelancer efectivamente existan**, que el id del freelancer que tenemos como argumento NO exista, etc.

```
def exists(attr: String, value: Any): Boolean = 
    all.exists(m => m.toMap(attr) == value)
```
**nota:** decidimos aprovechar este método utilizándolo en los endpoints directamente, en lugar de cambiar alguna clase para controlar que las categorías asignadas a un freelancer existan.

- También definimos el metodo `checkParameters` que toma una lista de parametros válidos y un json, y verifica que el json contenga esos parámetros.

```
  private def checkParameters(
    validParameters:List[String],jsonValue: JValue):Boolean = { 
      val inputData= jsonValue.extract[Map[String,Any]]
      val result = inputData == 
        inputData.filterKeys(validParameters.contains(_))
      result
  } 
```  
**nota:** cuando ingresamos un int en un campo como string el método extract hace el parseo a string por eso no da error de tipo y lo toma como válido.

- **El código de la lógica de la acción de pagar** la colocamos en el `post("/api/jobs/pay")` y para efectuar los pagos en `post("/api/jobs/pay")`. Decidimos implementar en las clases client y freelancer un metodo denominado `increment(amount: Int)` que incrementa los atributos `total_earnings` y `total_spend` de cada entidad.

```
  def increment(amount: Int): Freelancer = {
     total_earnings += amount
     this
  }
```
```
  def increment(amount: Int): Client = {
    total_spend += amount
    this
  }  
```

Gracias a la sobrecarga, concepto de la programación orientada a objetos, **es lo que permite que un mismo endpoint como `api/freelancers` tome distintos parámetros**.


**Si decidiéramos llamar una API como MercadoPago el código para realizar las transacciones** debería estar en el request del post("/api/jobs/pay"), suponemos que desde el server se le pide autorización a mercado pago y se hace la transferencia.
