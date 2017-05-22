//##########################################
//
// Pythagoras Encryption Algorithm 2.1
// https://eerokaan.de/
//
//##########################################
//
// MIT License
//
// Copyright (c) 2016 Eero Kaan
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
//
//##########################################

package de.eerokaan.pea;

import java.util.*;

class Keyprozessor {
    public static int[][][] Start (int[] Key){
        int[][][] SBoxen = new int[16][16][16];
        int[][] SBox;
        int[][] RoundKey = new int[16][16];
        int[][] RoundKeySBoxErsteller = new int[16][16];

        for (int SBoxNummer = 0; SBoxNummer < 16; ++SBoxNummer) {
            //Erzeuge aus dem Key einen Rundenbasierten RoundKey
            RoundKey[SBoxNummer] = RoundKeyShifting(Key, SBoxNummer);

            //Erzeuge aus RoundKey den RoundKeySBoxErsteller, welcher letztendlich auch die S-Box erzeugt
            RoundKeySBoxErsteller[SBoxNummer] = SBoxTranspositionierung(RoundKey[SBoxNummer]);

            //Erzeuge aus RoundKeySBoxErsteller nun eine Rundenbasierte S-Box
            SBox = SBoxErstellen(RoundKeySBoxErsteller[SBoxNummer]);

            for (int Zeile = 0; Zeile < 16; ++Zeile) {
                for (int Spalte = 0; Spalte < 16; ++Spalte) {
                    SBoxen[SBoxNummer][Zeile][Spalte] = SBox[Zeile][Spalte];
                }
            }
        }
        return SBoxen;
    }

    public static int[] RoundKeyShifting (int[] Key, int RoundKeyNummer){
        //Wenn erstes Mal RoundKey erzeugt wird
        if (RoundKeyNummer == 1) {
            Key = RoundKeyShiftingHorizontal(0, Key);
            Key = RoundKeyShiftingVertikal(0, Key);
        }

        //Wenn Horizontal geshiftet wird
        if (RoundKeyNummer == 2 || RoundKeyNummer == 3 || RoundKeyNummer == 4 || RoundKeyNummer == 6 || RoundKeyNummer == 7 || RoundKeyNummer == 8 || RoundKeyNummer == 10 || RoundKeyNummer == 11 || RoundKeyNummer == 12 || RoundKeyNummer == 14 || RoundKeyNummer == 15 || RoundKeyNummer == 16) {
            Key = RoundKeyShiftingHorizontal(1, Key);
            Key = RoundKeyShiftingVertikal(0, Key);
        }

        //Wenn Vertikal geshiftet wird
        if (RoundKeyNummer == 5 || RoundKeyNummer == 9 || RoundKeyNummer == 13) {
            Key = RoundKeyShiftingHorizontal(1, Key);
            Key = RoundKeyShiftingVertikal(1, Key);
        }

        return Key;
    }

    public static int[] RoundKeyShiftingHorizontal (int Anzahl, int[] Key){
        for (int Counter = 0; Counter < Anzahl; ++Counter) {
            int ShiftingKeyTemp1 = Key[0];
            int ShiftingKeyTemp2 = Key[4];
            int ShiftingKeyTemp3 = Key[8];
            int ShiftingKeyTemp4 = Key[12];

            Key[0] = Key[1];
            Key[4] = Key[5];
            Key[8] = Key[9];
            Key[12] = Key[13];

            Key[1] = Key[2];
            Key[5] = Key[6];
            Key[9] = Key[10];
            Key[13] = Key[14];

            Key[2] = Key[3];
            Key[6] = Key[7];
            Key[10] = Key[11];
            Key[14] = Key[15];

            Key[3] = ShiftingKeyTemp1;
            Key[7] = ShiftingKeyTemp2;
            Key[11] = ShiftingKeyTemp3;
            Key[15] = ShiftingKeyTemp4;
        }
        return Key;
    }

    public static int[] RoundKeyShiftingVertikal (int Anzahl, int[] Key){
        for (int Counter = 0; Counter < Anzahl; ++Counter) {
            int ShiftingKeyTemp1 = Key[0];
            int ShiftingKeyTemp2 = Key[1];
            int ShiftingKeyTemp3 = Key[2];
            int ShiftingKeyTemp4 = Key[3];

            Key[0] = Key[4];
            Key[1] = Key[5];
            Key[2] = Key[6];
            Key[3] = Key[7];

            Key[4] = Key[8];
            Key[5] = Key[9];
            Key[6] = Key[10];
            Key[7] = Key[11];

            Key[8] = Key[12];
            Key[9] = Key[13];
            Key[10] = Key[14];
            Key[11] = Key[15];

            Key[12] = ShiftingKeyTemp1;
            Key[13] = ShiftingKeyTemp2;
            Key[14] = ShiftingKeyTemp3;
            Key[15] = ShiftingKeyTemp4;
        }
        return Key;
    }

