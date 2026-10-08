# Task Manager Backend (Spring Boot + TDD)

Ein einfaches Task-Management-Backend, entwickelt mit Spring Boot, Spring Data JPA und MySQL. Verwaltet Projekte und die dazugehörigen Tasks, inklusive Registrierung, Login und Zugriffsschutz.

## Verwendete Konzepte

- Test-Driven Development (Repository-Ebene, mit @DataJpaTest)
- Spring Boot (REST-Controller, Dependency Injection)
- Spring Data JPA (automatische Datenbank-Anbindung über Repositories)
- Spring Security (Basic Auth, BCrypt-Passwort-Hashing)
- Datenbank-Beziehungen (@ManyToOne von Task zu Projekt und zu Nutzer)
- MySQL als relationale Datenbank
- Validierung und zentrale Fehlerbehandlung (@ExceptionHandler)
- Vollständige REST-Prinzipien (GET, POST, PUT, DELETE)

## Endpunkte

### Authentifizierung
- `POST /auth/registrieren` – Neuen Nutzer registrieren (ohne Login erreichbar)

### Projekt
- `GET /projekt` – Alle Projekte anzeigen
- `POST /projekt` – Neues Projekt erstellen

### Task
- `GET /task` – Alle Tasks anzeigen
- `POST /task` – Neue Task erstellen (mit Verknüpfung zu einem bestehenden Projekt, wird dem eingeloggten Nutzer zugewiesen)
- `PUT /task/{id}/status` – Status einer Task ändern
- `DELETE /task/{id}` – Task löschen (nur durch den Bearbeiter)

## Setup

1. MySQL installieren und eine Datenbank namens `taskmanager_db` erstellen
2. `application.properties.example` zu `application.properties` kopieren
3. Eigenes MySQL-Passwort in `application.properties` eintragen
4. Projekt über `TaskmanagerSpringApplication` starten

## Authentifizierung und Berechtigungen

- Alle Endpunkte außer `POST /auth/registrieren` sind per Basic Auth geschützt
- Passwörter werden mit BCrypt gehasht gespeichert und nie in Antworten zurückgegeben
- Alle eingeloggten Nutzer sehen alle Projekte und Tasks
- Eine neue Task wird automatisch dem eingeloggten Nutzer zugewiesen
- Den Status einer Task darf jeder eingeloggte Nutzer ändern
- Löschen darf nur der Bearbeiter der Task, bei fremden Tasks antwortet die API mit 403 Forbidden
- Tasks ohne Bearbeiter (zum Beispiel vor dem Einbau der Nutzer angelegte) können nicht gelöscht werden

## Testen

Die Repository-Schicht ist testgetrieben entwickelt (siehe `src/test`). Die REST-Endpunkte lassen sich mit Postman testen: zuerst über `POST /auth/registrieren` einen Nutzer anlegen (Body: `{"username": "...", "password": "..."}`), danach bei allen weiteren Anfragen unter **Authorization → Basic Auth** die Zugangsdaten eintragen. Ohne Login antwortet die API mit 401.

## Tests
- Repository-Tests mit @DataJpaTest
- Controller-Tests mit @WebMvcTest, MockMvc und Mockito (@MockitoBean):
    - Zugriff ohne Login wird mit 401 abgewiesen
    - Task ohne Titel liefert 400
    - Fremde Task löschen liefert 403