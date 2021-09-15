type entry = {rank: int option; source: string; target: string}

let entry_to_string entry =
  Printf.sprintf "rank:%s; source:%s; target:%s" 
    (match entry.rank with Some x -> string_of_int x | None -> "XXXX")
    entry.source entry.target
    
let parse_entry ~sep string = match String.split_on_char sep string with
  | r :: s :: t :: [] ->  
    {rank=Util.parse_int r; source=String.trim s; target=String.trim t}
  | _ -> failwith ("invalid entry "^ string)

let parse_entries ~sep stream =
   Streams.fold stream ~init:[] 
     ~f:(fun accu line -> parse_entry ~sep line :: accu)

let key string = String.sub (string^"  ") 0 2

type table = (string, string list) Hashtbl.t

let add_entry table entry =
  Hashtbl.add table (key entry.source) entry

let main ~sep in_channel =
  let table = Hashtbl.create 1000 in
  print_endline "table created";
  Streams.of_channel in_channel 
  |> parse_entries ~sep
  |> List.iter (add_entry table);
  Hashtbl.iter (fun key entry -> 
    print_endline key;
    print_endline (entry_to_string entry))
    table