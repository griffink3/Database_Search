let hide = 0;

$(document).ready(function() {
	$("#autocorrect").on("input", function(e) {
    const val = $(this).val();
    if(val === "") return;

    const postParameters = {input: val};

    if (hide == 0) {
      $.post("/suggestions", postParameters, responseJSON => {

          const responseObject = JSON.parse(responseJSON);;

          const suggestions = responseObject.suggestions;
          const corpus = responseObject.corpus;
          const $list = $("#suggestions");
          $list.empty();

          const $message = $("#message");
          if (suggestions.length == 0 && corpus == false) {
            $message.html("<p>Error occurred: make sure a corpus has been loaded or suggestion settings are on</p>");
          } else {
            $message.html("<p></p>");
          }
          for (let i = 0; i < suggestions.length; i++) {
            // add as an option
            console.log(suggestions[i]);
            const opt = $("<li></li>").text(suggestions[i]);
  				  $list.append(opt);
          }
      });
    }
  });
});

function hideSuggestions() {
  // const $autocorrect = $("#autocorrect");
  hide = 1;
  const $list = $("#suggestions");
  $list.empty();
}

function showSuggestions() {
  hide = 0;
}
