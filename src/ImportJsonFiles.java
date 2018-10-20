import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.TreeMultimap;
import com.google.common.collect.ArrayListMultimap;
import org.apache.commons.io.FileUtils;
import java.util.List;



public class ImportJsonFiles {
    ///////////////INFO VARS
    private String songName;
    private String songSubName;
    private String authorName;
    private double beatsPerMinute;
    private double previewStartTime;
    private double previewDuration;
    //private String audioPath;
    private String coverImagePath;
    private String environmentName;
    private double songTimeOffset;
    private double shuffle;
    private double shufflePeriod;
    private boolean oneSaber;
    private double noteHitVolume;
    private double noteMissVolume;
    private JSONArray difficultyLevels;
    //////////////JSON VARS
    private String version;
    private double beatsPerBar;
    private double noteJumpSpeed;
    private JSONArray events;
    private JSONArray notes;
    private JSONArray obstacles;
    private LinkedList<LinkedList<Double>> notesList = new LinkedList<>();
    ///Class Variables
    private File[] jsonSongFilePath = new File[5];
    private File jsonInfoFilePath;

    ImportJsonFiles() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setDialogTitle("*Please choose the directory of the song you wish to convert.");
        chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        chooser.showOpenDialog(null);
        File jInput = chooser.getSelectedFile();
        //System.out.println(jsonInput.getParent());// /home/jigar/Desktop
        //System.out.println(jsonInput.getName());  //1.txt
        String newFolderName = "OS - " + jInput.getName();
        File sourceDirectory = new File(jInput.getParent() + "\\" + jInput.getName());
        File targetDirectory = new File(jInput.getParent() + "\\" + newFolderName);
        try
        {FileUtils.copyDirectory(sourceDirectory, targetDirectory);}
        catch (Exception e) {
            e.printStackTrace();
        }
        File jsonInput = targetDirectory;
        try {
            File[] files = jsonInput.listFiles();
            for (File file : files) {
                if (file.getName().toLowerCase().equals("expert+.json")) {
                    jsonSongFilePath[4] = file;
                }
                if (file.getName().toLowerCase().equals("expert.json")) {
                    jsonSongFilePath[3] = file;
                }
                if (file.getName().toLowerCase().equals("hard.json")) {
                    jsonSongFilePath[2] = file;
                }
                if (file.getName().toLowerCase().equals("medium.json")) {
                    jsonSongFilePath[1] = file;
                }
                if (file.getName().toLowerCase().equals("easy.json")) {
                    jsonSongFilePath[0] = file;
                }
                if (file.getName().toLowerCase().equals("info.json")) {
                    jsonInfoFilePath = file;
                }

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    void fullJsonParse() {
        this.getJsonInfo();
        this.getJsonSongInfo();
        this.printInfo();
        this.writeInfo();
        this.sortNotesByTime();
        this.changeNotes();
    }
    private void getJsonInfo() {

        JSONParser parser = new JSONParser();
        //ImportJsonFiles json = new ImportJsonFiles();
        try {

            FileReader tempReader = new FileReader(jsonInfoFilePath);
            JSONObject jsonFile;
            try {
                jsonFile = (JSONObject) parser.parse(tempReader);
            } catch (Exception e) {
                jsonFile = null;
                System.out.println("Error: json File not initialized");
                e.printStackTrace();
            }
            if (jsonFile.get("songName") != null)
            {songName = ("OneSaber - ") + ((String) jsonFile.get("songName"));}

            if (jsonFile.get("songSubName") != null)
            {songSubName = (String) jsonFile.get("songSubName");}
            if (jsonFile.get("authorName") != null)
            {authorName = (String) jsonFile.get("authorName");}
            if (jsonFile.get("beatsPerMinute") != null)
            {beatsPerMinute = ((Number)jsonFile.get("beatsPerMinute")).doubleValue();}
            if (jsonFile.get("previewStartTime") != null)
            {previewStartTime = ((Number)jsonFile.get("previewStartTime")).doubleValue();}
            if (jsonFile.get("previewDuration") != null)
            {previewDuration = ((Number)jsonFile.get("previewDuration")).doubleValue();}
            if (jsonFile.get("coverImagePath") != null)
            {coverImagePath = (String) jsonFile.get("coverImagePath");}
            if (jsonFile.get("environmentName") != null)
            {environmentName = (String) jsonFile.get("environmentName");}
            if (jsonFile.get("songTimeOffset") != null)
            {songTimeOffset = ((Number)jsonFile.get("songTimeOffset")).doubleValue();}
            if (jsonFile.get("shuffle") != null)
            {shuffle = ((Number)jsonFile.get("shuffle")).doubleValue();}
            if (jsonFile.get("shufflePeriod") != null)
            {shuffle = ((Number)jsonFile.get("shufflePeriod")).doubleValue();}
            if (jsonFile.get("noteHitVolume") != null)
            {noteHitVolume = ((Number)jsonFile.get("noteHitVolume")).doubleValue();}
            if (jsonFile.get("noteMissVolume") != null)
            {noteMissVolume = ((Number)jsonFile.get("noteMissVolume")).doubleValue();}
            if (jsonFile.get("difficultyLevels") != null)
            {difficultyLevels = (JSONArray) jsonFile.get("difficultyLevels");}
            oneSaber = true;

        }
        catch (FileNotFoundException e){
            System.out.println("Error: FileNotFoundException.");
        }
    }
    private void getJsonSongInfo() {

        JSONParser parser = new JSONParser();
        //ImportJsonFiles json = new ImportJsonFiles();
        try {

            FileReader tempReader = new FileReader(jsonSongFilePath[3]);
            ///
            JSONObject jsonFile;
            try {
                jsonFile = (JSONObject) parser.parse(tempReader);
            } catch (Exception IOException) {
                jsonFile = null;
                System.out.println("Error: json File not initialized");
            }

            if (jsonFile.get("_version") != null) {
                version = (String) jsonFile.get("_version");
            }
            if (jsonFile.get("_beatsPerMinute") != null) {
                beatsPerMinute = ((Number) jsonFile.get("_beatsPerMinute")).doubleValue();
            }
            if (jsonFile.get("_beatsPerBar") != null) {
                beatsPerBar = ((Number) jsonFile.get("_beatsPerBar")).doubleValue();
            }
            if (jsonFile.get("_noteJumpSpeed") != null) {
                noteJumpSpeed = ((Number) jsonFile.get("_noteJumpSpeed")).doubleValue();
            }
            if (jsonFile.get("_shuffle") != null) {
                shuffle = ((Number) jsonFile.get("_shuffle")).doubleValue();
            }
            if (jsonFile.get("_shufflePeriod") != null) {
                shufflePeriod = ((Number) jsonFile.get("_shufflePeriod")).doubleValue();
            }
            events = (JSONArray) jsonFile.get("_events");
            notes = (JSONArray) jsonFile.get("_notes");
            obstacles = (JSONArray) jsonFile.get("_obstacles");
        }
        catch (FileNotFoundException e){
            System.out.println("Error: FileNotFoundException.");
        }

    }
    private void changeNotes() {
        Iterator<JSONObject> notesIt = notes.iterator();
        while (notesIt.hasNext()) {
            JSONObject noteObject = (JSONObject) notesIt.next();
            Iterator<JSONObject> listOf5Itr = noteObject.entrySet().iterator();
            //LinkedList<Double> eachFive = new LinkedList<>();
            while (listOf5Itr.hasNext()) {
                String[] tempArray = new String[5];
                //LinkedList<Double> doubArray = new LinkedList<>();
                Map.Entry pair = (Map.Entry) listOf5Itr.next();
                //Iterator<JSONObject> listOf5Itr = noteObject.iterator();
                if (pair.getKey().equals("_type")) {
                    tempArray[3] = String.valueOf(pair.getValue());
                    String tempValue = tempArray[3];
                    if (tempValue != null && !tempValue.isEmpty()) {
                        double tempDouble = Double.parseDouble(tempValue);
                        if (tempDouble == 0)
                        {
                            noteObject.put("_type",1);
                        }
                    }
                }
            }

            //noteObject.put("_type",1);
            System.out.println(noteObject);

        }
        JSONObject object = new JSONObject();
        object.put("_version",version);
        object.put("_beatsPerMinute",beatsPerMinute);
        object.put("_beatsPerBar",beatsPerBar);
        object.put("_noteJumpSpeed",noteJumpSpeed);
        object.put("_shuffle",shuffle);
        object.put("_shufflePeriod",shufflePeriod);
        object.put("_events",events);
        object.put("_notes",notes);
        object.put("_obstacles",obstacles);
        try (FileWriter file = new FileWriter(jsonSongFilePath[3])) {
            file.write(object.toJSONString());
            System.out.println("Successfully Copied JSON Array to File...");
            System.out.println("\nJSON Array: " + object);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void writeInfo() {
        JSONObject newInfo = new JSONObject();
        newInfo.put("songName",songName);
        newInfo.put("songSubName",songSubName);
        newInfo.put("authorName",authorName);
        newInfo.put("beatsPerMinute",beatsPerMinute);
        newInfo.put("previewStartTime",previewStartTime);
        newInfo.put("previewDuration",previewDuration);
        //private String audioPath;
        newInfo.put("coverImagePath",coverImagePath);
        newInfo.put("environmentName",environmentName);
        newInfo.put("songTimeOffset",songTimeOffset);
        newInfo.put("shuffle",shuffle);
        newInfo.put("shufflePeriod",shufflePeriod);
        newInfo.put("oneSaber",oneSaber);
        newInfo.put("noteHitVolume",noteHitVolume);
        newInfo.put("noteMissVolume",noteMissVolume);
        newInfo.put("difficultyLevels",difficultyLevels);
        try (FileWriter file = new FileWriter(jsonInfoFilePath)) {
            file.write(newInfo.toJSONString());
            System.out.println("Successfully Copied JSON Object to File...");
            System.out.println("\nJSON Object: " + newInfo);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void printInfo() {
        System.out.println("******************JSON INFORMATION******************");
        //System.out.println("Version: " + version);
        System.out.println("Song Name: " + songName);
        System.out.println("Artist Name: " + songSubName);
        System.out.println("Beatmap Author: " + authorName);
        System.out.println("Beats Per Minute: " + beatsPerMinute);
        System.out.println("Preview Start Time: " + previewStartTime);
        System.out.println("Preview Duration: " + previewDuration);
        //System.out.println("Audio Path: " + audioPath);
        System.out.println("Cover Image FilePath: " + System.getProperty("user.dir") + coverImagePath );
        System.out.println("Environment Name: " + environmentName);
        System.out.println("Song Time Offset: " + songTimeOffset);
        System.out.println("Shuffle: " + shuffle);
        System.out.println("Shuffle Period: " + shufflePeriod);
        System.out.println("One Saber: " + oneSaber);
        System.out.println("Note Hit Volume: " + noteHitVolume);
        System.out.println("Note Miss Volume: " + noteMissVolume);
        System.out.println("Difficulty Levels: " + difficultyLevels);
        //System.out.println("Beats Per Bar: " + beatsPerBar);
        //System.out.println("Note Jump Speed: " + noteJumpSpeed);
        System.out.println("*********************************************************");
        System.out.println("******************SONG JSON INFORMATION******************");
        System.out.println("Version: " + version);
        System.out.println("Beats Per Minute: " + beatsPerMinute);
        System.out.println("Beats Per Bar: " + beatsPerBar);
        System.out.println("Note Jump Speed: " + noteJumpSpeed);
        System.out.println("Shuffle: " + shuffle);
        System.out.println("Shuffle Period: " + shufflePeriod);



    }
    private void sortNotesByTime() {
        Iterator<JSONObject> notesIt = notes.iterator();
        while (notesIt.hasNext()) {
            JSONObject noteObject = (JSONObject) notesIt.next();
            Iterator<JSONObject> listOf5Itr = noteObject.entrySet().iterator();
            LinkedList<Double> eachFive = new LinkedList<>();
            while (listOf5Itr.hasNext())
            {
                String[] tempArray = new String[5];
                LinkedList<Double> doubArray = new LinkedList<>();
                Map.Entry pair = (Map.Entry) listOf5Itr.next();
                //System.out.println(pair);
                if (pair.getKey().equals("_time")) {
                    tempArray[0] = String.valueOf(pair.getValue());
                    String tempValue = tempArray[0];
                    if (tempValue != null && !tempValue.isEmpty()) {
                        double tempDouble = Double.parseDouble(tempValue);
                        ///this "time" isnt in seconds, but in ascending beats. We need to oonvert it to seconds. secondsIn = ((_time)/(BPM/60))
                        //System.out.println("DEBUG: " + beatsPerMinute);
                        double secondsIn = ((tempDouble)/(beatsPerMinute/60));
                        //System.out.println("Temp: secondsIn =" + secondsIn + "||" + tempDouble + "," + bpm + "," + (bpm/60));
                        eachFive.add(secondsIn);
                    } else {
                        eachFive.add(0.0);
                    }
                    //System.out.println("time: " + tempArray[0]);
                }
                else if (pair.getKey().equals("_lineIndex")) {
                    tempArray[1] = String.valueOf(pair.getValue());
                    String tempValue = tempArray[1];
                    if (tempValue != null && !tempValue.isEmpty()) {
                        double tempDouble = Double.parseDouble(tempValue);
                        //System.out.println("temp Double: " + tempDouble);
                        eachFive.add(tempDouble);
                    } else {
                        eachFive.add(0.0);
                    }
                    //System.out.println("lI: " + tempArray[1]);
                }
                else if (pair.getKey().equals("_lineLayer")) {
                    tempArray[2] = (String.valueOf(pair.getValue()));
                    String tempValue = tempArray[2];
                    if (tempValue != null && !tempValue.isEmpty()) {
                        double tempDouble = Double.parseDouble(tempValue);
                        //System.out.println("temp Double: " + tempDouble);
                        eachFive.add(tempDouble);
                    } else {
                        eachFive.add(0.0);
                    }
                    //System.out.println("lL: " + tempArray[2]);
                }

                else if (pair.getKey().equals("_type")) {
                    tempArray[3] = String.valueOf(pair.getValue());
                    String tempValue = tempArray[3];
                    if (tempValue != null && !tempValue.isEmpty()) {
                        double tempDouble = Double.parseDouble(tempValue);
                        //System.out.println("temp Double: " + tempDouble);
                        eachFive.add(tempDouble);
                    } else {
                        doubArray.add(0.0);
                    }
                    //System.out.println("type: " + tempArray[3]);
                }
                else if (pair.getKey().equals("_cutDirection")) {
                    tempArray[4] = String.valueOf(pair.getValue());
                    String tempValue = tempArray[4];
                    if (tempValue != null && !tempValue.isEmpty()) {
                        double tempDouble = Double.parseDouble(tempValue);
                        //System.out.println("temp Double: " + tempDouble);
                        eachFive.add(tempDouble);
                    } else {
                        eachFive.add(0.0);
                    }
                    //System.out.println("cD: " + tempArray[4]);
                }
            }
            notesList.add(eachFive);
        }
        //System.out.println("notesList:" + notesList);
        ListMultimap<Double,LinkedList<Double>> tempMap = jSonToTimeDomainMap(notesList);
        //System.out.println(tempMap);
        changeImpossibleNotes(tempMap);

    }
    /*
if theres more than one note per time interval
	if theres more than only blue notes
		if there is only one blue and one red
			rotate the red and add it to the blue
		if theres more reds then blue
			delete the blue
		if theres multiple reds and multiple blues
			delete the reds

if cutDir == > and theres space to the left,
	move it to the left and put cutDir = blue cutDir
if cutDir == > and theres space to the right
	move it to the right and put cutDir = blue cutDir

same for all 8 directions?
_cutDirection: the direction which the note has to be cut
0 is cut up,
1 is cut down,
2 is cut left,
3 is cut right,
4 is cut up left,
5 is cut up right,
6 is cut down left,
7 is cut down right,
8 is cut any direction
*/
    private void changeImpossibleNotes(ListMultimap<Double,LinkedList<Double>> tempMap)
    {
        double lastKey = 0;
        LinkedList<Double> keysWithMultiple = new LinkedList<>();
        for (Object key : tempMap.keys())
        {
            //System.out.println("KEY: " + key);
            if ((lastKey == (Double)key) && (!keysWithMultiple.contains(lastKey)))
            {
                keysWithMultiple.add(lastKey);
                //System.out.println("DUPLICATE KEY: " + lastKey);
            }
            lastKey = (Double)key;
        }
        //System.out.println("FINAL LIST: " + keysWithMultiple);

        Iterator multipleItr = keysWithMultiple.iterator();
        while (multipleItr.hasNext())
        {
            double tempKeyAt = (Double)multipleItr.next();
            List<LinkedList<Double>> duplicateData = (List<LinkedList<Double>>)tempMap.get(tempKeyAt);
            System.out.println("Duplicate Data" + duplicateData);
            if (duplicateData.size() == 2)
            {
                LinkedList dataPoint1 = duplicateData.get(0);
                LinkedList dataPoint2 = duplicateData.get(1);
                //Rotate red to match blues open spot
            }
            else if (duplicateData.size() > 2)
            {
                Iterator dupDataItr = duplicateData.iterator();
                int numbOfBlues = 0;
                int numbOfReds = 0;
                while (dupDataItr.hasNext())
                {
                    LinkedList<Double> onNote = (LinkedList<Double>)dupDataItr.next();
                    double type = onNote.get(2);
                    if (type == 1)
                        {numbOfBlues++;}
                    if (type == 0)
                        {numbOfReds++;}
                }
                if (numbOfBlues >= numbOfReds)
                {
                    Iterator dupDataItr2 = duplicateData.iterator();
                    while (dupDataItr2.hasNext())
                    {
                        LinkedList<Double> onNote2 = (LinkedList<Double>)dupDataItr2.next();
                        double type2 = onNote2.get(2);
                        if (type2 == 0)
                        {duplicateData.remove(type2);}


                    }
                }
                else
                    {
                        Iterator dupDataItr2 = duplicateData.iterator();
                        while (dupDataItr2.hasNext())
                        {
                            LinkedList<Double> onNote2 = (LinkedList<Double>)dupDataItr2.next();
                            double type2 = onNote2.get(2);
                            if (type2 == 1)
                            {duplicateData.remove(type2);}


                        }
                    }
            }
            tempMap.replaceValues(tempKeyAt,duplicateData);
        }
        System.out.println("FINAL MAP: " + tempMap);

    }

    public ListMultimap<Double,LinkedList<Double>> jSonToTimeDomainMap(LinkedList<LinkedList<Double>> notesList)
    {
        ListMultimap<Double,LinkedList<Double>> multimap = ArrayListMultimap.create();
        //TreeMap<Double,LinkedList<Double>> fullJSonMap = new TreeMap<>();
        Iterator fullDataIterator = notesList.iterator();
        while (fullDataIterator.hasNext()) {
            LinkedList<Double> at5 = (LinkedList<Double>)fullDataIterator.next();
            double timeAt = at5.remove(3);
            //System.out.println("DEBUG: " + timeAt);
            multimap.put(timeAt,at5);
        }

        return multimap;
    }



}
