open GMain

let locale = GtkMain.Main.init ()

let join ~separator = function 
  | [] -> "" | head :: tail -> List.fold_left (fun a b -> a ^ separator ^ b) head tail

let evaluate command_text input = 
  let command = Operation.parse command_text in
  String.split_on_char '\n' input
  |> List.map (fun x -> Operation.String x)
  |> Operation.evaluate command
  |> List.map (fun x -> Operation.value_to_string x)
    
let run_ui input_list = 
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
  let input_text_view = GText.view ~height:250 ~width:400 ~editable:false ~packing:hbox#pack () in
  let output_text_view = GText.view ~height:250 ~width:400 ~editable:false ~packing:hbox#pack () in
  input_text_view#buffer#set_text (join ~separator:"\n" input_list);
  output_text_view#buffer#set_text "Output text";
  button#connect#clicked 
    ~callback:(fun () -> 
      evaluate (text_edit#buffer#get_text()) (input_text_view#buffer#get_text())
      |> join ~separator:"\n"
      |> output_text_view#buffer#set_text)
   |> ignore ;

  window#show ();
  Main.main ()

let _ =
  match Array.to_list Sys.argv with
  | _command :: "tree" :: file_name :: [] -> 
    let lines = FileUtil.read_file file_name in
    lines |> Tree.process |> List.iter print_endline;
    print_endline "=================";
    List.map Tree.abstract_tabulation lines |> Tree.to_tree 
    |> List.map (fun x -> x |> Tree.to_string)
    |> run_ui
  | _command :: "table" :: file_name :: [] 
  | _command :: file_name :: [] -> 
    FileUtil.read_file file_name |> run_ui 
  | _ -> failwith "Invalid command argument, should be: tree <filename> | table <filename> | <filename>"
