# 🐧 GUIDA GIT PER DEBIAN - Carica su GitHub in 5 minuti

## ✅ PERFETTO! Usi Debian - è ancora più facile!

Con Git da terminale carichi TUTTO il progetto (cartelle incluse) in 1 minuto.

---

## 📋 PREREQUISITI

### Controlla se hai Git installato:
```bash
git --version
```

**Se vedi la versione (es: git version 2.x.x):**
✅ Git già installato → Vai al PASSO 1

**Se dice "command not found":**
❌ Installa Git prima:

```bash
sudo apt update
sudo apt install git -y
```

Inserisci password sudo e attendi 30 secondi.

✅ Git installato!

---

## 🚀 PASSO 1: Crea Account e Repository su GitHub

### 1.1 - Account GitHub (se non ce l'hai)
1. Vai su: https://github.com/signup
2. Crea account (2 minuti)
3. Verifica email

### 1.2 - Crea Repository
1. Login su https://github.com
2. Clicca **"New"** (pulsante verde)
3. Compila:
   - **Repository name:** `parking-qr-manager`
   - **Visibility:** ✅ **Public** (IMPORTANTE!)
   - **Initialize:** ❌ NON selezionare nulla
4. Clicca **"Create repository"**

✅ Repository creato!

**Lascia la pagina aperta** - ti servirà l'URL del repository.

---

## 📁 PASSO 2: Estrai il Progetto

Sul tuo PC Debian:

```bash
# Vai nella tua home (o dove vuoi)
cd ~

# Estrai il file ZIP (modifica il path se è altrove)
unzip ~/Downloads/ParkingApp_GitHub_Ready.zip

# Entra nella cartella
cd ParkingApp
```

✅ Progetto estratto!

---

## 🔧 PASSO 3: Configura Git (solo la prima volta)

**Solo se è la prima volta che usi Git su questo PC:**

```bash
# Imposta il tuo nome (apparirà nei commit)
git config --global user.name "Il Tuo Nome"

# Imposta la tua email (usa la stessa di GitHub)
git config --global user.email "tua@email.com"
```

Esempio:
```bash
git config --global user.name "Mario Rossi"
git config --global user.email "mario.rossi@gmail.com"
```

✅ Git configurato!

---

## 📤 PASSO 4: Carica su GitHub (COMANDI PRONTI!)

### 4.1 - Inizializza repository locale

```bash
# Assicurati di essere nella cartella ParkingApp
cd ~/ParkingApp

# Inizializza Git
git init
```

Output: `Initialized empty Git repository...`

### 4.2 - Aggiungi tutti i file

```bash
# Aggiungi tutti i file (incluse cartelle)
git add .

# Verifica cosa verrà caricato (opzionale)
git status
```

Dovresti vedere tanti file "new file" in verde.

### 4.3 - Fai il primo commit

```bash
git commit -m "Initial commit - Parking QR Manager"
```

Output: tanti file creati

### 4.4 - Collega a GitHub

**⚠️ IMPORTANTE:** Sostituisci `TUO_USERNAME` con il tuo username GitHub!

```bash
# Collega al repository remoto
git remote add origin https://github.com/TUO_USERNAME/parking-qr-manager.git

# Verifica (opzionale)
git remote -v
```

**Esempio:**
Se il tuo username GitHub è `mario123`:
```bash
git remote add origin https://github.com/mario123/parking-qr-manager.git
```

### 4.5 - Carica tutto su GitHub! 🚀

```bash
# Rinomina branch in "main"
git branch -M main

# Carica!
git push -u origin main
```

**Ti chiederà:**
- **Username:** il tuo username GitHub
- **Password:** ⚠️ NON la password normale!
  
**Devi usare un Personal Access Token:**

#### Come ottenere il token:
1. Su GitHub → Clicca sulla tua foto (in alto a destra)
2. **Settings** → **Developer settings** (in fondo alla sidebar)
3. **Personal access tokens** → **Tokens (classic)**
4. **Generate new token** → **Generate new token (classic)**
5. **Note:** "Parking App Upload"
6. **Expiration:** 90 days
7. **Select scopes:** ✅ **repo** (seleziona solo questo)
8. Scroll in fondo → **Generate token**
9. **COPIA IL TOKEN** (lo vedi solo una volta!)
   - Formato: `ghp_xxxxxxxxxxxxxxxxxxxx`

#### Usa il token:
- **Username:** tuo_username
- **Password:** **incolla il token** (ghp_xxx...)

Premi Invio e...

🎉 **Caricamento completato!**

---

## ⚡ PASSO 5: GitHub Compila Automaticamente

### 5.1 - Vai su GitHub
1. Vai al tuo repository: `https://github.com/TUO_USERNAME/parking-qr-manager`
2. Clicca sulla tab **"Actions"**

### 5.2 - Guarda la magia! ✨
- Vedrai **"Build Android APK"** in esecuzione
- 🟡 Punto giallo = compilazione in corso (3-5 minuti)
- ✅ Spunta verde = completato!

☕ Prendi un caffè, in 5 minuti è pronto!

---

## 📥 PASSO 6: Scarica l'APK

Quando il workflow è verde ✅:

1. Clicca sul workflow completato
2. Scroll in basso fino a **"Artifacts"**
3. Clicca **"app-debug"**
4. Download! 📱

**🎉 HAI IL TUO APK!**

---

## 🔄 MODIFICHE FUTURE (Bonus)

Quando modifichi qualcosa:

```bash
# Vai nella cartella del progetto
cd ~/ParkingApp

# Aggiungi le modifiche
git add .

# Commit
git commit -m "Descrizione della modifica"

# Carica
git push
```

GitHub ricompila automaticamente! 🎊

