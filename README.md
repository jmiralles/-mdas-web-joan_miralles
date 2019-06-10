# pokemon-client

A [re-frame](https://github.com/Day8/re-frame) 

Cliente para la api de pokemon.

## Codigo prod

No es buena práctica pero en resources/public/index.html está el resultado de producción.
De esta forma no hace falta tener Leiningen configurado para lanzar la web.

## Development Mode

### Run application:

```
lein clean
lein figwheel dev
```

Figwheel will automatically push cljs changes to the browser.

Wait a bit, then browse to [http://localhost:3449](http://localhost:3449).

## Production Build


To compile clojurescript to javascript:

```
lein clean
lein cljsbuild once min
```
