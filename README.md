# test2_redo


|Token Code | Operator   |  Regex
|-----------|------------|-----------
|cong       |    +       |    +
|tru      |       -    |     -
|nhan        |      *     |     *
|chia        |     /      |     /
|du        |    %       |      %
|phu_dinh | ~     |    ~
|Tron_Mo   |     (      |      (
|Tron_Dong    |       )    |     )
|Nhon_Mo     |      {     |      {
|Nhon_Dong     |      }     |     }
|B         |     =      |     =
|KB | ~= | ~=
|BB         |       ==   |     ==
|N         |       <    |    <
|L        |      >     |    >
|NHB     |     <=     |    <=
|LHB        |   >=       |    >=

Priority Order
- ( )
-  \+
-  \-
-  \*
-  \/
-  \%


Keyword Types
|Token Code| Regex   
|----------|------------
|ten        |  [_a-zA-Z]{6,8}
|bat_dau     |  bat_dau
|ket_thuc    | ket_thuc
|laplai    |  laplai
|neu   |  neu
|khongthi|khongthi
|String| "([^"]|\\[sS])*"
|ki_tu | '([^']|\\[sS])?'


Data Type 
|Name   |     Range                            |   Size      |   Regex
|-------|--------------------------------------|-------------|------------
|t     |    -128 to 127                       |  1 byte     |   [0-9]+      
|i     |    -32768 to 32767                   |  2 bytes    |   [0-9]+ 
|e     |     -2,147,483,648 to 2,147,483,647  |  4 bytes    |   [0-9]+ 
|n    |     -9,223,372,036,854,775,808 to 9,223,372,036,854,775,807      |   8 bytes   |  [0-9]+
      

Production rules:
- <program> bat_dau \<stmt>; ket_thuc
- \<stmt> --> \<ifstmt> | \<while_loop> | \<assign> | \<declaration> 
- \<block> --> '{' <stmt>; '}'
- \<if_stmt> --> neu '(' \<bool_expr> ')' <block> khongthi  <block>
- \<while_loop> --> laplai '(' \<bool_expr> ')' <block>
- \<bool_exp> --> \<expr> { < | > | <=| >= | == } \<expr> 
- \ten --> [_a-zA-Z]{6,8}
- \<assign> --> ten \`=` \<expr>
- \<declaration> --> \<dtype> ten \`;`
- \<dtype> --> {t|i|e|n}
- \<expr> --> \<term> { (\`+\`|\`-\`) \<term> }
- \<term> --> \<factor> { (\`*\`|\`/\`|\`%\`) \<factor> }
- \<factor> --> 'id' | \`(\` \<expr> \`)\`


- S -> E
- E -> E + T | E - T | T 
- T -> T * F | T / F | T % F | F
- F -> ten | ( E ) | t | i | e | n

LL Grammar:
Those production rules are LR Grammar because it is read from left to right and the recursive descent parse use 
the current input symbol to choose which path it should take. It passes the pairwise disjointness test. 

Parse Table:<img width="954" alt="Screenshot 2022-12-07 at 5 05 26 PM" src="https://user-images.githubusercontent.com/72286897/206306858-fa212867-a8a4-4ab2-9c9c-a821b978b4af.png">
<img width="945" alt="Screenshot 2022-12-07 at 5 05 42 PM" src="https://user-images.githubusercontent.com/72286897/206306875-5357c423-3878-410b-b6f9-fa4f2adba23f.png">
<img width="944" alt="Screenshot 2022-12-07 at 5 05 56 PM" src="https://user-images.githubusercontent.com/72286897/206306887-778502ca-3bf7-41a4-8b67-c9c315b781d6.png">
<img width="945" alt="Screenshot 2022-12-07 at 5 06 08 PM" src="https://user-images.githubusercontent.com/72286897/206306895-a05de016-18ae-477f-95a1-1fd212c03907.png">


 
- Case pass: ten + ten - ( t )
<img width="954" alt="Screenshot 2022-12-07 at 5 08 12 PM" src="https://user-images.githubusercontent.com/72286897/206307124-15594824-976a-45c4-a19c-52e9276afa16.png">
- Case pass: i * n / ( ten ) + e 
<img width="982" alt="Screenshot 2022-12-07 at 5 09 58 PM" src="https://user-images.githubusercontent.com/72286897/206307432-285fdd2c-f20d-47ea-937b-9a7972aee440.png">
- Case fail: ten ^ ten - t + i --> Unrecognized symbol ^
<img width="355" alt="Screenshot 2022-12-07 at 5 11 32 PM" src="https://user-images.githubusercontent.com/72286897/206307687-07c2a493-e22b-46e6-bc4f-f317979ea412.png">
- Case fail: 
<img width="322" alt="Screenshot 2022-12-07 at 5 18 25 PM" src="https://user-images.githubusercontent.com/72286897/206308840-ab606234-c339-4502-8ba0-a266c8e447e1.png">
