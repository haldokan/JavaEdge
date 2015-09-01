/**
 * Created by haytham.aldokanji on 9/1/15.
 */

//=============================================
// Objects
//=============================================
// Javascripts objects are simple name-val paris similar to hash maps. Javascript is heavily dependent on hash lookups (similar to Python)
// empty obj
var obj = new Object();
// or similar to Python using object literal - preferred. Used also in JSON
var obj = {};

var blog = {
    author: "haldokan",
    date: new Date("2015-09-01"),
    tag: "programming",
    entry: {
        text: "Blockchain technology can have disruptive effects on the way consistent global ledgers are designed and implemented"
    }
};
document.writeln("blog> " + blog.toString()); // it doesn't print the obj; must be a way to do that
document.writeln(blog.author);
document.writeln(blog.entry.text);
//accessing the obj attrs can be done also using array syntax
document.writeln(blog["entry"]["text"]);

// Javascript does not have classes. It has functions that act like classes
function Blog(author, entry) {
    this.author = author;
    this.entry = entry;
}

// create an instance of the Blog func
var myBlog = new Blog("haldokan", "Concurrent hash maps reley on read/write locks and separte hash spaces to increase concurrency");
document.writeln(myBlog.author);
document.writeln(myBlog["entry"]); // using field name as string allows run-time based access to object maps but forgoes optimizations

//change field value
myBlog.author = "Mr. Bean";
// or
myBlog["entry"] = "I've got a funny joke!";
document.writeln(myBlog.author);
document.writeln(myBlog["entry"]);

// can we just add a new attr to the obj similar to Python? Sure!
myBlog.tag = "tech";
document.writeln(myBlog.tag);
