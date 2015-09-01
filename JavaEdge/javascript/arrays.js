/**
 * Created by haytham.aldokanji on 9/1/15.
 */

//=============================================
// Arrays
//=============================================
// similar to objects; they have length property
var a = new Array();
a[0] = "foo";
a[1] = "bar";
document.writeln(a); // actually prints the array
document.writeln(a.length);

// more convenient
var a = [1, 2, 3];
document.writeln(a.length);

// unlike java array they behave like maps
a[10] = 77;
// length is the highest index + 1 = 78
document.writeln(a.length);
// accessing and element at an index that is not defined returns "undefined"
document.writeln(a[6]);
// iterate over an array; note how we calculate the len so it is not recalculated at every iteration (unlike Java)
for (var i = 0, len = a.length; i < len; i++) {
    document.writeln(a[i]);
    if (a[i] === undefined) {
        document.write("|");
    }
}

// iteration can also be done in a fancier way. However this stops at the index of the 1st undefined item since the 'item'
// in the loop is tested for truthfulness at each iteration. This means that we should not use this iteration idiom for integer
// arrays that may have 0 or string arrays that may have empty strings. Arrays of objext or DOM nodes are good candidates for using
// this idiom however.
a[3] = 66;
for (var i = 0, item; item = a[i++];) {
    document.writeln(item);
}

//adding an element to the array
var a = [11];
a.push(22);
document.writeln(a);

// iterate using forEach (added in ECMA5)
["foo", "bar", "baz"].forEach(function (currVal, ndx, arr) {
    document.writeln(ndx + "->" + currVal + "/" + arr.length);
});

// some array methods
var cities = ["NewYork", "NewJersey", "Newark", "Brooklyn", "Bronx", "Hoboken"];
document.writeln(cities.toString());
//add to the end of the array
cities.push("FortLee", "ColdSprings");
document.writeln(cities.toString());
// add to the start of the array
cities.unshift("Houston", "LosAngelese");
document.writeln(cities.toString());
// pop from end of the array
cities.pop();
document.writeln(cities.toString());
// pop from the start of the arr
cities.shift();
document.writeln(cities);
// convert to string separated by 'sep'
document.writeln(cities.join("|"));
// reverse
document.writeln(cities.reverse());
// slice
document.writeln(cities.slice(2, 5));
//sort(compr funct), splice

