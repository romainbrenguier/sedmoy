
let _ =
  match Array.to_list Sys.argv with
  | _command :: "tree" :: file_name :: [] -> 
    let lines = FileUtil.read_file file_name in
    lines |> Tree.process |> List.iter print_endline;
    print_endline "=================";
    List.map Tree.abstract_tabulation lines |> Tree.to_tree 
    |> List.map (fun x -> x |> Tree.to_string)
    |> Ui.run
  | _ :: "start-day" :: [] -> StartDay.run ()
  | _command :: "split" :: expression :: [] -> 
    SplitParser.parse expression 
    |> Split.expression_to_automaton
    |> fun auto -> Split.apply_to_channel auto stdin
    |> Split.write_stream 
  | _command :: "shell" :: file_name :: [] ->
    Util.let_open_in file_name 
      (fun in_channel -> 
         ShellInterpret.transform_channel in_channel stdout)
  | _command :: "ngrams" :: file_name :: [] ->
    Util.let_open_in file_name (NgramSort.main ~sep:'\t')
  | _command :: "table" :: file_name :: [] 
  | _command :: file_name :: [] -> 
    FileUtil.read_file file_name |> Ui.run 
  | _ -> failwith "Invalid command argument, should be: tree <filename> | table <filename> | <filename>"
