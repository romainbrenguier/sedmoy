open GMain

let locale = GtkMain.Main.init ()

let _ =
  (* Simple gtk example extracted from https://ocaml.org/learn/tutorials/introduction_to_gtk.html *)
  let window = GWindow.window ~width:320 ~height:240
                              ~title:"Simple lablgtk program" () in
  let vbox = GPack.vbox ~packing:window#add () in 
  ignore vbox;
  window#show ();
  Main.main ()
