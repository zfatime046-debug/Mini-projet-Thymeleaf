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

<img width="1853" height="847" alt="image" src="https://github.com/user-attachments/assets/2750be48-b03e-4896-b140-f1ba4e37b681" />

---

## 📡 Endpoints REST
###Test avec swagger:http://localhost:8081/swagger-ui/index.html#/
<img width="1621" height="940" alt="image" src="https://github.com/user-attachments/assets/05cff36c-6601-4010-b480-83183843ec4d" />


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
<img width="1546" height="948" alt="image" src="https://github.com/user-attachments/assets/aec01f23-8109-43b4-8efb-f5cceb404f46" />

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
<img width="1548" height="930" alt="image" src="https://github.com/user-attachments/assets/0807561f-6ca4-4a2b-b858-ca49473d478b" />


---

## ScreenShots:
<img width="1919" height="739" alt="image" src="https://github.com/user-attachments/assets/e7a31852-c355-4461-98d2-e0ed57cf82e5" />
<img width="1750" height="595" alt="image" src="https://github.com/user-attachments/assets/df5a6145-2196-4395-be0a-c607ee138367" />

<img width="1919" height="650" alt="image" src="https://github.com/user-attachments/assets/1c8408e4-c2e1-472d-8213-46e4db4b039c" />
<img width="1919" height="730" alt="image" src="https://github.com/user-attachments/assets/ce9239ca-60f8-4e77-9411-bccf38d757e3" />

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
| JUnit 5 + Mockito | — | Tests unitaires |

---

## 🔄 Migration vers MySQL (production)

Dans `application.properties`, remplacer la section H2 par :

```properties
spring.datasource.url=jdbc:mysql://localhost:3307/lagestiondons?useSSL=false&serverTimezone=UTC
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
