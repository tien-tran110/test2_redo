import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
public class Compiler {
    public static void main(String[] args) throws FileNotFoundException{
        List<Token> output = new LinkedList<>();
        File f = new File("input.txt");
        Scanner s = new Scanner(f);
        String string = "";
        while(s.hasNext()){
            string += s.nextLine() + "\n";
        }
        s.close();
        Lexer lex = new Lexer(string);
        
        output = lex.printTokens();
        System.out.println(output);

        Parser par = new Parser(output);
        par.parse();
        
    }
}