    public static int[] SBoxTranspositionierung (int[] RoundKey){
        //Allgemeine Initialisierung
        int ShiftingKey_1_1;
        int ShiftingKey_1_2;
        int ShiftingKey_2_1;
        int ShiftingKey_2_2;
        int ShiftingKey_TempVar;

        //Dezimalzeilensummen berechnen
        int Dezimalzeilensumme[] = new int[4];
        Dezimalzeilensumme[0] = (RoundKey[0] + RoundKey[1]) - (RoundKey[2] + RoundKey[3]);
        Dezimalzeilensumme[1] = (RoundKey[4] + RoundKey[5]) - (RoundKey[6] + RoundKey[7]);
        Dezimalzeilensumme[2] = (RoundKey[8] + RoundKey[9]) - (RoundKey[10] + RoundKey[11]);
        Dezimalzeilensumme[3] = (RoundKey[12] + RoundKey[13]) - (RoundKey[14] + RoundKey[15]);

        //Zahlen formatieren
        for (int Counter = 0; Counter < 4; ++Counter) {
            //Negative Zahlen positiv machen
            if (Dezimalzeilensumme[Counter] < 0) {
                Dezimalzeilensumme[Counter] = Dezimalzeilensumme[Counter] * -1;
            }
            //Zahlen größer als 16 anpassen
            while (Dezimalzeilensumme[Counter] >= 16) {
                Dezimalzeilensumme[Counter] = Dezimalzeilensumme[Counter] / 16;
            }
        }

        //Key Transpositionierung
        ShiftingKey_1_1 = RoundKey[Dezimalzeilensumme[0]];
        ShiftingKey_1_2 = RoundKey[Dezimalzeilensumme[1]];
        ShiftingKey_2_1 = RoundKey[Dezimalzeilensumme[2]];
        ShiftingKey_2_2 = RoundKey[Dezimalzeilensumme[3]];
        ShiftingKey_TempVar = ShiftingKey_1_1;
        ShiftingKey_1_1 = ShiftingKey_1_2;
        ShiftingKey_1_2 = ShiftingKey_TempVar;
        ShiftingKey_TempVar = ShiftingKey_2_1;
        ShiftingKey_2_1 = ShiftingKey_2_2;
        ShiftingKey_2_2 = ShiftingKey_TempVar;
        RoundKey[Dezimalzeilensumme[0]] = ShiftingKey_1_1;
        RoundKey[Dezimalzeilensumme[1]] = ShiftingKey_1_2;
        RoundKey[Dezimalzeilensumme[2]] = ShiftingKey_2_1;
        RoundKey[Dezimalzeilensumme[3]] = ShiftingKey_2_2;

        return RoundKey;
    }

    public static int[][] SBoxErstellen (int[] RoundKeySBoxErsteller) {
        HashSet<Integer> BereitsVorhandeneZahlen = new HashSet<>();
        int[][] SBox = new int[16][16];
        int ShiftingKeyTemp;

        //Filtere RoundKeySBoxErstellers derart, dass alle Elemente einzigartig sind
        for (int Counter = 0; Counter < 16; ++Counter) {
            if (!BereitsVorhandeneZahlen.contains(RoundKeySBoxErsteller[Counter])) {
                BereitsVorhandeneZahlen.add(RoundKeySBoxErsteller[Counter]);
            }
            else {
                int ByteSeacher = 0;
                while (BereitsVorhandeneZahlen.contains(ByteSeacher)) {
                    ++ByteSeacher;
                }
                RoundKeySBoxErsteller[Counter] = ByteSeacher;
                BereitsVorhandeneZahlen.add(RoundKeySBoxErsteller[Counter]);
            }
        }

        //Belege erste Spalte der S-Box mit den Elementen des RoundKeySBoxErstellers
        for (int Counter = 0; Counter < 16; ++Counter) {
            SBox[Counter][0] = RoundKeySBoxErsteller[Counter];
        }

        //Expandiere Zahlen der ersten Spalte nach rechts - Bedingung: Expandierte Zahl noch nicht vorhanden
        for (int CounterZeile = 0; CounterZeile < 16; ++CounterZeile) {
            for (int CounterSpalte = 1; CounterSpalte < 16; ++CounterSpalte) {
                if (!BereitsVorhandeneZahlen.contains(SBox[CounterZeile][CounterSpalte - 1] + 1)) {
                    SBox[CounterZeile][CounterSpalte] = SBox[CounterZeile][CounterSpalte - 1] + 1;
                    BereitsVorhandeneZahlen.add(SBox[CounterZeile][CounterSpalte]);
                }
                else {
                    SBox[CounterZeile][CounterSpalte] = 0;
                }
            }
        }

        //Fülle 0er der Expansionsstellen wieder auf
        for (int CounterZeile = 0; CounterZeile < 16; ++CounterZeile) {
            for (int CounterSpalte = 1; CounterSpalte < 16; ++CounterSpalte) {
                if (SBox[CounterZeile][CounterSpalte] == 0) {

                    int ByteSeacher = 0;
                    while (BereitsVorhandeneZahlen.contains(ByteSeacher)) {
                        ++ByteSeacher;
                    }
                    SBox[CounterZeile][CounterSpalte] = ByteSeacher;
                    BereitsVorhandeneZahlen.add(ByteSeacher);
                }
            }
        }

        //Transpositioniere Zeilenweise
        for (int Zeile = 0; Zeile < 16; ++Zeile) {
            ShiftingKeyTemp = SBox[Zeile][0];
            SBox[Zeile][0] = SBox[Zeile][11];
            SBox[Zeile][11] = ShiftingKeyTemp;

            ShiftingKeyTemp = SBox[Zeile][2];
            SBox[Zeile][2] = SBox[Zeile][9];
            SBox[Zeile][9] = ShiftingKeyTemp;

            ShiftingKeyTemp = SBox[Zeile][4];
            SBox[Zeile][4] = SBox[Zeile][15];
            SBox[Zeile][15] = ShiftingKeyTemp;

            ShiftingKeyTemp = SBox[Zeile][7];
            SBox[Zeile][7] = SBox[Zeile][13];
            SBox[Zeile][13] = ShiftingKeyTemp;
        }

        return SBox;
    }
}

