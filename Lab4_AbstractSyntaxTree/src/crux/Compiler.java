package crux;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.file.Files;


@SuppressWarnings("unused")
public class Compiler {
    public static String studentName = "Carlos Estrada";
    public static String studentID = "58042643";
    public static String uciNetID = "carloe1";
    
    public static void main(String[] args) throws IOException
    {
        String sourceFilename = args[0];
        
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
            System.out.println("Error parsing file " + sourceFilename);
            System.out.println(p.errorReport());
            System.exit(-3);
        }
       
        ast.PrettyPrinter pp = new ast.PrettyPrinter();
        syntaxTree.accept(pp);
        System.out.println(pp.toString());
        
        /*
        PrintWriter testFile = new PrintWriter("testfile.txt");
        testFile.println(pp.toString());
        testFile.close();
        
        FileReader answer = new FileReader(args[1]);
        FileReader myFile = new FileReader("testfile.txt");
        
        BufferedReader br =  new BufferedReader(answer);
        BufferedReader br1 = new BufferedReader(myFile);
        
        String line1 = br.readLine();
        String line2 = br1.readLine();
        
        while (line1 != null){
        	System.out.println(line1.equals(line2));
        	line1 = br.readLine();
        	line2 = br1.readLine();
        }
        */
    }
}
    
