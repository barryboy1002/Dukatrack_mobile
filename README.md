# Dukatrack POS

Dukatrack is a lightweight, offline-first Point of Sale (POS) system designed for small businesses to manage sales and products efficiently. Built with modern Android technologies, it provides a fast and reliable experience for local retail operations.

##  Features

### Free Tier (Mobile)
- **Product Management**: Create and organize products with categories.
- **Dynamic Search**: Instantly find products by name or category.
- **Sales Workflow**: 
  - Add products to cart.
  - Quick checkout process.
  - Basic receipt generation.
- **Sales History**: Track and view previous transactions.
- **Offline First**: All data is stored locally using Room Database—no internet required for core operations.

### Pro Features (Desktop Version)
The mobile app serves as a gateway to the **Dukatrack Desktop Pro** version. Advanced features available on desktop include:
- **Inventory & Stock Management**: Real-time tracking and low-stock alerts.
- **Supplier Management**: Keep track of your supply chain.
- **Advanced Reports**: Deep insights into business performance.
- **Bulk Import**: Quickly upload your entire inventory via Excel/CSV.
- **Barcode Support**: Speed up checkout with scanners.
- **Printing**: Official thermal receipt printing support.

##Tech Stack
- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose)
- **Database**: [Room SQLite](https://developer.android.com/training/data-storage/room)
- **Architecture**: MVVM (Model-View-ViewModel)
- **Navigation**: Compose Navigation
- **Asynchronous**: Kotlin Coroutines & Flow

##Getting Started

### Prerequisites
- Android Studio Ladybug (or newer)
- Android SDK 34+
- Java 17

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/dukatrack.git
   ```
2. Open the project in **Android Studio**.
3. Let Gradle sync and download dependencies.
4. Run the app on an emulator or physical device (API Level 26+ recommended).

## License
This project is licensed under the MIT License - see the LICENSE file for details.
