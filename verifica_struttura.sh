#!/bin/bash

# Script di verifica struttura progetto Parking QR Manager

echo "=========================================="
echo "  Verifica Struttura Progetto Android"
echo "=========================================="
echo ""

# Colori per output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

check_file() {
    if [ -f "$1" ]; then
        echo -e "${GREEN}✓${NC} $1"
        return 0
    else
        echo -e "${RED}✗${NC} $1 ${RED}MANCANTE${NC}"
        return 1
    fi
}

check_dir() {
    if [ -d "$1" ]; then
        echo -e "${GREEN}✓${NC} Directory: $1"
        return 0
    else
        echo -e "${RED}✗${NC} Directory: $1 ${RED}MANCANTE${NC}"
        return 1
    fi
}

missing=0

echo "Verifica File Configurazione:"
echo "------------------------------"
check_file "AndroidManifest.xml" || ((missing++))
check_file "build.gradle" || ((missing++))
check_file "build.gradle.root" || ((missing++))
check_file "proguard-rules.pro" || ((missing++))
echo ""

echo "Verifica File Java:"
echo "-------------------"
check_dir "java/com/parking/qrmanager" || ((missing++))
check_file "java/com/parking/qrmanager/MainActivity.java" || ((missing++))
check_file "java/com/parking/qrmanager/GenerateQRActivity.java" || ((missing++))
check_file "java/com/parking/qrmanager/ScanActivity.java" || ((missing++))
check_file "java/com/parking/qrmanager/HistoryActivity.java" || ((missing++))
check_file "java/com/parking/qrmanager/ExportActivity.java" || ((missing++))
check_file "java/com/parking/qrmanager/DatabaseHelper.java" || ((missing++))
echo ""

echo "Verifica Layout XML:"
echo "--------------------"
check_dir "res/layout" || ((missing++))
check_file "res/layout/activity_main.xml" || ((missing++))
check_file "res/layout/activity_generate_qr.xml" || ((missing++))
check_file "res/layout/activity_scan.xml" || ((missing++))
check_file "res/layout/activity_history.xml" || ((missing++))
check_file "res/layout/activity_export.xml" || ((missing++))
check_file "res/layout/custom_barcode_scanner.xml" || ((missing++))
echo ""

echo "Verifica Resources:"
echo "-------------------"
check_dir "res/values" || ((missing++))
check_file "res/values/strings.xml" || ((missing++))
check_file "res/values/colors.xml" || ((missing++))
check_file "res/values/styles.xml" || ((missing++))
check_dir "res/xml" || ((missing++))
check_file "res/xml/file_paths.xml" || ((missing++))
echo ""

echo "Verifica Documentazione:"
echo "------------------------"
check_file "README.md" || ((missing++))
check_file "GUIDA_COMPILAZIONE.md" || ((missing++))
check_file "MANUALE_OPERATIVO.md" || ((missing++))
echo ""

echo "=========================================="
if [ $missing -eq 0 ]; then
    echo -e "${GREEN}✓ SUCCESSO: Tutti i file sono presenti!${NC}"
    echo ""
    echo "Prossimi passi:"
    echo "1. Importa il progetto in Android Studio"
    echo "2. Segui GUIDA_COMPILAZIONE.md"
    echo "3. Build APK"
else
    echo -e "${RED}✗ ATTENZIONE: Mancano $missing file/directory${NC}"
    echo ""
    echo "Controlla i file mancanti sopra elencati"
fi
echo "=========================================="
