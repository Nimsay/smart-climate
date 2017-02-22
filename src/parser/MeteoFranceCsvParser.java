package parser;

import java.io.IOException;
import java.util.ArrayList;


public class MeteoFranceCsvParser extends CSV {

    MeteoFranceCsvParser(String csvFile) {
        super(csvFile, ";", "", true, true);
    }


}
