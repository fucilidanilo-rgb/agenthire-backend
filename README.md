
# 🤖 AgentHire AI — Backend Spring Boot

**AgentHire Backend** è il servizio REST sviluppato in Java e Spring Boot per il progetto Full Stack AgentHire AI.

Il backend gestisce l'autenticazione degli utenti, la sicurezza delle richieste HTTP, i ruoli applicativi e la persistenza dei candidati.

Il progetto è integrato con due frontend Angular e utilizza PostgreSQL su Neon come database cloud.

## 🌐 Applicazione online

**Backend REST API**

https://agenthire-backend-hxpv.onrender.com

**Frontend Angular — Danilo**

https://agenthire-danilo.vercel.app/home

**Frontend Angular — Roberta**

https://agenthire-ai.vercel.app/home

### Account dimostrativo

Email: `demo@agenthire.example`

Password: `AgentHire-Demo-2026!`

L'account DEMO permette di accedere alle funzionalità dimostrative dell'applicazione.

> Il backend utilizza Render Free. Dopo un periodo di inattività, il primo accesso può richiedere più tempo.

---

## 🛠️ Stack tecnologico

| Categoria | Tecnologia |
|---|---|
| Linguaggio | Java 21 |
| Framework | Spring Boot 4.1.1 |
| API | Spring Web, REST |
| Sicurezza | Spring Security |
| Autenticazione | JWT |
| Password | BCrypt |
| Persistenza | Spring Data JPA |
| ORM | Hibernate |
| Database | PostgreSQL |
| Hosting database | Neon |
| Validazione | Jakarta Bean Validation |
| Build | Maven |
| Containerizzazione | Docker |
| Hosting backend | Render |
| Versionamento | Git e GitHub |

---

## ✨ Funzionalità

### 1. Autenticazione utenti

Il backend implementa un sistema di autenticazione con:

- Login tramite email e password
- Verifica delle credenziali
- Hashing delle password con BCrypt
- Generazione di token JWT
- Validazione dei token JWT
- Autenticazione stateless
- Protezione degli endpoint REST

Le password non vengono memorizzate in chiaro nel database.

### 2. Ruoli applicativi

Sono previsti due ruoli:

**USER**

Account personale con accesso alle funzionalità autorizzate.

**DEMO**

Account dimostrativo con permessi limitati.

La gestione dei permessi viene effettuata lato backend tramite Spring Security.

### 3. Gestione candidati

Il backend espone API REST per la gestione dei candidati.

Le operazioni CRUD comprendono:

- Creazione di un candidato
- Recupero dell'elenco dei candidati
- Ricerca di un candidato tramite ID
- Aggiornamento dei dati
- Eliminazione di un candidato

I dati principali comprendono:

- Nome
- Email
- Ruolo professionale desiderato

Le operazioni sono sottoposte ai controlli di autenticazione e autorizzazione.

### 4. Separazione dei dati

La gestione dei candidati è collegata all'identità dell'utente autenticato.

Gli utenti accedono ai candidati secondo i controlli di proprietà e autorizzazione implementati nel backend.

Il ruolo DEMO dispone di permessi limitati sulle operazioni di modifica.

### 5. Validazione

Il backend utilizza Jakarta Bean Validation per controllare i dati ricevuti.

Sono previsti controlli su:

- Campi obbligatori
- Formato degli indirizzi email
- Dati dei candidati
- Credenziali di accesso
- Requisiti delle password

Le richieste non valide vengono gestite con risposte HTTP appropriate.

### 6. Gestione centralizzata degli errori

Il progetto include un GlobalExceptionHandler per gestire le eccezioni applicative.

Tra i casi gestiti:

- Dati non validi
- Credenziali errate
- Candidato non trovato
- Richieste non autorizzate
- Operazioni non consentite

---

## 🏗️ Architettura

Il progetto utilizza un'architettura a livelli.

```text
           FRONTEND ANGULAR
                 |
                 | HTTPS / REST
                 | JWT Bearer
                 v
           SPRING BOOT
               Render
                 |
                 v
             Security
           JWT / BCrypt
                 |
                 v
             Controller
                 |
                 v
               DTO
                 |
                 v
              Service
                 |
                 v
             Repository
                 |
                 v
          JPA / Hibernate
                 |
                 v
            PostgreSQL
                Neon
```

### Controller

Espone gli endpoint HTTP e gestisce le richieste dei client.

### Service

Contiene la logica applicativa relativa ad autenticazione e gestione dei candidati.

### Repository

Gestisce l'accesso al database attraverso Spring Data JPA.

### Entity

Rappresenta le entità persistenti del sistema.

### DTO

Definisce i dati scambiati attraverso le API e separa le informazioni esposte dalle entità persistenti.

### Config e Security

Comprende le configurazioni di Spring Security, autenticazione JWT e autorizzazioni.

---

## 🔐 Sicurezza e JWT

Il backend utilizza Spring Security con autenticazione stateless.

### Flusso di autenticazione

```text
Utente
   |
   v
POST /api/auth/login
   |
   v
Verifica credenziali
   |
   v
BCrypt Password Check
   |
   v
Generazione JWT
   |
   v
Token restituito al frontend
   |
   v
Richiesta con Bearer Token
   |
   v
JwtAuthenticationFilter
   |
   v
Verifica token e autorizzazioni
   |
   v
Accesso alla risorsa protetta
```

