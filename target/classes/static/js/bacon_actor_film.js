$(document).ready(function() {

  const postParameters = {};

    $.post("/bacon/putFilms", postParameters, responseJSON => {

        const responseObject = JSON.parse(responseJSON);;

        const films = responseObject.films;
        const actor_film = responseObject.actor_film;
        if (actor_film == 1) {
          const $list = $("#films");
          $list.empty();

            for (let i = 0; i < films.length; i++) {
              // add as an option
              const url = films[i].replace(/ /g, "_").replace(/[.]/g, "+").replace(/[^a-z0-9\_]/gmi, "-").replace(/["]/g, "=");
              const opt = $("<li><a href='/bacon/film/" + url + "' onclick='return putActors();'>" + films[i]+ "</a></li>");
              $list.append(opt);
            }
        }
    });
    $.post("/bacon/putActors", postParameters, responseJSON => {

        const responseObject = JSON.parse(responseJSON);;

        const actors = responseObject.actors;
        const actor_film = responseObject.actor_film;
        if (actor_film == 2) {
          const $list = $("#actors");
          $list.empty();

            for (let i = 0; i < actors.length; i++) {
              // add as an option
              const url = actors[i].replace(/ /g, "_").replace(/[.]/g, "+").replace(/[^a-z0-9\_]/gmi, "-").replace(/["]/g, "=");
              const opt = $("<li><a href='/bacon/actor/" + url + "' onclick='putFilms()'>" + actors[i]+ "</a></li>");
              $list.append(opt);
            }
        }
      });

});

const postParameters = {};

function putFilms() {

    $.post("/bacon/putFilms", postParameters, responseJSON => {

        const responseObject = JSON.parse(responseJSON);;

        const films = responseObject.films;
        const actor_film = responseObject.actor_film;
        if (actor_film == 1) {
          const $list = $("#films");
          $list.empty();

            for (let i = 0; i < films.length; i++) {
              // add as an option
              const url = films[i].replace(/ /g, "_").replace(/[^a-z0-9\_]/gmi, "-").replace(/["]/g, "=");
              const opt = $("<li><a href='/bacon/film/" + url + "' onclick='return putActors();'>" + films[i]+ "</a></li>");
              $list.append(opt);
            }
        }
    });
    return true;
}

function putActors() {

  $.post("/bacon/putActors", postParameters, responseJSON => {

      const responseObject = JSON.parse(responseJSON);;

      const actors = responseObject.actors;
      const actor_film = responseObject.actor_film;
      if (actor_film == 2) {
        const $list = $("#actors");
        $list.empty();

          for (let i = 0; i < actors.length; i++) {
            // add as an option
            const url = actors[i].replace(/ /g, "_").replace(/[^a-z0-9\_]/gmi, "-").replace(/["]/g, "=");
            const opt = $("<li><a href='/bacon/actor/" + url + "' onclick='putFilms()'>" + actors[i]+ "</a></li>");
            $list.append(opt);
          }
      }
    });
  return true;
}
