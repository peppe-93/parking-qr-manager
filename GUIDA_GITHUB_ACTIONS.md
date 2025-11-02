# 🚀 GUIDA COMPILAZIONE APK AUTOMATICA CON GITHUB

## 🎯 Panoramica

Con questa guida otterrai l'APK **AUTOMATICAMENTE** e **GRATIS** in ~5 minuti!

**Cosa fa GitHub Actions:**
- ✅ Compila l'APK automaticamente per te
- ✅ Completamente GRATIS (illimitato per progetti pubblici)
- ✅ APK pronto da scaricare in 3-5 minuti
- ✅ Ogni volta che carichi modifiche, ricompila automaticamente
- ✅ Nessun software da installare sul tuo PC

---

## 📋 PREREQUISITI (Semplici!)

### Cosa ti serve:
1. **Account GitHub** (gratis) - Se non ce l'hai: vai su https://github.com/signup
2. **I file del progetto** (già pronti nel ZIP)
3. **5 minuti di tempo**

### Non ti serve:
- ❌ Android Studio
- ❌ Java/JDK
- ❌ Gradle
- ❌ Competenze tecniche
- ❌ Software da installare

---

## 🎬 PASSO 1: Crea Account GitHub (se non ce l'hai)

### Se hai già GitHub → **SALTA AL PASSO 2**

1. Vai su: https://github.com/signup
2. Inserisci:
   - Email
   - Password
   - Username (es: "MarioRossi")
3. Verifica email
4. ✅ Account creato!

**Tempo:** 2 minuti

---

## 📁 PASSO 2: Crea Nuovo Repository

### 2.1 - Vai su GitHub

1. Fai login su https://github.com
2. Clicca sul **pulsante verde "New"** (in alto a sinistra)
   - Oppure vai direttamente: https://github.com/new

### 2.2 - Configura Repository

**Compila così:**

| Campo | Valore da inserire |
|-------|-------------------|
| Repository name | `parking-qr-manager` |
| Description | `App gestione parcheggio QR Code` |
| Visibilità | ✅ **Public** (IMPORTANTE per GitHub Actions gratis!) |
| Initialize | ❌ NON selezionare nulla |

3. Clicca **"Create repository"**

**✅ Repository creato!**

**Tempo:** 1 minuto

---

## 📤 PASSO 3: Carica i File del Progetto

### Hai 2 opzioni (scegli la più facile per te):

---

### **OPZIONE A: Caricamento via Web** (PIÙ SEMPLICE)

#### 3.1 - Estrai il file ZIP sul tuo PC
- Estrai `ParkingApp_Android_Complete_v2.zip`
- Avrai una cartella `ParkingApp/`

#### 3.2 - Nella pagina del repository su GitHub:

1. Clicca **"uploading an existing file"** (link nella pagina vuota)
2. **Trascina TUTTI i file** dalla cartella `ParkingApp/` nella finestra
   - Oppure clicca "choose your files" e selezionali
3. Aspetta il caricamento (può richiedere 1-2 minuti)
4. In basso, scrivi:
   - Commit message: `Initial commit`
5. Clicca **"Commit changes"**

**⚠️ IMPORTANTE:** Carica TUTTI i file e cartelle, incluse:
- `.github/workflows/build.yml` (FONDAMENTALE!)
- `app/`
- `gradle/`
- `gradlew`
- `gradlew.bat`
- `build.gradle`
- `settings.gradle`
- Tutti gli altri file

**✅ File caricati!**

---

### **OPZIONE B: Caricamento via Git** (Se conosci Git)

```bash
# 1. Vai nella cartella ParkingApp estratta
cd ParkingApp

# 2. Inizializza Git
git init

# 3. Aggiungi tutti i file
git add .

# 4. Fai il primo commit
git commit -m "Initial commit"

# 5. Collega al repository (sostituisci TUO_USERNAME)
git remote add origin https://github.com/TUO_USERNAME/parking-qr-manager.git

# 6. Carica
git branch -M main
git push -u origin main
```

**✅ File caricati!**

**Tempo:** 3-5 minuti (caricamento dipende dalla velocità internet)

---

## ⚡ PASSO 4: GitHub Actions Compila Automaticamente!

