# Apartment Renovation Cost Estimate (ARCEA)

***W fazie rozwoju...***

## Opis
**Apartment Renovation Cost Estimate** jest internetową aplikacją do tworzenia kosztorysów wykończenia i remontu mieszkań. Umożliwia użytkownikom łatwe i szybkie szacowanie kosztów remontu poprzez dodawanie produktów do personalizowanego koszyka zakupowego.

## Funkcjonalności
- **Zarządzanie użytkownikami**: dodawanie nowych użytkowników, usuwanie istniejących oraz przydzielanie ról, takich jak Administrator i Użytkownik.
- **Zarządzanie pomieszczeniami**: możliwość dodawania i modyfikowania pomieszczeń takich jak salon, sypialnia, kuchnia czy łazienka.
- **Zarządzanie produktami**: tworzenie listy produktów, dodawanie nowych, modyfikacja istniejących, możliwość dodania linku do strony dostawcy, określenie ceny produktu, filtrowanie produktów po kategorii.
- **Zarządzanie koszykami**: tworzenie koszyka zakupowego przypisanego do użytkownika, dodawanie produktów do koszyka, filtrowanie produktów w koszyku po kategorii, usuwanie koszyków oraz ich zawartości. Automatyczna aktualizacja wartości koszyka na podstawie produktów w nim się znajdujących.
- **Backup/Restore**: tworzenie backupu oraz przywracanie bazy danych z poziomu aplikacji. ***TODO: ograniczenie dostępu do poziomu Administratora.***

## Mechanizmy zabezpieczeia aplikacji
**Zabezpieczenia i role użytkowników**:
- Wykorzystano mechanizmy autoryzacji i uwierzytelniania za pomocą **Spring Security**,
- Kontrola dostępu oparta na rolach użytkownika: USER i ADMIN, pozwalająca na autoryzację dostępu do zasobów i akcji,
- Kontrola dostępu na poziomie metod, w tym szczegółowa autoryzacja przy wywołaniach kontrolerów.

**Obsługa błędów związanych z autoryzacją**:
- Obsługa błędów dla żądań niezautoryzowanych i zabronionych.

**Hashowanie haseł**:
  - Hasła użytkowników są hashowane przy użyciu algorytmu **BCrypt** przed zapisaniem w bazie danych.

## Technologie i narzędzia
- **Backend**: Java v17, Spring Boot v3.3.0, Spring Data JPA v3.3.0, Spring Security v6.3.0, Hibernate
- **Frontend**: HTML, CSS, JavaScript, Bootstrap (wersja 5.2.3), Thymeleaf v3.1.1
- **Baza danych**: MySQL
- **Testowanie**: Spring Test, JUnit 5, Mockito, AssertJ
- **Dokumentacja**: Springdoc OpenAPI (Swagger)
- **Narzędzia**: Lombok 1.18.32, Postman v11.17.1, phpMyAdmin

## Jak zacząć?
Aby uruchomić projekt lokalnie, wykonaj następujące kroki:
1. Sklonuj repozytorium na swoją maszynę.
2. Zainstaluj wymagane zależności opisane w `pom.xml`.
3. Ustaw lokalne połączenie z bazą danych MySQL, korzystając z `application.yml`.
4. Uruchom aplikację poprzez Spring Boot.

## Wymagania wstępne
- **JDK 17+**: Aplikacja jest zbudowana z użyciem wersji 17 JDK lub nowszej.
- **MySQL**: Konfiguracja bazy danych w `application.yml` powinna być dostosowana do lokalnej instalacji MySQL.
- **Maven**: Używany jako narzędzie do zarządzania zależnościami i budowania projektu.

## Dokumentacja API
### **Sawgger:**
``` http request
GET http://localhost:8080/swagger-ui/index.html
```
![Swagger_Doc_01](https://github.com/user-attachments/assets/85c02f03-2818-4808-8835-b0d3dd48664e)


## Pokrycie testami
![Zrzut ekranu 2025-03-11 184147](https://github.com/user-attachments/assets/b544166b-b8e7-4b96-b673-d5989b76f2c0)

