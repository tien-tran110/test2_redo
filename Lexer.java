import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class Lexer {
    private char ch;
    private String str;
    int pos;
    int position;
    Map<String, TokenType> Tokens = new HashMap<>();
    List<Token> output = new LinkedList<>();

    //function for error
    void error(String e){
        System.out.println(e);
        System.exit(0);
    }

    //Constructor for Lexer
    Lexer(String source){
        this.str = source;
        this.pos = 0;
        this.position = 0;
        this.ch = this.str.charAt(0);
        this.Tokens.put("neu", TokenType.neu);//if
        this.Tokens.put("khongthi", TokenType.khongthi);//else
        this.Tokens.put("laplai", TokenType.laplai);//while
        this.Tokens.put("in", TokenType.in);//print
        this.Tokens.put("bat_dau", TokenType.bat_dau);
        this.Tokens.put("ket_thuc", TokenType.ket_thuc);
        
    }

    //function to check the following char
    Token follow(char expect, TokenType ifyes, TokenType ifno){
        String symbol = Character.toString(this.ch);
        if(getNextChar() == expect){
            symbol += Character.toString(this.ch);
            getNextChar();
            return new Token(ifyes, symbol);
        }
        if(ifno == TokenType.EOI){
            error(String.format("follow: unrecognized character: %c", symbol));
        }

        return new Token(ifno, symbol);
    }

    //check for character literal 
    Token char_liter(){
        char c = getNextChar();//skip opening quote
        String lexemes = "";
        if(c == '\''){
            error("empty character constant");
        }
        else if( c == '\\'){
            c = getNextChar();
            if(c == 'n' || c == '\\'){
                lexemes += c;
            }
            else{
                error(String.format("unknown escapse sequence \\%c", c));
            }
        }
        if(getNextChar() != '\''){
            error("multi-character literal");
        }
        getNextChar();

    return new Token(TokenType.ki_tu, lexemes);
    }

    //chekc for string literal
    Token string_liter(){
        String output = "";
        while(getNextChar() != '\"'){
            if(this.ch == '\u0000'){//check for null
                error("EOF while scanning for string");

            }
            if(this.ch == '\n'){
                error("EOL while scanning for string");
            }

            output += this.ch;
        } 
        getNextChar();
        return new Token(TokenType.String, output);       
    }

    Token div_or_cmt() {
        if (getNextChar() != '*') {
            return new Token(TokenType.chia, Character.toString(ch));
        }
        getNextChar();
        while (true) { 
            if (this.ch == '\u0000') {
                error("EOF in comment");
            } else if (this.ch == '*') {
                if (getNextChar() == '/') {
                    getNextChar();
                    return getToken();
                }
            } else {
                getNextChar();
            }
        }
    }

    //check for identifer or number

    Token ident_or_num() {
        boolean is_number = true;
        String output = "";
        
        while (Character.isAlphabetic(this.ch) || Character.isDigit(this.ch) || this.ch == '_') {
            output += this.ch;
            if (!Character.isDigit(this.ch)) {
                is_number = false;
            }
            getNextChar();
        }
        if(output.equals("t") || output.equals("i") || output.equals("e") || output.equals("n")){
            return new Token(TokenType.datatype, output);
        }
        
        if (output.equals("")) {
            error(String.format("identifer or number unrecognized character: %c", this.ch));
        }
        
        if (Character.isDigit(output.charAt(0))) {
            if (!is_number) {
                error(String.format("invalid number: %s", output));
            }
            if(Long.parseLong(output) < 127 && Long.parseLong(output) > -128 )
                return new Token(TokenType.t, output);
            else if(Long.parseLong(output) < 32767 && Long.parseLong(output) > -32768 )
                return new Token(TokenType.i, output);
            else if(Long.parseLong(output) < 2147483647 && Long.parseLong(output) > -2147483648 )
                return new Token(TokenType.e, output);
            else
                return new Token(TokenType.n, output);
            
        }
        
        if (this.Tokens.containsKey(output)) {
            return new Token(this.Tokens.get(output), output);
        }
        return new Token(TokenType.ten, output);
    }
    

    Token getToken() {
        //ignore white space
        while (Character.isWhitespace(this.ch)) {
            getNextChar();
        }
        char c = this.ch;
        switch (this.ch) {
            
            case '\u0000': return new Token(TokenType.EOI, "");
            case '/': return div_or_cmt();
            case '\'': return char_liter();
            case '<': return follow('=', TokenType.NHB, TokenType.N);
            case '>': return follow('=', TokenType.LHB, TokenType.L);
            case '=': return follow('=', TokenType.BB, TokenType.B);
            case '~': return follow('=', TokenType.KB, TokenType.phu_dinh);//logical not
            case '&': return follow('&', TokenType.va, TokenType.EOI);
            case '|': return follow('|', TokenType.hoac_la, TokenType.EOI);
            case '"': return string_liter();
            case '{': getNextChar(); return new Token(TokenType.Nhon_Mo, Character.toString(c));
            case '}': getNextChar(); return new Token(TokenType.Nhon_Dong, Character.toString(c));
            case '(': getNextChar(); return new Token(TokenType.Tron_Mo, Character.toString(c));
            case ')': getNextChar(); return new Token(TokenType.Tron_Dong, Character.toString(c));
            case '+': getNextChar(); return new Token(TokenType.cong, Character.toString(c));
            case '-': getNextChar(); return new Token(TokenType.tru, Character.toString(c));
            case '*': getNextChar(); return new Token(TokenType.nhan, Character.toString(c));
            case '%': getNextChar(); return new Token(TokenType.du, Character.toString(c));
            case ';': getNextChar(); return new Token(TokenType.cham_phay, Character.toString(c));
            case ',': getNextChar(); return new Token(TokenType.phay, Character.toString(c));
            default: return ident_or_num();
        }
    }

    List<Token> printTokens() {
        Token t;
        while ((t = getToken()).type != TokenType.EOI) {
            output.add(t);
        }
        return output;
    }
     
    
    //function to get the next char
    char getNextChar(){
        this.position++;

        if(this.position >= this.str.length()){
            this.ch = '\u0000';
            return this.ch;
        }

        this.ch = this.str.charAt(this.position);
        return this.ch;
    }
    
}
class Token{
    public TokenType type;
    public String value;
    
        

    //constructor for token
    Token(TokenType _type, String _value){
        this.type = _type;
        this.value = _value;

    }

    @Override
    public String toString(){
        return type + " : " + value;
    }
}

enum TokenType{
    EOI,
    cong, //+
    tru, //-
    nhan, //* 
    chia, // /
    du, //%
    Tron_Mo, // (
    Tron_Dong, // )
    Nhon_Mo, // {
    Nhon_Dong, //}
    B, // =
    phu_dinh, //~
    va,//&&
    hoac_la,//||
    BB, //==
    KB, //~= 
    L, // >
    N, // < 
    LHB, // >=
    NHB, //<=
    cham, //.
    phay, //,
    cham_phay, //;
    ten, //identifier
    datatype,//t|i|e|n
    natural_num, 
    t, //data type from -128 to 127
    i, //-32768 to 32767
    e, //-2,147,483,648 to 2,147,483,647
    n, //-9,223,372,036,854,775,808 to 9,223,372,036,854,775,807	
    String, 
    ki_tu, //character
    neu, //if
    khongthi, //else
    laplai, //while
    in, //print
    bat_dau,
    ket_thuc
}