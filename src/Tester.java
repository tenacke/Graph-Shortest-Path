import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Tester {
    private static final String[] casess = new String[]{"smallCases", "largeCases"}; // check the format strings in main method
    private final String dirName; // this is the directory of the inputs
    private final String realName; // this is the directory of the outputs that you want to compare
    private final String outName; // this is the directory that you want to store your outputs
    // PLEASE CHECK THE VARIABLE NAMES ABOVE AND EDIT IF NECESSARY
    // ALL THESE DIRECTORIES MUST EXIST! IF YOU DON'T HAVE THIS DIRECTORIES PLEASE MAKE THEM
    // IF YOU GET A FILENOTFOUNDEXCEPTION ABOUT NOT EXISTING FILE TRY DELETING .DS_Store FILE
    // BELOW THIS LINE DO NOT CHANGE ANYTHING EXCEPT THE LINES 18-19
    private final String[] inputs;
    private final String[] outputs;

    public static void main(String[] args) throws IOException {
        String dirName = "cases\\%s\\input\\";
        String realName = "cases\\%s\\output\\";
        String outName = "cases\\%s\\myput\\";
        for (String cases : casess) {
            long start = System.nanoTime();
            Tester tester = new Tester(String.format(dirName, cases), String.format(realName, cases), String.format(outName, cases));
            long end = System.nanoTime();
            System.out.println("Tester initialized in " + String.format("%,.8f", (double) (end - start) / 1000000000L) + " seconds");
            tester.run(); // you can toggle this to execute the inputs
            tester.compare(); // you can toggle this to compare yourputs (outName) with some outputs (realName)}
        }
    }

    public Tester(String dirName, String realName, String outName) {
        this.dirName = dirName;
        this.realName = realName;
        this.outName = outName;
        File inDir = new File(dirName);
        File realDir = new File(realName);
        File outDir = new File(outName);
        File[] files = inDir.listFiles();
        File[] outFiles = realDir.listFiles();
        assert files != null;
        assert outFiles != null;
        inputs = new String[files.length];
        outputs = new String[outFiles.length];
        int i = 0;
        boolean hasIssue = false;
        boolean hasIssuee = false;
        for (File file : files) {
            if (file.getPath().equals(dirName.replace('/', '\\').concat(".DS_Store"))) {
                hasIssue = true;
                continue;
            }
            inputs[i++] = file.getPath();
        } i = 0;
        for (File file : outFiles) {
            if (file.getPath().equals(realName.replace('/', '\\').concat(".DS_Store"))) {
                hasIssuee = true;
                continue;
            }
            outputs[i++] = file.getPath();
        } i = hasIssue ? 1 : 0; if (hasIssuee) i--;
        if (inputs.length - i != outputs.length) {
            System.out.println("Warning: The numbers of inputs and outputs are not same!! Some inputs might not be compared");
            System.out.println("In this case you will have a FileNotFoundException so you must check your directories again");
        }
        if (outDir.mkdir())
            System.out.println("Output directory created!");
//        System.out.println(fileNumber);
    }

    private void run() throws IOException {
        for (int i = 0; i < inputs.length; i++) {
            if (inputs[i] == null)
                continue;
            long start = System.nanoTime();
            project4.main(new String[]{inputs[i], outName.concat(outputs[i].substring(realName.length()))});
            long end = System.nanoTime();
            System.out.println(inputs[i].substring(dirName.length()) + " is executed in " + String.format("%,.8f", (double) (end - start)/1000000000L) + " seconds");
        }
    }

    private void compare() throws IOException {
        for (int i = 0; i < inputs.length; i++) {
            if (inputs[i] == null)
                continue;
            File realOutput = new File(outputs[i]);
            File myOutput = new File(outName.concat(outputs[i].substring(realName.length())));

            int line = filesCompareByLine(realOutput, myOutput);
            String fileStr = inputs[i].substring(dirName.length());
            if (line == -1) {
                System.out.println(fileStr + " : True");
            } else {
                System.out.println(fileStr + " : False. Wrong line is " + line);
            }
        }
    }

    private static int filesCompareByLine(File path1, File path2) throws IOException {
        BufferedReader bf1 = new BufferedReader(new InputStreamReader(new FileInputStream(path1), StandardCharsets.UTF_8));
        BufferedReader bf2 = new BufferedReader(new InputStreamReader(new FileInputStream(path2), StandardCharsets.UTF_8));

        int lineNumber = 1;
        String line1, line2;
        while ((line1 = bf1.readLine()) != null) {
            line2 = bf2.readLine().strip();
            if (line1.strip().equals(line2)){
                lineNumber++;
            }else {
                if (!line2.equals("0"))
                    return lineNumber;
            }
        }
        if (bf2.readLine() == null)
            return -1;
        else {
            return lineNumber;
        }
    }
}
