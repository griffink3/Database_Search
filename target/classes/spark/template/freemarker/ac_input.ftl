<#assign content>

<div class="container">
<h1> Text Prediction - Try It! </h1>

<p> <p1><strong>For smart mode, select ngram size before uploading corpus.</strong></p1>
<form method="POST" action="/corpusLoad">
	<textarea name="fileName" placeholder="Corpus file (any text file)"></textarea><br>
  <input type="submit">
</form>
</p>

<div class="left">
<form method="POST" action="/toggleSelect">
	<p> <p1<strong>Prefix suggestions?</strong></p1>
	<select id="prefix" name="Prefix">
  		<option value="on">ON</option>
  		<option value="off" selected>OFF</option>
	</select>
	<p> <p1<strong>Whitespace suggestions?</strong></p1>
	<select id="whitespace" name="WhiteSpace">
  		<option value="on">ON</option>
  		<option value="off" selected>OFF</option>
	</select>
	<p> <p1<strong>Smart suggestions?</strong></p1>
	<select id="smart" name="Smart">
  		<option value="on">ON</option>
  		<option value="off" selected>OFF</option>
	</select>
	<br></br>
	<textarea name="led" placeholder="Levenshtein edit distance"></textarea><br>
	<textarea name="nGram" placeholder="N-Gram size for smart mode"></textarea><br>
  <input type="submit">
</form>
<br></br>
<form method="POST" action="/toggleInfo">
	<input type="submit" value="Get settings info">
</form>
</div>

<div class="right">
<p3>Make sure to upload a corpus before typing!<p3>
<br></br>
<button type="button" onclick="hideSuggestions()">Hide Autocorrect Suggestions</button>
<button type="button" onclick="showSuggestions()">Show Autocorrect Suggestions</button>
<br></br>
<form id="myForm">
	<input type="text" name="autocorrect" id="autocorrect" placeholder="Type Here">
	<ul id="suggestions" value>
	</ul>
</form>
</div>

<div class="bottom"><p2 id="message"><strong>${message}</strong><p2><div>

</div>

</#assign>
<#include "main1.ftl">
