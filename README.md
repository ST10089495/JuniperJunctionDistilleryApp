# Juniper Junction Distillery Android App

![image](https://github.com/ST10089495/JuniperJunctionDistilleryApp/assets/126766252/24934563-6bc1-4446-ab5f-2c56b735f43f)

## Overview

The Juniper Junction Distillery Android App is designed to streamline the management of gin tasting and making experiences, stock inventory, supplies, and employee information for the distillery. The app is built using Android Studio with Kotlin.

## Table of Contents

- [Features](#features)
- - [Screenshots](#screenshots)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Firebase Setup](#firebase-setup)
  - [Dependancies](#dependancies)
- [Contributing](#contributing) 
- [License](#license)

## Features

- **Gin Tasting and Making Experiences:**
  - Manage and track bookings from the connected website.
  
- **Stock Management:**
  - Keep track of available gin products.
  
- **Supplies and Invoices:**
  - Manage supplies, create invoices for suppliers and distributors.

- **Employee Management:**
  - Add, edit, and delete employee details.

## Screenshots

![image](https://github.com/ST10089495/JuniperJunctionDistilleryApp/assets/126766252/333a1dbf-9421-435a-9691-36b327feee79)
![image](https://github.com/ST10089495/JuniperJunctionDistilleryApp/assets/126766252/5a1e7fd9-4054-403f-b2e8-36d1c6888d19)
![image](https://github.com/ST10089495/JuniperJunctionDistilleryApp/assets/126766252/68fa640d-57ef-4934-b2ec-d8d9a50ba26d)
![image](https://github.com/ST10089495/JuniperJunctionDistilleryApp/assets/126766252/3c31c2db-6e14-43a8-860d-c3e65e2a6e5b)
![image](https://github.com/ST10089495/JuniperJunctionDistilleryApp/assets/126766252/5ea034cd-d700-4e41-8854-d06a3b7fb0be)
![image](https://github.com/ST10089495/JuniperJunctionDistilleryApp/assets/126766252/063b0f3d-2a14-4aa9-a987-f3b1075fa536)
![image](https://github.com/ST10089495/JuniperJunctionDistilleryApp/assets/126766252/8f14b3a4-858c-4a20-8fdb-39dee5691750)
![image](https://github.com/ST10089495/JuniperJunctionDistilleryApp/assets/126766252/dda5e1a1-3e74-4b4c-a160-24de2f13544f)

## Getting Started

### Prerequisites

- Android Studio
- Kotlin

### Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/ST10089495/JuniperJunctionDistilleryApp.git
    ```
2.Open the project in Android Studio.

3.Build and run the application.

# Firebase Setup

To integrate Firebase with your Android application, follow these steps:

## 1. Create a Firebase Project

- Visit the [Firebase Console](https://console.firebase.google.com/) and create a new project.

## 2. Add Your App to the Project

- Click on "Add App" and select the appropriate platform (Android).
- Register your app by providing a package name (com.example.myapp).
- Download the `google-services.json` configuration file.

## 3. Add Firebase SDK to Your App

- Add the following dependencies to your app's `build.gradle` file:

```gradle
dependencies {
    // Firebase Realtime Database
    implementation 'com.google.firebase:firebase-database:22.0.0'

    // Firebase Authentication
    implementation 'com.google.firebase:firebase-auth:22.0.0'
    
    // Add other Firebase dependencies as needed
}
```
Apply the Google Services plugin and include the google-services.json file:
```gradle
apply plugin: 'com.google.gms.google-services'
```
4. Initialize Firebase
Initialize Firebase in your app's onCreate method:

```java
// Add this to your MainActivity or Application class
FirebaseApp.initializeApp(this);
```
## Dependencies
Here are the main dependencies used in this project:

- Firebase Database: For real-time data storage.
- Firebase Authentication: For user authentication.

Refer to the [official Firebase documentation](https://firebase.google.com/docs/android/setup) for more details on setting up Firebase in your Android app.

## Usage

### 1. Gin Tasting and Making Experiences

- **Booking a Experience:**
  - Launch the app and navigate to the "Booking Management" section.
  - This manages the bookings made from the website to the application.

![image](https://github.com/ST10089495/JuniperJunctionDistilleryApp/assets/126766252/080ffcee-a87d-4976-a20b-62729b791a90)
![image](https://github.com/ST10089495/JuniperJunctionDistilleryApp/assets/126766252/a1535eba-4efc-4825-b461-fe02eba5a9dd)


### 2. Stock Management

- **Viewing Available Products:**
  - Explore the "Stock" section to see the list of available gin products.
  - Tap on a specific product to view more details.

![image](https://github.com/ST10089495/JuniperJunctionDistilleryApp/assets/126766252/db758fc3-5651-4730-8b65-376fa3c7486f)
![image](https://github.com/ST10089495/JuniperJunctionDistilleryApp/assets/126766252/0c7f7583-63aa-4746-87c6-57bafd95d2a5)
![image](https://github.com/ST10089495/JuniperJunctionDistilleryApp/assets/126766252/d5fa73f5-af79-45a6-bf8b-8ffa928f7afc)


### 3. Supplies and Invoices

- **Managing Supplies:**
  - Access the "Supplies" section to manage and track supplies.
  - Create new invoices for suppliers and distributors.

![image](https://github.com/ST10089495/JuniperJunctionDistilleryApp/assets/126766252/3982258c-db92-46d8-814d-8897e76b4017)
![image](https://github.com/ST10089495/JuniperJunctionDistilleryApp/assets/126766252/8588a3e6-2416-468a-8ce6-8a5b745a602d)
![image](https://github.com/ST10089495/JuniperJunctionDistilleryApp/assets/126766252/79df83f2-f2b1-4689-8a2d-e239925e8692)
![image](https://github.com/ST10089495/JuniperJunctionDistilleryApp/assets/126766252/1f470bc9-490b-473f-81e9-ec942b99e68f)


### 4. Employee Management

- **Adding and Editing Employees:**
  - Head to the "Employees" section to add, edit, or delete employee details.
  - Keep the employee database up-to-date.

![image](https://github.com/ST10089495/JuniperJunctionDistilleryApp/assets/126766252/ae1dda15-6ee2-49bb-b75f-b43d73028d48)
![image](https://github.com/ST10089495/JuniperJunctionDistilleryApp/assets/126766252/4b1cf00e-b792-4dec-9b0c-7f81b1541a89)


### 5. General Navigation

- **App Navigation:**
  - Utilize the bottom navigation bar to switch between different sections of the app.
  - Access the menu for additional options and settings.

![image](https://github.com/ST10089495/JuniperJunctionDistilleryApp/assets/126766252/7cb27939-82bb-4ee4-833e-45221e4a5280)

## Contributing

If you'd like to contribute to BirdQuest, feel free to fork the repository and submit pull requests. We welcome any improvements, bug fixes, or additional features.

## License

[MIT License](https://opensource.org/licenses/MIT).


   
