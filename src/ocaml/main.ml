open GMain

let locale = GtkMain.Main.init ()

let parse_program program_text = 
  String.split_on_char '\n' program_text |> List.map Operation.parse

let execute_program program input_values = 
  List.map (fun command -> Operation.evaluate command input_values) program

let evaluate command_text input = 
  let input_values = String.split_on_char '\n' input |> Util.limit [] 7 |> List.map (fun x -> Operation.StringValue x) in
  let output_values = execute_program (parse_program command_text) input_values in
  List.map 
    (fun output -> List.map (fun x -> Operation.value_to_string x) output |> Util.join ~separator:"\n") 
    output_values
  |> Util.join ~separator:"\n===================\n"

let suggest_text prefix = 
  Operation.suggest_command prefix |> List.map (fun x -> x.Operation.name) |> Util.join ~separator:"   "

let run_ui input_list = 
  (* Simple gtk example extracted from https://ocaml.org/learn/tutorials/introduction_to_gtk.html *)
  let window = GWindow.window
    ~width:1200 ~height:800
    ~title:"Sedmoy" () in
    
  let vbox = GPack.vbox ~packing:window#add () in 
  window#connect#destroy ~callback:Main.quit |> ignore;
  
  (*
  let scroll = GBin.scrolled_window
                ~hpolicy:`AUTOMATIC ~vpolicy:`AUTOMATIC
                ~packing:vbox#pack () in
*)
  let button = GButton.button ~label:"Evaluate" ~packing:vbox#add () in

  let suggestions = GText.view ~height:50 ~editable:false ~packing:vbox#add () in
  let hbox = GPack.hbox ~packing:vbox#add () in
  let input_text_view = GText.view  ~height:700 ~width:400 ~editable:false ~packing:hbox#pack () in
  let text_edit = GText.view ~height:700 ~width:400 ~border_width:5 ~editable:true ~packing:hbox#pack (*scroll#add_with_viewport*) () in
  let output_text_view = GText.view  ~height:700 ~width:400 ~editable:false ~packing:hbox#pack () in
  suggestions#buffer#set_text (suggest_text "");
  text_edit#buffer#set_text "";
  input_text_view#buffer#set_text (Util.join ~separator:"\n" input_list);
  output_text_view#buffer#set_text "Output text";

  button#connect#clicked 
    ~callback:(fun () -> 
      evaluate (text_edit#buffer#get_text()) (input_text_view#buffer#get_text())
      |> output_text_view#buffer#set_text)
   |> ignore ;

  text_edit#buffer#connect#changed ~callback:(fun () -> 
    let current = Util.current_word (text_edit#buffer#get_text()) (text_edit#buffer#cursor_position) in
    suggestions#buffer#set_text (suggest_text current) 
  ) |> ignore;

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
