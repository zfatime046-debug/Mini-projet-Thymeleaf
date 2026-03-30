# 🌿 Gestion de Dons — Spring Boot REST API

Plateforme de gestion des dons pour associations et ONG.

---

## 🏗️ Architecture

```
src/main/java/com/association/gestiondons/
│
├── GestionDonsApplication.java          # Point d'entrée Spring Boot
│
├── entity/                              # Couche domaine (JPA)
│   ├── Campagne.java
│   ├── Donateur.java
│   ├── Don.java
│   ├── StatutCampagne.java              # Enum : ACTIVE | TERMINEE | PAUSE
│   ├── TypeDonateur.java                # Enum : PARTICULIER | ENTREPRISE | ASSOCIATION
│   └── MoyenPaiement.java               # Enum : VIREMENT | CASH | CHEQUE | CARTE | PAYPAL
│
├── repository/                          # Couche persistance (Spring Data JPA)
│   ├── CampagneRepository.java
│   ├── DonateurRepository.java
│   └── DonRepository.java               # + JpaSpecificationExecutor, requêtes JPQL
│
├── service/                             # Interfaces de la couche métier
│   ├── CampagneService.java
│   ├── DonateurService.java
│   └── DonService.java
│
├── service/impl/                        # Implémentations métier
│   ├── CampagneServiceImpl.java
│   ├── DonateurServiceImpl.java
│   └── DonServiceImpl.java
│
├── controller/                          # Couche présentation (REST)
│   ├── CampagneController.java
│   ├── DonateurController.java
│   └── DonController.java
│
├── dto/
│   ├── request/                         # Objets entrants (validation Bean Validation)
│   │   ├── CampagneRequest.java
│   │   ├── DonateurRequest.java
│   │   ├── DonRequest.java
│   │   └── DonFilterRequest.java
│   └── response/                        # Objets sortants
│       ├── CampagneResponse.java
│       ├── DonateurResponse.java
│       ├── DonResponse.java
│       ├── StatistiquesResponse.java
│       └── ApiResponse.java             # Wrapper générique {success, message, data}
│
├── mapper/                              # MapStruct (Entity ↔ DTO)
│   ├── CampagneMapper.java
│   ├── DonateurMapper.java
│   └── DonMapper.java
│
├── exception/                           # Gestion centralisée des erreurs
│   ├── ResourceNotFoundException.java
│   ├── BusinessException.java
│   └── GlobalExceptionHandler.java      # @RestControllerAdvice
│
└── config/
    ├── WebConfig.java                   # CORS
    └── DataInitializer.java             # Données de démo (profil "dev")
```

---

## 🚀 Lancer le projet

```bash
# Compiler et démarrer
mvn spring-boot:run

# Avec données de démo (profil dev)
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

Accès :
- **API** : http://localhost:8080/api
- **H2 Console** : http://localhost:8080/h2-console  
  *(JDBC URL: `jdbc:h2:mem:gestiondonsdb`, user: `sa`, password: vide)*

---

## 📡 Endpoints REST

### Campagnes `/api/campagnes`

| Méthode | URL | Description |
|---------|-----|-------------|
| GET | `/api/campagnes` | Liste toutes les campagnes |
| GET | `/api/campagnes?statut=ACTIVE` | Filtre par statut |
| GET | `/api/campagnes?titre=eau` | Recherche par titre |
| GET | `/api/campagnes/{id}` | Détail d'une campagne |
| POST | `/api/campagnes` | Créer une campagne |
| PUT | `/api/campagnes/{id}` | Modifier une campagne |
| DELETE | `/api/campagnes/{id}` | Supprimer une campagne |

### Donateurs `/api/donateurs`

| Méthode | URL | Description |
|---------|-----|-------------|
| GET | `/api/donateurs` | Liste tous les donateurs |
| GET | `/api/donateurs?type=ENTREPRISE` | Filtre par type |
| GET | `/api/donateurs?nom=ahmed` | Recherche par nom |
| GET | `/api/donateurs/{id}` | Détail d'un donateur |
| POST | `/api/donateurs` | Créer un donateur |
| PUT | `/api/donateurs/{id}` | Modifier un donateur |
| DELETE | `/api/donateurs/{id}` | Supprimer un donateur |

### Dons `/api/dons`

| Méthode | URL | Description |
|---------|-----|-------------|
| GET | `/api/dons` | Liste tous les dons |
| GET | `/api/dons?campagneId=1&moyen=VIREMENT` | Filtres combinés |
| GET | `/api/dons?debut=2025-01-01&fin=2025-12-31` | Filtre par période |
| GET | `/api/dons/{id}` | Détail d'un don |
| GET | `/api/dons/campagne/{id}` | Dons d'une campagne |
| GET | `/api/dons/donateur/{id}` | Dons d'un donateur |
| GET | `/api/dons/statistiques` | Statistiques globales |
| POST | `/api/dons` | Enregistrer un don |
| PUT | `/api/dons/{id}` | Modifier un don |
| DELETE | `/api/dons/{id}` | Supprimer un don |

---

## 📋 Exemples de requêtes

### Créer une campagne
```json
POST /api/campagnes
{
  "titre": "Eau potable pour tous",
  "objectif": 300000.00,
  "debut": "2025-06-01",
  "fin": "2025-12-31",
  "statut": "ACTIVE"
}
```

### Enregistrer un don
```json
POST /api/dons
{
  "montant": 5000.00,
  "dateDon": "2025-05-15",
  "moyen": "VIREMENT",
  "campagneId": 1,
  "donateurId": 2
}
```

### Réponse standard
```json
{
  "success": true,
  "message": "Ressource créée avec succès",
  "data": { ... },
  "timestamp": "2025-05-15T10:30:00"
}
```

---

## 🧪 Tests

```bash
# Lancer tous les tests
mvn test

# Rapport de couverture (avec JaCoCo)
mvn verify
```

---

## 🔧 Stack technique

| Technologie | Version | Rôle |
|-------------|---------|------|
| Java | 17 | Langage |
| Spring Boot | 3.2.3 | Framework principal |
| Spring Data JPA | — | Persistance |
| Hibernate | — | ORM |
| MapStruct | 1.5.5 | Mapping Entity ↔ DTO |
| Lombok | 1.18.30 | Réduction boilerplate |
| Bean Validation | — | Validation des entrées |
| H2 | — | BDD embarquée (dev) |
| JUnit 5 + Mockito | — | Tests unitaires |

---

## 🔄 Migration vers MySQL (production)

Dans `application.properties`, remplacer la section H2 par :

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/gestiondons?useSSL=false&serverTimezone=UTC
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=update
```

Et ajouter la dépendance Maven :
```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>
```
