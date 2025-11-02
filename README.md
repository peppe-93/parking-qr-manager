# Parking QR Manager - App Android

## Descrizione
Applicazione Android per la gestione di parcheggi privati a pagamento tramite QR Code.

## Caratteristiche Principali

### 1. Generazione QR Code
- Generazione di QR Code univoci e sicuri
- Prefisso personalizzabile per azienda (NOME_AZIENDA_XXXXXXXXX)
- Codice sicuro con algoritmo SecureRandom
- Salvataggio automatico nel database locale

### 2. Scansione QR Code
- Scansione tramite fotocamera senza necessità di connessione dati
- Selezione area di parcheggio (Verde, Rosso, Giallo, Blu)
- 100 postazioni per ogni area
- Rilevamento automatico duplicati nella stessa sessione
- Possibilità di segnare postazioni vuote
- Gestione sessioni di scansione con ID univoco

### 3. Rilevamento Duplicati
- Controllo automatico se un QR Code è già stato scansionato nella sessione corrente
- Alert con informazioni su area e postazione della scansione precedente
- Prevenzione errori di inserimento dati

### 4. Storico Scansioni
- Visualizzazione di tutte le scansioni effettuate
- Ordinamento per data e ora
- Informazioni dettagliate: QR Code, Area, Postazione, Sessione

### 5. Esportazione Dati
- Export in formato CSV
- Condivisione file tramite email, WhatsApp, etc.
- Eliminazione dati vecchi (>30 giorni) per liberare memoria
- File salvati nella memoria interna dell'app

## Struttura Parcheggio

### Aree
- 🟢 **Verde** (100 posti)
- 🔴 **Rosso** (100 posti)
- 🟡 **Giallo** (100 posti)
- 🔵 **Blu** (100 posti)

Ogni azienda cliente può acquistare un numero variabile di posti (20-30-50...) in una specifica area.

## Requisiti

### Ambiente di Sviluppo
- Android Studio Arctic Fox o superiore
- JDK 8 o superiore
- Android SDK 24 (Android 7.0) o superiore
- Target SDK: 34 (Android 14)

### Permessi Richiesti
- CAMERA: per scansionare QR Code
- WRITE_EXTERNAL_STORAGE: per esportare file CSV
- READ_EXTERNAL_STORAGE: per leggere file esportati

### Dipendenze Principali
- AndroidX AppCompat
- Google Material Design
- ZXing (Zebra Crossing) per QR Code
- JourneyApps ZXing Android Embedded per scanner

## Installazione e Compilazione

### Passo 1: Preparazione
1. Installa Android Studio: https://developer.android.com/studio
2. Scarica e estrai il progetto
3. Apri Android Studio

### Passo 2: Importazione Progetto
1. File → Open
2. Seleziona la cartella ParkingApp
3. Attendi il sync di Gradle

### Passo 3: Configurazione Struttura
Assicurati che la struttura del progetto sia:
```
ParkingApp/
├── AndroidManifest.xml
├── build.gradle
├── java/com/parking/qrmanager/
│   ├── MainActivity.java
│   ├── GenerateQRActivity.java
│   ├── ScanActivity.java
│   ├── HistoryActivity.java
│   ├── ExportActivity.java
│   └── DatabaseHelper.java
└── res/
    ├── layout/
    │   ├── activity_main.xml
    │   ├── activity_generate_qr.xml
    │   ├── activity_scan.xml
    │   ├── activity_history.xml
    │   ├── activity_export.xml
    │   └── custom_barcode_scanner.xml
    ├── values/
    │   ├── strings.xml
    │   ├── colors.xml
    │   └── styles.xml
    └── xml/
        └── file_paths.xml
```

### Passo 4: Build APK
1. Build → Build Bundle(s) / APK(s) → Build APK(s)
2. Attendi il completamento della build
3. Il file APK sarà in: `app/build/outputs/apk/debug/app-debug.apk`

### Passo 5: Installazione su Dispositivo

#### Via USB (Debug)
1. Abilita "Opzioni Sviluppatore" su Android
2. Abilita "Debug USB"
3. Collega il dispositivo
4. Run → Run 'app'

