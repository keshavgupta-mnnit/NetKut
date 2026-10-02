# Android Jetpack Compose Architecture & Coding Guidelines

This document outlines the standard architectural patterns, UI structuring, and coding practices to be followed across all Android Jetpack Compose projects. 

When generating, refactoring, or updating code, strictly adhere to these rules to maintain a professional, modular, and performant workspace.

## 1. Project Folder Structure

Organize the codebase by feature and separation of concerns.

*   **`ui/screens/<feature_name>/`**: 
    *   Contains complete screen-level Composables (e.g., `MainScreen.kt`, `SettingsScreen.kt`).
    *   If a screen has multiple tabs or sub-screens (e.g., `SelectedAppsTabContent.kt`), they belong in this folder.
    *   **ViewModels** specific to a feature must reside in the same folder as their corresponding screen (e.g., `MainViewModel.kt` alongside `MainScreen.kt`).
*   **`ui/components/`**: 
    *   Contains highly reusable, individual, and stateless UI components (e.g., `AppItemRow.kt`, `SearchBar.kt`, `AppDropdown.kt`).
    *   *Never* put full screens or feature-specific business logic here.
*   **`ui/theme/`**: 
    *   Contains Kotlin-based resource definitions (`Color.kt`, `Dimens.kt`, `Strings.kt`, `Theme.kt`, `Type.kt`).
*   **`util/`**: 
    *   Contains domain-specific utility objects and helper functions.
    *   Separate utilities by their purpose (e.g., `PreferenceUtils.kt`, `WorkManagerUtils.kt`, `VpnUtils.kt`, `AppFilterUtils.kt`).
*   **`data/` & `domain/`**: 
    *   Follow Clean Architecture. Put interfaces and models in `domain/` and implementations in `data/`.

## 2. Resource Management (No Hardcoding)

**Do not use hardcoded strings, dimensions, or colors in UI components.**

*   **Kotlin Resources over XML**: For Compose-first apps, prefer defining design-system values in Kotlin objects rather than Android XML files. This avoids context-switching and makes usage seamless in Compose.
*   **`Strings.kt`**: Keep all UI text, button labels, and descriptions in a `Strings` object.
*   **`Dimens.kt`**: Keep all paddings, margins, icon sizes, corner radiuses, and font sizes in a `Dimens` object (e.g., `Dimens.PaddingLarge`, `Dimens.FontSizeTitle`).
*   **`Color.kt`**: Define all semantic and raw colors here.
*   **`AppConstants.kt`**: Store all non-UI constants in a central `AppConstants` object (e.g., `PREFS_NAME`, `KEY_VPN_ACTIVE`, `WORK_MANAGER_TAG`, default integer values).

## 3. UI & Compose Best Practices

*   **Maximum Reusability**: Do not write redundant Compose functions. If two components look similar but handle different data (e.g., a Category Dropdown and a Sync Interval Dropdown), build a generic `<T> AppDropdown` and pass the data/state to it.
*   **Stateless Components**: Keep `ui/components/` as stateless as possible. Pass data in via arguments and events out via lambda callbacks (State Hoisting).
*   **Modifier Parameter**: *Every* Composable function must accept a `modifier: Modifier = Modifier` as a parameter and apply it to its root layout. This allows the parent screen to dictate sizing and padding.
*   **Clean Views**: Do not perform data mapping or list generation inside the Composable. Map your data in the ViewModel or utility classes, then pass a clean UI-State data class to the Composable.

## 4. Separation of Concerns & Utilities

*   **Keep Composables Clean**: Composables should only care about drawing the UI. If you have logic to map categories, fetch intents, or filter lists, extract it.
*   **Specific Util Classes**: Do not dump everything into a single `AppUtils.kt`. Create specific utilities:
    *   `PreferenceUtils.kt`: A wrapper for all SharedPreferences `get/set` logic.
    *   `WorkManagerUtils.kt`: Logic to enqueue, cancel, and update periodic workers.
    *   `AppFilterUtils.kt`: Pure Kotlin functions to filter or sort lists.
*   **ViewModel Responsibilities**: ViewModels should bridge the gap between Repositories and the UI. Use `StateFlow` and `combine` to reactively update the UI. Move complex filtering algorithms out of the ViewModel into a pure utility function to keep the ViewModel readable.

## 5. Performance & Data Caching

*   **Avoid Expensive Calls on Recomposition**: System-level calls (like `PackageManager.getInstalledApplications`) or reading large files are expensive. 
*   **Repository Caching**: Cache the results of expensive operations in the Repository implementation (e.g., using `@Volatile private var cachedData`) so that UI events like tab switching or searching do not trigger redundant system queries. Serve from memory wherever possible.
