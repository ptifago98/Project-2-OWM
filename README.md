# Project-2-OWM
Projet de composant logiciel de OWM RMI

## Paramètres du SDK
java version 25


## Description

Cette application Java permet de :

1. Saisir une **latitude** et une **longitude**.
2. Appeler une **API météo** afin de récupérer les informations météorologiques.
3. Désérialiser les données JSON à l’aide de **Jackson**.
4. Récupérer les informations sur le **pays** via un service web.
5. Insérer les données météo, la station météo et le pays dans une **base de données Oracle**.

Le projet utilise **Maven** pour la gestion des dépendances.

La structure de données correspondant au projet se trouve dans le fichier **main/resources/OWM.sql**.

---

# Structure du projet
## RMI_SERVER 

src
├── main
    ├── java
    │   └── ch.hearc.ig.scl
    │       ├── app
    │       │   └── App.java
    │       │
    │       ├── business
    │       │   ├── Meteo.java
    │       │   ├── Pays.java
    │       │   └── StationMeteo.java
    │       │
    |       ├── deserializer
    │       │   └── MeteoDeserializer
    │       │   └── PaysDeserializer
    │       │   └── StationMeteoDeserializer
    │       │
    │       │
    │       ├── persistence
    │       │   └── DBConnection.java
    │       │
    │       ├── repository
    │       │   ├── MeteoRepository.java
    │       │   ├── PaysRepository.java
    │       │   └── StationRepository.java
    │       │
    │       ├── service
    │       │   ├── ApiCallService.java
    │       │   ├── ApiCallPaysService.java
    │       │   ├── IOWMManager.java
    │       │   ├── OWMManager.java
    │       │
    │       └── tools
    │           ├── EnvProperties.java
    │           └── Log.java
    │
    └── resources
        ├── env
        └── OWM.sql

**Client**
src
├── main
    ├── java
    │   └── ch.hearc.ig.scl
    │       ├── app
    │       │   └── App.java
    │       │
    │       ├── business
    │       │   ├── Meteo.java
    │       │   ├── Pays.java
    │       │   └── StationMeteo.java
    │       │
    │       ├── service
    │       │   └── IOWMManager.java
    │       │
    │       └── tools
    │           ├── EnvProperties.java
    │           └── Log.java
    │
    └── resources
        

```

---

# Configuration du projet

Avant d'exécuter l'application, il faut compléter le fichier .env contenant les paramètres suivants :

```
USER=
PASSWORD=
HOST=
PORT=
SID=

APIKEY=
```

### Paramètres de base de données

| Paramètre | Description                             |
| --------- | --------------------------------------- |
| USER      | Nom d'utilisateur de la base de données |
| PASSWORD  | Mot de passe de la base                 |
| HOST      | Adresse du serveur de base de données   |
| PORT      | Port de connexion                       |
| SID       | Identifiant de la base Oracle           |

### Clé API météo

| Paramètre | Description            |
| --------- | ---------------------- |
| APIKEY    | Clé API OpenWeatherMap |

---

# Exécution

Lancer la classe :

```
Main.java
```

L'application demande ensuite :

```
Entrer une latitude :
Entrer une longitude :
```

Exemple :

```
Entrer une latitude : 46,99
Entrer une longitude : 6,93
```

---

# Auteurs

Projet réalisé par Auriane Ndonfack et Nathan Favre
