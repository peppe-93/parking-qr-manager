# 🅿️ Parking QR Manager

App Android per la gestione di parcheggi privati a pagamento tramite QR Code.

![Build Status](https://github.com/USERNAME/parking-qr-manager/workflows/Build%20Android%20APK/badge.svg)

## 📱 Caratteristiche

✅ **Generazione QR Code**
- QR Code univoci e sicuri con prefisso aziendale
- Generazione singola o batch (fino a 100)
- Export PDF formato A4 stampabile
- Salvataggio in galleria e condivisione

✅ **Scansione QR Code**
- Funziona completamente offline
- 4 aree colorate (Verde, Rosso, Giallo, Blu)
- 100 postazioni per area
- Rilevamento automatico duplicati
- Segnalazione postazioni vuote

✅ **Gestione Dati**
- Database SQLite locale
- Storico completo scansioni
- Export CSV per analisi
- Gestione sessioni di controllo

## 🚀 Download APK

### Ultima versione:
[**Scarica APK**](https://github.com/USERNAME/parking-qr-manager/releases/latest)

Oppure vai alla tab [**Releases**](https://github.com/USERNAME/parking-qr-manager/releases) per vedere tutte le versioni.

## 📋 Requisiti

- Android 7.0 (API 24) o superiore
- Permessi: Camera, Storage

## 🛠️ Compilazione

Il progetto usa GitHub Actions per compilare automaticamente l'APK ad ogni commit.

Per compilare manualmente:
```bash
./gradlew assembleDebug
```

L'APK sarà in: `app/build/outputs/apk/debug/app-debug.apk`

## 📚 Documentazione

- [**GUIDA_GITHUB_ACTIONS.md**](GUIDA_GITHUB_ACTIONS.md) - Come ottenere APK automaticamente
- [**GUIDA_COMPILAZIONE.md**](GUIDA_COMPILAZIONE.md) - Compilazione con Android Studio
- [**GUIDA_STAMPA_QR.md**](GUIDA_STAMPA_QR.md) - Come stampare i QR Code
- [**MANUALE_OPERATIVO.md**](MANUALE_OPERATIVO.md) - Esempi pratici di utilizzo
- [**README_TECNICO.md**](README.md) - Documentazione tecnica completa

## 🎯 Utilizzo

### 1. Genera QR Code
- Apri app → "Genera QR Code"
- Seleziona azienda
- Genera singolo o batch
- Salva/Stampa

### 2. Scansiona Parcheggio
- Apri app → "Scansiona Area"
- Seleziona area (Verde/Rosso/Giallo/Blu)
- Scansiona QR Code di ogni veicolo
- L'app rileva automaticamente duplicati

### 3. Export Dati
- Apri app → "Esporta Dati"
- Export CSV per analisi
- Elimina dati vecchi per liberare spazio

## 🏗️ Struttura Progetto

```
parking-qr-manager/
├── app/
│   ├── src/main/
│   │   ├── java/com/parking/qrmanager/
│   │   │   ├── MainActivity.java
│   │   │   ├── GenerateQRActivity.java
│   │   │   ├── BatchQRGeneratorActivity.java
│   │   │   ├── ScanActivity.java
│   │   │   ├── HistoryActivity.java
│   │   │   ├── ExportActivity.java
│   │   │   └── DatabaseHelper.java
│   │   ├── res/
│   │   └── AndroidManifest.xml
│   └── build.gradle
├── .github/workflows/
│   └── build.yml
├── gradle/
├── build.gradle
├── settings.gradle
├── gradlew
└── gradlew.bat
```

## 🔧 Tecnologie

- **Linguaggio:** Java
- **Min SDK:** 24 (Android 7.0)
- **Target SDK:** 34 (Android 14)
- **Database:** SQLite
- **Librerie:**
  - ZXing (QR Code generation/scanning)
  - AndroidX Camera
  - Material Design

## 📄 Licenza

Codice proprietario. Tutti i diritti riservati.

## 👤 Autore

[Il tuo nome/azienda]

## 🤝 Supporto

Per problemi o domande, apri una Issue su GitHub.

---

**Versione:** 1.0  
**Ultimo aggiornamento:** 2025
