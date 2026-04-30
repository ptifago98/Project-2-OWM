# Project-2-OWM
Projet de composant logiciel de OWM RMI

## Paramètres du SDK
java version 25


## Description

Cette application Java permet de :

1. Saisir une **latitude** et une **longitude**, appeler une **API météo**, désérialiser les données JSON, récupérer les informations d'un **pays** via un service web et pour finir insérer ces données dans la base.
2. Afficher la liste des stations dans la base de données.
3. Afficher une station enregistrée et ses données météo à l'aide de son identifiant.
4. Raffraichir toutes les données météo des stations enregistrées.
5. Quitter l'application

Le projet utilise **Maven** pour la gestion des dépendances.

La structure de données correspondant au projet se trouve dans le fichier **main/resources/OWM.sql**.

---


# Structure du projet

## RMI_SERVER 
```
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
```

## Client 
```
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

**Il faut également exécuter le script SQL dans le fichier OWM.sql**
---

# Exécution
1. Ouvrir séparément les dossiers RMI_SERVER et Client dans IntellIJ
2. Lancer le serveur RMI depuis nimporte quelle classe du projet **RMI_SERVER**.
3. Lancer le client depuis nimporte quelle classe du projet **Client**.

Un menu s'ouvre laissant le choix à l'utilisateur :
```
1. Afficher une donnée méteo à partir des coordonnées et enregister la station
2. Voir la liste des stations météos
3. Afficher toutes les données d'une station météo
4. Rafraichir toutes les données des stations
5. Quitter l'application
```
**Choix numéro 1 :**
L'application demande les coordonnées
```
Entrer une latitude :
Entrer une longitude :
```

Exemple :

```
Entrer une latitude : 46,99
Entrer une longitude : 6,93
```
**Choix numéro 2 :**
L'application affiche l'identifiant ainsi que le nom des stations actuellement dans la base de données.

**Choix numéro 3 :**
L'application demande à l'utilisateur l'identifiant de la station qu'il désire.

```
Veuillez entrer l'identifiant de la station (Entrez 'exit' pour revenir au menu)
```
L'application affiche ensuite toutes les informations de la station ainsi que les données météos qui y sont attachées.

**Choix numéro 4 :**
L'application raffraichit toutes les données météo des stations enregistrées.

**Choix numéro 5 :**
Quitter l'application... Si vous en êtes capable....

---

# Auteurs

Projet réalisé par Nathan Favre
