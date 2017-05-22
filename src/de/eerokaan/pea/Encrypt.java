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

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class Encrypt {
    public static int[] Start (String Dateiname) throws IOException, java.nio.BufferOverflowException {

        //Key generieren
        int[] Key1 = new int[16];
        int[] Key2 = new int[16];
        for (int Counter = 0; Counter < 16; ++Counter) {
            Key1[Counter] = (int) ((Math.random() * 16) * (Math.random() * 16));
            Key2[Counter] = Key1[Counter];
        }

        //BorderTransposition: Paare ermitteln
        int[] BorderTranspositionPaare = BorderTransposition.Erstellen(Key1);

        //S-Boxen generieren
        int[][][] SBoxen = Keyprozessor.Start(Key1);

        //Hauptschleife -> Blockweise Einlesen, Verschlüsseln und wieder Herausschreiben
        Boolean ModusEncrypt = true;
        int[] Data = new int[16];
        int CounterData = 0;

        File InputFile = new File(Dateiname);
        File OutputFile = new File(Dateiname + "_Encrypted");

        FileChannel InputKanal = new RandomAccessFile(InputFile, "r").getChannel();
        FileChannel OutputKanal = new RandomAccessFile(OutputFile, "rw").getChannel();

        MappedByteBuffer InputBuffer = InputKanal.map(FileChannel.MapMode.READ_ONLY, 0, InputKanal.size());
        MappedByteBuffer OutputBuffer = OutputKanal.map(FileChannel.MapMode.READ_WRITE, 0, InputKanal.size());

        while (InputBuffer.hasRemaining()) {
            //CounterData zurücksetzen
            if (CounterData == 17) {
                CounterData = 0;
            }

            //DataArray immer neu füllen
            if (CounterData < 16){
                Data[CounterData] = InputBuffer.get() & 0xFF;
            }

            //Fertigen DataArray verarbeiten
            if (CounterData == 16) {

                //BorderTransposition
                Data = BorderTransposition.Ersetzen(ModusEncrypt, BorderTranspositionPaare[0], BorderTranspositionPaare[1], BorderTranspositionPaare[2], BorderTranspositionPaare[3], Data);

                //Substitutionsindex durch Zahl ermitteln und durch Umformung in SBox Substitutionspartner finden + Shifting
                int Zeile;
                int Spalte;

                for (int CounterPassedCycles = 0; CounterPassedCycles < 16; ++CounterPassedCycles) {
                    Zeile = Data[CounterPassedCycles]/16;
                    Spalte = Data[CounterPassedCycles] - (Zeile * 16);

                    if (Zeile < 0) {
                        Zeile = Zeile * -1;
                    }
                    if (Spalte < 0) {
                        Spalte = Spalte * -1;
                    }

                    //Substituiere hier
                    Data[CounterPassedCycles] = SBoxen[CounterPassedCycles][Zeile][Spalte];
                }

                //Shifting
                Data = Shifting.Start(ModusEncrypt, Data);

                //BorderTransposition
                Data = BorderTransposition.Ersetzen(ModusEncrypt, BorderTranspositionPaare[4], BorderTranspositionPaare[5], BorderTranspositionPaare[6], BorderTranspositionPaare[7], Data);

                //DataArray in OutputDatei schreiben
                for (int CounterDarstellung = 0; CounterDarstellung < 16; ++CounterDarstellung) {
                    OutputBuffer.put((byte) (Data[CounterDarstellung]));
                }
            }
            ++CounterData;
        }
        InputKanal.close();
        OutputKanal.close();
        return Key2;
    }
}