### Esempio header HTTP

```http
Authorization: Bearer <JWT>
```

Il backend verifica la validità del token prima di autorizzare l'accesso agli endpoint protetti.

### Registrazione pubblica

La registrazione pubblica è disabilitata tramite la configurazione Spring Security.

Gli account autorizzati vengono predisposti separatamente.

---

## 🌐 API REST

### Autenticazione

| Metodo | Endpoint | Accesso |
|---|---|---|
| POST | `/api/auth/login` | Pubblico |
| POST | `/api/auth/register` | Bloccato nella configurazione pubblica |

### Gestione candidati

| Metodo | Endpoint | Accesso |
|---|---|---|
| GET | `/api/candidates` | USER / DEMO |
| GET | `/api/candidates/{id}` | USER / DEMO |
| POST | `/api/candidates` | USER |
| PUT | `/api/candidates/{id}` | USER |
| DELETE | `/api/candidates/{id}` | USER |

Il backend applica inoltre controlli di proprietà dei dati.

### Endpoint di test

```http
GET /api/test
```

### Esempio di login

Richiesta:

```http
POST /api/auth/login
Content-Type: application/json
```

Body di esempio:

```json
{
  "email": "demo@agenthire.example",
  "password": "AgentHire-Demo-2026!"
}
```

La risposta contiene:

- ID utente
- Email
- Token JWT
- Ruolo applicativo

Esempio semplificato:

```json
{
  "id": 1,
  "email": "demo@agenthire.example",
  "token": "<JWT>",
  "role": "DEMO"
}
```

---

## 🗄️ Database PostgreSQL — Neon

Il database cloud è ospitato su Neon.

La persistenza utilizza:

- PostgreSQL
- Spring Data JPA
- Hibernate
- Driver JDBC PostgreSQL

Le principali entità persistenti comprendono gli utenti e i candidati.

### Configurazione Hibernate

```properties
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.open-in-view=false
```

La modalità `validate` verifica la compatibilità dello schema del database con le entità JPA.

### Migrazione database

Il progetto utilizzava inizialmente MySQL durante lo sviluppo.

La configurazione cloud attuale è stata migrata a PostgreSQL su Neon.

---

## ⚙️ Variabili d'ambiente

L'applicazione utilizza le seguenti variabili:

| Variabile | Descrizione |
|---|---|
| `DB_URL` | URL JDBC PostgreSQL |
| `DB_USERNAME` | Username database |
| `DB_PASSWORD` | Password database |
| `JWT_SECRET` | Segreto per la firma JWT |

Configurazione Spring Boot:

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

jwt.secret=${JWT_SECRET}
```

**Importante:** le credenziali del database e il segreto JWT devono rimanere privati e non devono essere pubblicati su GitHub.

---

## 💻 Installazione locale

### Requisiti

- Java 21
- IntelliJ IDEA o editor equivalente
- Maven Wrapper
- PostgreSQL configurato
- Variabili d'ambiente necessarie

### 1. Clonare il repository

```bash
git clone https://github.com/fucilidanilo-rgb/agenthire-backend.git
```

### 2. Entrare nella cartella

```bash
cd agenthire-backend
```

### 3. Configurare le variabili d'ambiente

Impostare:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
JWT_SECRET
```

Il database deve contenere lo schema previsto dalle entità JPA.

### 4. Avviare Spring Boot

Su Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Su macOS/Linux:

```bash
./mvnw spring-boot:run
```

Il backend locale utilizza normalmente:

http://localhost:8080

---

## 🐳 Docker e deployment

Il progetto include un Dockerfile per distribuire il backend come container.

L'infrastruttura pubblicata comprende:

| Componente | Servizio |
|---|---|
| Frontend Angular Danilo | Vercel |
| Frontend Angular Roberta | Vercel |
| Backend Java Spring Boot | Render |
| Database PostgreSQL | Neon |
| Codice sorgente | GitHub |

Entrambi i frontend utilizzano lo stesso backend REST e lo stesso database cloud.

---

## 🔗 Repository collegati

### Backend Spring Boot

https://github.com/fucilidanilo-rgb/agenthire-backend

### Frontend Angular — Danilo

https://github.com/fucilidanilo-rgb/agenthire-ai

### Frontend Angular — Roberta

https://github.com/robertabianca92/agenthire-ai

---

## 🎯 Competenze dimostrate

- Java 21
- Programmazione orientata agli oggetti
- Spring Boot
- API REST
- Spring Security
- Autenticazione JWT
- BCrypt
- DTO e validazione
- Controller / Service / Repository
- Spring Data JPA
- Hibernate
- PostgreSQL
- Gestione dei permessi
- Gestione delle eccezioni
- Docker
- Render
- Neon
- Git e GitHub

---

## 👥 Collaborazione

Il progetto AgentHire AI integra due frontend Angular sviluppati separatamente e un backend Spring Boot condiviso.

**Danilo Fucile**

Backend Development e Full Stack Integration

GitHub: https://github.com/fucilidanilo-rgb

**Roberta Bianca**

Collaborazione al progetto AgentHire AI e sviluppo frontend.

GitHub: https://github.com/robertabianca92

---

## 👨‍💻 Autore

**Danilo Fucili**

Junior Full Stack Developer

Java · Spring Boot · Angular · TypeScript · PostgreSQL

GitHub: https://github.com/fucilidanilo-rgb
