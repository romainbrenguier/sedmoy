open GMain

let locale = GtkMain.Main.init ()

let _ =
  (* Simple gtk example extracted from https://ocaml.org/learn/tutorials/introduction_to_gtk.html *)
  let window = GWindow.window
    ~width:800 ~height:600
    ~title:"Simple lablgtk program" () in
    
  let vbox = GPack.vbox ~packing:window#add () in 
  window#connect#destroy ~callback:Main.quit |> ignore;
  
  (*
  let scroll = GBin.scrolled_window
                ~hpolicy:`AUTOMATIC ~vpolicy:`AUTOMATIC
                ~packing:vbox#pack () in
*)
  let text_edit = GText.view ~height:300 ~editable:true ~packing:vbox#pack (*scroll#add_with_viewport*) () in
  text_edit#buffer#set_text "multi-\nline\ntext";
  

  let button = GButton.button ~label:"Evaluate" ~packing:vbox#add () in
  let hbox = GPack.hbox ~packing:vbox#add () in
  let input_text_view = GText.view ~justification:`CENTER ~height:250 ~width:400 ~editable:false ~packing:hbox#pack () in
  let output_text_view = GText.view ~height:250 ~width:400 ~editable:false ~packing:hbox#pack () in
  input_text_view#buffer#set_text "Input text";
  output_text_view#buffer#set_text "Output text";
  button#connect#clicked ~callback:(fun () -> output_text_view#buffer#set_text 
    ((input_text_view#buffer#get_text()) ^ (text_edit#buffer#get_text()))) |> ignore ;

  window#show ();
  Main.main ()
