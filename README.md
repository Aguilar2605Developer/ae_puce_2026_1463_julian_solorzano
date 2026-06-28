
## Microservicio `events` — Plataforma de reserva de entradas con autenticación AWS Cognito

---
El sistema permite que **cualquiera vea la cartelera de eventos** (endpoint público), pero **solo un usuario autenticado puede crear eventos, reservar o cancelar entradas** (endpoints privados). No se usan roles ni permisos: quien tenga un token válido de Cognito entra; quien no, recibe `401`.

---

## Arquitectura por capas

Repliqué exactamente la misma separación por capas del proyecto de referencia `students`:

```
src/main/kotlin/com/pucetec/events/
├── EventsApplication.kt
├── config/
│   └── SecurityConfig.kt              # Seguridad con Cognito — nuevo respecto a students
├── controllers/
│   ├── EventController.kt
│   ├── AttendeeController.kt
│   └── ReservationController.kt
├── dto/
│   ├── EventDto.kt                    # EventRequest / EventResponse
│   ├── AttendeeDto.kt
│   └── ReservationDto.kt
├── entities/
│   ├── Event.kt
│   ├── Attendee.kt
│   └── Reservation.kt                 # @ManyToOne a Event y Attendee
├── exceptions/
│   ├── BlankFieldException.kt
│   ├── InvalidCapacityException.kt
│   ├── AttendeeNotFoundException.kt
│   ├── EventNotFoundException.kt
│   ├── ReservationNotFoundException.kt
│   ├── SoldOutException.kt
│   ├── ReservationLimitExceededException.kt
│   ├── ReservationAlreadyCancelledException.kt
│   └── GlobalExceptionHandler.kt
├── mappers/
│   ├── EventMapper.kt
│   ├── AttendeeMapper.kt
│   └── ReservationMapper.kt
├── repositories/
│   ├── EventRepository.kt
│   ├── AttendeeRepository.kt
│   └── ReservationRepository.kt
└── services/
    ├── EventService.kt
    ├── AttendeeService.kt
    └── ReservationService.kt
```

**Reglas que respeté:**
- Los **controllers** no tienen lógica: solo reciben el request, llaman al service y devuelven el response.
- Los **services** tienen toda la lógica de negocio y lanzan las excepciones de dominio.
- Los **mappers** son extension functions (`fun Event.toResponse() = ...`), sin lógica.
- Las **entities** son JPA y nunca se exponen directamente: siempre se devuelve un `...Response`.
- Las **excepciones** se traducen a HTTP en `GlobalExceptionHandler` con `ExceptionResponse(message, source)`.

---

## Tablas (Entities)

Modelé 3 tablas con JPA:

| Tabla | Entidad | Campos |
|-------|---------|--------|
| `attendees` | `Attendee` | `id`, `name`, `email` |
| `events` | `Event` | `id`, `name`, `venue`, `totalTickets`, `availableTickets` |
| `reservations` | `Reservation` | `id`, `attendee` *(ManyToOne)*, `event` *(ManyToOne)*, `status`, `createdAt` |

`Reservation` relaciona a `Attendee` con `Event`, igual que `Enrollment` relaciona `Student` con `Subject` en el proyecto de referencia. `status` solo puede ser `ACTIVE` o `CANCELLED`.

---

**Datos del User Pool de prueba:**

| Dato | Valor |
|------|-------|
| Región | `us-east-1` |
| User Pool ID | `us-east-1_yzwNALI2A` |
| `issuer-uri` | `https://cognito-idp.us-east-1.amazonaws.com/us-east-1_yzwNALI2A` |
| Dominio Hosted UI | `https://us-east-1yzwnali2a.auth.us-east-1.amazoncognito.com` |
| App client ID | `3gv2oqe4niko3s47srn1kitsk6` |

** `GET /api/events/**` es público; todo lo demás requiere token.



1. Abrí la Hosted UI en el navegador y me autorregistré:
   ```
   https://us-east-1yzwnali2a.auth.us-east-1.amazoncognito.com/login?client_id=3gv2oqe4niko3s47srn1kitsk6&response_type=code&scope=email+openid+phone&redirect_uri=https%3A%2F%2Fd84l1y8p4kdic.cloudfront.net
   ```
2. Tras el login, copié el `code` de la URL de redirección.
3. Lo canjeé por los tokens con curl:
   ```bash
   curl --location 'https://us-east-1yzwnali2a.auth.us-east-1.amazoncognito.com/oauth2/token' \
     --header 'Content-Type: application/x-www-form-urlencoded' \
     --data-urlencode 'grant_type=authorization_code' \
     --data-urlencode 'client_id=3gv2oqe4niko3s47srn1kitsk6' \
     --data-urlencode 'client_secret=14qdd388f1j6fge52el3l5r2ouvcg5sperlno3701t2jj1chgeiu' \
     --data-urlencode 'code=<CODE>' \
     --data-urlencode 'redirect_uri=https://d84l1y8p4kdic.cloudfront.net'
   ```
4. Usé el `access_token` como `Authorization: Bearer <token>` en Postman.

---

## Endpoints

| Método | Ruta | Acceso | Descripción |
|--------|------|--------|-------------|
| `GET` | `/api/events` |  Público | Lista la cartelera de eventos |
| `GET` | `/api/events/{id}` |  Público | Detalle de un evento |
| `POST` | `/api/events` |  Privado | Crea un evento |
| `POST` | `/api/attendees` |  Privado | Crea un asistente |
| `POST` | `/api/reservations` |  Privado | Reserva una entrada |
| `PUT` | `/api/reservations/{id}/cancel` |  Privado | Cancela una reserva |
| `GET` | `/api/reservations` |  Privado | Lista las reservas |
| `GET` | `/api/attendees` |  Privado | Lista los asistentes |

---

## Tests — 100% de cobertura en `services`

Implementé un archivo de test por service usando Mockito con el patrón **Arrange / Act / Assert**:

- `EventServiceTest.kt` — 7 tests
- `AttendeeServiceTest.kt` — 4 tests
- `ReservationServiceTest.kt` — 9 tests

Por cada excepción de las reglas de negocio existe un test con `assertThrows`, y por cada camino feliz un test con `assertEquals`. La capa `services` alcanzó **100% de líneas y 100% de ramas**.

---

## 📁 Contenido del repositorio

```
├── src/                          # Código fuente completo
├── evidencias/                   # 5 capturas de pantalla requeridas
├── events-api.postman_collection.json   # Colección de Postman
├── README.md                     # Este archivo
└── build.gradle.kts              # Dependencias del proyecto
```