### 4.1 - Controlla che GitHub Actions sia attivo

1. Nel repository, clicca sulla tab **"Actions"** (in alto)
2. Dovresti vedere un workflow chiamato **"Build Android APK"** in esecuzione

**Se vedi:**
- 🟡 **Punto giallo** = Compilazione in corso (3-5 minuti)
- 🟢 **Spunta verde** = Compilazione completata! ✅
- 🔴 **X rossa** = Errore (vai alla sezione Risoluzione Problemi)

### 4.2 - Attendi la compilazione

**⏱️ Tempo di compilazione:** ~3-5 minuti

Puoi:
- Cliccare sul workflow per vedere i progressi in tempo reale
- Chiudere la pagina e tornare dopo 5 minuti

**Cosa sta facendo GitHub:**
1. Prepara ambiente Linux
2. Installa Java JDK
3. Scarica Android SDK
4. Compila il progetto
5. Genera APK
6. Carica APK come artifact

---

## 📥 PASSO 5: Scarica il Tuo APK!

### Metodo 1: Download da Actions (SEMPRE disponibile)

1. Vai nella tab **"Actions"**
2. Clicca sul workflow completato (spunta verde ✅)
3. Scrolla in basso fino a **"Artifacts"**
4. Clicca su **"app-debug"** per scaricare

**📱 Hai il tuo APK!** → File: `app-debug.apk`

---

### Metodo 2: Download da Releases (se configurato)

1. Vai nella tab **"Releases"** (lato destro)
2. Clicca sulla release più recente
3. In "Assets", clicca su `app-debug.apk`

**📱 Hai il tuo APK!**

---

## 📲 PASSO 6: Installa APK su Android

### 6.1 - Trasferisci APK su smartphone

**Opzioni:**
- Via USB: collega telefono e copia file
- Via email: invia a te stesso
- Via cloud: carica su Drive/Dropbox e scarica su telefono
- Via WhatsApp: invia a te stesso

### 6.2 - Abilita installazione da fonti sconosciute

**Su Android:**
1. Impostazioni → Sicurezza
2. Attiva **"Installa app sconosciute"** o **"Sorgenti sconosciute"**
   - Oppure attiva solo per l'app che userai per installare (es: File Manager)

### 6.3 - Installa

1. Apri il file `app-debug.apk` sul telefono
2. Tocca **"Installa"**
3. Attendi installazione
4. Tocca **"Apri"**

**🎉 APP INSTALLATA E FUNZIONANTE!**

---

## 🔄 BONUS: Modifiche e Ricompilazione Automatica

### Ogni volta che modifichi qualcosa:

1. Carica i file modificati su GitHub
2. GitHub Actions **ricompila automaticamente**
3. Nuovo APK disponibile in 5 minuti

**È GRATIS e ILLIMITATO!**

---

## ⏱️ RIEPILOGO TEMPI

