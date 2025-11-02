# MANUALE OPERATIVO - Esempi Pratici

## Scenario 1: Setup Iniziale Parcheggio

### Passo 1: Generazione QR Code per Aziende
Supponiamo di avere 3 aziende clienti:
- **AZIENDA_ALFA** - 30 posti - Area Verde
- **AZIENDA_BETA** - 50 posti - Area Rossa  
- **AZIENDA_GAMMA** - 20 posti - Area Gialla

#### Generazione QR per AZIENDA_ALFA (30 veicoli)
1. Apri app → "Genera QR Code"
2. Seleziona "AZIENDA_A" o inserisci "AZIENDA_ALFA" in personalizzato
3. Premi "Genera QR Code" → salva/stampa
4. Ripeti 30 volte per avere 30 QR code unici
5. Applica ogni QR su un veicolo autorizzato

**Esempio QR generati:**
```
AZIENDA_ALFA_K7M9P2N4X1Q5_18A3B5C7D9E
AZIENDA_ALFA_R2T8W3M5B6P9_18A3B5C8E2F
AZIENDA_ALFA_N4Q7L1V9D3K8_18A3B5C8E4A
...
```

## Scenario 2: Controllo Giornaliero Area Verde

### Mattina - Ore 8:00
Operatore avvia controllo Area Verde (AZIENDA_ALFA - 30 posti autorizzati)

#### Passo 1: Avvio Sessione
1. Apri app → "Scansiona Area"
2. Seleziona "🟢 Verde"
3. Inserisci "1" in Numero Postazione
4. Sessione automatica: `SESSION_20250130_080000`

#### Passo 2: Scansione Postazioni
**Postazione 1:** Auto presente con QR
- Premi "Scansiona QR Code"
- Inquadra QR: `AZIENDA_ALFA_K7M9P2N4X1Q5_18A3B5C7D9E`
- ✅ Successo: "QR Code scansionato"
- Numero postazione auto-incrementa a "2"

**Postazione 2:** Auto presente con QR
- Premi "Scansiona QR Code"
- Inquadra QR: `AZIENDA_ALFA_R2T8W3M5B6P9_18A3B5C8E2F`
- ✅ Successo: "QR Code scansionato"
- Numero postazione auto-incrementa a "3"

**Postazione 3:** Postazione vuota
- Premi "Segna Postazione Vuota"
- Conferma → ✅ "Postazione 3 segnata come vuota"
- Numero postazione auto-incrementa a "4"

**Postazione 4:** Auto con QR già scansionato (ERRORE!)
- Premi "Scansiona QR Code"
- Inquadra QR: `AZIENDA_ALFA_K7M9P2N4X1Q5_18A3B5C7D9E` (già scansionato in pos. 1)
- ⚠️ ALERT: "QR Code Duplicato! Questo QR è già stato scansionato: Area Verde, Postazione 1"
- **AZIONE**: Verificare irregolarità - possibile doppio utilizzo QR

**Postazione 5:** Auto senza QR code (non autorizzata!)
- Non si può scansionare nulla
- **AZIONE**: Contattare proprietario o rimuovere veicolo

**Postazione 6-30:** Continua scansione...

#### Passo 3: Fine Controllo
- Premi "Termina Sessione"
- Riepilogo: "Totale scansioni: 27"
- Risultato: 25 auto autorizzate, 2 postazioni vuote, 1 duplicato rilevato, 2 auto non autorizzate

## Scenario 3: Controllo Multi-Area

### Operatore controlla tutte le 4 aree in sequenza

**8:00-8:30 - Area Verde**
- Sessione: `SESSION_20250130_080000`
- Scansioni: 25 QR + 5 vuote

**8:30-9:00 - Area Rossa**
- Stessa sessione: `SESSION_20250130_080000`
- Seleziona "🔴 Rosso"
- Inserisci "1" e riparte da postazione 1 area rossa
- Scansioni: 48 QR + 2 vuote

**9:00-9:30 - Area Gialla**
- Stessa sessione
- Seleziona "🟡 Giallo"
- Scansioni: 19 QR + 1 vuota

**9:30-10:00 - Area Blu**
- Stessa sessione
- Seleziona "🔵 Blu"
- Scansioni: 35 QR + 15 vuote

**10:00 - Fine controllo**
- Premi "Termina Sessione"
- Totale: 127 QR scansionati, 23 postazioni vuote, 0 duplicati

## Scenario 4: Rilevamento Irregolarità

### Caso A: Veicolo con QR duplicato
**Situazione:** Stesso QR applicato su 2 veicoli diversi

**Area Verde - Postazione 12**
- Scansione QR: `AZIENDA_ALFA_K7M9P2N4X1Q5_18A3B5C7D9E`
- ✅ Successo

**Area Verde - Postazione 45**
- Scansione stesso QR: `AZIENDA_ALFA_K7M9P2N4X1Q5_18A3B5C7D9E`
- ⚠️ ALERT: "QR Code Duplicato! Area: Verde, Postazione: 12"

**AZIONE:**
1. Controllare entrambi i veicoli (pos. 12 e 45)
2. Verificare quale è il veicolo autorizzato originale
3. Rimuovere il QR falso/duplicato
4. Annotare targa veicolo non autorizzato
5. Applicare sanzione o rimozione

