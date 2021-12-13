type entry = {rank: int option; source: string; target: string}

let entry_to_string entry =
  Printf.sprintf "rank:%s; source:%s; target:%s" 
    (match entry.rank with Some x -> string_of_int x | None -> "XXXX")
    entry.source entry.target

let set_length string = string ^ (String.sub "           " 0 (max 0 (10 - String.length string)))

let entry_to_short_string entry =
  Printf.sprintf " * %03d %s : %s" 
    (match entry.rank with Some x -> x | None -> 1000)
    (set_length entry.source) entry.target

    
let parse_entry ~sep string = match String.split_on_char sep string with
  | r :: s :: t :: [] ->  
    {rank=Util.parse_int r; source=String.trim s; target=String.trim t}
  | _ -> failwith ("invalid entry "^ string)

let parse_entries ~sep stream =
   Streams.fold stream ~init:[] 
     ~f:(fun accu line -> parse_entry ~sep line :: accu)

let key_of_string string = String.sub (string^"  ") 0 2

type table = (string, entry) Hashtbl.t

type key_set = (string, unit) Hashtbl.t

type indexed_table = {keys:key_set; table:table}

let add_entry indexed_table entry =
  let key = key_of_string entry.source in
  Hashtbl.replace indexed_table.keys key ();
  Hashtbl.add indexed_table.table key entry

let collect_entries entry_list =
  let indexed_table = {keys=Hashtbl.create 1000; table=Hashtbl.create 1000} in
  List.iter (add_entry indexed_table) entry_list;
  indexed_table

let rec score_entry_list offset = function [] -> offset
  | {rank=None;_} :: tail -> score_entry_list offset tail
  | {rank=Some r;_} :: tail -> score_entry_list (offset + 10000 / r) tail

let score_keys indexed_table =
  let ranks = Hashtbl.create 1000 in
  Hashtbl.iter
    (fun key () -> 
      score_entry_list 0 (Hashtbl.find_all indexed_table.table key)
      |> Hashtbl.add ranks key)
    indexed_table.keys;
  ranks

let sort_keys indexed_table scores =
  let keys = Hashtbl.fold (fun key () accu -> key :: accu) indexed_table.keys [] in
  List.sort (fun k1 k2 -> Hashtbl.find scores k2 - Hashtbl.find scores k1) keys 
  
let main ~sep in_channel =
  let indexed_table = Streams.of_channel in_channel 
    |> parse_entries ~sep
    |> collect_entries
  in
  let scores = score_keys indexed_table in
  sort_keys indexed_table scores
  |> List.iter (fun key ->
      let entries = Hashtbl.find_all indexed_table.table key in
      if List.length entries > 1
      then Printf.printf "# %s [%d]\n\n" key (Hashtbl.find scores key);
      List.iter (fun entry -> print_endline (entry_to_short_string entry)) entries;
      print_newline())
