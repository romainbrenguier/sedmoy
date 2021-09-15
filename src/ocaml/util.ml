
let join ~separator = function 
  | [] -> "" | head :: tail -> List.fold_left (fun a b -> a ^ separator ^ b) head tail

let rec limit accu size list = match size, list with
  | 0, _ | _, [] -> List.rev accu 
  | s, head :: tail -> limit (head :: accu) (s - 1) tail
      
(** Word containing the character at given position *)
let current_word text position =
  let last_space = try String.rindex_from text (position - 1) ' ' with Not_found | Invalid_argument _ -> -1 in
  String.sub text (last_space + 1) (position - 1 - last_space)
 
let starts_with string prefix =
  String.length string >= String.length prefix &&
  String.sub string 0 (String.length prefix) = prefix

let let_open_in file_name in_channel_fun =
  let in_channel = open_in file_name in
  try in_channel_fun in_channel with _ -> ();
  close_in in_channel

let parse_int string =
  try Some (int_of_string (String.trim string)) with _ -> None