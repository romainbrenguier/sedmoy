let rec read_channel accu in_channel = 
  let line = try Some (input_line in_channel) with End_of_file -> None in
  match line with None -> accu | Some l -> read_channel (l :: accu) in_channel

let read_file filename =
  let channel = open_in filename in
  let lines = read_channel [] channel |> List.rev in
  close_in channel;
  lines

let rec number_of_leading_spaces line from_index =
  if String.length line < from_index + 1 || String.get line from_index <> ' '
  then from_index
  else number_of_leading_spaces line (from_index + 1)

type tabulated = {pos: int; substring: string}

let abstract_tabulation line =
  let pos = number_of_leading_spaces line 0 in
  let len = String.length line - pos in
  let substring = StringLabels.sub ~pos ~len line in
  {pos; substring}
   
let process lines =
  List.map (fun line -> 
    let abstracted = abstract_tabulation line in
    (string_of_int abstracted.pos) ^ " | " ^ abstracted.substring)
    lines

type tree = {label: string; children:tree list}

let rec to_string t = "(\""^ t.label ^ "\" " ^ (List.fold_left (fun a b -> a ^ " " ^ to_string b) "" t.children) ^ ")"
  
(** Return list of nodes below the current level, equal to the current level, and the list of tabulated which happen after current level *)
let rec split_remaining current_level = function 
  | [] -> ([], [], [])
  | head :: tail when head.pos < current_level -> ([], [], head :: tail)
  | head :: tail when head.pos = current_level ->
    let (subtree, equal_trees, remaining) = split_remaining head.pos tail in
    [], {label=head.substring; children=subtree}::equal_trees, remaining 
  | head :: tail -> 
    let (subtree, equal_trees, remaining) = split_remaining head.pos tail in
    let (subtree2, equal_trees2, remaining2) = split_remaining current_level remaining in
    {label=head.substring; children=subtree} :: equal_trees @ subtree2, equal_trees2, remaining2
  
let to_tree tabulated_list = match split_remaining (-1) tabulated_list with
  | (a, [], []) -> a
  | (a, b, c) ->
  print_endline "===== 1 =====";
  List.iter (fun t -> print_endline (to_string t)) a; 

  print_endline "===== 2 =====";
  List.iter (fun t -> print_endline (to_string t)) b;

  print_endline "===== 3 =====";
  List.iter (fun ta -> print_endline ta.substring) c;
  failwith "unexpected result"
  