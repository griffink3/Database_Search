$(document).ready(function() {

  const postParameters = {};

    $.post("/bacon/connectPath", postParameters, responseJSON => {

        const responseObject = JSON.parse(responseJSON);;

        const path = responseObject.path;
        const connectCalled = responseObject.connectCalled;
        const $list = $("#connectPath");
        $list.empty();

        if (connectCalled) {
          const $message = $("#message");
          if (path.length == 0) {
            $message.html("<p>Error occurred: make sure a database has been uploaded or actor name is correct</p>");
          } else {
            $message.html("<p></p>");
            if (path[0].length == 2) {
              const opt = $("<li><a href='/bacon/actor/" + path[0][0] + "' onclick='return putFilms();'>" + path[0][0] + "</a> cannot pass the bacon to <a href='/bacon/actor/" + path[0][1] + "' onclick='return putFilms();'>" + path[0][1] + "</a></li>");
              $list.append(opt);
            } else {
              for (let i = 0; i < path.length; i++) {
                // add as an option
                const opt = $("<li><a href='/bacon/actor/" + path[i][0] + "' onclick='return putFilms();'>" + path[i][0] + "</a> to <a href='/bacon/actor/" + path[i][1] + "' onclick='return putFilms();'>" + path[i][1] + "</a> in the movie <a href='/bacon/film/" + path[i][2] + "' onclick='return putActors();'>" + path[i][2] + "</a></li>");
                $list.append(opt);
              }
            }
          }
        }

    });
});