---

## 📋 COMANDI COMPLETI - COPIA/INCOLLA

**Per comodità, ecco tutti i comandi in sequenza:**

```bash
# 1. Estrai progetto
cd ~
unzip ~/Downloads/ParkingApp_GitHub_Ready.zip
cd ParkingApp

# 2. Configura Git (solo prima volta)
git config --global user.name "Il Tuo Nome"
git config --global user.email "tua@email.com"

# 3. Inizializza e carica
git init
git add .
git commit -m "Initial commit - Parking QR Manager"
git remote add origin https://github.com/TUO_USERNAME/parking-qr-manager.git
git branch -M main
git push -u origin main
```

**Ricorda:** Sostituisci `TUO_USERNAME` con il tuo username GitHub!

---

## 🆘 ERRORI COMUNI

### ❌ "fatal: remote origin already exists"
**Soluzione:**
```bash
git remote remove origin
git remote add origin https://github.com/TUO_USERNAME/parking-qr-manager.git
```

### ❌ "Authentication failed"
**Causa:** Password normale non funziona, serve token

**Soluzione:** 
1. Genera Personal Access Token su GitHub (vedi sopra)
2. Usa il token come password

### ❌ "fatal: not a git repository"
**Soluzione:**
```bash
# Assicurati di essere nella cartella giusta
cd ~/ParkingApp
# Poi riprova git init
```

### ❌ "Permission denied (publickey)"
**Soluzione:** Usa HTTPS invece di SSH
```bash
git remote set-url origin https://github.com/TUO_USERNAME/parking-qr-manager.git
```

---

## 💡 TIPS PER DEBIAN

### Salva le credenziali (per non riscriverle ogni volta)
```bash
# Dopo il primo push, esegui:
git config --global credential.helper store
```

La prossima volta che fai `git push`, salverà username e token!

### Verifica cosa stai per caricare
```bash
git status        # Vedi file modificati
git diff          # Vedi le modifiche nel dettaglio
```

### Ignora file temporanei
Il progetto ha già un `.gitignore`, ma se vuoi aggiungere:
```bash
echo "*.log" >> .gitignore
echo "temp/" >> .gitignore
```

---

## ✅ CHECKLIST COMPLETA

- [ ] Git installato (`git --version`)
- [ ] Account GitHub creato
- [ ] Repository "parking-qr-manager" creato (Public!)
- [ ] File ZIP estratto
- [ ] Git configurato (nome ed email)
- [ ] Personal Access Token generato
- [ ] Comandi eseguiti in sequenza
- [ ] Push completato con successo
- [ ] GitHub Actions in esecuzione (tab Actions)
- [ ] APK scaricato
- [ ] APK installato su Android

---

## 🎯 VANTAGGI USARE GIT

| Vantaggio | Spiegazione |
|-----------|-------------|
| 📁 **Cartelle incluse** | Carica tutto in un colpo |
| ⚡ **Veloce** | 1 minuto per caricare |
| 🔄 **Versioning** | Storico di tutte le modifiche |
| 🌳 **Branching** | Testa modifiche senza rischi |
| 🤝 **Collaboration** | Altri possono contribuire |

---

## 📊 CONFRONTO METODI

| Metodo | Tempo | Difficoltà | Cartelle |
|--------|-------|------------|----------|
| Web UI | 15+ min | Media | ❌ No |
| **Git CLI** | **1 min** | **Facile** | **✅ Si** |
| GitHub Desktop | 5 min | Facile | ✅ Si |

**Git CLI è il migliore per Linux! 🐧**

---

## 🎓 COMANDI GIT UTILI

```bash
# Vedi lo stato
git status

# Vedi la history
git log --oneline

# Crea un nuovo branch
git checkout -b feature-nuova

# Torna al branch main
git checkout main

# Scarica ultimi aggiornamenti
git pull

# Vedi remote configurati
git remote -v

# Rimuovi file dal tracciamento
git rm --cached nomefile
```

---

## 🚀 WORKFLOW COMPLETO

```
1. Modifichi file in ~/ParkingApp
         ↓
2. git add .
         ↓
3. git commit -m "descrizione"
         ↓
4. git push
         ↓
5. GitHub Actions compila automaticamente
         ↓
6. Nuovo APK pronto in 5 minuti! 🎉
```

---

## 🎊 RISULTATO FINALE

Dopo aver seguito questa guida avrai:

✅ Progetto caricato su GitHub (cartelle incluse!)
✅ GitHub Actions che compila automaticamente
✅ APK Android pronto da scaricare
✅ Sistema di versioning attivo
✅ Possibilità di modificare e ricompilare facilmente

**TUTTO in ~10 minuti con Git! 🚀**

---

## 📞 SUPPORTO

**Problemi con Git?**
```bash
# Ricomincia da capo
cd ~
rm -rf ParkingApp
unzip ParkingApp_GitHub_Ready.zip
cd ParkingApp
# Riprova i comandi
```

**Problemi con GitHub?**
- Verifica che il repository sia Public
- Verifica di aver generato il token correttamente
- Controlla tab Actions dopo il push

---

## 🎯 PROSSIMI PASSI

1. **Ora:** Esegui i comandi sopra
2. **Poi:** Attendi compilazione GitHub (5 min)
3. **Infine:** Scarica APK e installa
4. **Bonus:** Impara altri comandi Git per gestire il progetto

---

**Pronto? Copia i comandi e inizia! 🐧🚀**

```bash
cd ~
unzip ~/Downloads/ParkingApp_GitHub_Ready.zip
cd ParkingApp
git init
git add .
git commit -m "Initial commit"
# ... (continua con gli altri comandi)
```

**Buon upload! 🎉**