#### Via APK
1. Copia `app-debug.apk` sul dispositivo
2. Abilita "Sorgenti sconosciute" nelle impostazioni
3. Apri il file APK e installa

## Utilizzo

### 1. Generazione QR Code
1. Apri l'app
2. Seleziona "Genera QR Code"
3. Scegli l'azienda dal menu a tendina o inserisci un nome personalizzato
4. Premi "Genera QR Code"
5. Il QR Code verrà visualizzato e salvato nel database
6. Stampa il QR Code e applicalo sul veicolo

### 2. Scansione Area Parcheggio
1. Apri l'app
2. Seleziona "Scansiona Area"
3. Seleziona l'area (Verde, Rosso, Giallo, Blu)
4. Inserisci il numero della postazione (1-100)
5. Premi "Scansiona QR Code"
6. Inquadra il QR Code sul veicolo
7. Se il QR è già stato scansionato, riceverai un alert con i dettagli
8. Per postazioni vuote, premi "Segna Postazione Vuota"
9. Continua con la postazione successiva (il numero si auto-incrementa)
10. Al termine, premi "Termina Sessione"

### 3. Visualizzazione Storico
1. Apri l'app
2. Seleziona "Storico Scansioni"
3. Visualizza tutte le scansioni effettuate

### 4. Esportazione Dati
1. Apri l'app
2. Seleziona "Esporta Dati"
3. Premi "Esporta in CSV"
4. Scegli dove condividere il file (Email, WhatsApp, etc.)
5. Per eliminare dati vecchi, premi "Elimina Dati Vecchi (>30gg)"

## Database

### Tabella qr_codes
- id: INTEGER (PRIMARY KEY)
- qr_code: TEXT (UNIQUE)
- company_name: TEXT
- date_created: TEXT

### Tabella scans
- scan_id: INTEGER (PRIMARY KEY)
- qr_code: TEXT
- area: TEXT (Verde/Rosso/Giallo/Blu)
- spot_number: INTEGER (1-100)
- session_id: TEXT
- scan_date: TEXT
- scan_time: TEXT
- is_empty: INTEGER (0/1)

## Funzionalità Offline
L'app funziona completamente offline:
- Generazione QR Code
- Scansione e salvataggio
- Rilevamento duplicati
- Esportazione dati

Non è richiesta connessione internet o dati mobili.

## Formato Export CSV
```
ID,QR_Code,Area,Postazione,Sessione,Data,Ora,Vuota
1,AZIENDA_A_XYZ123,Verde,15,SESSION_20250101_080000,2025-01-01,08:00:00,NO
2,VUOTA,Verde,16,SESSION_20250101_080000,2025-01-01,08:01:00,SI
```

## Sicurezza QR Code
I QR Code generati contengono:
- Prefisso azienda
- Codice alfanumerico random (12 caratteri)
- Timestamp in esadecimale

Esempio: `AZIENDA_A_K7M9P2N4X1Q5_18A3B5C7D9E`

## Personalizzazione

### Aggiungere Nuove Aziende Predefinite
Modifica `GenerateQRActivity.java`, metodo `setupCompanySpinner()`:
```java
companies.add("NUOVA_AZIENDA");
```

### Modificare Numero Postazioni
Modifica la validazione in `ScanActivity.java`:
```java
if (spotNumber < 1 || spotNumber > 100) // Cambia 100 con il numero desiderato
```

### Modificare Colori Aree
Modifica `activity_scan.xml` e `ScanActivity.java` per aggiungere/modificare le aree.

## Troubleshooting

### Errore: "Permesso camera negato"
Soluzione: Vai in Impostazioni → App → Parking QR Manager → Permessi → Abilita Camera

### Errore: "Impossibile esportare CSV"
Soluzione: Abilita permessi di scrittura nelle impostazioni dell'app

### Scanner non funziona
Soluzione: 
- Verifica che la camera funzioni
- Controlla i permessi
- Riavvia l'app

### Errore di build "ZXing not found"
Soluzione: Verifica che il file `build.gradle` includa le dipendenze ZXing e sincronizza il progetto

## Supporto
Per problemi o domande, contatta il team di sviluppo.

## Licenza
Proprietario: [Nome Azienda]
Tutti i diritti riservati.

## Versione
v1.0 - Data: 2025
