# ApartmentRenovationCostEstimate (ARCEA)

## Opis
**ApartmentRenovationCostEstimate** jest internetową aplikacją do tworzenia kosztorysów wykończenia i remontu mieszkań. Umożliwia użytkownikom łatwe i szybkie szacowanie kosztów remontu poprzez dodawanie produktów do personalizowanego koszyka zakupowego.

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
- Niestandardowa obsługa błędów dla żądań niezautoryzowanych i zabronionych.
- Obsługa żądań API z odpowiedziami JSON dla niezautoryzowanych i zabronionych żądań oraz osobne strony błędu dla żądań przeglądarkowych.

**Hashowanie haseł**:
  - Hasła użytkowników są hashowane przy użyciu algorytmu **BCrypt** przed zapisaniem w bazie danych.

## Technologie i narzędzia
- **Backend**: Java, Spring Boot v3.3.0, Spring Data JPA, Spring Security v6.3.0, Hibernate
- **Frontend**: HTML, CSS, JavaScript, Bootstrap (wersja 5.2.3), Thymeleaf
- **Baza danych**: MySQL
- **Narzędzia**: Lombok, Postman, phpMyAdmin

## Jak zacząć?
Aby uruchomić projekt lokalnie, wykonaj następujące kroki:
1. Sklonuj repozytorium na swoją maszynę.
2. Zainstaluj wymagane zależności opisane w `pom.xml`.
3. Ustaw lokalne połączenie z bazą danych MySQL, korzystając z `application.properties`.
4. Uruchom aplikację poprzez Spring Boot.

## Wymagania wstępne
- **JDK 17+**: Aplikacja jest zbudowana z użyciem wersji 17 JDK lub nowszej.
- **MySQL**: Konfiguracja bazy danych w `application.properties` powinna być dostosowana do lokalnej instalacji MySQL.
- **Maven**: Używany jako narzędzie do zarządzania zależnościami i budowania projektu.