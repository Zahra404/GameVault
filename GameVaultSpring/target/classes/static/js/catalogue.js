const someData = 'the_Data_You_Want_To_Print'

document.getElementById('printButton').onclick = function() {
    //print in DOM element
    document.getElementById('printHere').innerHTML = someData;
    //print on console
    console.log(someData)
};