# Boot Devcards example

An example of using [devcards][] with the [boot][] toolchain rather than
Leiningen.

## Usage

Make sure you have boot set up. You may want to [download][download-boot] the
latest binary even if you have an existing version.

Then, from the root of the project, run `boot dev -p 5000`.

Point your browser at http://localhost:5000/cards.html. As you edit
`src/cljs/carder_devcards/core.cljs`, the browser will update live.

[devcards]: https://github.com/bhauman/devcards
[boot]: http://boot-clj.com
[download-boot]: https://github.com/boot-clj/boot#install
