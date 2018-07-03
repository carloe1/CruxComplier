package crux;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import javafx.scene.shape.Line;

public class Compiler {
    public static String studentName = "Carlos Estrada";
    public static String studentID = "58042643";
    public static String uciNetID = "carloe1";
    
    public static void main(String[] args) throws IOException
    {
    	//String fileNumber = "12";
    	
        String sourceFilename = args[0];
        //String sourceFilename = "tests/test" + fileNumber + ".crx";
        //String answerFile = "tests/test" + fileNumber + ".out";
    	
        Scanner s = null;
        try {
            s = new Scanner(new FileReader(sourceFilename));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error accessing the source file: \"" + sourceFilename + "\"");
            System.exit(-2);
        }

        Parser p = new Parser(s);
        ast.Command syntaxTree = p.parse();
        if (p.hasError()) {
            System.out.println("Error parsing file.");
            System.out.println(p.errorReport());
            System.exit(-3);
        }
            
        types.TypeChecker tc = new types.TypeChecker();
        tc.check(syntaxTree);
        if (tc.hasError()) {
            System.out.println("Error type-checking file.");
            System.out.println(tc.errorReport());
            System.exit(-4);
        }
        System.out.println("Crux Program is has no type errors.");

        
        /*
        PrintWriter testFile = new PrintWriter("testfile.txt");
        
        if (tc.hasError()) {
            System.out.println("Error type-checking file.");
            System.out.println(tc.errorReport());
            //System.exit(-4);
        	
            testFile.println("Error type-checking file.");
        	testFile.println(tc.errorReport());
        	
        }
        else{
        	System.out.println("Crux Program has no type errors.");
        	testFile.println("Crux Program has no type errors.");
        }
        
        testFile.close();
        
        FileReader answer = new FileReader(answerFile);
        FileReader myFile = new FileReader("testfile.txt");
        
        BufferedReader br =  new BufferedReader(answer);
        BufferedReader br1 = new BufferedReader(myFile);
        
        String line1 = br.readLine();
        String line2 = br1.readLine();
        
        
        while (line1 != null){
        	System.out.print(line1.equals(line2));
        	System.out.println("	out: " + line1);
        	System.out.println("	Moi: " + line2);
        	System.out.println();
        	line1 = br.readLine();
        	line2 = br1.readLine();
        }
        */
    }
}
    
