# GUIDA STAMPA E SALVATAGGIO QR CODE

## 🎯 Panoramica Nuove Funzionalità

L'app offre ora **3 modalità** per generare e stampare QR Code:

### 1️⃣ **Generazione Singola**
- Genera 1 QR Code alla volta
- Visualizzazione immediata
- Salvataggio in Galleria
- Condivisione diretta

### 2️⃣ **Generazione in Blocco**
- Genera fino a 100 QR Code contemporaneamente
- Export automatico in PDF formato A4
- 6 QR Code per pagina (2 colonne x 3 righe)
- Pronto per la stampa

### 3️⃣ **Condivisione Flessibile**
- Condividi via WhatsApp, Email, Drive, etc.
- Stampa diretta da smartphone
- Trasferimento a PC per stampa professionale

---

## 📱 MODALITÀ 1: Generazione Singola

### Come Usare:

1. **Apri app** → "Genera QR Code"
2. **Seleziona azienda** (o inserisci nome personalizzato)
3. **Premi** "📱 Genera Singolo QR Code"
4. Il QR Code appare sullo schermo con il codice sotto

### Opzioni Disponibili:

#### 💾 **Salva**
- Salva il QR Code come immagine PNG
- Viene salvato nella cartella Galleria/QRCodes
- Nome file: `QR_AZIENDA_CODICE_DATA.png`
- Risoluzione: 600x700 pixel (ottima per stampa)

**Quando usare:**
- Vuoi stampare 1 solo QR Code
- Vuoi modificare l'immagine prima di stampare
- Vuoi conservare il QR in galleria

#### 📤 **Condividi**
- Condividi immediatamente il QR Code
- Puoi inviarlo via: WhatsApp, Email, Google Drive, etc.
- Stampare direttamente da smartphone (se hai stampante wireless)

**Quando usare:**
- Devi inviare QR a qualcun altro
- Vuoi stampare immediatamente
- Devi trasferire il QR su PC

### Esempio Workflow Singolo:

```
Scenario: Devi generare QR per 1 auto nuova

1. Genera QR Code → appare sullo schermo
2. Premi "Salva" → salvato in Galleria
3. Apri Galleria → trova immagine QR
4. Stampa direttamente o trasferisci su PC
5. Stampa su etichetta adesiva
6. Applica su veicolo
```

---

## 📋 MODALITÀ 2: Generazione in Blocco (PDF)

### Come Usare:

1. **Apri app** → "Genera QR Code"
2. **Seleziona azienda**
3. **Premi** "📋 Genera QR Code in Blocco"
4. **Inserisci quantità** (es: 30)
5. **Premi** "⚡ Genera QR Codes"
6. Attendi (circa 3-10 secondi per 30 QR)
7. **Premi** "📄 Esporta PDF Stampabile"
8. PDF creato automaticamente!

### Caratteristiche PDF:

**Layout:**
- Formato A4 (595 x 842 punti)
- 6 QR Code per pagina (2 colonne × 3 righe)
- Margini: 40pt su tutti i lati
- Spaziatura tra QR: 15pt

**Contenuto QR Code:**
- QR Code: 240 x 240 punti
- Testo sotto QR: 3 righe con codice
- Font: Bold, dimensione 14

**Footer Pagina:**
- Numero pagina (es: "Pagina 1 di 5")
- Nome azienda
- Data e ora generazione

**Esempio Output:**
```
Per 30 QR Codes:
- Totale: 5 pagine (30 ÷ 6 = 5 pagine)
- Pagina 1: 6 QR Code
- Pagina 2: 6 QR Code
- Pagina 3: 6 QR Code
- Pagina 4: 6 QR Code
- Pagina 5: 6 QR Code
```

### Dove Viene Salvato il PDF:

**Percorso:** `/storage/emulated/0/Android/data/com.parking.qrmanager/files/Documents/QRCodes/`

**Nome file:** `QRCodes_NOMEAZIENDA_DATA_ORA.pdf`

