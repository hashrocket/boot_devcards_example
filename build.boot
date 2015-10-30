(set-env!
  :source-paths #{"src/cljs" "test/clj"}
  :resource-paths #{"src/clj" "src/cljc" "resources"}
  :dependencies '[[org.clojure/clojure    "1.7.0"      :scope "provided"]
                  [boot/core              "2.4.2"      :scope "test"]

                  [org.clojure/clojurescript  "1.7.145"    :scope "test"]
                  [adzerk/boot-cljs           "1.7.48-6"   :scope "test"]
                  [adzerk/boot-cljs-repl      "0.2.0"      :scope "test"]
                  [adzerk/boot-reload         "0.4.1"      :scope "test"]
                  [adzerk/boot-test           "1.0.4"      :scope "test"]
                  [pandeiro/boot-http         "0.7.0"      :scope "test"]
                  [devcards                   "0.2.0-8"]
                  [sablono                    "0.3.6"]
                  [reagent                    "0.5.1"]
                  ])

(require
  '[adzerk.boot-cljs      :refer [cljs]]
  '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl repl-env]]
  '[adzerk.boot-reload    :refer [reload]]
  '[adzerk.boot-test      :refer [test]]
  '[pandeiro.boot-http    :refer [serve]])

(task-options!
  pom {:project 'carder
       :version "0.1.0-SNAPSHOT"
       :description "Example application"
       :license {"The MIT License (MIT)" "http://opensource.org/licenses/mit-license.php"}}
  aot {:namespace #{'backend.main}}
  jar {:main 'backend.main}
  cljs {:source-map true})

(deftask dev
  "Start the dev env..."
  [s speak           bool "Notify when build is done"
   p port       PORT int  "Port for web server"]
  (comp
    (watch)
    (reload :open-file "vim --servername saapas --remote-silent +norm%sG%s| %s")
    (cljs-repl)
    (cljs :compiler-options {:devcards true})
    (serve :port port, :dir "target")
    (if speak (boot.task.built-in/speak) identity)))

(deftask run-tests []
  (test))

(deftask autotest []
  (comp
    (watch)
    (run-tests)))

(deftask package
  "Build the package"
  []
  (comp
    (cljs :optimizations :advanced)
    (aot)
    (pom)
    (uber)
    (jar)))
