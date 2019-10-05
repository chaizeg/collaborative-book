/* adding choices - beware linkurl[]contains one null field so insertion may fail */
var ct = 1;
function new_link()
{
	ct++;
	var div1 = document.createElement('div');
	div1.id = ct;
	// link to delete extended form elements
	var delLink = '<div style="text-align:right;margin-right:65px"><a href="javascript:delIt('+ ct +')">Supprimer ce choix</a></div>';
	div1.innerHTML = document.getElementById('newlinktpl').innerHTML + delLink;
	document.getElementById('newlink').appendChild(div1);
}
// function to delete the newly added set of elements
function delIt(eleId)
{
	d = document;
	var ele = d.getElementById(eleId);
	var parentEle = d.getElementById('newlink');
	parentEle.removeChild(ele);
}

var dr=1;
function new_linkDropdown(){
	dr++;
	var div2 = document.createElement('div');
	div2.id = dr;
	// link to delete extended form elements
	var delLinkDr = '<div style="text-align:right;margin-right:65px"><a href="javascript:delItdr('+ dr +')">Supprimer ce choix</a></div>';
	div2.innerHTML = document.getElementById('newlinktplDr').innerHTML + delLinkDr;
	document.getElementById('newlinkList').appendChild(div2);
}

// function to delete the newly added set of elements
function delItdr(eleId)
{
	d = document;
	var ele = d.getElementById(eleId);
	var parentEle = d.getElementById('newlinkList');
	parentEle.removeChild(ele);
}
/*adding collaborators */
var ctt = 1;
function new_linkP()
{
	ctt++;
	var div1 = document.createElement('div');
	div1.id = ctt;
	// link to delete extended form elements
	var delLink = '<div style="text-align:right;margin-right:65px"><a href="javascript:delItP('+ ctt +')">Supprimer ce collaborateur</a></div>';
	div1.innerHTML = document.getElementById('newlinktplP').innerHTML + delLink;
	document.getElementById('newlinkP').appendChild(div1);
}
// function to delete the newly added set of elements
function delItP(eleId)
{
	d = document;
	var ele = d.getElementById(eleId);
	var parentEle = d.getElementById('newlinkP');
	parentEle.removeChild(ele);
}