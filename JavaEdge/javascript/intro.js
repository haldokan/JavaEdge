/**
 * Created by haytham.aldokanji on 9/1/15.
 */

//=============================================
// Types
//=============================================
// Number, String, Boolean, Symbol(edition 6), Object(Function, Array, Date, RegExp), null, undefined

//=============================================
// Numbers
//=============================================
document.writeln(Math.PI);
// must always specify the base to avoid surprises
document.writeln(parseInt("00123", 10));
document.writeln(parseInt("00777", 10));
// convert binary: 1001 is 9
document.writeln(parseInt("1001", 2));
document.writeln(123 + 2);
//special value NaN
document.writeln(isNaN(parseInt("foobar"), 10));
// Infinity
document.writeln(isFinite(1 / 0));
document.writeln(isFinite(-Infinity));
// parseInt and parseFloat reads the string until the first unvalid char and convert the valid part!
document.writeln(parseInt("0123foobar"));

//=============================================
// Strings
//=============================================
document.writeln("foobar".length);
document.writeln("foobar".charAt(0));
document.writeln("foobar".replace("bar", "baz"));
document.writeln("foobar".toUpperCase());

//=============================================
// Other types
//=============================================
// undefined is a type for vars that are not initialized (consts), and null indicate a deliberate non-value
// boolean false is: false, 0, empty string, Nan, null, and undefined. All other values are true
document.writeln(Boolean(""));
document.writeln(Boolean(null));
document.writeln(Boolean(0));
document.writeln(Boolean("foobar"));
document.writeln(Boolean("123"));
document.writeln(null && "123");

//=============================================
// Variables
//=============================================
// no assignment so the var val is 'undefined' (not null).
var v1;
var v2 = "foobar";

// like pythons var declarations escapse their defs blocks. However funcs have scopes. Starting with ECMA6 let and const allow
// block-scoped declarations

//=============================================
// Operators
//=============================================
var v = 3;
document.writeln(v + 4);
document.writeln("foo" + "bar");
// bcz javascript parse forward only it adds 3 an 4 then it realizes that the next operand is string
document.writeln(3 + 4 + "foobar");
//compare with previous line; the + here works as string concatenation
document.writeln("foobar" + 3 + 4);

// 2 equality operators == which performs type coercion and === which does not
document.writeln(1 == true); // true since 1 is coerced to boolean true
document.writeln(1 === true); // false since 1 is not converted and is considered a number
//there are also != and !==
document.writeln("foobar" == "foobar");

//=============================================
// Control structures
//=============================================
var lang = "German";
var greeting;
if (lang == "German") {
    greeting = "Hallo";
} else if (lang == "French") {
    greeting = "Bonjour";
} else {
    greeting = "Hello"
}
document.writeln(lang + " - " + greeting);

var i = 0;
while (i++ < 3) {
    document.writeln("loop" + i);
}
// do-while insure loop is executed at least once similar to java
var j = -3;
do {
    document.writeln("loop" + j);
} while (j > 5);

// for loop comme toujours
for (var i = 0; i < 3; i++) {
    document.writeln("fooloop" + i);
}

// && and || operator short-circuit similar to other languages
var str;
var strLen = str && str.length(); // strLen will have undefined val
document.writeln(strLen);

var strLen2 = str || 7;
document.writeln(strLen2);

// ternary operator similar to Java
var income = 10000;
var exemptFromTaxes = (income < 12000) ? "yes" : "no";
document.writeln(exemptFromTaxes);
exemptFromTaxes = (income < 8000) ? true : false;
document.writeln(exemptFromTaxes);

// switch stmt can work with numbers or strings. Thw switch and case stmts can have expressions (switch(intval + 3), or case 1 + 2
var language = "French";
switch (language) {
    case "French":
        document.writeln("Salut");
        break;
    case "German":
        document.writeln("Hallo");
        break;
    default:
        document.writeln("Hello");
}
