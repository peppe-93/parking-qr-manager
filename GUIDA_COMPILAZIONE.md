# GUIDA RAPIDA - Compilazione APK

## Prerequisiti
1. Scarica Android Studio: https://developer.android.com/studio
2. Installa Android Studio seguendo la procedura guidata
3. Assicurati di installare anche Android SDK

## Struttura Progetto Android Studio

Per importare correttamente il progetto in Android Studio, crea questa struttura:

```
MioParcheggio/                          (cartella root del progetto)
├── app/                                (modulo app)
│   ├── build.gradle                    (usa build.gradle fornito)
│   ├── proguard-rules.pro
│   └── src/
│       └── main/
│           ├── AndroidManifest.xml
│           ├── java/
│           │   └── com/parking/qrmanager/
│           │       ├── MainActivity.java
│           │       ├── GenerateQRActivity.java
│           │       ├── ScanActivity.java
│           │       ├── HistoryActivity.java
│           │       ├── ExportActivity.java
│           │       └── DatabaseHelper.java
│           └── res/
│               ├── layout/
│               ├── values/
│               └── xml/
├── build.gradle                        (usa build.gradle.root rinominato)
├── settings.gradle                     (crea questo file)
└── gradle.properties                   (crea questo file)
```

## Passo 1: Crea il Progetto Android Studio

### Opzione A: Importa i file forniti

1. Crea una cartella vuota chiamata `MioParcheggio`
2. All'interno crea la struttura sopra indicata
3. Copia tutti i file nella posizione corretta:
   - File Java in `app/src/main/java/com/parking/qrmanager/`
   - AndroidManifest.xml in `app/src/main/`
   - File layout in `app/src/main/res/layout/`
   - File values in `app/src/main/res/values/`
   - file_paths.xml in `app/src/main/res/xml/`

4. Crea `settings.gradle` nella root:
```gradle
include ':app'
rootProject.name = "Parking QR Manager"
```

5. Crea `gradle.properties` nella root:
```properties
android.useAndroidX=true
android.enableJetifier=true
```

6. Rinomina `build.gradle.root` in `build.gradle` nella root

### Opzione B: Nuovo Progetto Android Studio

1. Apri Android Studio
2. File → New → New Project
3. Seleziona "Empty Activity"
4. Nome: Parking QR Manager
5. Package name: com.parking.qrmanager
6. Language: Java
7. Minimum SDK: API 24
8. Finish
9. Sostituisci tutti i file generati con quelli forniti

## Passo 2: Verifica Dipendenze

Apri `app/build.gradle` e verifica che contenga:

```gradle
dependencies {
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.11.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    implementation 'com.google.zxing:core:3.5.2'
    implementation 'com.journeyapps:zxing-android-embedded:4.3.0'
    implementation 'androidx.camera:camera-camera2:1.3.1'
    implementation 'androidx.camera:camera-lifecycle:1.3.1'
    implementation 'androidx.camera:camera-view:1.3.1'
}
```

## Passo 3: Sync e Build

1. In Android Studio, clicca su "Sync Project with Gradle Files" (icona elefante)
2. Attendi il completamento (potrebbe richiedere alcuni minuti)
3. Risolvi eventuali errori mostrati
4. Build → Build Bundle(s) / APK(s) → Build APK(s)

## Passo 4: Trova l'APK

Dopo il build, l'APK sarà in:
```
MioParcheggio/app/build/outputs/apk/debug/app-debug.apk
```

## Passo 5: Installa su Android

### Via USB:
1. Abilita "Modalità Sviluppatore" su Android:
   - Impostazioni → Informazioni sul telefono
   - Tocca "Numero build" 7 volte
2. Abilita "Debug USB"
3. Collega il telefono al PC
4. In Android Studio: Run → Run 'app'

### Via APK:
1. Copia `app-debug.apk` sul telefono
2. Abilita "Installa app sconosciute" per il file manager
3. Apri l'APK e installa

## Risoluzione Problemi Comuni

### Errore: "SDK not found"
Soluzione: Tools → SDK Manager → Installa Android SDK Platform 34

### Errore: "Build failed"
Soluzione: 
- Verifica connessione internet (per scaricare dipendenze)
- File → Invalidate Caches → Invalidate and Restart
- Build → Clean Project, poi Build → Rebuild Project

### Errore: "ZXing dependency not found"
Soluzione: 
- Verifica connessione internet
- Sync Project with Gradle Files
- Se persiste, aggiungi manualmente nel build.gradle:
```gradle
repositories {
    google()
    mavenCentral()
}
```

### Errore compilazione Java
Soluzione: File → Project Structure → SDK Location → Verifica JDK (usa JDK 11 o 17)

## Build Release (per produzione)

Per creare un APK firmato per il Play Store:

1. Build → Generate Signed Bundle / APK
2. Seleziona "APK"
3. Crea un nuovo keystore o usa uno esistente
4. Compila i dettagli del keystore
5. Seleziona "release"
6. Finish

L'APK release sarà in: `app/build/outputs/apk/release/app-release.apk`

## Verifica Installazione

Dopo l'installazione, verifica:
1. L'app si apre correttamente
2. Tutti i menu sono accessibili
3. Richiede i permessi (Camera, Storage)
4. Prova a generare un QR Code
5. Prova a scansionare un QR Code

## Checklist Pre-Rilascio

- [ ] App si compila senza errori
- [ ] Tutte le Activity si aprono
- [ ] Generazione QR Code funziona
- [ ] Scanner camera funziona
- [ ] Database salva correttamente
- [ ] Rilevamento duplicati funziona
- [ ] Export CSV funziona
- [ ] Testato su almeno 2 dispositivi Android diversi
- [ ] Verificati tutti i permessi

## Supporto

Per ulteriori informazioni, consulta:
- README.md completo
- Documentazione Android: https://developer.android.com
- Stack Overflow: https://stackoverflow.com/questions/tagged/android

## Note Importanti

⚠️ ATTENZIONE:
- Il file app-debug.apk è solo per test, non per produzione
- Per distribuzione, usa sempre un APK firmato (release)
- Testa sempre su dispositivi reali, non solo emulatore
- Verifica compatibilità con diverse versioni Android (min API 24)
