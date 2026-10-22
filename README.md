# AgentHire Backend

Backend REST API di **AgentHire AI**, un progetto portfolio pensato per gestire dati dei candidati e autenticazione attraverso un’architettura Spring Boot sicura.

Il progetto dimostra la realizzazione di un backend Java moderno con API REST, persistenza su database, validazione, gestione centralizzata degli errori, hashing delle password e autenticazione tramite JWT.

## Tecnologie utilizzate

- Java 21
- Spring Boot 4
- Spring Web
- Spring Data JPA
- Spring Security
- Hibernate
- MySQL
- BCrypt
- JWT (JSON Web Token)
- Maven
- Git / GitHub

## Funzionalità

### Gestione candidati

L’applicazione espone operazioni CRUD complete sui candidati:

- Creazione di un candidato
- Visualizzazione di tutti i candidati
- Ricerca di un candidato per ID
- Aggiornamento dei dati
- Eliminazione di un candidato

I dati principali gestiti sono:

- Nome
- Email
- Ruolo desiderato

## Autenticazione

Il backend comprende un flusso completo di autenticazione:

- Registrazione utente
- Login
- Hashing delle password con BCrypt
- Generazione JWT
- Validazione JWT
- Endpoint protetti
- Autenticazione stateless

Le password non vengono memorizzate in chiaro nel database.

## Sicurezza

Spring Security distingue tra endpoint pubblici ed endpoint protetti.

Endpoint pubblici:

```text
POST /api/auth/register
POST /api/auth/login
GET  /api/test
```

Endpoint protetti:

```text
GET    /api/candidates
GET    /api/candidates/{id}
POST   /api/candidates
PUT    /api/candidates/{id}
DELETE /api/candidates/{id}
```

Per accedere agli endpoint protetti è necessario inviare il JWT tramite header HTTP:

```text
Authorization: Bearer <token>
```

Se il token è assente, non valido o scaduto, il backend restituisce una risposta HTTP `401 Unauthorized`.

## Validazione

I dati ricevuti dalle API vengono validati tramite Jakarta Validation.

Alcuni controlli implementati:

- Nome obbligatorio
- Email obbligatoria
- Formato email valido
- Ruolo desiderato obbligatorio
- Credenziali di login obbligatorie
- Lunghezza minima della password

In caso di dati non validi, il backend restituisce `400 Bad Request`.

## Gestione degli errori

Il progetto utilizza un `GlobalExceptionHandler` per gestire gli errori dell’applicazione in modo centralizzato.

Tra i casi gestiti:

- Candidato non trovato
- Credenziali errate
- Errori di validazione
- Accesso non autorizzato

Esempio di risposta:

```json
{
  "status": 401,
  "error": "Unauthorized",
  "message": "Token non valido o scaduto"
}
```

## Architettura del progetto

Il backend segue una struttura a livelli:

```text
Controller
   ↓
Service
   ↓
Repository
   ↓
Database
```

Package principali:

```text
config/
controller/
dto/
entity/
exception/
repository/
service/
```

### Controller

Riceve le richieste HTTP ed espone gli endpoint REST.

### Service

Contiene la logica applicativa e coordina le operazioni tra controller e repository.

### Repository

Gestisce l’accesso al database tramite Spring Data JPA.

### Entity

Rappresenta le tabelle del database.

### DTO

Rappresenta i dati scambiati tramite API e permette di separare i dati esposti all’esterno dalle entity del database.

## Flusso di autenticazione JWT

```text
Login utente
    ↓
Verifica credenziali
    ↓
Controllo password con BCrypt
    ↓
Generazione JWT
    ↓
Token restituito al client
    ↓
Authorization: Bearer <JWT>
    ↓
JwtAuthenticationFilter
    ↓
Validazione del token
    ↓
Accesso all'endpoint protetto
```

## Database

Il progetto utilizza MySQL.

Tabelle principali:

```text
candidates
users
```

La persistenza è gestita tramite Hibernate e Spring Data JPA.

## Variabili d’ambiente

Le informazioni sensibili non vengono salvate direttamente nel codice sorgente.

L’applicazione utilizza le seguenti variabili d’ambiente:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
JWT_SECRET
```

Configurazione utilizzata:

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

jwt.secret=${JWT_SECRET}
```

Le credenziali del database e la secret JWT non devono essere pubblicate su GitHub.

## Avvio del progetto

Clonare il repository:

```bash
git clone https://github.com/fucilidanilo-rgb/agenthire-backend.git
```

Entrare nella cartella:

```bash
cd agenthire-backend
```

Configurare le variabili d’ambiente necessarie:

```text
DB_URL=jdbc:mysql://localhost:3306/agenthire
DB_USERNAME=tuo_username_mysql
DB_PASSWORD=tua_password_mysql
JWT_SECRET=tua_secret_jwt
```

Avviare quindi l’applicazione Spring Boot.

Il backend sarà disponibile su:

```text
http://localhost:8080
```

## Esempio di login

Richiesta:

```http
POST /api/auth/login
Content-Type: application/json
```

Body:

```json
{
  "email": "user@example.com",
  "password": "your-password"
}
```

Risposta:

```json
{
  "id": 1,
  "email": "user@example.com",
  "token": "eyJ..."
}
```

Il token restituito può poi essere utilizzato per accedere agli endpoint protetti.

## Competenze messe in pratica

Durante lo sviluppo di questo progetto ho lavorato su:

- Creazione di API REST con Spring Boot
- Architettura Controller / Service / Repository
- Utilizzo di DTO ed Entity
- Operazioni CRUD con Spring Data JPA
- Collegamento Java e MySQL
- Validazione dei dati
- Gestione centralizzata delle eccezioni
- Hashing delle password con BCrypt
- Configurazione di Spring Security
- Autenticazione JWT
- Protezione degli endpoint
- Codici di stato HTTP
- Variabili d’ambiente
- Gestione sicura dei segreti
- Git e GitHub

## Stato del progetto

Backend core completato.

Funzionalità attualmente disponibili:

- CRUD candidati
- Persistenza MySQL
- DTO
- Validazione
- Global Exception Handler
- Registrazione utente
- Login
- Password BCrypt
- JWT
- Endpoint protetti
- Configurazione tramite variabili d’ambiente

Prossimo step: integrazione completa con il frontend Angular di **AgentHire AI**.

## Autore

**Danilo Fucili**

Junior Full Stack Developer

Java · Spring Boot · Angular · TypeScript · SQL