| Passo | Tempo |
|-------|-------|
| 1. Account GitHub | 2 min (se non ce l'hai) |
| 2. Crea repository | 1 min |
| 3. Carica file | 3-5 min |
| 4. Compilazione automatica | 3-5 min |
| 5. Download APK | 30 sec |
| 6. Installa su Android | 1 min |
| **TOTALE** | **~10-15 minuti** |

---

## 🎯 DIAGRAMMA VISUALE PROCESSO

```
TU                           GITHUB ACTIONS                   RISULTATO
│                                  │                               │
│  1. Carica file progetto         │                               │
├─────────────────────────────────>│                               │
│                                  │                               │
│                                  │  2. Compila automaticamente   │
│                                  ├───────────────────────────────>│
│                                  │     - Installa Android SDK    │
│                                  │     - Compila codice Java     │
│                                  │     - Genera APK              │
│                                  │                               │
│  3. Scarica APK                  │                               │
│<─────────────────────────────────┴───────────────────────────────┤
│                                                                  │
│  4. Installa su Android                                          │
├─────────────────────────────────────────────────────────────────>│
│                                                          APP FUNZIONANTE! 🎉
```

---

## 🆘 RISOLUZIONE PROBLEMI

### ❌ Errore: "Workflow file not found"

**Causa:** File `.github/workflows/build.yml` non caricato

**Soluzione:**
1. Verifica di aver caricato la cartella `.github`
2. La struttura deve essere: `.github/workflows/build.yml`
3. Ricarica il file se manca

---

### ❌ Compilazione fallita (X rossa)

**Soluzione:**
1. Clicca sul workflow fallito
2. Leggi l'errore
3. Cause comuni:
   - File mancanti → Ricarica tutti i file
   - Errori nel codice → Controlla file Java
   - Permessi → Repository deve essere Public

---

### ❌ "Actions disabled for this repository"

**Soluzione:**
1. Repository → Settings
2. Sidebar → Actions → General
3. Seleziona **"Allow all actions and reusable workflows"**
4. Salva

---

### ❌ APK non si installa su Android

**Causa:** Sorgenti sconosciute non abilitate

**Soluzione:**
1. Impostazioni → Sicurezza
2. Attiva "Sorgenti sconosciute"
3. Riprova installazione

---

### ❌ "Repository must be public"

**Causa:** Repository privato (GitHub Actions limitato)

**Soluzione:**
1. Repository → Settings
2. Scroll in fondo
3. Danger Zone → Change visibility → Make public

---

## 🎓 CONSIGLI PRO

### 🔐 Firma APK (per pubblicazione Play Store)

Il workflow attuale genera APK debug (per test).
Per la produzione:
1. Genera keystore
2. Aggiungi secrets su GitHub
3. Modifica workflow per `assembleRelease`

### 🏷️ Versioning Automatico

Il workflow crea release con tag automatici:
- `v1.0-1`, `v1.0-2`, etc.
- Ogni push = nuova versione

### 📊 Badge Status

Aggiungi al README.md:
```markdown
![Build Status](https://github.com/TUO_USERNAME/parking-qr-manager/workflows/Build%20Android%20APK/badge.svg)
```

Mostra lo stato della build in tempo reale!

---

## 🎉 VANTAGGI SOLUZIONE GITHUB ACTIONS

| Vantaggio | Dettaglio |
|-----------|-----------|
| ✅ Gratis | Illimitato per progetti pubblici |
| ✅ Automatico | Compila ad ogni modifica |
| ✅ Cloud | Nessun software locale |
| ✅ Veloce | 3-5 minuti compilazione |
| ✅ Affidabile | Infrastruttura GitHub |
| ✅ Storico | Tutti gli APK salvati |
| ✅ Sharing | Link diretto per condividere |

---

## 📞 SERVE AIUTO?

### Problemi comuni:

**"Non riesco a caricare i file"**
→ Usa drag & drop nella pagina web GitHub

**"Actions non parte"**
→ Verifica che il file `.github/workflows/build.yml` sia presente

**"Compilazione fallisce"**
→ Controlla i log nella tab Actions → clicca sul workflow fallito

**"APK non funziona"**
→ Assicurati di aver caricato TUTTI i file del progetto

---

## ✅ CHECKLIST FINALE

Prima di iniziare, assicurati di avere:

- [ ] Account GitHub (o crealo al Passo 1)
- [ ] File ZIP estratto sul PC
- [ ] Connessione internet stabile
- [ ] 10-15 minuti di tempo
- [ ] Smartphone Android per testare

**Sei pronto? INIZIA DAL PASSO 1! 🚀**

---

## 🎯 RISULTATO FINALE

Alla fine di questa guida avrai:

✅ Repository GitHub con il tuo progetto
✅ Sistema di compilazione automatica attivo
✅ APK Android scaricabile
✅ App installata e funzionante sul telefono
✅ Possibilità di modificare e ricompilare automaticamente

**TUTTO GRATIS e senza installare NULLA sul tuo PC!**

---

## 📝 NOTE IMPORTANTI

1. **Repository pubblico:** Necessario per GitHub Actions gratis
2. **Tempo compilazione:** 3-5 minuti per ogni build
3. **Limiti:** 2000 minuti/mese gratis (più che sufficienti)
4. **APK Debug:** Per test, non per Play Store (senza firma release)
5. **Aggiornamenti:** Ad ogni push, nuova build automatica

---

**INIZIA ADESSO! Vai al PASSO 1! 🎊**
