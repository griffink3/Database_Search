<#assign content>

<div class="container">

<body onload="putFilms()">

<h1>${actor}</h1>

<p4><strong>Films they've acted in:</strong></p4>
<ul id="films" class="films" value></ul>

<form method="GET" action="/bacon">
	<button type="submit">Return to homepage</button>
</form>

<div class="verybottom">
	<p2 id="message"><strong>${message}</strong><p2>
</div>

</div>

</#assign>
<#include "main2.ftl">