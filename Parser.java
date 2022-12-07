import java.util.List;

public class Parser {
    private List<Token> source;
    private Token token;
	private int position;
    
    //constructor
    Parser(List<Token> source) {
		this.source = source;
        this.position = 0;
		this.token = getNextToken();

	}
    void parse(){
        if(this.token.type == TokenType.bat_dau){//begin of a program
            System.out.println("Enter program <stmt>");
            while(getNextToken().type != TokenType.ket_thuc){
                stmt();
            }
            if(this.token.type == TokenType.ket_thuc){
                System.out.println("Parse successfull");
            }
            else{
                error(String.format("Expected ending: %s", TokenType.ket_thuc));
            }
        }
        else{
            error(String.format("Expected heading: %s", TokenType.bat_dau));
        }
    }
    //function to return the next Token
    Token getNextToken() {
		this.token = this.source.get(this.position++);
		return this.token;
	}

    // <stmt> --> <ifstmt> | <while_loop> | <as_s> | <declaration>
    void stmt(){
        if(this.token.type == TokenType.neu){
            System.out.println("Enter <ifstmt>");
            ifstmt();
        }
        else if(this.token.type == TokenType.laplai){
            System.out.println("Enter <while_loop>");
            while_loop();
        }
        else if(this.token.type == TokenType.ten){
            System.out.println("Enter <assign>");
            assign();
        }
        else{
            System.out.println("Enter <declaration>");
            declaration();
        }
    }

    //ifstmt --> neu '(' <bool_exp> ')' <block> khongthi <block>
    void ifstmt(){
        if(getNextToken().type  == TokenType.Tron_Mo){//(
            bool_exp();
            if(getNextToken().type  == TokenType.Tron_Dong){
                block();
                if(getNextToken().type  == TokenType.khongthi){
                    block();
                    System.out.println("Parse <ifstmt> done");
                }
                else{
                    error(String.format("Expected %s", TokenType.khongthi));//else
                }
            }
            else{
                error("Expected ')'");
            }
        }
        else{
            error("Expected '('");
        } 
    }

    //while_loop --> laplai '(' <bool_exp> ')' <block>
    void while_loop(){
        
        if(getNextToken().type == TokenType.Tron_Mo){
            bool_exp();
            if(getNextToken().type == TokenType.Tron_Dong){
                block();
                System.out.println("Parse <while_loop> done");
            }
            else{
                error("Expected ')");
            }
        }
        else{
            error("Expected '(");
        }
    }

    //<assign> --> ten '=' <expr>;
    void assign(){
        if(getNextToken().type == TokenType.B){//=
            System.out.println("Enter <expr>");
            expr();
            if(this.token.type == TokenType.cham_phay){//;
                System.out.println("Parse <assign> done");
            }
            else{
                error("Expected ';'");
            }
        }
        else{
            error("Expected '='");
        }
    }

    //<expr> --> <term> {(+ |-) <term>}
    void expr(){
        term();
        if(this.token.type == TokenType.cong || this.token.type == TokenType.tru){
            term();
        }
        System.out.println("<expr> parse done");
    }

    //<term> --> <factor> {(*|/|%) <factor>} 
    void term(){
        factor();
        getNextToken();
        if(token.type == TokenType.nhan || token.type == TokenType.chia || token.type == TokenType.du){ // token == + | -
            factor();
        }
        System.out.println("<term> parse done");

    }

    //<factor> --> ten | '(' <expr> ')' | <datatype>
    void factor(){
        getNextToken();
        if(token.type == TokenType.ten){
            System.out.println("done <factor>");
        }
        else if(token.type == TokenType.Tron_Mo){
            expr();
            if(getNextToken().type == TokenType.Tron_Dong){
                System.out.println("done <factor>");
            }
            else{
                error("Expected ')");
            }
        }
        else{
            datatype();
            System.out.println("done <factor>");
        }
    }

    //<datatype> --> {t|i|e|n}
    void datatype(){
       if(this.token.type == TokenType.t
       || this.token.type == TokenType.i
       || this.token.type == TokenType.e
       || this.token.type == TokenType.n){
        System.out.println("<datatype>");
        
       }
       else{
        error(String.format("Expected %s", TokenType.datatype));
       }
        
    }

    //<declaration> --> <datatype> ten;
    void declaration(){
        if(getNextToken().type == TokenType.ten){
            if(getNextToken().type == TokenType.cham_phay){
                System.out.println("Parse <declaration> done");
            }
            else{
                error("Expected ';'");
            }  
        }
        else{
            error(String.format("Expected id %s", TokenType.ten));//expected id
        }
    }

    //<block> --> '{' <stmt>; '}'
    void block(){
        if(getNextToken().type == TokenType.Nhon_Mo){
            stmt();
            if(getNextToken().type == TokenType.cham_phay){
                if(getNextToken().type == TokenType.Nhon_Dong){
                    System.out.println("Parse <block> done");
                }
                else{
                    error("Expected '}'");
                }
            }
            else{
                error("Expected ';'");
            }
        }
        else{
            error("Expected '{'");
        }

    }

    //<bool_expr> -->  <expr> { < | > | <= | >= | == } <expr>  
    void bool_exp(){
        System.out.println("Enter <bool_exp>");
        expr();
        getNextToken();
        if(token.type == TokenType.B || token.type == TokenType.BB
        || token.type == TokenType.L || token.type == TokenType.LHB
        || token.type == TokenType.BB){
            expr();
            System.out.println("done <bool_exp>");
        }
        
    }

    void error(String msg){
        System.out.println(msg);
        System.exit(-1);
    }
}
