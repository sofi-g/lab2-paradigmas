# Grupo 15		
## Corrección		
	Tag o commit corregido:	último commit
		
### Entrega y git		100,00%
	Informe	100,00%
	Commits de cada integrante	100,00%
	En tiempo y con tag correcto	100,00%
	Commits frecuentes y con nombres significativos	100,00%
### Funcionalidad		97,00%
	Parte1: pasa los test de respuesta con código 200 de freelancer	100,00%
	Parte2: pasa los tests de respuesta con código 200 de cliente	100,00%
	Parte2: pasa los tests de respuesta con código 200 de trabajo	80,00%
	Parte 3: pasa los test de filtrado de freelancer	100,00%
	Parte 3: pasa los test de filtrado de trabajo	100,00%
	Parte 4: pasa los test de pago	100,00%
	Parte 4: guardan el cliente y el freelancer después de modificar sus atributos	100,00%
	Manejan correctamente los casos donde el input tiene errores de campos o tipos	100,00%
### Modularización y diseño		92,50%
	Cada modelo está definido en un archivo separado	100,00%
	Usan conceptos de la programación funcional, como las funciones map y filter	100,00%
	Evitan repetir código aprovechando la herencia a partir de Model. 	100,00%
	Mantienen la separación de responsabilidades: el manejo de la API en el Servlet, el manejo de la tabla y el conjunto de instancias en DatabaseTable, y el manejo de atributos y métodos particulares en cada subclase de Model.	100,00%
	Usan DatabaseTable.get y Option para comprobar si los objetos adecuados existen antes de crear nuevos (ej. que las categorías existan antes de crear el freelancer)	50,00%
	El código de filtrado no está duplicado en cada una de las subclases de Model	100,00%
	La modificación de las instancias cliente y freelancer ocurre dentro de la clase correpondiente, y no en el endpoint	100,00%
### Calidad de código		100,00%
	Estilo de línea	100,00%
	Estructuras de código simples	100,00%
	Hacen uso de los métodos de las estructuras de datos estándares como Map o List, en lugar de intentar re-implementarlos	100,00%
	Reutilizan funciones de librería, por ejemplo para serializar y deserializar Json	100,00%
	Estilo de código	100,00%
### Opcionales		
	Punto estrella 1: User y serialización de Reputation	0,00%
	Punto estrella 2: Filtros ordenados	0,00%
	Punto estrella 3: Mantener referencias a objetos en lugar de ids	0,00%
	Puntos estrella 4: Contratos	0,00%
		
# Nota Final		9,49375
		
		
# Comentarios		
		
No agregan los nuevos trabajos al cliente para mostrarlos en el atributo job_ids		
		
Yo sé que ustedes trabajan en grupo, y por eso los commits por ahí son muy grandes. Sin embargo, les recomiendo que hagan paquetitos de trabajo más pequeños. Por ejemplo, si terminan una endpoint GET, aunque vayan a seguir trabajando en ese mismo momento, es una buena práctica hacer un commit con el código que anda.		
		
Definieron una nueva función exists en lugar de utilizar directamente el tipo Option que devuelve get, lo cual hubiera sido más adecuado según el paradigma funcional.		
