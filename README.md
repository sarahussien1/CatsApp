# CatsApp

# Overview
This document outlines the design, functionality, and architecture of the Cat Management App, an Android application that enables users to view, manage, and favorite cat information. The system is built around a reliable data experience for users, handling network connectivity issues gracefully through local caching and fallback mechanisms. The system also supports offline operations, syncs data when connectivity is restored, and enhances user experience with optimized UI patterns.

# Functional Requirements
View Cats List (Remote Fallback to Local):

- The app will retrieve a list of cats from a remote server.
- If the remote request fails, the app will fallback to a local database.
- If neither remote nor local data is available, the user will be shown an error message.
- The app ensures users always see a fresh, updated list of cats when online.

Add/Remove Cat to/from Favorites Online:

- Users can mark cats as favorite or remove them from favorites.
- This operation will be performed online by default to ensure the backend is updated immediately.
- Phase 2 Enhancement:
Local Caching of Favorites: When offline, favorite actions are stored locally and marked with isSynced = false.
Background Sync: The app will monitor network connectivity and automatically sync unsynced favorite actions with the backend when the internet is restored.
Backend Requirement: A syncAllFavorites API endpoint that accepts all locally modified favorites needing synchronization.

View Favorites (Remote Fallback to Local):

- Users can view their list of favorite cats.
- Favorites are fetched from the remote server for consistency.
- If remote access fails, the app falls back to local storage.
- If both sources are unavailable, the user is notified of the failure.
- Phase 2 Enhancement:
Pagination: For improved performance and scalability, implement pagination when fetching favorites, especially for large lists.

Search Cats (Local-Only):

- Users can perform searches on the cat list, To optimize performance, search operations is conducted locally on the device.

View Cat Details:

- Users can view detailed information about each cat.
- This screen will be accessible from the main list and favorites list.
# System Architecture
The system follows the MVVM (Model-View-ViewModel) and Repository design patterns to promote separation of concerns and enhance testability.

- Model: Manages data and includes network responses, local entities, and data mappers.
- ViewModel: Holds UI logic and state, fetching data from the repository or usecase and exposing it to the UI.
- View (Jetpack Compose): Provides a UI layer that reacts to changes in the ViewModel’s state.
Technologies and Tools
- UI and Navigation: Built using Jetpack Compose, ensuring a modern and reactive UI.
- Dependency Injection: Hilt is used for injecting dependencies, enhancing modularity and testability.
- Data Management: Room database for local storage, Retrofit for network calls, and Flow/LiveData for reactive data handling.
- Error Handling: A Resource sealed class encapsulates loading, success, and error states.
- Data Classes: Unified data models to avoid redundancy, using the same data transfer objects (DTO) and Room entities.
- Testing: JUnit and Mockito are used to ensure unit and mock testing coverage.
# Implementation Details
- Data Flow
- Repository Layer: Repositories manage data retrieval and handle logic for fallback scenarios between remote and local sources.
- UseCase Layer: UseCases manage data retrieval from more than Repsository accordingly and map to Ui models
- A VersionCatalog is implemented to manage dependency versions centrally, streamlining updates.
