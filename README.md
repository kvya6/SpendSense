# 💰 SpendSense - Smart Expense Manager

[![Platform](https://img.shields.io/badge/Platform-Android-green.svg)](https://www.android.com/)
[![Language](https://img.shields.io/badge/Language-Kotlin-blue.svg)](https://kotlinlang.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

> A modern, offline-first personal finance and expense management Android app built with Jetpack Compose

![SpendSense Banner](screenshots/banner.png)

## ✨ Features

- 📊 **Smart Expense Tracking** - Add expenses in under 5 seconds
- 💰 **Income Management** - Track all income sources
- 📈 **Visual Analytics** - Beautiful animated pie charts
- 🎯 **Budget Management** - Set monthly limits with smart alerts
- 🔒 **Privacy First** - 100% offline, data stays on your device
- ⚡ **Lightning Fast** - Optimistic updates, instant UI response
- 🎨 **Modern UI** - Material Design 3 with smooth animations

## 📱 Screenshots

<p align="center">
  <img src="screenshots/login.png" width="200" />
  <img src="screenshots/home.png" width="200" />
  <img src="screenshots/analytics.png" width="200" />
  <img src="screenshots/budget.png" width="200" />
</p>

## 🏗️ Architecture

- **Pattern**: MVVM (Model-View-ViewModel)
- **UI**: Jetpack Compose
- **Database**: Room (SQLite)
- **Language**: Kotlin
- **Min SDK**: 28 (Android 9.0)
- **Target SDK**: 34

### Architecture Diagram
┌─────────────────────────────────────┐
│           VIEW LAYER                │
│    (Jetpack Compose Screens)        │
└────────────┬────────────────────────┘
             │
             ↓
┌─────────────────────────────────────┐
│        VIEWMODEL LAYER              │
│   (Business Logic & State)          │
└────────────┬────────────────────────┘
             │
             ↓
┌─────────────────────────────────────┐
│       REPOSITORY LAYER              │
│   (Data Coordination)               │
└────────────┬────────────────────────┘
             │
             ↓
┌─────────────────────────────────────┐
│         DATA LAYER                  │
│  (Room Database + SharedPreferences)│
└─────────────────────────────────────┘
## 🛠️ Tech Stack

| Component | Technology |
|-----------|-----------|
| UI Framework | Jetpack Compose |
| Language | Kotlin |
| Architecture | MVVM |
| Database | Room |
| Dependency Injection | Manual DI |
| Async | Coroutines + Flow |
| Navigation | Navigation Compose |
| Design | Material Design 3 |

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog or newer
- JDK 17 or higher
- Android SDK 34
- Gradle 8.5+

### Installation

1. **Clone the repository**
```bash
git clone https://github.com/YOUR_USERNAME/SpendSense.git
cd SpendSense
```

2. **Open in Android Studio**
   - File → Open → Select `SpendSense` folder
   - Wait for Gradle sync

3. **Run the app**
   - Connect Android device or start emulator
   - Click Run button (▶️) or press `Shift + F10`

### Building

#### Debug Build
```bash
./gradlew assembleDebug
```

#### Release Build
```bash
./gradlew assembleRelease
```

## 🎯 Key Features Explained

### Optimistic Updates
SpendSense uses optimistic UI updates for instant feedback:
```kotlin
fun addExpense(expense: Expense) {
    // Update UI immediately
    _expenses.value = _expenses.value + expense
    
    // Save to database in background
    viewModelScope.launch {
        repository.insertExpense(expense)
    }
}
```

### Offline-First
- All data stored locally in Room Database
- No internet required
- Complete privacy and security
- Optional Firebase sync (future feature)

### Performance
- ⚡ < 50ms UI response time
- 📊 Animated pie charts at 60fps
- 💾 Efficient database queries
- 🔄 StateFlow for reactive updates

## 📊 Database Schema

### Tables

**User**
- id (PK), name, email, password, currency, monthlyIncome, createdAt

**Expense**
- id (PK), amount, category, date, note, paymentMethod

**Income**
- id (PK), amount, source, date, note

**Budget**
- id (PK), month, limitAmount, category

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Your Name**
- GitHub: [@YOUR_USERNAME](https://github.com/YOUR_USERNAME)
- LinkedIn: [Your Profile](https://linkedin.com/in/yourprofile)
- Email: your.email@example.com

## 🙏 Acknowledgments

- Jetpack Compose for modern UI
- Material Design 3 for design guidelines
- Room Database for data persistence
- Kotlin Coroutines for async operations

## 📱 Download

[<img src="https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png" width="200">](https://play.google.com/store/apps/details?id=com.yourname.spendsense)

## 🗺️ Roadmap

- [ ] Cloud sync with Firebase
- [ ] Receipt OCR scanning
- [ ] Recurring expenses
- [ ] Multi-currency support
- [ ] Export to Excel/PDF
- [ ] Dark mode
- [ ] Widgets
- [ ] Biometric authentication

## 📸 More Screenshots

### Home Dashboard
![Home](screenshots/home.png)

### Analytics
![Analytics](screenshots/analytics.png)

### Budget Management
![Budget](screenshots/budget.png)

---

**Made with ❤️ and ☕ in India**

⭐ Star this repo if you like it!