class BorderTransposition {
    public static int[] Erstellen (int[] Key){
        //Initalisierung
        int BorderTranspositionPaare[] = new int[8];
        double LogGleichungArray[][] = new double[4][256];
        double LogGleichungWertDavor = 0.5;
        double LogGleichungR = 0;

        //Bestimme "Variable R" für die Logistische Gleichung
        for (int KeySegmentCounter = 0; KeySegmentCounter < 4; KeySegmentCounter++) {
            switch(KeySegmentCounter) {
                case 0:
                    LogGleichungR = Double.parseDouble("3.90" + (Key[1] + "" + Key[2]));
                    break;
                case 1:
                    LogGleichungR = Double.parseDouble("3.90" + (Key[5] + "" + Key[6]));
                    break;
                case 2:
                    LogGleichungR = Double.parseDouble("3.90" + (Key[9] + "" + Key[10]));
                    break;
                case 3:
                    LogGleichungR = Double.parseDouble("3.90" + (Key[13] + "" + Key[14]));
                    break;
            }

            //Fülle alle 256 Einträge der Logistischen Gleichung
            for (int LogGleichungCounter = 0; LogGleichungCounter < 256; LogGleichungCounter++) {
                LogGleichungArray[KeySegmentCounter][LogGleichungCounter] = LogGleichungR * LogGleichungWertDavor * (1 - LogGleichungWertDavor);
                LogGleichungWertDavor = LogGleichungArray[KeySegmentCounter][LogGleichungCounter];
            }
        }

        //Wähle mit den TranspositionsIndizes die TranspositionsPaare aus
        //Verarbeite sie dabei und schreibe diese in das BorderTranspositionPaare[] Array
        BorderTranspositionPaare[0] = (int) ((LogGleichungArray[0][Key[0]] * 10) * 1.67) - 1;
        BorderTranspositionPaare[1] = (int) ((LogGleichungArray[0][Key[3]] * 10) * 1.67) - 1;
        BorderTranspositionPaare[2] = (int) ((LogGleichungArray[1][Key[4]] * 10) * 1.67) - 1;
        BorderTranspositionPaare[3] = (int) ((LogGleichungArray[1][Key[7]] * 10) * 1.67) - 1;
        BorderTranspositionPaare[4] = (int) ((LogGleichungArray[2][Key[8]] * 10) * 1.67) - 1;
        BorderTranspositionPaare[5] = (int) ((LogGleichungArray[2][Key[11]] * 10) * 1.67) - 1;
        BorderTranspositionPaare[6] = (int) ((LogGleichungArray[3][Key[12]] * 10) * 1.67) - 1;
        BorderTranspositionPaare[7] = (int) ((LogGleichungArray[3][Key[15]] * 10) * 1.67) - 1;

        return BorderTranspositionPaare;
    }

