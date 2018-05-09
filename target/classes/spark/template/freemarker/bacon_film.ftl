<#assign content>

<div class="container">

<body onload="putActors()">

<h1>${film}</h1>

<p4><strong>Actors in this film:</strong></p4>
<ul id="actors" class="actors" value></ul>

<form method="GET" action="/bacon">
	<button type="submit">Return to homepage</button>
</form>

<div class="verybottom">
	<p2 id="message"><strong>${message}</strong><p2>
</div>

</div>

</#assign>
<#include "main2.ftl">