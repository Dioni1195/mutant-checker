# Mutant checker

###### _Por Dionisio Arango Rojas_


Bajo el pedido de el lider mutante rebelde Magneto, se desarrolló este software que tiene como finalidad validar matrices de ADN para identificar mutantes, almacenar dichos datos en una base de datos y poder consultar estadísticas.

La aplicación se encuentra alojada en el servicio gratuito de Heroku, siendo desplegada automáticamente desde la rama _main_ ,así mismo es ejecutada su validación de código con SonarCloud en la misma rama; dicha app utiliza como base de datos mongodb, con su servicio en la nube Mongo Atlas.

Esta fue construida con Java Spring.

## Features
- Validar cadenas de ADN e identificar mutantes ("/mutant/")
- Obtener estadísticas sobre mediciones anteriores ("/mutant/stats")

## Modo de uso
La url base de la aplicación es
```sh
https://mutant-checker-meli.herokuapp.com/
```

### Validación de cadenas de ADN
Para realizar la validación de un ADN se creó un servicio con las siguientes especificaciones
1. **Verbo**: _POST_
2. **URL**:  https://mutant-checker-meli.herokuapp.com/mutant/
3. **Cuerpo**: 

| Atributo | Tipo |
 | ------- | ----- |
| dna | String[] | 
-  Cada String solo puede contener (ACTG)
- El número de filas debe ser igual al número de carácteres por cada String (Matriz cuadrada NxN)
- El número minímo de filas debe ser 4

4. **Respuesta**:

| Respuesta | Código |
 | ------- | ----- |
| true |  200 |
| false | 403 |

_en caso de error_

| Atributo | Tipo |
 | ------- | ----- |
| idError | Integer |
| descError | String |
| tipoError | String |

5. **Ejemplo**:
 ```
 curl --location --request POST 'https://mutant-checker-meli.herokuapp.com/mutant/' \
--header 'Content-Type: application/json' \
--data-raw '{
    "dna": [
        "AAAA",
        "CACA",
        "TTAA",
        "AGGA"
    ]
}'
 ```

NOTA: Este servicio luego de cada validación independientemente si es mutante o no, guarda en la base de datos el ADN validado y es importante aclarar que solo se mantiene un registyro por ADN, es decir, si se consulta un ADN ya validado se responde normal al usuario pero no se guarda ni se sobreescribe el registro existente en la base de datos.

### Consulta de estadísticas
Para realizar la consulta de estadísticas sobres los ADNs validados, se creó un servicio con las siguientes especificaciones
1. **Verbo**: _GET_
2. **URL**:  https://mutant-checker-meli.herokuapp.com/mutant/stats
3. **Cuerpo**: N/A

4. **Respuesta**:

| Atributo | Tipo |
 | ------- | ----- |
| count_mutant_dna | Integer |
| count_human_dna | Integer |
| ratio | Double |

| Respuesta | Código |
 | ------- | ----- |
| exitoso | 200 |
| error | _puede variar_ |

_en caso de error_

| Atributo | Tipo |
 | ------- | ----- |
| idError | Integer |
| descError | String |
| tipoError | String |

5. **Ejemplo**:
 ```
 curl --location --request GET 'https://mutant-checker-meli.herokuapp.com/mutant/stats'
 ```
 