**Esempio:** `QRCodes_AZIENDA_ALFA_20250130_143522.pdf`

### Opzioni Dopo Generazione:

#### ✅ Condividi PDF
- Email al reparto stampa
- Carica su Google Drive / Dropbox
- Invia via WhatsApp a stampante
- Trasferisci su PC via USB

#### ✅ Stampa Diretta
- Stampa wireless da smartphone
- Usa app stampante (HP Smart, Canon Print, etc.)
- Stampa tramite servizi cloud (Google Cloud Print)

### Esempio Workflow Batch:

```
Scenario: Nuova azienda con 50 veicoli

1. Genera QR Code in Blocco → inserisci "50"
2. Genera → attendi ~5 secondi
3. Esporta PDF → file creato (9 pagine, 50 QR)
4. Condividi PDF via Email al reparto stampa
5. Reparto stampa riceve PDF e stampa su A4
6. Ritaglia le etichette
7. Applica su 50 veicoli
```

---

## 🖨️ CONSIGLI PER LA STAMPA

### Stampa Singola (1 QR Code):

**Formato Consigliato:**
- Dimensione: 5x5 cm o 6x6 cm
- Materiale: Etichetta adesiva vinilica (resistente a intemperie)
- Qualità: 300 DPI minimo
- Colori: Bianco e nero (migliore contrasto)

**Strumenti:**
- Stampante laser o inkjet
- Etichette adesive pretagliate
- Plastificatrice (opzionale, per maggiore durata)

### Stampa Batch (PDF):

**Impostazioni Stampante:**
- Formato: A4 (210 x 297 mm)
- Orientamento: Verticale (Portrait)
- Margini: Predefiniti (già impostati nel PDF)
- Qualità: Alta / Fine
- Colori: Scala di grigi o B/N

**Materiali:**
- Carta adesiva A4 per etichette
- Carta normale A4 (poi tagliare e plastificare)
- Fogli etichette pre-perforati (tipo Avery)

**Processo Consigliato:**
1. Stampa PDF su carta adesiva A4
2. Ritaglia ogni QR Code (circa 8x8 cm)
3. Opzionale: plastifica con pellicola trasparente
4. Applica sul veicolo (parabrezza o vetro laterale)

---

## 💡 CASI D'USO PRATICI

### Caso 1: Setup Iniziale Parcheggio
**Situazione:** 3 aziende, 100 veicoli totali

**Soluzione:**
```
AZIENDA_ALFA (30 veicoli):
→ Genera Batch → 30 QR → Export PDF (5 pagine)

AZIENDA_BETA (50 veicoli):
→ Genera Batch → 50 QR → Export PDF (9 pagine)

AZIENDA_GAMMA (20 veicoli):
→ Genera Batch → 20 QR → Export PDF (4 pagine)

Totale: 3 PDF, 18 pagine, 100 QR Code
Tempo: ~2 minuti per generare tutto
Stampa: ~5 minuti (18 pagine A4)
```

### Caso 2: Veicolo Singolo Nuovo
**Situazione:** 1 nuova auto autorizzata

**Soluzione:**
```
→ Genera Singolo QR
→ Salva in Galleria
→ Stampa da smartphone su etichetta
→ Applica immediatamente
Tempo: ~30 secondi
```

### Caso 3: Sostituzione QR Danneggiato
**Situazione:** QR Code illeggibile/usurato

**Soluzione:**
```
→ Genera Singolo QR
→ Condividi via WhatsApp al tecnico
→ Tecnico stampa e sostituisce
Tempo: ~1 minuto
```

### Caso 4: Archive / Backup
**Situazione:** Backup periodico di tutti i QR generati

**Soluzione:**
```
→ Vai su "Esporta Dati"
→ Export CSV (lista completa codici)
→ Per ogni azienda, rigenera PDF batch
→ Salva tutto su cloud / NAS
Frequenza: trimestrale
```

---

## 📊 COMPARAZIONE MODALITÀ

