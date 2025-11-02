# 🚀 COMPILA APK CON VSCODE (SOLUZIONE PIÙ SEMPLICE!)

## ✅ PERCHÉ VSCODE È MEGLIO

| VSCode | Android Studio |
|--------|----------------|
| ⚡ **500 MB** download | 😰 3+ GB download |
| 🚀 **Veloce** e leggero | 🐌 Lento e pesante |
| 💻 Funziona su **PC normali** | 🔥 Richiede PC potente |
| ⏱️ Setup in **10 minuti** | 😴 Setup in 45+ minuti |
| 🎯 **1 click** per compilare | 🤯 Menu complicati |
| ✅ Già usato da sviluppatori | ❓ Da imparare da zero |

**VSCode è la scelta MIGLIORE se vuoi solo compilare l'APK!** 🎉

---

## 📋 COSA TI SERVE

### ✅ Da scaricare (LEGGERO):
1. **VSCode** (già ce l'hai? Skip!)
2. **JDK 17** (~200 MB)
3. **Android Command Line Tools** (~150 MB)

### ❌ NON ti serve:
- ❌ Android Studio (3+ GB)
- ❌ Android Emulator
- ❌ IDE pesanti

**Totale download: ~500 MB invece di 3+ GB!** 🎊

---

## 🎯 PROCEDURA COMPLETA (20 MINUTI)

### ⏱️ Timeline:
```
PASSO 1 (5 min):  Installa VSCode
PASSO 2 (8 min):  Installa JDK 17
PASSO 3 (5 min):  Installa Android SDK
PASSO 4 (2 min):  Apri progetto in VSCode
PASSO 5 (3 min):  Compila APK (1 CLICK!)
───────────────────────────────────────
TOTALE: 23 minuti (prima volta)
        30 secondi (successive volte)
```

---

## 🔧 PASSO 1: INSTALLA VSCODE (5 minuti)

### Se hai già VSCode:
✅ **Passa al Passo 2!**

### Se NON hai VSCode:

#### Windows:
1. Vai su: https://code.visualstudio.com/
2. Clicca **"Download for Windows"**
3. Esegui installer
4. **IMPORTANTE:** Seleziona tutte le opzioni durante installazione

#### Mac:
1. Vai su: https://code.visualstudio.com/
2. Clicca **"Download for Mac"**
3. Apri .dmg e trascina VSCode in Applications

#### Linux:
```bash
# Debian/Ubuntu
sudo snap install code --classic

# Fedora/RHEL
sudo rpm --import https://packages.microsoft.com/keys/microsoft.asc
sudo sh -c 'echo -e "[code]\nname=Visual Studio Code\nbaseurl=https://packages.microsoft.com/yumrepos/vscode\nenabled=1\ngpgcheck=1\ngpgkey=https://packages.microsoft.com/keys/microsoft.asc" > /etc/yum.repos.d/vscode.repo'
sudo dnf install code
```

✅ **VSCode installato!**

---

## ☕ PASSO 2: INSTALLA JDK 17 (8 minuti)

Il JDK è necessario per compilare app Android.

### Windows:

#### Opzione A: Microsoft Build (CONSIGLIATA)
1. Vai su: https://learn.microsoft.com/java/openjdk/download
2. Scarica **"Microsoft Build of OpenJDK 17"** (Windows x64)
3. Esegui installer (.msi)
4. Segui procedura guidata
5. ✅ Fatto! (installer configura tutto automaticamente)

#### Opzione B: Manuale
1. Vai su: https://adoptium.net/
2. Clicca **"Download"**
3. Seleziona:
   - Version: **17 - LTS**
   - Operating System: **Windows**
   - Architecture: **x64**
4. Scarica installer (.msi)
5. Esegui e installa
6. **Durante installazione:** Seleziona "Add to PATH" ✅

### Mac:
```bash
# Usa Homebrew (più semplice)
brew install openjdk@17

# Configura PATH
echo 'export PATH="/usr/local/opt/openjdk@17/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc
```

### Linux:
```bash
# Debian/Ubuntu
sudo apt update
sudo apt install openjdk-17-jdk

# Fedora/RHEL
sudo dnf install java-17-openjdk-devel
```

### Verifica installazione:
Apri terminale e digita:
```bash
java -version
```

Dovresti vedere: `openjdk version "17.x.x"`

✅ **JDK 17 installato!**

---

## 📱 PASSO 3: INSTALLA ANDROID SDK (5 minuti)

### Windows:

1. **Crea cartella per Android SDK:**
   ```
   C:\Android\sdk
   ```

2. **Scarica Command Line Tools:**
   - Vai: https://developer.android.com/studio#command-line-tools-only
   - Scorri fino a "Command line tools only"
   - Scarica versione **Windows**
   - File: `commandlinetools-win-XXXX_latest.zip`

3. **Estrai:**
   - Estrai ZIP in `C:\Android\sdk\cmdline-tools\latest\`
   - Struttura finale:
     ```
     C:\Android\sdk\
     └── cmdline-tools\
         └── latest\
             ├── bin\
             ├── lib\
             └── ...
     ```

4. **Configura variabili ambiente:**
   - Tasto destro su "Questo PC" → Proprietà
   - "Impostazioni di sistema avanzate"
   - "Variabili d'ambiente"
   - Nella sezione "Variabili di sistema", clicca "Nuova":
     - Nome: `ANDROID_HOME`
     - Valore: `C:\Android\sdk`
   - Modifica variabile `Path`, aggiungi:
     - `C:\Android\sdk\cmdline-tools\latest\bin`
     - `C:\Android\sdk\platform-tools`

5. **Installa SDK necessari:**
   Apri PowerShell/CMD e esegui:
   ```bash
   sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0"
   ```
   - Accetta le licenze digitando `y`

### Mac:
```bash
# Crea directory
mkdir -p ~/Android/sdk/cmdline-tools

# Scarica command line tools
cd ~/Downloads
curl -O https://dl.google.com/android/repository/commandlinetools-mac-11076708_latest.zip

# Estrai
unzip commandlinetools-mac-*_latest.zip -d ~/Android/sdk/cmdline-tools/
mv ~/Android/sdk/cmdline-tools/cmdline-tools ~/Android/sdk/cmdline-tools/latest

# Configura variabili ambiente
echo 'export ANDROID_HOME=$HOME/Android/sdk' >> ~/.zshrc
echo 'export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin' >> ~/.zshrc
echo 'export PATH=$PATH:$ANDROID_HOME/platform-tools' >> ~/.zshrc
source ~/.zshrc

# Installa SDK
sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0"
```

### Linux:
```bash
# Crea directory
mkdir -p ~/Android/sdk/cmdline-tools

# Scarica command line tools
cd ~/Downloads
wget https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip

# Estrai
unzip commandlinetools-linux-*_latest.zip -d ~/Android/sdk/cmdline-tools/
mv ~/Android/sdk/cmdline-tools/cmdline-tools ~/Android/sdk/cmdline-tools/latest

# Configura variabili ambiente
echo 'export ANDROID_HOME=$HOME/Android/sdk' >> ~/.bashrc
echo 'export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin' >> ~/.bashrc
echo 'export PATH=$PATH:$ANDROID_HOME/platform-tools' >> ~/.bashrc
source ~/.bashrc

# Installa SDK
sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0"
```

### Verifica installazione:
```bash
sdkmanager --list
```

Dovresti vedere una lista di pacchetti disponibili.

✅ **Android SDK installato!**

---

## 📂 PASSO 4: APRI PROGETTO IN VSCODE (2 minuti)

1. **Estrai il progetto:**
   - Estrai `ParkingApp_VSCode_Ready.zip` sul tuo PC
   - Esempio: `C:\Projects\ParkingApp\`

2. **Apri VSCode:**
   - Avvia VSCode
   - File → Open Folder
   - Seleziona la cartella `ParkingApp`

3. **Installa estensioni (OPZIONALI ma utili):**
   - Premi `Ctrl+Shift+X` (o `Cmd+Shift+X` su Mac)
   - Cerca e installa:
     - **"Extension Pack for Java"** (Microsoft)
     - **"Gradle for Java"** (Microsoft)
     - **"Android iOS Emulator"** (Didin)

✅ **Progetto aperto in VSCode!**

---

## 🔨 PASSO 5: COMPILA APK (1 CLICK!) (3 minuti)

### Metodo 1: Task VSCode (PIÙ SEMPLICE!)

1. **Apri Command Palette:**
   - Premi `Ctrl+Shift+P` (Windows/Linux)
   - Oppure `Cmd+Shift+P` (Mac)

2. **Digita:** `Tasks: Run Task`

3. **Seleziona:** `🔨 Build APK Debug`

4. **Attendi compilazione** (2-3 minuti la prima volta)

5. **APK pronto!** 🎉
   - Percorso: `app/build/outputs/apk/debug/app-debug.apk`

### Metodo 2: Terminale VSCode

1. **Apri terminale integrato:**
   - Premi `` Ctrl+` `` (backtick)
   - Oppure: Terminal → New Terminal

2. **Compila:**
   ```bash
   # Windows
   .\gradlew assembleDebug

   # Mac/Linux
   ./gradlew assembleDebug
   ```

3. **Attendi** (~2-3 minuti)

4. **APK pronto!**

### Trova il tuo APK:

**Metodo rapido:**
- Command Palette (`Ctrl+Shift+P`)
- Digita: `Tasks: Run Task`
- Seleziona: `📂 Open APK Folder`
- Si apre la cartella con l'APK! 🎊

**Percorso manuale:**
```
ParkingApp/
└── app/
    └── build/
        └── outputs/
            └── apk/
                └── debug/
                    └── app-debug.apk  ← QUESTO È IL TUO APK!
```

✅ **HAI COMPILATO L'APK! 🎉**

---

## 🎯 TASK VSCODE DISPONIBILI

Dopo aver aperto il progetto, hai questi task pronti:

### 🔨 Build APK Debug
**Uso più comune!**
- Compila APK per test
- 1 click per compilare
- APK pronto in 2-3 minuti

### 🚀 Build APK Release
Per produzione (richiede firma)

### 🧹 Clean Build
Pulisce file temporanei

### 🔄 Clean + Build APK
Pulisce e ricompila tutto

### 📱 Install on Device
Installa direttamente su Android collegato via USB

### 📂 Open APK Folder
Apre cartella con APK

---

## ⚡ COMPILAZIONI SUCCESSIVE (30 secondi!)

**Prima compilazione:** 2-3 minuti (scarica dipendenze)

**Compilazioni successive:** 30-60 secondi! ⚡

**Per ricompilare:**
1. `Ctrl+Shift+P`
2. `Tasks: Run Task`
3. `🔨 Build APK Debug`
4. ☕ Attendi 30 secondi
5. ✅ Nuovo APK pronto!

**OPPURE usa shortcut:**
- `Ctrl+Shift+B` (Windows/Linux)
- `Cmd+Shift+B` (Mac)
- Compila immediatamente!

---

## 📱 INSTALLA APK SU ANDROID

### Via USB (più veloce):

1. **Collega smartphone** via USB
2. **Abilita Debug USB** su Android:
   - Impostazioni → Info telefono
   - Tocca "Numero build" 7 volte
   - Torna indietro → Opzioni sviluppatore
   - Attiva "Debug USB"
3. **In VSCode:**
   - `Ctrl+Shift+P`
   - `Tasks: Run Task`
   - `📱 Install on Device`
4. ✅ App installata!

### Via file APK:

1. **Copia APK** su smartphone
2. **Abilita "Sorgenti sconosciute"**
3. **Apri file** e installa

---

## 🎨 PERSONALIZZA VSCODE

### Temi consigliati:
- **One Dark Pro** (scuro, elegante)
- **Material Theme** (colorato)
- **Dracula** (viola scuro)

### Installa: 
`Ctrl+Shift+X` → cerca tema → Install

### Shortcut utili:

| Shortcut | Azione |
|----------|--------|
| `Ctrl+Shift+P` | Command Palette |
| `Ctrl+Shift+B` | Build APK |
| `` Ctrl+` `` | Apri terminale |
| `Ctrl+P` | Cerca file |
| `Ctrl+F` | Cerca nel file |
| `Ctrl+Shift+F` | Cerca nel progetto |

---

## 🆘 RISOLUZIONE PROBLEMI

### ❌ "JAVA_HOME is not set"

**Soluzione Windows:**
1. Tasto destro "Questo PC" → Proprietà
2. Impostazioni di sistema avanzate
3. Variabili d'ambiente
4. Nuova variabile di sistema:
   - Nome: `JAVA_HOME`
   - Valore: `C:\Program Files\Microsoft\jdk-17.x.x` (percorso JDK)

**Soluzione Mac/Linux:**
```bash
# Trova percorso Java
/usr/libexec/java_home -V

# Aggiungi al .zshrc o .bashrc
echo 'export JAVA_HOME=$(/usr/libexec/java_home -v 17)' >> ~/.zshrc
source ~/.zshrc
```

### ❌ "SDK location not found"

**Soluzione:**
Crea file `local.properties` nella root del progetto:
```properties
sdk.dir=C:\\Android\\sdk
```
(Windows: usa `\\` doppio backslash)

Mac/Linux:
```properties
sdk.dir=/Users/TUONOME/Android/sdk
```

### ❌ "gradlew: Permission denied" (Mac/Linux)

**Soluzione:**
```bash
chmod +x gradlew
```

### ❌ Compilazione lenta

**Soluzioni:**
1. Aggiungi in `gradle.properties`:
   ```properties
   org.gradle.daemon=true
   org.gradle.parallel=true
   org.gradle.caching=true
   org.gradle.jvmargs=-Xmx4096m
   ```

2. Chiudi programmi pesanti

3. Disabilita antivirus temporaneamente

### ❌ "BUILD FAILED"

**Soluzione:**
1. Leggi errore nel terminale
2. Errori comuni:
   - SDK mancante → Reinstalla SDK (Passo 3)
   - JDK sbagliato → Usa JDK 17
   - Internet lento → Riprova (scarica dipendenze)

---

## 🎯 VANTAGGI VSCODE

### ✅ Pro:
- ⚡ **Velocissimo** (compila in 30 sec dopo prima volta)
- 💻 **Leggero** (500 MB vs 3+ GB Android Studio)
- 🎯 **1 click** per compilare (`Ctrl+Shift+B`)
- 🔧 **Già lo conosci** (se usi VSCode)
- 🚀 **Apre velocemente** (2 secondi vs 30+ sec)
- 💰 **PC normale** basta (non serve workstation)

### 😐 Contro (minori):
- ❌ No designer visuale (ma non serve per questo progetto)
- ❌ No emulator integrato (ma APK funziona su telefono reale)

**Per questo progetto, VSCode è PERFETTO!** 🎯

---

## 📊 CONFRONTO SOLUZIONI

| Soluzione | Tempo Setup | Difficoltà | Download |
|-----------|-------------|------------|----------|
| 🏆 **VSCode** | 20 min | ⭐⭐ Facile | 500 MB |
| GitHub Actions | 15 min | ⭐ Facilissimo | 0 MB |
| Android Studio | 45 min | ⭐⭐⭐⭐ Difficile | 3+ GB |

**Consiglio:**
- 🥇 **VSCode**: Se hai PC normale e vuoi modificare
- 🥈 **GitHub**: Se vuoi solo APK velocemente
- 🥉 **Android Studio**: Solo se sviluppi app professionalmente

---

## 🎓 COMANDI GRADLE UTILI

```bash
# Compila debug
./gradlew assembleDebug

# Compila release
./gradlew assembleRelease

# Pulisci build
./gradlew clean

# Installa su device
./gradlew installDebug

# Mostra tasks disponibili
./gradlew tasks

# Mostra dipendenze
./gradlew dependencies

# Test
./gradlew test
```

---

## ✅ CHECKLIST SETUP

Prima di compilare, verifica:

- [ ] VSCode installato
- [ ] JDK 17 installato
- [ ] `java -version` funziona
- [ ] Android SDK installato
- [ ] `sdkmanager --list` funziona
- [ ] ANDROID_HOME configurato
- [ ] JAVA_HOME configurato
- [ ] Progetto aperto in VSCode
- [ ] File `local.properties` creato (se necessario)

**Tutto ok? Compila con `Ctrl+Shift+B`! 🎉**

---

## 🚀 WORKFLOW QUOTIDIANO

**Modifichi il codice:**
1. Apri VSCode
2. Modifica file Java/XML
3. Salva (`Ctrl+S`)
4. `Ctrl+Shift+B` → Compila
5. Attendi 30 secondi
6. Nuovo APK pronto!

**Tempo:** 30 secondi dalla modifica all'APK! ⚡

---

## 🎊 RISULTATO FINALE

Avrai:
- ✅ Setup leggero (~500 MB)
- ✅ Compilazione velocissima (30 sec)
- ✅ Ambiente di sviluppo professionale
- ✅ 1 click per compilare APK
- ✅ Possibilità di modificare codice
- ✅ Tutto in VSCode (già lo conosci!)

**MOLTO MEGLIO di Android Studio! 🎯**

---

## 💡 TIPS PRO

### 🔥 Compilazione ULTRA-veloce:
Aggiungi in `gradle.properties`:
```properties
org.gradle.daemon=true
org.gradle.parallel=true
org.gradle.caching=true
org.gradle.configuration-cache=true
```

### 🎨 Temi Android per VSCode:
- Material Icon Theme (icone file)
- Bracket Pair Colorizer (parentesi colorate)

### ⚡ Shortcut custom:
File → Preferences → Keyboard Shortcuts
- Cerca "Tasks: Run Build Task"
- Imposta a `F5` per compilare ancora più veloce!

---

## 📞 SUPPORTO

**Problema con setup?**
→ Controlla sezione "Risoluzione Problemi"

**Compilazione fallisce?**
→ Leggi errore nel terminale, di solito è chiaro

**Serve aiuto?**
→ L'errore più comune è JDK/SDK non configurato correttamente

---

## 🏁 CONCLUSIONE

**VSCode è la soluzione MIGLIORE per questo progetto!**

- ✅ Più veloce di Android Studio
- ✅ Più semplice di GitHub Actions
- ✅ Più controllo che solo APK precompilato
- ✅ Ambiente professionale

**Inizia dal Passo 1 e in 20 minuti compili il tuo primo APK! 🚀**

---

**Pronto? Scarica il pacchetto VSCode Ready e inizia! 🎉**
