type entry = {rank: int option; source: string; target: string}

let make_entry r s t = {rank=Util.parse_int r; source=String.trim s; target=String.trim t} 
let entry_to_string entry =
  Printf.sprintf "rank:%s; source:%s; target:%s" 
    (match entry.rank with Some x -> string_of_int x | None -> "XXXX")
    entry.source entry.target

let set_length string = string ^ (String.sub "           " 0 (max 0 (10 - String.length string)))

let entry_to_short_string entry =
  Printf.sprintf " * %03d %s : %s" 
    (match entry.rank with Some x -> x | None -> 1000)
    (set_length entry.source) entry.target

(** Split according to separator but not breaking quoted string *)
let split_keeping_quoted ~sep string =
  let quote_splitted = String.split_on_char '"' string in
  let rec loop accu = function
    | "" :: [] | [] -> List.rev accu
    | non_quoted :: [] -> List.rev (List.rev_append (String.split_on_char sep non_quoted) accu)
    | non_quoted :: quoted :: tail ->
       match List.rev (String.split_on_char sep non_quoted)  with
       | tail_rev :: head_rev ->
           let new_accu = ((tail_rev ^ quoted) :: head_rev) @ accu in
           loop new_accu tail
       | [] -> loop (quoted :: accu) tail
  in loop [] quote_splitted


let parse_entry ~sep string = match split_keeping_quoted ~sep string with
  | r :: s :: t :: [] ->
     Log.return (Some (make_entry r s t)) (Log.empty)
  | r :: s :: t :: remainder ->
     Log.return (Some (make_entry r s t)) (Log.warn ["Ignoring remainder of line: " ^ Util.join ~separator:"|" remainder])
  | list ->
     Log.return None (Log.warn ["Ignoring line " ^ string ^ ", parsed as: " ^ Util.join ~separator:"|" list])

let parse_entries ~sep stream =
   Streams.fold stream ~init:[] 
     ~f:(fun accu line ->
       match parse_entry ~sep line with
       | {result=Some x; log} -> Log.to_stdout log; x :: accu
       | {result=None; log} -> Log.to_stdout log; accu)

(** Key size is in number of char. A single symbol in utf-8 may be using 1, 2 or 4 chars *)
let key_of_string ~key_size string =
  String.sub (string^"  ") 0 key_size |> Normalize.replace

type table = (string, entry) Hashtbl.t

type key_set = (string, unit) Hashtbl.t

type indexed_table = {keys:key_set; table:table}

let add_entry ~key_size ~indexed_table entry =
  let key = key_of_string ~key_size entry.source in
  Hashtbl.replace indexed_table.keys key ();
  Hashtbl.add indexed_table.table key entry

let collect_entries ~key_size entry_list =
  let indexed_table = {keys=Hashtbl.create 1000; table=Hashtbl.create 1000} in
  List.iter (add_entry ~key_size ~indexed_table) entry_list;
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

let keys indexed_table =
  Hashtbl.fold (fun key () accu -> key :: accu) indexed_table.keys []

let sort_keys_by_score indexed_table scores =
  keys indexed_table
  |> List.sort (fun k1 k2 -> Hashtbl.find scores k2 - Hashtbl.find scores k1) 

let sort_keys indexed_table =
  keys indexed_table |> List.sort String.compare
  
let main ~sep ~key_size in_channel =
  let indexed_table = Streams.of_channel in_channel 
    |> parse_entries ~sep
    |> collect_entries ~key_size
  in
  let scores = score_keys indexed_table in
  Printf.printf "# Summary [%d entries]\n\n" (List.length (keys indexed_table));
  let current_index = ref 0 in
  sort_keys indexed_table
  |> List.iter (fun key ->
         incr current_index;
         let entries = Hashtbl.find_all indexed_table.table key in
         Printf.printf "  %s [%d]" key (List.length entries);
         if !current_index mod 10 = 0 then print_newline();
       );
  print_endline "\n\n";
  sort_keys_by_score indexed_table scores
  |> List.iter (fun key ->
         let entries = Hashtbl.find_all indexed_table.table key in
         if List.length entries > 1
         then Printf.printf "# %s [%d]\n\n" key (Hashtbl.find scores key);
         List.iter (fun entry -> print_endline (entry_to_short_string entry)) entries;
         print_newline())
