$(document).ready(function() {
	$("#autocorrect1").on("input", function(e) {
    const val = $(this).val();
    if(val === "") return;

    const postParameters = {input: val};

      $.post("/suggestions", postParameters, responseJSON => {

          const responseObject = JSON.parse(responseJSON);;

          const suggestions = responseObject.suggestions;
          const corpus = responseObject.corpus;
          const $list = $("#suggestions1");
          $list.empty();

          const $message = $("#message");
          if (suggestions.length == 0 && corpus == false) {
            $message.html("<p>Error occurred: make sure a corpus has been loaded or suggestion settings are on</p>");
          } else {
            $message.html("<p></p>");
          }
          for (let i = 0; i < suggestions.length; i++) {
            // add as an option
            const opt = $("<option>" + suggestions[i] + "</option>");
  				  $list.append(opt);
          }
      });
  });

  $("#autocorrect2").on("input", function(e) {
    const val = $(this).val();
    if(val === "") return;

    const postParameters = {input: val};

      $.post("/suggestions", postParameters, responseJSON => {

          const responseObject = JSON.parse(responseJSON);;

          const suggestions = responseObject.suggestions;
          const corpus = responseObject.corpus;
          const $list = $("#suggestions2");
          $list.empty();

          const $message = $("#message");
          if (suggestions.length == 0 && corpus == false) {
            $message.html("<p>Error occurred: make sure a corpus has been loaded or suggestion settings are on</p>");
          } else {
            $message.html("<p></p>");
          }
          for (let i = 0; i < suggestions.length; i++) {
            // add as an option
            const opt = $("<option>" + suggestions[i] + "</option>");
  				  $list.append(opt);
          }
      });
  });
	const list1 = $("#suggestions1");
	for (let i = 0, len = list1.length; i < len; i++) {
		$(list1[i]).on('click', event => {
			document.getElementById("autocorrect1").value = list1[i].value;
		});
	};
	const list2 = $("#suggestions2");
	for (let i = 0, len = list2.length; i < len; i++) {
		$(list2[i]).on('click', event => {
			document.getElementById("autocorrect2").value = list2[i].value;
		});
	};

});
