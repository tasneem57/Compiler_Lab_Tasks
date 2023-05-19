grammar Task8;

/**
 * This rule is to check your grammar using "ANTLR Preview"
 */
test:  (IF|ELSE|ID|NUM|LIT|LP|RP|COMP)+ EOF;


IF: [iI][fF];
ELSE: [eE][lL][sS][eE];
ID: LETTER+ (DIGIT |LETTER)*;
COMP: '<'|'>'|'=='| '>=' | '<=' | '!=';
NUM: INT DEC? EXPON?;
LIT: DOUBLEQOUTES ( ~["\\]| ESCAPE)* DOUBLEQOUTES ;
LP: '(' ;
RP: ')';
WS: [ \r\t\n]+ -> skip;
fragment LETTER:[A-Za-z_];
fragment DIGIT: [0-9];
fragment INT: DIGIT+;
fragment DEC: '.' INT;
fragment EXPON: [eE] [+-]? INT ;
fragment ESCAPE:  BACKSLASHSTR | DOUBLEQSTR ;
fragment DOUBLEQOUTES: '"';
fragment FIRSTBACKSLASH: '\\';
fragment BACKSLASHSTR: FIRSTBACKSLASH FIRSTBACKSLASH;
fragment DOUBLEQSTR: FIRSTBACKSLASH DOUBLEQOUTES;
