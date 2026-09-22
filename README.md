# Task Manager Backend (Spring Boot + TDD)

Ein einfaches Task-Management-Backend, entwickelt mit Spring Boot, Spring Data JPA und MySQL. Verwaltet Projekte und die dazugehörigen Tasks, inklusive einer Datenbank-Beziehung zwischen beiden.

## Verwendete Konzepte

- Test-Driven Development (Repository-Ebene, mit @DataJpaTest)
- Spring Boot (REST-Controller, Dependency Injection)
- Spring Data JPA (automatische Datenbank-Anbindung über Repositories)
- Datenbank-Beziehungen (@ManyToOne zwischen Task und Projekt)
- MySQL als relationale Datenbank
- Validierung und zentrale Fehlerbehandlung (@ExceptionHandler)
- Vollständige REST-Prinzipien (GET, POST, PUT, DELETE)

## Endpunkte

### Projekt
- `GET /projekt` – Alle Projekte anzeigen
- `POST /projekt` – Neues Projekt erstellen

### Task
- `GET /task` – Alle Tasks anzeigen
- `POST /task` – Neue Task erstellen (mit Verknüpfung zu einem bestehenden Projekt)
- `PUT /task/{id}/status` – Status einer Task ändern
- `DELETE /task/{id}` – Task löschen

## Setup

1. MySQL installieren und eine Datenbank namens `taskmanager_db` erstellen
2. `application.properties.example` zu `application.properties` kopieren
3. Eigenes MySQL-Passwort in `application.properties` eintragen
4. Projekt über `TaskmanagerSpringApplication` starten

## Testen

Die Repository-Schicht ist testgetrieben entwickelt (siehe `src/test`). Die REST-Endpunkte können zusätzlich mit Postman getestet werden.