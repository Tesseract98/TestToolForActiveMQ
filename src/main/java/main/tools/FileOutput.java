package main.tools;

import main.exceptions.NotAccessNotFoundFileExc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class FileOutput {

    public ArrayList<String> readFileAndOut(File fileName) throws NotAccessNotFoundFileExc {
        ArrayList<String> fileOutput = new ArrayList<>();
        try {
            //Объект для чтения файла в буфер
            try (BufferedReader in = new BufferedReader(new FileReader(fileName.getAbsoluteFile()))) {
                //В цикле построчно считываем файл
                String s;
                while ((s = in.readLine()) != null) {
                    fileOutput.add(s);
//                    for(String element : s.split("\\s+")){ // на память
//                        fileOutput.add(element);
//                    }
                }
            }
        } catch (Exception exp) {
            throw new NotAccessNotFoundFileExc("File " + fileName.getName() + " can't be read", exp);
        }
        return fileOutput;
    }
}