| Caratteristica | Singolo | Batch (PDF) |
|----------------|---------|-------------|
| QR per volta | 1 | 1-100 |
| Tempo generazione | ~2 sec | ~5-10 sec |
| Formato output | PNG | PDF |
| Dimensione file | ~50 KB | ~500 KB - 2 MB |
| Pronto stampa | ✓ | ✓✓✓ |
| Uso professionale | Medio | Alto |
| Condivisione | Facile | Facile |
| Modifica possibile | Sì | No |

**Quando usare SINGOLO:**
- Pochi QR Code (1-5)
- Vuoi modificare l'immagine
- Stampa casalinga immediata
- Test / prototipo

**Quando usare BATCH:**
- Molti QR Code (>10)
- Stampa professionale
- Setup iniziale
- Archivio organizzato

---

## 🔧 RISOLUZIONE PROBLEMI

### ❌ "Permesso di scrittura negato"
**Soluzione:**
1. Impostazioni → App → Parking QR Manager
2. Permessi → Attiva "Archiviazione"
3. Riprova salvataggio

### ❌ "PDF non si apre"
**Soluzione:**
- Installa un lettore PDF (Adobe Acrobat Reader)
- Verifica spazio disponibile su dispositivo
- Rigenera il PDF

### ❌ "Stampante non trova il file"
**Soluzione:**
- Usa "Condividi" invece di stampa diretta
- Invia PDF via email e stampa da PC
- Trasferisci file via cavo USB

### ❌ "QR Code troppo piccolo/grande sulla stampa"
**Soluzione:**
- Stampa SINGOLO: usa impostazioni stampante per ridimensionare
- Stampa BATCH: stampare al 100% (no scaling), QR già dimensionato correttamente

### ❌ "Testo sotto QR Code non leggibile"
**Soluzione:**
- Aumenta qualità stampa (DPI più alto)
- Stampa su carta migliore
- Il testo è informativo, il QR Code è sufficiente per scansione

---

## 📁 GESTIONE FILE

### Pulizia Periodica:

**Galleria (QR singoli):**
```
Percorso: Galleria/QRCodes/
Frequenza: Mensile
Azione: Elimina QR vecchi o duplicati
```

**PDF (QR batch):**
```
Percorso: Documents/QRCodes/
Frequenza: Trimestrale
Azione: Sposta su PC/Cloud e elimina da smartphone
```

### Backup Consigliato:

```
1. Export CSV database (lista codici)
2. Copia tutti i PDF su computer
3. Salva su cloud (Google Drive/Dropbox)
4. Conserva 1 copia stampata fisica
```

---

## ✅ CHECKLIST STAMPA PROFESSIONALE

Prima di stampare 100+ QR Code:

- [ ] Verificato nome azienda corretto
- [ ] Generato batch con quantità corretta
- [ ] PDF esportato e verificato (aprire e controllare)
- [ ] Stampante configurata (A4, alta qualità)
- [ ] Carta/etichette adesive pronte
- [ ] Test stampa su 1 pagina prima di tutto
- [ ] QR Code di test scansionato e funzionante
- [ ] Processo di applicazione definito
- [ ] Database app contiene tutti i codici

---

## 🎓 BEST PRACTICES

1. **Genera sempre in batch** per nuove aziende (>10 veicoli)
2. **Usa singolo** solo per sostituzioni o aggiunte
3. **Salva sempre i PDF** generati come backup
4. **Stampa di test** prima della produzione
5. **Plastifica** i QR per durata maggiore
6. **Documenta** quale QR è su quale targa
7. **Export CSV periodico** per avere lista completa
8. **Backup cloud** dei PDF generati

---

## 📞 SUPPORTO STAMPA

Per problemi specifici di stampa:
- Consulta manuale stampante
- Verifica compatibilità formato A4
- Usa servizi di stampa professionale se necessario
- Per grandi quantità (>100), considera tipografia

---

**L'app è ora completa di tutte le funzionalità di stampa e salvataggio! 🎉**
