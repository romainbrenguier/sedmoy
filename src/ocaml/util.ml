
let join ~separator = function 
  | [] -> "" | head :: tail -> List.fold_left (fun a b -> a ^ separator ^ b) head tail

let rec limit accu size list = match size, list with
  | 0, _ | _, [] -> List.rev accu 
  | s, head :: tail -> limit (head :: accu) (s - 1) tail
      
(** Word containing the character at given position *)
let current_word text position =
  let last_space = try String.rindex_from text (position - 1) ' ' with Not_found | Invalid_argument _ -> -1 in
  String.sub text (last_space + 1) (position - 1 - last_space)
 
let line_stream in_channel =
  Stream.from (fun _ ->
    try Some (input_line in_channel) with End_of_file -> None)