    public static int[] Ersetzen (boolean ModusEncrypt, int Paar1_Zahl1, int Paar1_Zahl2, int Paar2_Zahl1, int Paar2_Zahl2, int[] Data){
        int TranspositionKeyTemp;

        if (ModusEncrypt == true) {
            //Tausche Zahlen der Indezes 13 und 15
            TranspositionKeyTemp = Data[15];
            Data[15] = Data[13];
            Data[13] = TranspositionKeyTemp;

            //Tausche Zahlen der Indezes 0 und 7 miteinander
            TranspositionKeyTemp = Data[7];
            Data[7] = Data[0];
            Data[0] = TranspositionKeyTemp;

           //Tausche Paar 1 von "BorderTranspositionErstellen()"
            TranspositionKeyTemp = Data[Paar1_Zahl1];
            Data[Paar1_Zahl1] = Data[Paar1_Zahl2];
            Data[Paar1_Zahl2] = TranspositionKeyTemp;

            //Tausche Paar 2 von "BorderTranspositionErstellen()"
            TranspositionKeyTemp = Data[Paar2_Zahl1];
            Data[Paar2_Zahl1] = Data[Paar2_Zahl2];
            Data[Paar2_Zahl2] = TranspositionKeyTemp;
        }
        if (ModusEncrypt == false) {
            //Tausche Paar 2 von "BorderTranspositionErstellen()"
            TranspositionKeyTemp = Data[Paar2_Zahl2];
            Data[Paar2_Zahl2] = Data[Paar2_Zahl1];
            Data[Paar2_Zahl1] = TranspositionKeyTemp;

            //Tausche Paar 1 von "BorderTranspositionErstellen()"
            TranspositionKeyTemp = Data[Paar1_Zahl2];
            Data[Paar1_Zahl2] = Data[Paar1_Zahl1];
            Data[Paar1_Zahl1] = TranspositionKeyTemp;

            //Tausche Zahlen der Indezes 7 und 0 miteinander
            TranspositionKeyTemp = Data[0];
            Data[0] = Data[7];
            Data[7] = TranspositionKeyTemp;

            //Tausche Zahlen der Indezes 15 und 13 miteinander
            TranspositionKeyTemp = Data[13];
            Data[13] = Data[15];
            Data[15] = TranspositionKeyTemp;
        }

        return Data;
    }
}

class Shifting {
    public static int[] Start (boolean ModusEncrypt, int[] Data){
        int ShiftingKeyTemp;

        if (ModusEncrypt == true) {
            //Zeile2
            ShiftingKeyTemp = Data[4];
            Data[4] = Data[5];
            Data[5] = Data[6];
            Data[6] = Data[7];
            Data[7] = ShiftingKeyTemp;

            //Zeile3
            ShiftingKeyTemp = Data[8];
            Data[8] = Data[9];
            Data[9] = Data[10];
            Data[10] = Data[11];
            Data[11] = ShiftingKeyTemp;
            ShiftingKeyTemp = Data[8];
            Data[8] = Data[9];
            Data[9] = Data[10];
            Data[10] = Data[11];
            Data[11] = ShiftingKeyTemp;

            //Zeile4
            ShiftingKeyTemp = Data[12];
            Data[12] = Data[13];
            Data[13] = Data[14];
            Data[14] = Data[15];
            Data[15] = ShiftingKeyTemp;
            ShiftingKeyTemp = Data[12];
            Data[12] = Data[13];
            Data[13] = Data[14];
            Data[14] = Data[15];
            Data[15] = ShiftingKeyTemp;
            ShiftingKeyTemp = Data[12];
            Data[12] = Data[13];
            Data[13] = Data[14];
            Data[14] = Data[15];
            Data[15] = ShiftingKeyTemp;
        }
        if (ModusEncrypt == false) {
            //Zeile2
            ShiftingKeyTemp = Data[7];
            Data[7] = Data[6];
            Data[6] = Data[5];
            Data[5] = Data[4];
            Data[4] = ShiftingKeyTemp;

            //Zeile3
            ShiftingKeyTemp = Data[11];
            Data[11] = Data[10];
            Data[10] = Data[9];
            Data[9] = Data[8];
            Data[8] = ShiftingKeyTemp;
            ShiftingKeyTemp = Data[11];
            Data[11] = Data[10];
            Data[10] = Data[9];
            Data[9] = Data[8];
            Data[8] = ShiftingKeyTemp;

            //Zeile4
            ShiftingKeyTemp = Data[15];
            Data[15] = Data[14];
            Data[14] = Data[13];
            Data[13] = Data[12];
            Data[12] = ShiftingKeyTemp;
            ShiftingKeyTemp = Data[15];
            Data[15] = Data[14];
            Data[14] = Data[13];
            Data[13] = Data[12];
            Data[12] = ShiftingKeyTemp;
            ShiftingKeyTemp = Data[15];
            Data[15] = Data[14];
            Data[14] = Data[13];
            Data[13] = Data[12];
            Data[12] = ShiftingKeyTemp;
        }

        return Data;
    }
}
