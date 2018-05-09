<#assign content>

<h1> Query The Stars! </h1>

<p> <p1><strong>Upload some stars!</strong> - Enter a csv file containing stars (format: iD, name, x, y, z)</p1>
<form method="POST" action="/starsLoad">
	<textarea name="fileName" placeholder="File from which to load stars"></textarea><br>
  <input type="submit">
</form>
</p>

<p> <p1><strong>Search for neighbors!</strong> - Enter the number of neighbors to look for.</p1>
<form method="POST" action="/neighbors">
	<textarea name="numNeighbors" placeholder="Number of neighbors"></textarea><br>
	<p>Also enter a star name surrounded by quotes or a point in the form of 3 numbers separated by spaces (whose neighbors to find).</p>
	<textarea name="searchParam" placeholder="Enter Star Name or Point"></textarea><br>
  <input type="submit">
</form>
</p>

<p> <p1><strong>Search for stars in range!</strong> - Enter a radius.</p1>
<form method="POST" action="/radius">
  	<textarea name="radius" placeholder="Radius"></textarea><br>
  	<p>Also enter a star name surrounded by quotes or a point in the form of 3 numbers separated by spaces (around which the radius is centered).</p>
	<textarea name="searchParam" placeholder="Star Name or Point"></textarea><br>
  <input type="submit">
</form>
</p>

<p2><strong>${message}</strong><p2>

</#assign>
<#include "main.ftl">