### Caso B: QR Code non valido
**Situazione:** QR non appartiene al sistema

**Scansione QR esterno:** `HTTP://EXAMPLE.COM/PARKING123`
- Il QR non ha il formato corretto (manca prefisso AZIENDA_)
- Non viene trovato nel database
- ⚠️ Trattare come veicolo non autorizzato

## Scenario 5: Export e Reportistica

### Fine Settimana - Export Dati

#### Passo 1: Esportazione
1. Apri app → "Esporta Dati"
2. Info: "Scansioni totali: 635"
3. Premi "Esporta in CSV"
4. File creato: `scansioni_20250130_180000.csv`

#### Passo 2: Analisi Dati
Apri CSV in Excel/Google Sheets:

**Esempio righe CSV:**
```csv
ID,QR_Code,Area,Postazione,Sessione,Data,Ora,Vuota
1,AZIENDA_ALFA_K7M9P2N4X1Q5_18A3B5C7D9E,Verde,12,SESSION_20250130_080000,2025-01-30,08:15:23,NO
2,AZIENDA_ALFA_R2T8W3M5B6P9_18A3B5C8E2F,Verde,13,SESSION_20250130_080000,2025-01-30,08:16:45,NO
3,VUOTA,Verde,14,SESSION_20250130_080000,2025-01-30,08:17:12,SI
```

**Analisi possibili:**
- Tasso occupazione per area
- Postazioni sempre vuote (sprecare risorse?)
- Frequenza utilizzo per azienda
- Irregolarità (duplicati rilevati)

#### Passo 3: Report Settimanale
**AZIENDA_ALFA (30 posti acquistati)**
- Posti mediamente utilizzati: 25/30 (83%)
- Postazioni spesso vuote: 5
- Suggerimento: Ottimizzare contratto (25 posti invece di 30)

#### Passo 4: Pulizia Database
1. Dopo export, premi "Elimina Dati Vecchi (>30gg)"
2. Conferma
3. Risultato: "Eliminate 4200 scansioni"
4. Memoria liberata per nuove scansioni

## Scenario 6: Gestione Emergenze

### Caso: Smartphone si scarica durante controllo

**Problema:** Batteria al 5% a metà controllo Area Rossa

**SOLUZIONE 1:** Salvataggio automatico
- Tutti i dati sono salvati istantaneamente nel database locale
- Anche se si spegne, al riavvio i dati sono preservati

**SOLUZIONE 2:** Terminare sessione anticipatamente
- Premi "Termina Sessione"
- Completa il controllo dopo ricarica
- Crea nuova sessione per continuare

**SOLUZIONE 3:** Export preventivo
- Prima di iniziare controlli, fare backup con export CSV
- In caso di problema, dati precedenti sono al sicuro

## Best Practices Operative

### ✅ DA FARE:
1. **Caricare smartphone al 100% prima di ogni turno**
2. **Esportare dati settimanalmente**
3. **Controllare sistematicamente in ordine numerico**
4. **Annotare manualmente irregolarità gravi**
5. **Verificare sempre alert duplicati**
6. **Terminare sessione a fine controllo**

### ❌ DA NON FARE:
1. **Non saltare postazioni** (perdita tracciabilità)
2. **Non ignorare alert duplicati** (frodi)
3. **Non accumulare più di 1 mese di dati** (rallentamenti)
4. **Non usare in condizioni estreme** (pioggia forte, etc.)
5. **Non modificare manualmente database** (corruzione dati)

## Codici QR Code - Interpretazione

### Formato Standard
`PREFISSO_AZIENDA_CODICE_UNIVOCO_TIMESTAMP`

**Esempio:** `AZIENDA_ALFA_K7M9P2N4X1Q5_18A3B5C7D9E`

- **AZIENDA_ALFA**: Identificativo cliente
- **K7M9P2N4X1Q5**: Codice random sicuro (12 caratteri)
- **18A3B5C7D9E**: Timestamp hex (univocità temporale)

### Validazione QR
Un QR è valido se:
✅ Ha formato corretto con prefisso aziendale
✅ È registrato nel database
✅ Non è duplicato nella sessione corrente
✅ Appartiene all'area corretta (opzionale)

## Manutenzione App

### Giornaliera
- Verificare spazio disponibile su smartphone
- Controllo batteria

### Settimanale  
- Export dati CSV
- Backup file export su computer/cloud

### Mensile
- Eliminare dati vecchi (>30gg)
- Verificare aggiornamenti app

### Annuale
- Revisionare QR code fisici (usura, leggibilità)
- Rigenerare QR danneggiati

## Supporto Rapido

**App non si apre?**
→ Riavvia smartphone

**Camera non funziona?**
→ Verifica permessi in Impostazioni → App → Parking QR Manager → Permessi

**Duplicati falsi positivi?**
→ Termina sessione e ricomincia con nuova sessione

**Export fallisce?**
→ Verifica permessi storage, libera spazio memoria

**Scansioni lente?**
→ Pulisci cache app, elimina dati vecchi
