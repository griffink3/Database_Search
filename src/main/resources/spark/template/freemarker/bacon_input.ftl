<#assign content>

<div class="container">
<h1> Pass The Bacon - Try It! </h1>

<p> <p1><strong>Choose a database.</strong></p1>
<form method="POST" action="/dbLoad">
	<textarea name="fileName" placeholder="Database File"></textarea><br>
  <input type="submit">
</form>
</p>

<div class="middle">
<p3>Make sure to upload a database before typing!<p3>
<br></br>
<form id="myForm" method="POST" action="/baconConnect">
<div class="connect">
	<div class="leftinput">
		<input type="text" name="autocorrect1" id="autocorrect1" placeholder="First Donor">
	</div>
	<button type="submit" onclick="connectFunction()" class="connectbutton">Connect</button>
	<div class="rightinput">
		<input type="text" name="autocorrect2" id="autocorrect2" placeholder="Last Recipient">
	</div>
</div>
	<div class="leftsugg"><select id="suggestions1" value></select></div>
	<div class="rightsugg"><select id="suggestions2" value></select></div>
</form>
<br>
</div>
<br></br>
<div class="verybottom">
	<p2 id="message"><strong>${message}</strong><p2>
</div>

</div>

</#assign>
<#include "main2.ftl">
